package io.vithor.yamvpframework.presenter

import android.util.SparseArray

import com.orhanobut.logger.Logger
import io.vithor.yamvpframework.presenter.sketch.Sketch
import kotlin.reflect.KClass

/**
 * Created by hazer on 5/6/15.
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
        bucket.put(presenter.javaClass.kotlin.hashCode(), presenter)
    }

    //    fun release(presenter: BasePresenter<*>) {
    //        release(presenter)
    //    }
    //    inline fun <reified PT : Sketch> release(presenter: BasePresenter<PT>) {
    //        release(presenter.javaClass.kotlin)
    //    }
    //
    //    inline fun <reified PT : Sketch> release(presenterClass: KClass<out BasePresenter<PT>>) {
    //        Logger.d("Class: ${presenterClass.java.simpleName} - ${presenterClass.java.hashCode()}")
    //        bucket.remove(presenterClass.java.hashCode())
    //    }

    fun getRetainedInstance(presenterClass: KClass<out BasePresenter<out Sketch>>): BasePresenter<*>? {
//        Logger.d("Class: ${presenterClass.java.simpleName} - ${presenterClass.java.hashCode()}")
        return bucket.get(presenterClass.java.hashCode())
    }

    fun <SK : Sketch> release(presenterClass: KClass<BasePresenter<SK>>) {
//        Logger.d("Class: ${presenterClass.java.simpleName} - ${presenterClass.java.hashCode()}")
        bucket.remove(presenterClass.java.hashCode())
    }
}
