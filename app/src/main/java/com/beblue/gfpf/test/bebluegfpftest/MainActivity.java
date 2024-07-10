package com.beblue.gfpf.test.bebluegfpftest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beblue.gfpf.test.bebluegfpftest.databinding.ActivityMainBinding;
import com.beblue.gfpf.test.bebluegfpftest.user.view.MainFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using DataBindingUtil
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("GFPF", "-onStart -MainActivity");

        if (!isShowingBack) {
            changeFragment(new MainFragment(), false, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GFPF", "-onResume -MainActivity");
    }

    private void init() {
        Fresco.initialize(this);

        setSupportActionBar(binding.toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        updateAndroidSecurityProvider(this);
    }

    //TODO GFPF - Remove this method
    //Routine to avoid 'SSL Hand Shake Exception' in devices under API 19 (KitKat)
    //https://stackoverflow.com/questions/29916962/javax-net-ssl-sslhandshakeexception-javax-net-ssl-sslprotocolexception-ssl-han
    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(@NonNull Fragment fragmentTo, boolean isToBackStack, boolean isCustomAnimation) {
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(fragmentTo.getClass().getSimpleName());
        if (existingFragment != null && existingFragment.isVisible()) {
            // Fragment already exists and is visible, no need to add it again
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isToBackStack) {
            if (isCustomAnimation) {
                transaction.setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out
                );
            }
            transaction.replace(R.id.view_holder_container, fragmentTo)
                    .addToBackStack(null);
        } else {
            transaction.replace(R.id.view_holder_container, fragmentTo, MainFragment.MAIN_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    private boolean isShowingBack;
    private View.OnClickListener originalToolbarListener;

    @Override
    public void onBackPressed() {
        if (isShowingBack) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        isShowingBack = (backStackEntryCount > 0);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(isShowingBack);

        if (isShowingBack) {
            hideFABOptions();
        } else {
            showFABOptions();
        }

        Fragment fragmentTo = getCurrentTopFragment(getSupportFragmentManager());
        if (fragmentTo instanceof MainFragment) {
            MainFragment mainFragment = (MainFragment) fragmentTo;
            mainFragment.updateTitle();
        }
    }

    public static Fragment getCurrentTopFragment(FragmentManager fm) {
        int stackCount = fm.getBackStackEntryCount();

        if (stackCount > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(stackCount - 1);
            return fm.findFragmentByTag(backEntry.getName());
        } else {
            List<Fragment> fragments = fm.getFragments();
            if (!fragments.isEmpty()) {
                for (Fragment f : fragments) {
                    if (f != null && !f.isHidden()) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private void hideFABOptions() {
        binding.fab.setVisibility(View.GONE);
    }

    private void showFABOptions() {
        binding.fab.setVisibility(View.VISIBLE);
    }
}
