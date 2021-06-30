package info.isuru.sheriff.helper;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import info.isuru.sheriff.enums.SheriffPermission;
import info.isuru.sheriff.interfaces.PermissionListener;


public class Sheriff {
    private ArrayList<String> permissionsToAsk;
    private String rationalMessage;
    private PermissionListener permissionListener;
    private Activity activity;
    private Fragment fragment;
    private Context context;

    private boolean isFragment;
    private boolean shouldShowRationaleDialog;
    private int requestCode;

    private Sheriff() {
        throw new AssertionError("Can't use default constructor. Use Sheriff.Builder class to create a new Method of Sheriff");
    }

    private Sheriff(Builder builder) {
        this.fragment = builder.fragment;
        this.activity = builder.activity;
        this.requestCode = builder.requestCode;
        this.rationalMessage = builder.rationalMessage;
        this.permissionsToAsk = builder.permissionsToAsk;
        this.permissionListener = builder.permissionListener;
        this.context = builder.context;
        this.isFragment = builder.isFragment;
    }

    /**
     * Entry point of {@link Sheriff}
     *
     * @return instance of {@link Builder}
     */

    public static IWith Builder() {
        return new Builder();
    }

    /**
     * Method that invokes permission dialog, if permission is already granted or
     * denied (with never asked ticked) then the result is delivered without showing any dialog.
     */
    public void requestPermissions() {
        if (!hasPermissions(context, permissionsToAsk.toArray(new String[0]))) {
            if (shouldShowRationale(permissionsToAsk.toArray(new String[0])) && rationalMessage != null) {
                DialogUtil.getInstance().showAlertDialog(context, null, 0, rationalMessage, "OK", (dialog, which) -> request(), "Cancel", true);
            } else {
                request();
            }
        } else {
            permissionListener.onPermissionsGranted(requestCode, permissionsToAsk);
        }
    }

    /* Shows a dialog for permission */
    private void request() {
        if (isFragment) {
            fragment.requestPermissions(permissionsToAsk.toArray(new String[0]), requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissionsToAsk.toArray(new String[0]), requestCode);
        }
    }

    /* Check whether any permission is denied before, if yes then we show a rational dialog for explanation */
    private boolean shouldShowRationale(String... permissions) {
        // Todo : Improve, check if this check can be done with only one call (ActivityCompat) for both fragment and activity
        if (isFragment) {
            for (String permission : permissions) {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        } else {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    shouldShowRationaleDialog = true;
                }
            }
        }


        return shouldShowRationaleDialog;


    }

    /* Check if we already have the permission */
    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called by the user when he gets the call in Activity/Fragment
     *
     * @param reqCode      Request Code
     * @param permissions  List of permissions
     * @param grantResults Permission grant result
     */
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == reqCode) {
            ArrayList<String> grantedPermissionList = new ArrayList<>();
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissionList.add(permissions[i]);
                } else {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (!grantedPermissionList.isEmpty()) {
                permissionListener.onPermissionsGranted(requestCode, grantedPermissionList);
            }
            if (!deniedPermissionList.isEmpty()) {
                permissionListener.onPermissionsDenied(requestCode, deniedPermissionList);
            }
        }
    }


    /**
     * {@link Builder} class for {@link Sheriff}.
     * Use only this class to create a new instance of {@link Sheriff}
     */
    public static class Builder implements IWith, IRequestCode, IPermissionResultCallback, IAskFor, IBuild {
        ArrayList<String> permissionsToAsk;
        String rationalMessage;
        PermissionListener permissionListener;
        Activity activity;
        Fragment fragment;
        Context context;
        int requestCode = -1;
        boolean isFragment;

        @Override
        public IRequestCode with(Activity activity) {
            this.activity = activity;
            this.context = activity;
            isFragment = false;
            return this;
        }

        @Override
        public IRequestCode with(Fragment fragment) {
            this.fragment = fragment;
            this.context = fragment.getActivity();
            isFragment = true;
            return this;
        }

        @Override
        public IPermissionResultCallback requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        @Override
        public IAskFor setPermissionResultCallback(PermissionListener permissionListener) {
            this.permissionListener = permissionListener;
            return this;
        }

        @Override
        public IBuild askFor(SheriffPermission... permission) {
            permissionsToAsk = new ArrayList<>();
            for (SheriffPermission mPermission : permission) {
                switch (mPermission) {
                    case CALENDAR:
                        permissionsToAsk.add(Manifest.permission.WRITE_CALENDAR);
                        break;
                    case CAMERA:
                        permissionsToAsk.add(Manifest.permission.CAMERA);
                        break;
                    case CONTACTS:
                        permissionsToAsk.add(Manifest.permission.READ_CONTACTS);
                        break;
                    case FINE_LOCATION:
                        permissionsToAsk.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        break;
                    case COARSE_LOCATION:
                        permissionsToAsk.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                        break;
                    case MICROPHONE:
                        permissionsToAsk.add(Manifest.permission.RECORD_AUDIO);
                        break;
                    case PHONE:
                        permissionsToAsk.add(Manifest.permission.CALL_PHONE);
                        break;
                    case SENSORS:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                            permissionsToAsk.add(Manifest.permission.BODY_SENSORS);
                        }
                        break;
                    case SMS:
                        permissionsToAsk.add(Manifest.permission.SEND_SMS);
                        break;
                    case STORAGE:
                        permissionsToAsk.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        break;
                    case CALL_LOG:
                        permissionsToAsk.add(Manifest.permission.READ_CALL_LOG);
                        permissionsToAsk.add(Manifest.permission.WRITE_CALL_LOG);
                        break;
                }
            }
            return this;
        }

        @Override
        public Sheriff.IBuild rationalMessage(String message) {
            this.rationalMessage = message;
            return this;
        }

        @Override
        public Sheriff build() {
            if (this.permissionListener == null) {
                throw new NullPointerException("Permission listener can not be null");
            } else if (this.context == null) {
                throw new NullPointerException("Context can not be null");
            } else if (this.permissionsToAsk.size() == 0) {
                throw new IllegalArgumentException("Not asking for any permission. At least one permission is expected before calling build()");
            } else if (this.requestCode == -1) {
                throw new IllegalArgumentException("Request code is missing");
            } else {
                return new Sheriff(this);
            }
        }

    }

    /*Interfaces for builder to make some methods must/required*/

    public interface IWith {
        IRequestCode with(Activity activity);

        IRequestCode with(Fragment fragment);
    }

    public interface IRequestCode {
        IPermissionResultCallback requestCode(int requestCode);
    }

    public interface IPermissionResultCallback {
        IAskFor setPermissionResultCallback(PermissionListener permissionListener);
    }

    public interface IAskFor {
        IBuild askFor(SheriffPermission... permission);
    }

    public interface IBuild {
        IBuild rationalMessage(String message);

        Sheriff build();
    }
}
