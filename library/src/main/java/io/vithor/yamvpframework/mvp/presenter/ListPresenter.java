package io.vithor.yamvpframework.mvp.presenter;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import io.vithor.yamvpframework.ErrorContainer;
import io.vithor.yamvpframework.ResponseContainer;
import io.vithor.yamvpframework.mvp.presenter.sketch.ListSketch;

/**
 * Created by Vithorio Polten on 5/5/15.
 */
public abstract class ListPresenter<SK extends ListSketch<ModelType>, ModelType, ResponseType> extends BasePresenter<SK> {

    @Override
    protected void onViewAttached() {
        loadData(PresenterAction.INITIAL_LOAD);
    }

    protected final PresenterCallback<ModelType, ResponseType> defaultHandler(PresenterAction action) {
        return new PresenterCallback<ModelType, ResponseType>(this, action) {
            @Override
            public void success(ModelType modelType, @NotNull ResponseContainer<ResponseType> response, @NotNull PresenterAction action) {
                successHandler(action, modelType, response);
            }
        };
    }

    protected abstract void loadData(PresenterAction action);

    protected void showLoading(PresenterAction action) {
        SK view = getView();
        if (view != null)
            view.showLoading(action);

    }

    protected void successHandler(PresenterAction action, ModelType model, ResponseContainer rawResponse) {
        SK view = getView();
        if (view != null) {
            switch (action) {
                case PULL_TO_REFRESH:
                    view.onRefreshCompleted(model);
                    break;
                case PAGINATE_FORWARD:
                    view.onMoreDataFetched(model);
                    break;
                default:
                    view.showData(model);
                    view.showContent();
            }
        }
    }

    @Override
    public void handleRestFailure(@NonNull ErrorContainer error, @NonNull PresenterAction action) throws ViewDetachedException {
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
