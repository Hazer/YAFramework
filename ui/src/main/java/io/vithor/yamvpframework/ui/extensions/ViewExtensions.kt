package io.vithor.yamvpframework.ui.extensions

import android.R
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.TextView

/**
 * Created by Vithorio Polten on 2/22/16.
 */

val TextView.compoundDrawableLeft: Drawable
    get() {
        return compoundDrawables[0]
    }

val TextView.compoundDrawableRight: Drawable
    get() {
        return compoundDrawables[2]
    }

fun View.animateEnter() = animateTranslationY(0, DecelerateInterpolator(3f))
fun View.animateExit() = animateTranslationY(-height, AccelerateInterpolator(3f))

fun View.animateTranslationY(translationY: Int, interpolator: Interpolator) {
    with(ObjectAnimator.ofFloat(this, "translationY", translationY.toFloat())) {
        duration = context.resources.getInteger(R.integer.config_mediumAnimTime).toLong()
        setInterpolator(interpolator)
        start()
    }
}

fun View.animateTranslationX(delay: Long, duration: Long, translationX: Float, interpolator: Interpolator = FastOutLinearInInterpolator()) {
    animate()
            .setStartDelay(delay)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .translationX(translationX)
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    super.onAnimationEnd(animation)
//                    //                        mAnimatingFab = false
//                }
//            })
            .start()
}