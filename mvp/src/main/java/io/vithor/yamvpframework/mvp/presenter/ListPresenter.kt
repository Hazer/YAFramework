package io.vithor.yamvpframework.mvp.presenter

import io.vithor.yamvpframework.mvp.ResponseContainer
import io.vithor.yamvpframework.mvp.presenter.sketch.ListSketch

/**
 * Created by Vithorio Polten on 5/5/15.
 */
abstract class ListPresenter<SK : ListSketch<ModelType>, ModelType, ResponseType> : BasePresenter<SK>() {

    override fun onViewAttached() {
        loadData(PresenterAction.INITIAL_LOAD)
    }

    protected fun defaultHandler(action: PresenterAction): PresenterCallback<ModelType, ResponseType> {
        return object : PresenterCallback<ModelType, ResponseType>(this, action) {
            override fun success(t: ModelType, response: ResponseContainer<ResponseType>, action: PresenterAction) {
                successHandler(action, t)
            }
        }
    }

    protected abstract fun loadData(action: PresenterAction)

    protected fun showLoading(action: PresenterAction) {
        val view = view
        view?.showLoading(action)

    }


    protected fun successHandler(action: PresenterAction, model: ModelType) {
        val view = view
        if (view != null) {
            when (action) {
                PresenterAction.PULL_TO_REFRESH -> view.onRefreshCompleted(model)
                PresenterAction.PAGINATE_FORWARD -> view.onMoreDataFetched(model)
                else -> {
                    view.showData(model)
                    view.showContent()
                }
            }
        }
    }

    //@Throws(ViewDetachedException::class)
    //override fun handleRestFailure(error: ErrorContainer, action: PresenterAction) {
        //Logger.e(error.getError(), "RestError")
        //        RetrofitError retrofitError = (RetrofitError) error.getError();

        //        switch (retrofitError.getKind()) {
        //            case NETWORK:
        //                getView().showNetworkError(action == PresenterAction.PULL_TO_REFRESH);
        //                break;
        //            case CONVERSION:
        //                getView().showServerError(action == PresenterAction.PULL_TO_REFRESH);
        //                break;
        //            default:
        //                getView().showServerError(action == PresenterAction.PULL_TO_REFRESH);
        //        }
    //}
}
