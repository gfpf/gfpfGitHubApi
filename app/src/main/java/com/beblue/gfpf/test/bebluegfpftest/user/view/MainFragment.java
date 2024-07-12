package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.databinding.ContentMainFragBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.GHUserViewModel;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.beblue.gfpf.test.bebluegfpftest.util.ProgressBarManager;
import com.beblue.gfpf.test.bebluegfpftest.util.Util;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

import android.view.animation.LayoutAnimationController;
import android.view.animation.AnimationUtils;

public class MainFragment extends Fragment implements
        GHUserContract.View
        , CardRecyclerViewAdapter.RecyclerViewClickListener {

    private ContentMainFragBinding binding;
    private GHUserViewModel mGHUserViewModel;
    private CardRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ContentMainFragBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetup();
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(R.string.nav_header_search);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GFPF", "-onStart -MainFragment -DataCount: " + mAdapter.getItemCount());

        //TODO GFPF - Should I do check if the list is empty?
        //TODO GFPF - Need to scroll up when do new search!
        //TODO GFPF - Need show error msg when search term is empty!
        // Even when loading again after back from detail screen, it keeps list's state & the visible items on the screen

        // Keep checking to avoid refresh search results
        // Must swipe to refresh content
        /*if (mAdapter.isEmpty())
            doLoadAll();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GFPF", "-onResume -MainFragment -DataCount: " + mAdapter.getItemCount());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void initSetup() {
        updateTitle();
        setupSwipeRefresh();
        setupRecyclerView();
        setupSearchView();
        mGHUserViewModel = ViewModelProviders.of(this).get(GHUserViewModel.class);
    }

    private void setupSwipeRefresh() {
        //Refresh Listener
        binding.swipeRefresh.setOnRefreshListener(this::doLoadAll);

        // Init ProgressBarManager
        ProgressBarManager.init(binding.swipeRefresh);
        // Update the progress position if needed
        //ProgressBarManager.getInstance().updateProgressPosition(Offset.Center);
    }

    private void setupRecyclerView() {
        // Set up the RecyclerView
        binding.recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        int smallPadding = getResources().getDimensionPixelSize(R.dimen.nav_header_vertical_spacing);
        CardItemDecoration itemDecoration = new CardItemDecoration(smallPadding, smallPadding);
        binding.recyclerView.addItemDecoration(itemDecoration);

        // Layout animation (RecyclerView)
        //LayoutAnimationController animController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.anim_fall_down);
        //binding.recyclerView.setLayoutAnimation(animController);

        // Item animation (CardView)
        //binding.recyclerView.setItemAnimator(new SlideInDownAnimator());

        //Adapter
        if (mAdapter == null) {
            mAdapter = new CardRecyclerViewAdapter(getContext(), this);
            binding.recyclerView.setAdapter(mAdapter);
            // Load data initially
            doLoadAll();
        } else {
            binding.recyclerView.setAdapter(mAdapter);
            showGHUserListUI(mAdapter.getItems(), false);
        }
        //TODO GFPF - IS OK TO CREATE THE 'VIEWS' AGAIN BUT NOT THE 'OBJECTS' LIKE THE 'ADAPTER'?
    }

    private final int minSearchTermLength = 3;
    private void setupSearchView() {
        int searchViewPlateId = binding.searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        EditText searchPlateEditText = binding.searchView.findViewById(searchViewPlateId);
        searchPlateEditText.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String searchTerm = view.getText().toString();
                if (!TextUtils.isEmpty(searchTerm)) {
                    if (searchTerm.length() >= minSearchTermLength) {
                        //Search
                        doSearch(searchTerm);
                    } else {
                        //Short search term
                        searchPlateEditText.setError(getString(R.string.search_term_min_length));
                    }
                } else {
                    //No search term
                    searchPlateEditText.setError(getString(R.string.search_term_empty));
                }
            }
            return true;
        });
    }

    private void doLoadAll() {
        setProgressIndicator(true);

        //TODO GFPF - REMOVE SLEEP
        Disposable subscribe = Observable.fromCallable(() -> {
                    Thread.sleep(1000); // Simulate delay
                    return mGHUserViewModel.loadAllGHUsers().blockingGet(); // Blocking call to get the result
                })
                .subscribeOn(Schedulers.io()) // Run on background thread
                .observeOn(AndroidSchedulers.mainThread()) // Observe on main thread
                .subscribe(
                        result -> {
                            if (result != null) {
                                showGHUserListUI(result, false);
                            }
                            setProgressIndicator(false); // Hide progress indicator
                        }, throwable -> {
                            // handle error event
                            showGHUserListUI(null, true);
                            setProgressIndicator(false); // Hide progress indicator
                        });
    }

    private void doSearch(String searchTerm) {
        //TODO GFPF - Start using LiveData to update the UI
        Util.hideKeyboard(requireActivity());
        setProgressIndicator(true);

        //TODO GFPF - REMOVE SLEEP
        //Disposable disposable = mGHUserViewModel.searchGHUserByName(searchTerm, false)
        Disposable disposable = Observable.fromCallable(() -> {
                    Thread.sleep(1000); // Simulate delay
                    return mGHUserViewModel.searchGHUserByName(searchTerm, false).blockingGet(); // Blocking call to get the result
                })
                .subscribeOn(Schedulers.io()) // Run the search operation on IO thread
                .observeOn(AndroidSchedulers.mainThread()) // Observe results on main thread
                .subscribe(
                        ghSearchUser -> {
                            int firstItem = 0;
                            if (ghSearchUser.getUsers().size() > firstItem) {
                                showGHUserListUI(ghSearchUser.getUsers(), false);
                            } else {
                                showGHUserListUI(Collections.emptyList(), false); // Empty list case
                            }
                            setProgressIndicator(false); // Hide progress indicator
                        },
                        throwable -> {
                            // Handle error event
                            showGHUserListUI(null, true);
                            setProgressIndicator(false); // Hide progress indicator
                        }
                );
    }

    @Override
    public void showGHUserListUI(List<GHUser> users, boolean isAppend) {
        if (users == null || users.isEmpty()) {
            binding.noResultsLabel.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.noResultsLabel.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);

            if (isAppend) {
                mAdapter.appendData(users);
            } else {
                mAdapter.replaceData(users);
            }
        }
    }

    public void recyclerViewListClicked(View v, int position) {
        doLoadUserById(mAdapter.getItem(position));
    }

    private void doLoadUserById(GHUser selectedUser) {
        setProgressIndicator(true);

        //TODO GFPF - REMOVE SLEEP
        //Disposable disposable = mGHUserViewModel.loadGHUserById(selectedUser.getId())
        Disposable disposable = Observable.fromCallable(() -> {
                    Thread.sleep(1000); // Simulate delay
                    return mGHUserViewModel.loadGHUserById(selectedUser.getId()).blockingGet(); // Blocking call to get the result
                })
                .subscribeOn(Schedulers.io()) // Run the load operation on IO thread
                .observeOn(AndroidSchedulers.mainThread()) // Observe results on main thread
                .subscribe(
                        result -> {
                            if (result != null) {
                                showGHUserDetailUI(result);
                            } else {
                                // Handle null result case if needed
                            }
                            setProgressIndicator(false); // Hide progress indicator
                        },
                        throwable -> {
                            // Handle error event
                            // TODO: Handle this scenario
                            setProgressIndicator(false); // Hide progress indicator
                        }
                );
    }

    @Override
    public void showGHUserDetailUI(@NonNull GHUser requestedUser) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GHUser.REQUESTED_USER_KEY, requestedUser);

        DetailedFragment detailedOrderFragment = new DetailedFragment();
        detailedOrderFragment.setArguments(bundle);

        Navigation.findNavController(requireView()).navigate(R.id.action_main_frag_to_detailed_frag, bundle);
    }

    @Override
    public void setProgressIndicator(boolean isActive) {
        //todo gfpf - simplify that
        if (isActive) {
            ProgressBarManager.getInstance().showProgress();
        } else {
            ProgressBarManager.getInstance().hideProgress();
        }
    }

    @Override
    public void showToastMessage(String message) {
    }
}