package io.vithor.yamvpframework;

import android.app.Application;
import android.content.Context;

import com.mounacheikhna.decor.DecorContextWrapper;
import com.mounacheikhna.decor.Decorator;
import com.mounacheikhna.decorators.Decorators;

/**
 * Created by Hazer on 12/31/15.
 */
public class BaseApplication extends Application {

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
}
