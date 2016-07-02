package io.vithor.yamvpframework.ui

import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.EditText
import android.widget.TextView

/**
 * Created by Vithorio Polten on 3/1/16.
 */

class SingleTouchEditorActionListener(val action: (v: View?, actionId: Int, event: KeyEvent?) -> Boolean) : TextView.OnEditorActionListener {
    companion object {
        private val DOUBLE_CLICK_TIMEOUT = ViewConfiguration.getDoubleTapTimeout()
    }

    private var lastClick: Long = 0

    private fun getLastClickTimeout(): Long {
        return System.currentTimeMillis() - lastClick
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (getLastClickTimeout() > DOUBLE_CLICK_TIMEOUT) {
            lastClick = System.currentTimeMillis()
            return action(v, actionId, event)
        }
        return true
    }
}

/**
 * Click listener setter that prevents double click on the view it´s set
 */
fun EditText.singleAction(listener: (v: View?, actionId: Int, event: KeyEvent?) -> Boolean) {
    setOnEditorActionListener(SingleTouchEditorActionListener(listener))
}