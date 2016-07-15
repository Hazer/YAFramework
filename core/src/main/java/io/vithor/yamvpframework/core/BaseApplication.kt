package io.vithor.yamvpframework.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import de.halfbit.tinybus.TinyBus


/**
 * Created by Vithorio Polten on 12/31/15.
 */
abstract class BaseApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    val bus: TinyBus by lazy { TinyBus.from(this) }

    /*
    protected Decorator[] contextDecorators() {
        return Decorators.getAll();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Decorator[] decorators = contextDecorators();
        if (decorators != null) {
            super.attachBaseContext(DecorContextWrapper.wrap(newBase)
                    .with(decorators));
        } else {
            super.attachBaseContext(newBase);
        }
    }
    */

    override fun onCreate() {
        super.onCreate()
        bus.register(this)
    }

    open protected fun onBecameForeground() {

    }

    open protected fun onBecameBackground() {

    }

    open protected fun onActivityState(activity: Activity?, state: ActivityLifecycleEvent) {

    }

    override fun onActivityStarted(activity: Activity?) {
        activitiesStarted += 1
        if (activitiesStarted == 1) {
            state = State.Foreground
            onBecameForeground()
        }

        onActivityState(activity, ActivityLifecycleEvent.Started)
    }

    override fun onActivityStopped(activity: Activity?) {
        activitiesStarted -= 1
        if (activitiesStarted == 0) {
            state = State.Background
            onBecameBackground()
        }

        onActivityState(activity, ActivityLifecycleEvent.Stopped)
    }

    override fun onActivityResumed(activity: Activity?) {
        onActivityState(activity, ActivityLifecycleEvent.Resumed)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        onActivityState(activity, ActivityLifecycleEvent.SaveInstanceState)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        onActivityState(activity, ActivityLifecycleEvent.Destroyed)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        onActivityState(activity, ActivityLifecycleEvent.Created)
    }

    override fun onActivityPaused(activity: Activity?) {
        onActivityState(activity, ActivityLifecycleEvent.Paused)
    }

    companion object {
        internal var activitiesStarted = 0
            private set

        @JvmStatic val isOnBackground: Boolean
            get() = activitiesStarted == 0

        @JvmStatic var state = State.Background
            private set
    }

    enum class State {
        Foreground,
        Background
    }
}
