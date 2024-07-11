package com.beblue.gfpf.test.bebluegfpftest.util

import com.google.android.material.floatingactionbutton.FloatingActionButton

class FabManager private constructor(private val fab: FloatingActionButton) {

    companion object {
        @Volatile
        private var instance: FabManager? = null

        @JvmStatic
        fun init(fab: FloatingActionButton) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = FabManager(fab)
                    }
                }
            }
        }

        @JvmStatic
        fun getInstance(): FabManager {
            return instance
                ?: throw IllegalStateException("FabManager is not initialized")
        }
    }

    fun showFab() {
        fab.show()
    }

    fun hideFab() {
        fab.hide()
    }
}