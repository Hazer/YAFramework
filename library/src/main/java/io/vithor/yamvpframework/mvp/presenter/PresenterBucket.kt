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
    fun <SK : Sketch> add(tag: String, presenter: BasePresenter<SK>) {
        bucket.put(tag.hashCode(), presenter)
    }

    fun getRetainedInstance(tag: String): BasePresenter<*>? {
        return bucket.get(tag.hashCode())
    }

    fun release(tag: String) {
        bucket.remove(tag.hashCode())
    }

    object Singletons {
        val bucket = SparseArray<BasePresenter<*>>(2)

        /**
         * Add Presenter to Bucket. Now it will not be destroyed on orientation change.
         * DON'T FORGET TO RELEASE IT LATER!!!
         * @param presenter Presenter to persist in memory.
         */
        fun <SK : Sketch> add(presenter: BasePresenter<SK>) {
            bucket.put(presenter.javaClass.kotlin.hashCode(), presenter)
        }

        fun getRetainedInstance(presenterClass: KClass<out BasePresenter<out Sketch>>): BasePresenter<*>? {
            return bucket.get(presenterClass.java.hashCode())
        }

        fun <T: BasePresenter<*>> release(presenterClass: KClass<T>) {
            bucket.remove(presenterClass.java.hashCode())
        }
    }
}
