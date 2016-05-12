package io.vithor.yamvpframework.viewutils.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.support.percent.PercentLayoutHelper
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by zhy on 15/6/30.
 * https://github.com/JulienGenoud/android-percent-support-lib-sample/commit/0f0f52fe962f52b8d8a6044ac4646aab2c67c09d
 */
class PercentLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val mPercentLayoutHelper: PercentLayoutHelper

    init {

        mPercentLayoutHelper = PercentLayoutHelper(this)
    }

    override protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mPercentLayoutHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mPercentLayoutHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override protected fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mPercentLayoutHelper.restoreOriginalParams()
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(getContext(), attrs)
    }


    class LayoutParams : LinearLayout.LayoutParams, PercentLayoutHelper.PercentLayoutParams {
        var percentInfo: PercentLayoutHelper.PercentLayoutInfo? = null

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
            percentInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs)
        }

        override protected fun setBaseAttributes(a: TypedArray, widthAttr: Int, heightAttr: Int) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr)
        }

        constructor(width: Int, height: Int) : super(width, height) {
        }


        constructor(source: ViewGroup.LayoutParams) : super(source) {
        }

        constructor(source: MarginLayoutParams) : super(source) {
        }

        override fun getPercentLayoutInfo(): PercentLayoutHelper.PercentLayoutInfo? {
            return percentLayoutInfo
        }
    }

}