package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserDetailFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserCardItemDecoration
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserDetailFrag
import com.beblue.gfpf.test.bebluegfpftest.util.updateActionBarTitle
import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHUser
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserShowcaseFrag
import com.beblue.gfpf.test.bebluegfpftest.util.ProgressBarManager
import com.gfpf.github_api.domain.user.GHTag
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import me.relex.photodraweeview.PhotoDraweeView
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailFrag : Fragment(), IUserDetailFrag.View, View.OnClickListener {

    private lateinit var mBinding: UserDetailFragBinding
    private val mGHUserViewModel: UserViewModel by viewModels()
    private lateinit var mActionListener: IUserDetailFrag.ActionListener
    private var mUser: GHUser? = null
    private lateinit var mReposAdapter: ArrayAdapter<String>
    //private lateinit var mLayoutManager: LinearLayoutManager

    //TODO GFPF - Fix this Inject - UserDetailModule
    //@Inject
    //lateinit var mActionListener: IUserDetailFrag.ActionListener



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = UserDetailFragBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
        registerObservers()
        //mActionsListener = mGHUserViewModel
    }

    override fun onStart() {
        super.onStart()
        Log.d("GFPF", "-onStart -DetailFragment")
        showRequestedItem()
    }

    private fun initSetup() {
        mActionListener = mGHUserViewModel
        (activity as? AppCompatActivity)?.updateActionBarTitle(getString(R.string.details))
        mBinding.btnExpandSpecs.setOnClickListener(this)
        mBinding.scrollView.fullScroll(View.FOCUS_UP)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        if (!::mReposAdapter.isInitialized) {
            // Initialize an ArrayAdapter with a simple layout for items
            mReposAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        }

        mBinding.recyclerViewUserRepos.apply {
            // Set layoutManager once
            if (layoutManager == null) {
                //It can be added from file resources - app:layoutManager="LinearLayoutManager"
                //mLayoutManager = LinearLayoutManager(context)
                //layoutManager = mLayoutManager

                val smallPadding =
                    resources.getDimensionPixelSize(R.dimen.nav_header_vertical_spacing)
                val itemDecoration = UserCardItemDecoration(smallPadding, smallPadding)
                addItemDecoration(itemDecoration)
            }
            setHasFixedSize(true)
            // Create a simple RecyclerView.Adapter
            adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(android.R.layout.simple_list_item_1, parent, false)
                    return object : RecyclerView.ViewHolder(view) {}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    (holder.itemView as? TextView)?.text = mReposAdapter.getItem(position)

                    setItemSelector(holder.itemView)

                    // Set click listener for each item
                    holder.itemView.setOnClickListener {
                        val selectedRepo = mReposAdapter.getItem(position)
                        // Handle item click action here, e.g., navigate to detail fragment
                        Toast.makeText(context, "Clicked on: $selectedRepo", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun getItemCount(): Int {
                    return mReposAdapter.count
                }

                private fun setItemSelector(itemView: View) {
                    val outValue = TypedValue()
                    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                    val drawableResId = outValue.resourceId
                    // Set the background drawable
                    itemView.setBackgroundResource(drawableResId)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun registerObservers() {
        mGHUserViewModel.userRepos.observe(viewLifecycleOwner) { ghRepos ->
            Toast.makeText(context, "Repos: ${ghRepos.size}", Toast.LENGTH_SHORT).show()
            setProgressIndicator(false)
            showUserRepoListUI(ghRepos, false)
        }

        mGHUserViewModel.repositoryTags.observe(viewLifecycleOwner) { ghTags ->
            Toast.makeText(context, "Tags: ${ghTags.size}", Toast.LENGTH_SHORT).show()
            setProgressIndicator(false)
            showUserTagListUI(ghTags)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showUserRepoListUI(ghRepos: List<GHRepository>?, isAppend: Boolean) {
        if (ghRepos.isNullOrEmpty()) {
            mBinding.noReposResultsLabel.visibility = View.VISIBLE
            mBinding.recyclerViewUserRepos.visibility = View.GONE
        } else {
            mBinding.noReposResultsLabel.visibility = View.GONE
            mBinding.recyclerViewUserRepos.visibility = View.VISIBLE

            if (!isAppend) {
                // Clear previous data in ArrayAdapter
                mReposAdapter.clear()
            }
            // Update the adapter with the new data
            mReposAdapter.addAll(ghRepos.map { repo -> repo.name })
            // Notify RecyclerView's adapter of data change
            mBinding.recyclerViewUserRepos.adapter?.notifyDataSetChanged()
        }
    }

    override fun showUserTagListUI(ghTags: List<GHTag>?) {
        TODO("Not yet implemented")
    }

    private fun showRequestedItem() {
        val bundle = arguments
        //TODO GFPF - FIX THIS
        mUser = bundle?.getSerializable(GHUser.REQUESTED_USER_DETAIL_KEY) as GHUser?

        Picasso.get()
            .load(mUser?.avatarUrl)
            .placeholder(R.drawable.ic_thumbnail)
            .into(mBinding.userPhoto)
        mBinding.userPhoto.setOnClickListener { showImage(mUser?.avatarUrl) }
        mBinding.userName.text = mUser?.name
        mBinding.userLogin.text = mUser?.login
        mBinding.userGhurl.text = mUser?.ghUrl
    }

    private fun showImage(photoUri: String?) {
        val builder = Dialog(requireContext(), android.R.style.Theme_Light)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //builder.window?.setBackgroundDrawable(ColorDrawable(Color.RED))

        builder.setOnDismissListener {}

        val imageView = PhotoDraweeView(context)
        imageView.setPhotoUri(Uri.parse(photoUri))
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        builder.show()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_expand_specs) {
            if (mBinding.viewSpecs.visibility == View.GONE) {
                mBinding.viewSpecs.visibility = View.VISIBLE

                //Load user repos
                mUser?.login?.let { doLoadUserRepos(it) }

                //binding?.scrollView?.scrollTo(0, binding?.scrollView?.bottom ?: 0)
                //scroll()
                //expand(binding?.viewSpecs)
            } else {
                mBinding.viewSpecs.visibility = View.GONE
                //collapse(binding?.viewSpecs)
            }
        }
    }

    private fun doLoadUserRepos(username: String) {
        setProgressIndicator(true)
        mActionListener.loadUserRepos(username)
    }

    private fun doLoadRepoTags(owner: String, repo: String) {
        mActionListener.loadRepoTags("gfpf", "gfpfTwitterSearcher")
    }

    fun scroll() {
        mBinding.scrollView.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val scrollViewHeight = mBinding.scrollView.height
                if (scrollViewHeight > 0) {
                    mBinding.scrollView.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                    val lastView = mBinding.scrollView.getChildAt(
                        mBinding.scrollView.childCount.minus(1)
                    )
                    val lastViewBottom =
                        (lastView?.bottom ?: 0) + mBinding.scrollView.paddingBottom
                    val deltaScrollY =
                        (lastViewBottom - scrollViewHeight) - mBinding.scrollView.scrollY

                    /* If you want to see the scroll animation, call this. */
                    //binding?.scrollView?.smoothScrollBy(0, deltaScrollY)
                    mBinding.scrollView.smoothScrollTo(0, deltaScrollY)
                    /* If you don't want, call this. */
                    //binding?.scrollView?.scrollBy(0, deltaScrollY)
                }
            }
        })
    }

    companion object {
        private fun expand(v: View) {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val targetHeight = v.measuredHeight

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.layoutParams.height = 1
            v.visibility = View.VISIBLE

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    v.layoutParams.height = if (interpolatedTime == 1f)
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    else
                        (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // 1dp/ms
            a.duration =
                (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
            v.startAnimation(a)
        }

        private fun collapse(v: View) {
            val initialHeight = v.measuredHeight

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // 1dp/ms
            a.duration =
                (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
            v.startAnimation(a)
        }
    }

    override fun setProgressIndicator(isActive: Boolean) {
        if (isActive) {
            mBinding.progressBar.visibility = View.VISIBLE
        } else {
            mBinding.progressBar.visibility = View.GONE
        }
    }

    override fun showToastMessage(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}