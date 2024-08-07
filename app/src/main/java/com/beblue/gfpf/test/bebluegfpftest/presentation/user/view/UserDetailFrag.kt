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
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserDetailFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserCardItemDecoration
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserDetailFrag
import com.beblue.gfpf.test.bebluegfpftest.util.ProgressBarManager
import com.beblue.gfpf.test.bebluegfpftest.util.updateActionBarTitle
import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHUser
import com.gfpf.github_api.domain.user.GHTag
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import me.relex.photodraweeview.PhotoDraweeView

@AndroidEntryPoint
class UserDetailFrag : Fragment(), IUserDetailFrag.View, View.OnClickListener {

    private lateinit var mBinding: UserDetailFragBinding
    private val mGHUserViewModel: UserViewModel by viewModels()
    private lateinit var mActionListener: IUserDetailFrag.ActionListener
    private var mUser: GHUser? = null

    //private lateinit var mReposAdapter: ArrayAdapter<String>
    private lateinit var mReposAdapter: ArrayAdapter<GHRepository>

    private lateinit var mLayoutManager: LinearLayoutManager

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
        ProgressBarManager.init(requireContext())
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
                mLayoutManager = LinearLayoutManager(context)
                layoutManager = mLayoutManager // app:layoutManager="LinearLayoutManager"

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
                    val currentRepo = mReposAdapter.getItem(position)
                    (holder.itemView as? TextView)?.text = currentRepo?.name
                    setItemSelector(holder.itemView)
                    // Set click listener for each item
                    holder.itemView.setOnClickListener {
                        //keep the selected item highlighted code
                        holder.itemView.isSelected = true
                        //val selectedRepo = mReposAdapter.getItem(position)
                        //Toast.makeText(context, "Clicked on: ${selectedRepo?.name}", Toast.LENGTH_SHORT).show()
                        doLoadRepoTags(mUser?.login.toString(), currentRepo?.name.toString())
                    }
                }

                override fun getItemCount(): Int {
                    return mReposAdapter.count
                }

                private fun setItemSelector(itemView: View) {
                    val outValue = TypedValue()
                    context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
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
            //Toast.makeText(context, "Repos: ${ghRepos.size}", Toast.LENGTH_SHORT).show()
            showToastMessage("Repos: ${ghRepos.size}")
            setReposProgressIndicator(false)
            showUserRepoListUI(ghRepos, false)
        }

        mGHUserViewModel.repositoryTags.observe(viewLifecycleOwner) { ghTags ->
            //Toast.makeText(context, "Tags: ${ghTags.size}", Toast.LENGTH_SHORT).show()
            showToastMessage("Tags: ${ghTags.size}")
            setTagsProgressIndicator(false)
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
            mReposAdapter.addAll(ghRepos)
            //mReposAdapter.addAll(ghRepos.map { repo -> repo.name })

            // Notify RecyclerView's adapter of data change
            mBinding.recyclerViewUserRepos.adapter?.notifyDataSetChanged()
        }
    }

    override fun showUserTagListUI(ghTags: List<GHTag>?) {
        //Toast.makeText(context, "Tags: ${ghTags?.size}", Toast.LENGTH_SHORT).show()
        showToastMessage("Tags: ${ghTags?.size}")

        // Assuming 'tags' is a list of strings representing tags
        val chipGroupTags = mBinding.chipGroupTags
        // Clear previous data in ChipGroup
        //chipGroup.removeAllViews()

        ghTags?.forEach { tag ->
            val chip = Chip(requireContext())
            chip.text = tag.name
            chip.isClickable = true
            chip.isCheckable = false // Adjust based on your needs
            chipGroupTags.addView(chip)
        }

        // Scroll to the bottom if the list is not empty
        if (chipGroupTags.childCount > 0) {
            scrollToView(chipGroupTags)
            animateView(chipGroupTags)
        }
    }

    private fun animateView(view: View) {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_bounce_in_right)
        view.startAnimation(animation)
    }

    private fun scrollToView(view: View) {
        view.post {
            mBinding.scrollView.smoothScrollTo(0, view.top)
        }
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
        setReposProgressIndicator(true)
        // Clear previous data in ChipGroup
        mBinding.chipGroupTags.removeAllViews()
        mActionListener.loadUserRepos(username)
    }

    private fun doLoadRepoTags(owner: String, repo: String) {
        setTagsProgressIndicator(true)
        // Clear previous data in ChipGroup
        mBinding.chipGroupTags.removeAllViews()
        mActionListener.loadRepoTags(owner, repo)
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

    override fun setReposProgressIndicator(isActive: Boolean) {
        if (isActive) {
            mBinding.progressBar.visibility = View.VISIBLE
        } else {
            mBinding.progressBar.visibility = View.GONE
        }
    }

    override fun setTagsProgressIndicator(isActive: Boolean) {
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
}