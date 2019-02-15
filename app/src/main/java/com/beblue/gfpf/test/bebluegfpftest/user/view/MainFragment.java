package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.beblue.gfpf.test.bebluegfpftest.user.GHUserViewModel;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.beblue.gfpf.test.bebluegfpftest.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
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

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.results_label)
    TextView resultsLabel;

    private GHUserViewModel mGHUserViewModel;
    private CardRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main_frag, container, false);
        ButterKnife.bind(this, rootView);

        init(savedInstanceState);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_header_search);
    }

    private void init(Bundle savedInstanceState) {
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_header_search);

        // Set up the RecyclerView
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemAnimator();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        int smallPadding = getResources().getDimensionPixelSize(R.dimen.nav_header_vertical_spacing);
        CardItemDecoration itemDecoration = new CardItemDecoration(smallPadding, smallPadding);
        recyclerView.addItemDecoration(itemDecoration);

        //Adapter
        if (mAdapter == null) {
            mAdapter = new CardRecyclerViewAdapter(getContext(), this);
        } else {
            resultsLabel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(mAdapter);

        int searchViewPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        EditText searchPlateEditText = searchView.findViewById(searchViewPlateId);
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
        Util.hideKeyboard(Objects.requireNonNull(getActivity()));
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
                .subscribe(ghUser -> {

                    List result = new ArrayList<>();
                    result.add(ghUser);

                    //Result
                    if (ghUser != null) {
                        showGHUserListUI(result, false);
                    }

                }, throwable -> {
                    // handle error event
                    showGHUserListUI(null, true);
                });
    }


    @Override
    public void setProgressIndicator(boolean isActive) {
        if (isActive) {
            progressBar.setVisibility(View.VISIBLE);
            resultsLabel.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showGHUserListUI(List<GHUser> users, boolean isAppend) {
        setProgressIndicator(false);

        if (users == null || users.isEmpty()) {
            resultsLabel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            resultsLabel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

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
