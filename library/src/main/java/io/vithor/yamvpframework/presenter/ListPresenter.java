package io.vithor.yamvpframework.presenter;

import com.orhanobut.logger.Logger;

import io.vithor.yamvpframework.ErrorContainer;
import io.vithor.yamvpframework.ResponseContainer;
import io.vithor.yamvpframework.presenter.sketch.ListSketch;

/**
 * Created by hazer on 5/5/15.
 */
public abstract class ListPresenter<SK extends ListSketch<ModelType>, ModelType, ResponseType> extends BasePresenter<SK> {

    @Override
    protected void onViewAttached() {
        loadData(PresenterAction.INITIAL_LOAD);
    }

    protected final PresenterCallback<ModelType, ResponseType> defaultHandler(PresenterAction action) {
        return new PresenterCallback<ModelType, ResponseType>(action) {
            @Override
            public void success(ModelType model, ResponseContainer response, PresenterAction action) throws ViewDetachedException {
                successHandler(action, model, response);
            }
        };
    }

    protected abstract void loadData(PresenterAction action);

    protected void showLoading(PresenterAction action) {
        try {
            getView().showLoading(action);
        } catch (ViewDetachedException ignore) {

        }
    }

    protected void successHandler(PresenterAction action, ModelType model, ResponseContainer rawResponse) throws ViewDetachedException {
        switch (action) {
            case PULL_TO_REFRESH:
                getView().onRefreshCompleted(model);
                break;
            case PAGINATE_FORWARD:
                getView().onMoreDataFetched(model);
                break;
            default:
                getView().showData(model);
                getView().showContent();
        }
    }

    @Override
    protected void handleRestFailure(ErrorContainer error, PresenterAction action) throws ViewDetachedException {
        Logger.e(error.getError(), "RestError");
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
    }
}
