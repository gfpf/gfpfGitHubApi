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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUser
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUserContract
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserShowcaseFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserCardItemDecoration
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserRecyclerViewAdapter
import com.beblue.gfpf.test.bebluegfpftest.util.ProgressBarManager
import com.beblue.gfpf.test.bebluegfpftest.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserShowcaseFrag : Fragment(), GHUserContract.View,
    UserRecyclerViewAdapter.UserRecyclerViewClickListener {

    /*@Inject
    lateinit var binding: UserShowcaseFragBinding*/
    private lateinit var binding: UserShowcaseFragBinding
    //private val mGHUserViewModel: UserViewModel by viewModels()
    private val mGHUserViewModel: UserViewModel by activityViewModels()


    @Inject
    lateinit var mAdapter: UserRecyclerViewAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserShowcaseFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
        registerObservers()
    }

    override fun onStart() {
        super.onStart()
        Log.d("GFPF", "-onStart -ShowcaseFragment -DataCount: ${mAdapter.itemCount}")

        if (mAdapter.itemCount == 0) {
            doLoadAll()
        } else {
            // TODO GFPF - Make all observers as SingleEvent and handle back nav data
            //showGHUserListUI(mAdapter.getItems(), false)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("GFPF", "-onResume -ShowcaseFragment -DataCount: ${mAdapter.itemCount}")
    }

    private fun initSetup() {
        updateTitle()
        setupSwipeRefresh()
        setupRecyclerView()
        setupSearchView()
    }

    private fun registerObservers() {
        // TODO GFPF - Apply observe extension method and use state like marvel project
        //observe(viewModel.state, ::onViewStateChange)

        //All Users
        mGHUserViewModel.allUsers.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                showGHUserListUI(result, false)
            } else {
                showGHUserListUI(emptyList(), true)
            }
            setProgressIndicator(false)
        }

        // TODO GFPF - Make all observers as SingleEvent and handle back nav data
        //Search
        mGHUserViewModel.searchResult.observe(viewLifecycleOwner) { ghSearchUser ->
            if (ghSearchUser != null && ghSearchUser.users.isNotEmpty()) {
                showGHUserListUI(ghSearchUser.users, false)
            } else {
                showGHUserListUI(emptyList(), false)
            }
            setProgressIndicator(false)
        }
        // User
        mGHUserViewModel.userDetail.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { user ->
                showGHUserDetailUI(user)
            }
            setProgressIndicator(false)
        }
    }

    private fun updateTitle() {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.nav_header_search)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            doLoadAll()
            clearSearchView()
            animateSearchView()
        }
        ProgressBarManager.init(binding.swipeRefresh)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        val smallPadding = resources.getDimensionPixelSize(R.dimen.nav_header_vertical_spacing)
        val itemDecoration = UserCardItemDecoration(smallPadding, smallPadding)
        binding.recyclerView.addItemDecoration(itemDecoration)

        if (!::mAdapter.isInitialized) {
            mAdapter = UserRecyclerViewAdapter(requireContext(), this)
        }
        //mAdapter = UserRecyclerViewAdapter(requireContext(), this)
        binding.recyclerView.adapter = mAdapter

        /*if (mAdapter.itemCount == 0) {
            doLoadAll()
        } else {
            showGHUserListUI(mAdapter.getItems(), false)
        }*/
    }

    fun scrollToTop() {
        layoutManager.let {
            if (it.findFirstVisibleItemPosition() == 0) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.scroll_up_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                binding.recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupSearchView() {
        val searchViewPlateId = binding.searchView.context.resources.getIdentifier(
            "android:id/search_src_text",
            null,
            null
        )
        val searchPlateEditText: EditText = binding.searchView.findViewById(searchViewPlateId)
        searchPlateEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchTerm = view.text.toString()
                if (!TextUtils.isEmpty(searchTerm)) {
                    if (searchTerm.length >= minSearchTermLength) {
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
        binding.searchView.setQuery("", false)
        binding.searchView.clearFocus()
        binding.searchView.onActionViewCollapsed()
    }

    private fun animateSearchView() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_bounce_in_right)
        binding.searchView.startAnimation(animation)
    }

    private fun doLoadAll() {
        setProgressIndicator(true)
        mGHUserViewModel.loadAllUsers()
    }

    private fun doSearch(searchTerm: String) {
        Util.hideKeyboard(requireActivity())
        setProgressIndicator(true)
        mGHUserViewModel.searchUserByName(searchTerm, false)
    }

    override fun showGHUserListUI(users: List<GHUser>?, isAppend: Boolean) {
        if (users.isNullOrEmpty()) {
            binding.noResultsLabel.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.noResultsLabel.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
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
        mGHUserViewModel.loadUserById(selectedUser.id)
    }

    override fun showGHUserDetailUI(requestedUser: GHUser) {
        val bundle = Bundle().apply {
            putSerializable(GHUser.REQUESTED_USER_KEY, requestedUser)
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
        // Implementation for showing a toast message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove all observers to prevent updates after the fragment is destroyed
        /*mGHUserViewModel.allUsers.removeObservers(viewLifecycleOwner)
        mGHUserViewModel.searchResult.removeObservers(viewLifecycleOwner)
        mGHUserViewModel.userDetail.removeObservers(viewLifecycleOwner)*/
    }

    companion object {
        private const val minSearchTermLength = 3
    }
}