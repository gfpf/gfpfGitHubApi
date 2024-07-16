package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserDetailFragBinding
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserShowcaseFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserRecyclerViewAdapter
import com.beblue.gfpf.test.bebluegfpftest.util.updateActionBarTitle
import com.gfpf.github_api.domain.user.GHUser
import com.gfpf.github_api.domain.user.GHUserContract
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import me.relex.photodraweeview.PhotoDraweeView
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailFrag : Fragment(), View.OnClickListener {

    private lateinit var binding: UserDetailFragBinding
    private val mGHUserViewModel: UserViewModel by viewModels()
    private var mUser: GHUser? = null

    //@Inject
    //lateinit var mAdapter: UserRecyclerViewAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    private var mActionsListener: GHUserContract.UserActionsListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragBinding.inflate(inflater, container, false)
        //binding.btnExpandSpecs.setOnClickListener(this)
        //showRequestedItem()
        //binding.scrollView.fullScroll(View.FOCUS_UP)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
    }

    override fun onStart() {
        super.onStart()
        Log.d("GFPF", "-onStart -DetailFragment")
        showRequestedItem()
    }

    private fun initSetup() {
        (activity as? AppCompatActivity)?.updateActionBarTitle(getString(R.string.details))
        binding.btnExpandSpecs.setOnClickListener(this)
        binding.scrollView.fullScroll(View.FOCUS_UP)

        //setupRecyclerView()
        registerObservers()
    }

    private fun registerObservers() {
        mGHUserViewModel.userRepos.observe(viewLifecycleOwner) { ghRepos ->
            Toast.makeText(context, "Repos: ${ghRepos.size}", Toast.LENGTH_SHORT).show()

            //setProgressIndicator(false)
        }

        mGHUserViewModel.repositoryTags.observe(viewLifecycleOwner) { ghTags ->
            //setProgressIndicator(false)
        }
    }

    private fun showRequestedItem() {
        val bundle = arguments
        //TODO GFPF - FIX THIS
        mUser = bundle?.getSerializable(GHUser.REQUESTED_USER_DETAIL_KEY) as GHUser?

        Picasso.get()
            .load(mUser?.avatarUrl)
            .placeholder(R.drawable.ic_thumbnail)
            .into(binding.userPhoto)
        binding.userPhoto.setOnClickListener { showImage(mUser?.avatarUrl) }
        binding.userName.text = mUser?.name
        binding.userLogin.text = mUser?.login
        binding.userGhurl.text = mUser?.ghUrl
    }

    private fun showImage(photoUri: String?) {
        val builder = Dialog(requireContext(), android.R.style.Theme_Light)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.RED))

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
            if (binding.viewSpecs.visibility == View.GONE) {
                binding.viewSpecs.visibility = View.VISIBLE

                //Load user repos
                mUser?.login?.let { doLoadUserRepos(it) }

                //binding?.scrollView?.scrollTo(0, binding?.scrollView?.bottom ?: 0)
                //scroll()
                //expand(binding?.viewSpecs)
            } else {
                binding.viewSpecs.visibility = View.GONE
                //collapse(binding?.viewSpecs)
            }
        }
    }

    private fun doLoadUserRepos(username: String) {
        mGHUserViewModel.loadUserRepos(username)
    }

    private fun doLoadRepoTags(owner: String, repo: String) {
        mGHUserViewModel.loadRepoTags("gfpf", "gfpfTwitterSearcher")
    }


    fun scroll() {
        binding.scrollView.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val scrollViewHeight = binding.scrollView.height
                if (scrollViewHeight > 0) {
                    binding.scrollView.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                    val lastView = binding.scrollView.getChildAt(
                        binding.scrollView.childCount.minus(1)
                    )
                    val lastViewBottom =
                        (lastView?.bottom ?: 0) + binding.scrollView.paddingBottom
                    val deltaScrollY =
                        (lastViewBottom - scrollViewHeight) - binding.scrollView.scrollY

                    /* If you want to see the scroll animation, call this. */
                    //binding?.scrollView?.smoothScrollBy(0, deltaScrollY)
                    binding.scrollView.smoothScrollTo(0, deltaScrollY)
                    /* If you don't want, call this. */
                    //binding?.scrollView?.scrollBy(0, deltaScrollY)
                }
            }
        })
    }

    companion object {
        fun expand(v: View) {
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
}