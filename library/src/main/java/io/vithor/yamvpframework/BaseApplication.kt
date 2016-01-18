package io.vithor.yamvpframework

import android.app.Application

import de.halfbit.tinybus.TinyBus


/**
 * Created by Hazer on 12/31/15.
 */
public open class BaseApplication : Application() {
    private var mBus: TinyBus? = null

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
        mBus = TinyBus.from(this)
        mBus!!.register(this)
    }
}
