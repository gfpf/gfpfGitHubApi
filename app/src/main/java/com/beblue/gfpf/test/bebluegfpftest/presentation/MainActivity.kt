package com.beblue.gfpf.test.bebluegfpftest.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.beblue.gfpf.test.bebluegfpftest.R
import com.beblue.gfpf.test.bebluegfpftest.databinding.MainActivityBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.UserShowcaseFrag
import com.beblue.gfpf.test.bebluegfpftest.util.FabManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private var mainFragment: UserShowcaseFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        init()
    }

    override fun onStart() {
        super.onStart()
        Log.d("GFPF", "-onStart -MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.d("GFPF", "-onResume -MainActivity")
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        Fresco.initialize(this)
        setupNavigation()
        updateAndroidSecurityProvider(this)

        binding.fab.setOnClickListener { view ->
            mainFragment?.let {
                Snackbar.make(view, getString(R.string.scroll_up_hint), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
                it.scrollToTop()
            }
        }
    }

    private fun setupNavigation() {
        FabManager.init(binding.fab)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.user_showcase_frag) {
                FabManager.getInstance().showFab()
                mainFragment = navHostFragment.childFragmentManager.fragments[0] as UserShowcaseFrag
            } else {
                FabManager.getInstance().hideFab()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("SecurityException", "Google Play Services not available.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
