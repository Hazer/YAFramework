package io.vithor.yamvpframework.presenter;

import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import io.vithor.yamvpframework.ErrorContainer;
import io.vithor.yamvpframework.ResponseContainer;
import io.vithor.yamvpframework.presenter.sketch.TypedSketch;

/**
 * Created by hazer on 5/5/15.
 */
public abstract class SimplePresenter<ModelType, SK extends TypedSketch<ModelType>, ResponseType> extends BasePresenter<SK> {

    @Override
    protected void onViewAttached() {
        loadData(PresenterAction.INITIAL_LOAD);
    }

    protected final PresenterCallback<ModelType, ResponseType> defaultHandler(PresenterAction action) {
        return new PresenterCallback<ModelType, ResponseType>(this, action) {
            @Override
            public void success(ModelType modelType, @NotNull ResponseContainer<ResponseType> response, @NotNull PresenterAction action) throws ViewDetachedException {
                successHandler(action, modelType, response);
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

    protected void successHandler(PresenterAction action, ModelType t, ResponseContainer rawResponse) throws ViewDetachedException {
        getView().showData(t);
        getView().showContent();
    }

    @Override
    public void handleRestFailure(ErrorContainer error, PresenterAction action) throws ViewDetachedException {
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
