package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beblue.gfpf.test.bebluegfpftest.R;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;

public class DetailedFragment extends Fragment implements View.OnClickListener {

    private GHUserContract.UserActionsListener mActionsListener;

    private GHUser mUser;

    @BindView(R.id.user_photo)
    ImageView imgUserPhoto;

    @BindView(R.id.user_name)
    AppCompatTextView txtUsername;

    @BindView(R.id.user_login)
    AppCompatTextView txtUserLogin;

    @BindView(R.id.user_ghurl)
    AppCompatTextView txtUserGHUrl;

    @BindView(R.id.view_specs)
    View viewSpecs;

    @BindView(R.id.btn_expand_specs)
    AppCompatButton btnExpandSpecs;

    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_detailed_frag, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.details);

        btnExpandSpecs.setOnClickListener(this);

        showRequestedItem();

        scrollView.fullScroll(View.FOCUS_UP);
        return rootView;
    }

    private void showRequestedItem() {
        Bundle bundle = getArguments();
        mUser = (GHUser) bundle.get(GHUser.REQUESTED_USER_KEY);


        //OK
        Picasso.get()
                .load(mUser.getAvatarUrl())
                .placeholder(R.drawable.ic_thumbnail)
                .into(imgUserPhoto);

        imgUserPhoto.setOnClickListener(v -> showImage(mUser.getAvatarUrl()));

        txtUsername.setText(mUser.getName());
        txtUserLogin.setText(mUser.getLogin());
        txtUserGHUrl.setText(mUser.getGHUrl());
    }

    public void showImage(String photoUri) {
        Dialog builder = new Dialog(getContext(), android.R.style.Theme_Light);
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

        switch (id) {
            case R.id.btn_expand_specs:
                if (viewSpecs.getVisibility() == View.GONE) {
                    viewSpecs.setVisibility(View.VISIBLE);
                    //scrollView.scrollTo(0, scrollView.getBottom());

                    //scroll();

                    //expand(viewSpecs);
                } else {
                    viewSpecs.setVisibility(View.GONE);
                    //collapse(viewSpecs);
                }
                break;
        }

    }

    public void scroll() {
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int scrollViewHeight = scrollView.getHeight();
                if (scrollViewHeight > 0) {

                    //TODO IMPLEMENT THE ELSE SCENARIO
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    final View lastView = scrollView.getChildAt(scrollView.getChildCount() - 1);
                    final int lastViewBottom = lastView.getBottom() + scrollView.getPaddingBottom();
                    final int deltaScrollY = lastViewBottom - scrollViewHeight - scrollView.getScrollY();
                    /* If you want to see the scroll animation, call this. */
                    //scrollView.smoothScrollBy(0, deltaScrollY);
                    scrollView.smoothScrollTo(0, deltaScrollY);
                    /* If you don't want, call this. */
                    //scrollView.scrollBy(0, deltaScrollY);
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
