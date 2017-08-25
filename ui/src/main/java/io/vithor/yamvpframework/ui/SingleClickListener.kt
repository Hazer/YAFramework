package io.vithor.yamvpframework.ui

import android.view.View
import android.view.ViewConfiguration

/**
 * Created by Vithorio Polten on 2/22/16.
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

    private val lastClickTimeout: Long
        get() {
            return System.currentTimeMillis() - lastClick
        }

    override fun onClick(v: View) {
        if (lastClickTimeout > DOUBLE_CLICK_TIMEOUT) {
            lastClick = System.currentTimeMillis()
            click(v)
        }
    }
}

/**
 * Click listener setter that prevents double click on the view it´s set
 */
fun View.singleClick(listener: (View?) -> Unit){
    setOnClickListener(SingleClickListener(listener))
}