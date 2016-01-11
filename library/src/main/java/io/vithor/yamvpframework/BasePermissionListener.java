package io.vithor.yamvpframework;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class BasePermissionListener implements PermissionListener {

    private final PermissionDelegate delegate;

    public BasePermissionListener(PermissionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        delegate.showPermissionGranted(response.getPermissionName());
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        delegate.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        delegate.showPermissionRationale(token);
    }
}
