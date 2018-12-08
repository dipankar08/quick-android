package in.co.dipankar.quickandorid.arch;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

import in.co.dipankar.quickandorid.utils.DLog;

public abstract class BasePresenter<V extends BaseView<VS>, VS extends BaseViewState> {

    @Nullable private V mView;
    @Nullable private VS mLastViewState;
    private String mName;
    public BasePresenter(String name) {
        mName = name;
    }
    
    public final void attachView(V view) {
        if (this.mView == view) {
            return;
        }

        if (this.mView != null) {
            detachView();
        }
        this.mView = view;
        if (mLastViewState != null) {
            renderInternal(mLastViewState);
        }
        onViewAttached();
        DLog.d("attachView called");
    }

    public final void detachView() {
        if (this.mView == null) {
            return;
        }
        onViewDettached();
        this.mView = null;
        DLog.d("detachView called");
    }
    
    protected final Optional<V> getView() {
        return Optional.fromNullable(mView);
    }
    
    protected void onViewAttached() {}
    protected void onViewDettached() {}

    protected final void render(VS viewState) {
        boolean changed = !Objects.equal(mLastViewState, viewState);
        mLastViewState = Preconditions.checkNotNull(viewState);

        if (changed) {
            DLog.d(mName +":render called"+ mLastViewState.toString());
            renderInternal(mLastViewState);
        } else {
            DLog.d(mName +":Skip Render for "+ mLastViewState.toString());
        }
    }

    private void renderInternal(VS viewState) {
        if (getView().isPresent()) {
            getView().get().render(viewState);
        }
    }

    @Nullable
    public Context getContext() {
        if (mView == null) {
            return null;
        }
        if (mView instanceof View) {
            return ((View) mView).getContext();
        }

        if (mView instanceof Fragment) {
            return ((Fragment) mView).getContext();
        }
        if (mView instanceof Activity) {
            return (Context) mView;
        }
        return null;
    }
}