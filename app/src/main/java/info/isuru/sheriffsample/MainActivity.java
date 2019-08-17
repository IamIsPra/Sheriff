package info.isuru.sheriffsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import info.isuru.sheriff.enums.SheriffPermission;
import info.isuru.sheriff.helper.Sheriff;
import info.isuru.sheriff.interfaces.PermissionListener;

public class MainActivity extends AppCompatActivity implements PermissionListener {

    private Button btnSinglePerm, btnMultiPerm, btnOpenSettings;
    private RelativeLayout layoutGithub;

    private Sheriff sheriffPermission;

    private static final int REQUEST_MULTIPLE_PERMISSION = 101;
    private static final int REQUEST_SINGLE_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSinglePerm = findViewById(R.id.btnSinglePerm);
        btnMultiPerm = findViewById(R.id.btnMultiPerm);
        btnOpenSettings = findViewById(R.id.btnOpenSettings);

        layoutGithub = findViewById(R.id.layoutGithub);

        btnSinglePerm.setOnClickListener(view ->
            requestSinglePermission()
        );

        btnMultiPerm.setOnClickListener(view ->
            requestMultiplePermission()
        );

        btnOpenSettings.setOnClickListener(view ->
            openAppPermissionSettings()
        );

        layoutGithub.setOnClickListener(view ->
            openGitHubPage()
        );


    }

    private void requestSinglePermission() {
        sheriffPermission = Sheriff.Builder()
                .with(this)
                .requestCode(REQUEST_SINGLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askFor(SheriffPermission.CONTACTS)
                .build();
        sheriffPermission.requestPermissions();
    }

    private void requestMultiplePermission() {
        sheriffPermission = Sheriff.Builder()
                .with(this)
                .requestCode(REQUEST_MULTIPLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askFor(SheriffPermission.CALENDAR, SheriffPermission.CAMERA, SheriffPermission.LOCATION)
                .rationalMessage("These Permissions are required to work app with all functions.")
                .build();
        sheriffPermission.requestPermissions();
    }

    private void openAppPermissionSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void openGitHubPage(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/IamIsPra/Sheriff"));
        startActivity(browserIntent);
    }

    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

    }

    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {
        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sheriffPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
