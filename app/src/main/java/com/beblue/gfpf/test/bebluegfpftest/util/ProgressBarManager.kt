package com.beblue.gfpf.test.bebluegfpftest.util

import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ProgressBarManager private constructor(
    private val swipeRefreshLayout: SwipeRefreshLayout,
    //private val progressBar: ProgressBar
) {
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

    fun updateProgressPosition(offset: Offset) {
        selectedOffset = offset
        val circleOffset = calculateOffset(offset)
        // TODO GFPF - Verify why setProgressViewOffset is overriding the isRefreshing property
        // Update progress view to be vertically centered based on the calculated circleOffset
        swipeRefreshLayout.setProgressViewOffset(false, 0, circleOffset)
    }

    private fun calculateOffset(offset: Offset): Int {
        return when (offset) {
            Offset.Center -> (swipeRefreshLayout.height - progressCircleDiameter) / 2
            Offset.Top -> swipeRefreshLayout.height / 8 - progressCircleDiameter / 2
            Offset.Bottom -> swipeRefreshLayout.height * 2 / 3 - progressCircleDiameter / 2
        }
    }

    fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    // Enum to represent different offsets
    enum class Offset {
        Center, Top, Bottom
    }

    companion object {
        @Volatile
        private var instance: ProgressBarManager? = null

        @JvmStatic
        fun init(swipeRefreshLayout: SwipeRefreshLayout): ProgressBarManager {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ProgressBarManager(swipeRefreshLayout)
                    }
                }
            }
            return instance!!
        }

        @JvmStatic
        fun getInstance(): ProgressBarManager {
            return instance
                ?: throw IllegalStateException("ProgressBarManager is not initialized, call init() method first.")
        }
    }
}
