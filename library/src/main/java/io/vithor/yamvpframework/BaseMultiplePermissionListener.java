package io.vithor.yamvpframework;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class BaseMultiplePermissionListener implements MultiplePermissionsListener {

    private final PermissionDelegate delegate;

    public BaseMultiplePermissionListener(PermissionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
            delegate.showPermissionGranted(response.getPermissionName());
        }

        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
            delegate.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
        }
    }

    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                             PermissionToken token) {
        delegate.showPermissionRationale(token);
    }
}
