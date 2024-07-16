package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserShowcaseFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserCardItemDecoration
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserRecyclerViewAdapter
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserDetailFrag
import com.beblue.gfpf.test.bebluegfpftest.util.ProgressBarManager
import com.beblue.gfpf.test.bebluegfpftest.util.Util
import com.beblue.gfpf.test.bebluegfpftest.util.updateActionBarTitle
import com.gfpf.github_api.domain.user.GHUser
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserShowcaseFrag
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserShowcaseFrag : Fragment(), IUserShowcaseFrag.View,
    UserRecyclerViewAdapter.UserRecyclerViewClickListener {

    private lateinit var mBinding: UserShowcaseFragBinding
    private val mGHUserViewModel: UserViewModel by viewModels()
    private lateinit var mActionListener: IUserShowcaseFrag.ActionListener

    @Inject
    lateinit var mAdapter: UserRecyclerViewAdapter

    //@Inject
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = UserShowcaseFragBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
        registerObservers()
    }

    override fun onStart() {
        super.onStart()
        Log.d("GFPF", "-onStart -ShowcaseFragment -DataCount: ${mAdapter.itemCount}")

        if (mAdapter.itemCount == 0)
            doLoadAll()
    }

    private fun initSetup() {
        mActionListener = mGHUserViewModel
        (activity as? AppCompatActivity)?.updateActionBarTitle(getString(R.string.nav_header_search))
        setupSwipeRefresh()
        setupRecyclerView()
        setupSearchView()
    }

    private fun registerObservers() {
        // TODO GFPF - Apply observe extension method and use state like marvel project
        //observe(viewModel.state, ::onViewStateChange)

        /**
         * All Users
         * IsNot SingleEvent to avoid make new request when frag is recreated (nav-back, config change)
         * @see <SingleEvent> class.
         */
        mGHUserViewModel.allUsers.observe(viewLifecycleOwner) { result ->
            if (!isSearchMode) {
                showUserListUI(result ?: emptyList(), false)
                setProgressIndicator(false)
            }
        }

        /**
         * Search Users
         * IsNot SingleEvent to avoid make new request when frag is recreated (nav-back, config change)
         * @see <SingleEvent> class.
         */
        mGHUserViewModel.searchResult.observe(viewLifecycleOwner) { ghSearchUser ->
            if (isSearchMode) {
                showUserListUI(ghSearchUser?.users ?: emptyList(), false)
                setProgressIndicator(false)
                showToastMessage(getString(R.string.clear_results))
            }
        }

        /**
         * User Detail
         * Using SingleEvent strategy to avoid observe data twice
         * @see <SingleEvent> class.
         */
        mGHUserViewModel.userDetail.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { user ->
                showUserDetailUI(user)
            }
            setProgressIndicator(false)
        }
    }

    private fun setupSwipeRefresh() {
        mBinding.swipeRefresh.setOnRefreshListener {
            isSearchMode = false
            doLoadAll()
            clearSearchView()
            animateSearchView()
        }
        ProgressBarManager.init(mBinding.swipeRefresh)
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.apply {
            // Set layoutManager once
            if (layoutManager == null) {
                mLayoutManager = LinearLayoutManager(context)
                layoutManager = mLayoutManager

                val smallPadding =
                    resources.getDimensionPixelSize(R.dimen.nav_header_vertical_spacing)
                val itemDecoration = UserCardItemDecoration(smallPadding, smallPadding)
                addItemDecoration(itemDecoration)
            }
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    fun scrollToTop() {
        mLayoutManager.let {
            if (it.findFirstVisibleItemPosition() == 0) {
                showToastMessage(getString(R.string.scroll_up_error))
            } else {
                mBinding.recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupSearchView() {
        val searchViewPlateId = mBinding.searchView.context.resources.getIdentifier(
            "android:id/search_src_text",
            null,
            null
        )
        val searchPlateEditText: EditText = mBinding.searchView.findViewById(searchViewPlateId)
        searchPlateEditText.imeOptions = EditorInfo.IME_ACTION_SEARCH

        searchPlateEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchTerm = view.text.toString()
                if (!TextUtils.isEmpty(searchTerm)) {
                    if (searchTerm.length >= MIN_SEARCH_TERM_LENGTH) {
                        doSearch(searchTerm)
                    } else {
                        searchPlateEditText.error = getString(R.string.search_term_min_length)
                    }
                } else {
                    searchPlateEditText.error = getString(R.string.search_term_empty)
                }
            }
            true
        }
        animateSearchView()
    }

    private fun clearSearchView() {
        mBinding.searchView.setQuery("", false)
        mBinding.searchView.clearFocus()
        mBinding.searchView.onActionViewCollapsed()
    }

    private fun animateSearchView() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_bounce_in_right)
        mBinding.searchView.startAnimation(animation)
    }

    private fun doLoadAll() {
        setProgressIndicator(true)
        mActionListener.loadAllUsers()
    }

    private fun doSearch(searchTerm: String) {
        Util.hideKeyboard(requireActivity())
        setProgressIndicator(true)
        isSearchMode = true
        mActionListener.searchUserByName(searchTerm, false)
    }

    override fun showUserListUI(users: List<GHUser>?, isAppend: Boolean) {
        if (users.isNullOrEmpty()) {
            mBinding.noResultsLabel.visibility = View.VISIBLE
            mBinding.recyclerView.visibility = View.GONE
        } else {
            mBinding.noResultsLabel.visibility = View.GONE
            mBinding.recyclerView.visibility = View.VISIBLE
            if (isAppend) {
                mAdapter.appendData(users)
            } else {
                mAdapter.replaceData(users)
            }
        }
    }

    override fun recyclerViewClicked(v: View, position: Int) {
        doLoadUserById(mAdapter.getItem(position))
    }

    private fun doLoadUserById(selectedUser: GHUser) {
        setProgressIndicator(true)
        selectedUser.id?.let { mActionListener.loadUserById(it) }
    }

    override fun showUserDetailUI(requestedUser: GHUser) {
        val bundle = Bundle().apply {
            putSerializable(GHUser.REQUESTED_USER_DETAIL_KEY, requestedUser)
        }
        findNavController().navigate(R.id.action_main_frag_to_detailed_frag, bundle)
    }

    override fun setProgressIndicator(isActive: Boolean) {
        if (isActive) {
            ProgressBarManager.getInstance().showProgress()
        } else {
            ProgressBarManager.getInstance().hideProgress()
        }
    }

    override fun showToastMessage(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val MIN_SEARCH_TERM_LENGTH = 3
        private var isSearchMode = false
    }
}