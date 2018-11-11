package com.vaidoos.getlocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static com.github.florent37.runtimepermission.RuntimePermission.askPermission;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        askPermission(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                .onAccepted((result) -> {
                    //all permissions already granted or just granted

                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    //launchActivity();
                    startActivity(new Intent(this,MainActivity.class));

                    finish();

                })
                .onDenied(result -> {
                    Toast.makeText(this, "Denied: " + result.getDenied(), Toast.LENGTH_SHORT).show();
                    for (String permission : result.getDenied()) {
                        //appendText(tvUsersName, permission);
                        Toast.makeText(this, "Denied permision: " + permission, Toast.LENGTH_SHORT).show();
                    }

                    new AlertDialog.Builder(this)
                            .setMessage("Please accept our permissions")
                            .setPositiveButton("yes", (dialog, which) -> {
                                result.askAgain();
                            }) // ask again
                            .setNegativeButton("no", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();

                })
                .onForeverDenied(result -> {
                    Toast.makeText(this, "Forever Denied: " + result.getForeverDenied(), Toast.LENGTH_SHORT).show();

                    for (String permission : result.getForeverDenied()) {
                        Toast.makeText(this, "Forever", Toast.LENGTH_SHORT).show();
                    }
                })
                .ask();

    }
}
