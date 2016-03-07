package io.vithor.yamvpframework

import android.support.multidex.MultiDexApplication
import de.halfbit.tinybus.TinyBus


/**
 * Created by Vithorio Polten on 12/31/15.
 */
open class BaseApplication : MultiDexApplication() {
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
}
