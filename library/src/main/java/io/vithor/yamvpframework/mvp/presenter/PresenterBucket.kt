package io.vithor.yamvpframework.mvp.presenter

import android.util.SparseArray

import com.orhanobut.logger.Logger
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import kotlin.reflect.KClass

/**
 * Created by Vithorio Polten on 5/6/15.
 */
internal object PresenterBucket {
    val bucket = SparseArray<BasePresenter<*>>(2)

    /**
     * Add Presenter to Bucket. Now it will not be destroyed on orientation change.
     * DON'T FORGET TO RELEASE IT LATER!!!
     * @param presenter Presenter to persist in memory.
     */
    fun <SK : Sketch> add(presenter: BasePresenter<SK>) {
//        Logger.d("Class: ${presenter.javaClass.kotlin.simpleName} - ${presenter.javaClass.kotlin.hashCode()}")
        PresenterBucket.bucket.put(presenter.javaClass.kotlin.hashCode(), presenter)
    }

    fun getRetainedInstance(presenterClass: KClass<out BasePresenter<out Sketch>>): BasePresenter<*>? {
//        Logger.d("Class: ${presenterClass.java.simpleName} - ${presenterClass.java.hashCode()}")
        return PresenterBucket.bucket.get(presenterClass.java.hashCode())
    }

    fun <SK : Sketch> release(presenterClass: KClass<BasePresenter<SK>>) {
//        Logger.d("Class: ${presenterClass.java.simpleName} - ${presenterClass.java.hashCode()}")
        PresenterBucket.bucket.remove(presenterClass.java.hashCode())
    }
}
