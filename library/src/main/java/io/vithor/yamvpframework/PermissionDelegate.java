package io.vithor.yamvpframework;

import com.karumi.dexter.PermissionToken;

/**
 * Created by Hazer on 1/5/16.
 */
public interface PermissionDelegate {
    void showPermissionGranted(String permissionName);

    void showPermissionDenied(String permissionName, boolean permanentlyDenied);

    void showPermissionRationale(PermissionToken token);
}
