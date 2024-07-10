package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.beblue.gfpf.test.bebluegfpftest.MainActivity;
import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.databinding.ContentMainFragBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.GHUserViewModel;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.beblue.gfpf.test.bebluegfpftest.util.Util;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements
        GHUserContract.View
        , CardRecyclerViewAdapter.RecyclerViewClickListener {

    public static final String MAIN_FRAGMENT_TAG = MainFragment.class.getSimpleName();

    private ContentMainFragBinding binding;

    private GHUserViewModel mGHUserViewModel;
    private CardRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ContentMainFragBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        init(savedInstanceState);
        return rootView;
    }

    public void updateTitle() {
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_header_search);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(R.string.nav_header_search);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GFPF", "-onStart -MainFragment -DataCount: " + mAdapter.getItemCount());
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

    private void init(Bundle savedInstanceState) {
        updateTitle();

        // Set up the RecyclerView
        binding.recyclerView.setHasFixedSize(true);
        //binding.recyclerView.setItemAnimator();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        int smallPadding = getResources().getDimensionPixelSize(R.dimen.nav_header_vertical_spacing);
        CardItemDecoration itemDecoration = new CardItemDecoration(smallPadding, smallPadding);
        binding.recyclerView.addItemDecoration(itemDecoration);

        //Adapter
        if (mAdapter == null) {
            mAdapter = new CardRecyclerViewAdapter(getContext(), this);
        } else {
            binding.resultsLabel.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
        binding.recyclerView.setAdapter(mAdapter);

        int searchViewPlateId = binding.searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        EditText searchPlateEditText = binding.searchView.findViewById(searchViewPlateId);
        searchPlateEditText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String searchTerm = v.getText().toString();

                if (!TextUtils.isEmpty(searchTerm)) {
                    //Text entered
                    doSearch(searchTerm);

                } else {
                    //No search term
                    doInitialLoad();
                }
            }
            return true;
        });

        mGHUserViewModel = ViewModelProviders.of(this).get(GHUserViewModel.class);
        doInitialLoad();

    }

    private void doInitialLoad() {
        //if (savedInstanceState == null) {
        //Load all users
        setProgressIndicator(true);
        mGHUserViewModel.loadAllGHUsers()
                .subscribe(result -> {

                    //Result
                    if (result != null) {
                        showGHUserListUI(result, false);
                    }

                }, throwable -> {
                    // handle error event
                    showGHUserListUI(null, true);
                });
        //}
    }

    private void doSearch(String searchTerm) {
        Util.hideKeyboard(requireActivity());
        setProgressIndicator(true);

        //TODO Use observe method with LiveData only
        /*mGHUserViewModel.searchRepositoryByName(searchTerm, false).observe(this, new Observer<List<GHUser>>() {
            @Override
            public void onChanged(List<GHUser> ghEntities) {
                //Update RecyclerView
                Toast.makeText(getActivity(), "onChanged", Toast.LENGTH_SHORT).show();
                mAdapter.replaceData(ghEntities);
            }

        });*/

        mGHUserViewModel.searchGHUserByName(searchTerm, false)
                .subscribe(ghSearchUser -> {

                    int firstItem = 0;
                    if (ghSearchUser.getUsers().size() > firstItem) {
                        showGHUserListUI(ghSearchUser.getUsers(), false);
                    }

                }, throwable -> {
                    // handle error event
                    showGHUserListUI(null, true);
                });
    }


    @Override
    public void setProgressIndicator(boolean isActive) {
        if (isActive) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.resultsLabel.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showGHUserListUI(List<GHUser> users, boolean isAppend) {
        setProgressIndicator(false);

        if (users == null || users.isEmpty()) {
            binding.resultsLabel.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.resultsLabel.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);

            if (isAppend) {
                mAdapter.appendData(users);

            } else {
                mAdapter.replaceData(users);
            }
        }
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showGHUserDetailUI(@NonNull GHUser requestedUser) {
        setProgressIndicator(false);

        Bundle bundle = new Bundle();
        bundle.putSerializable(GHUser.REQUESTED_USER_KEY, requestedUser);

        DetailedFragment detailedOrderFragment = new DetailedFragment();
        detailedOrderFragment.setArguments(bundle);

        MainActivity.class.cast(getActivity()).changeFragment(detailedOrderFragment, true, true);
    }

    public void recyclerViewListClicked(View v, int position) {
        GHUser selectedUser = mAdapter.getItem(position);

        setProgressIndicator(true);
        mGHUserViewModel.loadGHUserById(selectedUser.getId())
                .subscribe(result -> {

                    //Result
                    if (result != null) {
                        showGHUserDetailUI(result);
                    }

                }, throwable -> {
                    // handle error event
                    //TODO Handle this scenario
                });


    }

}
