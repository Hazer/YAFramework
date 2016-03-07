package io.vithor.yamvpframework.extensions

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import io.vithor.yamvpframework.SingleClickListener

/**
 * Created by Vithorio Polten on 2/22/16.
 */

fun View.animateEnter() = animateTranslationY(0, DecelerateInterpolator(3f))
fun View.animateExit() = animateTranslationY(-height, AccelerateInterpolator(3f))

fun View.animateTranslationY(translationY: Int, interpolator: Interpolator) {
    with(ObjectAnimator.ofFloat(this, "translationY", translationY.toFloat())) {
        duration = context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        setInterpolator(interpolator)
        start()
    }
}

/**
 * Click listener setter that prevents double click on the view it´s set
 */
fun View.singleClick(listener: (android.view.View?) -> Unit){
    setOnClickListener(SingleClickListener(listener))
}