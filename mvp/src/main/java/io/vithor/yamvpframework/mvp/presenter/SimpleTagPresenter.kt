package io.vithor.yamvpframework.mvp.presenter

import io.vithor.yamvpframework.mvp.ResponseContainer
import io.vithor.yamvpframework.mvp.presenter.sketch.TypedSketch

/**
 * Created by Vithorio Polten on 5/5/15.
 */
abstract class SimpleTagPresenter<ModelType, SK : TypedSketch<ModelType>, ResponseType>(tag: String) : TagPresenter<SK>(tag) {

    override fun onViewAttached() {
        loadData(PresenterAction.INITIAL_LOAD)
    }

    protected fun defaultHandler(action: PresenterAction): BasePresenter.Callback<ModelType, ResponseType> {
        return object : BasePresenter.Callback<ModelType, ResponseType>(this, action) {
            override fun success(model: ModelType, response: ResponseContainer<ResponseType>, action: PresenterAction) {
                successHandler(action, model, response)
            }
        }
    }

    protected abstract fun loadData(action: PresenterAction)

    protected fun showLoading(action: PresenterAction) {
        view?.showLoading(action)

    }

    protected fun successHandler(action: PresenterAction, model: ModelType, rawResponse: ResponseContainer<ResponseType>) {
        view?.apply {
            showData(model)
            showContent()
        }
    }


    /*    @Override
    public void handleRestFailure(@NonNull ErrorContainer error, @NonNull PresenterAction action) throws ViewDetachedException {
        //Logger.e(error.getError(), "RestError");
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
    }*/
}
