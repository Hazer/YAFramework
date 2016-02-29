package io.vithor.yamvpframework

import android.view.View
import android.view.ViewConfiguration

/**
 * Created by Hazer on 2/22/16.
 */
/**
 * Prevents from double clicks on a view, which could otherwise lead to unpredictable states. Useful
 * while transitioning to another activity for instance.
 */
class SingleClickListener(val click: (v: View) -> Unit) : View.OnClickListener {

    companion object {
        private val DOUBLE_CLICK_TIMEOUT = ViewConfiguration.getDoubleTapTimeout()
    }

    private var lastClick: Long = 0

    override fun onClick(v: View) {
        if (getLastClickTimeout() > DOUBLE_CLICK_TIMEOUT) {
            lastClick = System.currentTimeMillis()
            click(v)
        }
    }

    private fun getLastClickTimeout(): Long {
        return System.currentTimeMillis() - lastClick
    }
}