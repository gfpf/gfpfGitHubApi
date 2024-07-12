package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.databinding.ContentDetailedFragBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import me.relex.photodraweeview.PhotoDraweeView;

public class DetailedFragment extends Fragment implements View.OnClickListener {

    private ContentDetailedFragBinding binding;

    private GHUserContract.UserActionsListener mActionsListener;

    private GHUser mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ContentDetailedFragBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.details);

        binding.btnExpandSpecs.setOnClickListener(this);

        showRequestedItem();

        binding.scrollView.fullScroll(View.FOCUS_UP);
        return rootView;
    }

    private void showRequestedItem() {
        Bundle bundle = getArguments();
        mUser = (GHUser) bundle.get(GHUser.REQUESTED_USER_KEY);

        Picasso.get()
                .load(mUser.getAvatarUrl())
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.userPhoto);
        binding.userPhoto.setOnClickListener(v -> showImage(mUser.getAvatarUrl()));
        binding.userName.setText(mUser.getName());
        binding.userLogin.setText(mUser.getLogin());
        binding.userGhurl.setText(mUser.getGHUrl());
    }

    public void showImage(String photoUri) {
        Dialog builder = new Dialog(requireContext(), android.R.style.Theme_Light);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));*/

        builder.setOnDismissListener(dialogInterface -> {
            //nothing;
        });

        PhotoDraweeView imageView = new PhotoDraweeView(getContext());
        imageView.setPhotoUri(Uri.parse(photoUri));

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_expand_specs) {
            if (binding.viewSpecs.getVisibility() == View.GONE) {
                binding.viewSpecs.setVisibility(View.VISIBLE);
                //binding.scrollView.scrollTo(0, binding.scrollView.getBottom());
                //scroll();
                //expand(viewSpecs);
            } else {
                binding.viewSpecs.setVisibility(View.GONE);
                //collapse(viewSpecs);
            }
        }

    }

    public void scroll() {
        binding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int scrollViewHeight = binding.scrollView.getHeight();
                if (scrollViewHeight > 0) {
                    binding.scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    final View lastView = binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);
                    final int lastViewBottom = lastView.getBottom() + binding.scrollView.getPaddingBottom();
                    final int deltaScrollY = lastViewBottom - scrollViewHeight - binding.scrollView.getScrollY();
                    /* If you want to see the scroll animation, call this. */
                    //binding.scrollView.smoothScrollBy(0, deltaScrollY);
                    binding.scrollView.smoothScrollTo(0, deltaScrollY);
                    /* If you don't want, call this. */
                    //binding.scrollView.scrollBy(0, deltaScrollY);
                }
            }
        });
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}