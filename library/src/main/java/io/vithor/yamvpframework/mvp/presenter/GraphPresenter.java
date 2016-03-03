package io.vithor.yamvpframework.mvp.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazer on 1/10/16.
 */
public abstract class GraphPresenter {
    private List<GraphPresenter> childPresenters = new ArrayList<>(4);
    private @Nullable GraphPresenter parent;

    public GraphPresenter() {
    }

    public GraphPresenter(@Nullable GraphPresenter parent) {
        this.parent = parent;
    }

    public void addChildPresenter(GraphPresenter presenter) {
        if (!childPresenters.contains(presenter)) {
            childPresenters.add(presenter);
        }
    }

    public void removeChildPresenter(GraphPresenter presenter) {
        childPresenters.remove(presenter);
    }

    public void notifyChildPresenters() {
        for (GraphPresenter presenter: childPresenters) {

        }
    }
}
