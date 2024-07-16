package com.beblue.gfpf.test.bebluegfpftest.util

import android.app.ProgressDialog
import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ProgressBarManager private constructor() {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressDialog: ProgressDialog? = null
    private var progressCircleDiameter = 0
    private var selectedOffset: Offset = Offset.Top

    // TODO GFPF - Verify why setProgressViewOffset is overriding the isRefreshing property
    /*init {
        swipeRefreshLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    swipeRefreshLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    progressCircleDiameter = swipeRefreshLayout.progressCircleDiameter
                    updateProgressPosition(selectedOffset)
                }
            }
        )
    }*/

    fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout) {
        synchronized(this) {
            this.swipeRefreshLayout = swipeRefreshLayout
            //setupProgressViewOffset()
        }
    }

    fun setProgressDialog(context: Context) {
        synchronized(this) {
            progressDialog = ProgressDialog(context).apply {
                setMessage("Loading...")
                setCancelable(false)
            }
        }
    }

    fun updateProgressPosition(offset: Offset) {
        selectedOffset = offset
        val circleOffset = calculateOffset(offset)
        // TODO GFPF - Verify why setProgressViewOffset is overriding the isRefreshing property
        // Update progress view to be vertically centered based on the calculated circleOffset
        swipeRefreshLayout?.setProgressViewOffset(false, 0, circleOffset)
    }

    private fun calculateOffset(offset: Offset): Int {
        return when (offset) {
            Offset.Center -> (swipeRefreshLayout?.height ?: (0 - progressCircleDiameter)) / 2
            Offset.Top -> (swipeRefreshLayout?.height ?: 0) / 8 - progressCircleDiameter / 2
            Offset.Bottom -> (swipeRefreshLayout?.height ?: 0) * 2 / 3 - progressCircleDiameter / 2
        }
    }

    fun showProgress() {
        synchronized(this) {
            swipeRefreshLayout?.isRefreshing = true
            progressDialog?.show()
        }
    }

    fun hideProgress() {
        synchronized(this) {
            swipeRefreshLayout?.isRefreshing = false
            progressDialog?.dismiss()
        }
    }

    // Enum to represent different offsets
    enum class Offset {
        Center, Top, Bottom
    }

    companion object {
        @Volatile
        private var instance: ProgressBarManager? = null

        @JvmStatic
        fun init(swipeRefreshLayout: SwipeRefreshLayout) {
            synchronized(this) {
                if (instance == null) {
                    instance = ProgressBarManager()
                }
                instance?.setSwipeRefreshLayout(swipeRefreshLayout)
            }
        }

        @JvmStatic
        fun init(context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = ProgressBarManager()
                }
                instance?.setProgressDialog(context)
            }
        }

        @JvmStatic
        fun getInstance(): ProgressBarManager {
            return instance
                ?: throw IllegalStateException("ProgressBarManager is not initialized, call init() method first.")
        }
    }
}
