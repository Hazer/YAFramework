package io.vithor.yamvpframework.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import de.halfbit.tinybus.TinyBus


/**
 * Created by Vithorio Polten on 12/31/15.
 */
open class BaseApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
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
        activityState = ActivityLifecycleEvent.None
        bus.register(this)
    }

    override fun onActivityStarted(activity: Activity?) {
        activityState = ActivityLifecycleEvent.Started
    }

    override fun onActivityStopped(activity: Activity?) {
        activityState = ActivityLifecycleEvent.Stopped
    }

    override fun onActivityResumed(activity: Activity?) {
        activityState = ActivityLifecycleEvent.Resumed
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        activityState = ActivityLifecycleEvent.SaveInstanceState
    }

    override fun onActivityDestroyed(activity: Activity?) {
        activityState = ActivityLifecycleEvent.Destroyed
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activityState = ActivityLifecycleEvent.Created
    }

    override fun onActivityPaused(activity: Activity?) {
        activityState = ActivityLifecycleEvent.Paused
    }

    companion object {
        var activityState = ActivityLifecycleEvent.None
            private set
    }
}
