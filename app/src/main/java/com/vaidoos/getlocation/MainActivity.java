package com.vaidoos.getlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CHECK_SETTINGS = 123;
    private static final int REQUEST_CODE = 1000;
    private TextView textView;
    private Button btn_start;

    private Button btn_stop;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView tvLastLocation;
    private Button btnLastlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        tvLastLocation = findViewById(R.id.tvLastLocation);
        btnLastlocation = (Button) findViewById(R.id.btnLastlocation);

        btnLastlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    tvLastLocation.setText("Last Location: "+location.getLatitude()+" / "+location.getLongitude());
                                }
                            }
                        });
            }
        });

        textView = findViewById(R.id.textView);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_stop.setEnabled(false);



        // if permission granted
        buildLocationRequest();
        builtLocationCallBack();

        //set event for buttons
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                btn_start.setEnabled(!btn_start.isEnabled());
                btn_stop.setEnabled(true);

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                    return;
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                btn_start.setEnabled(true);
                btn_stop.setEnabled(!btn_stop.isEnabled());
            }
        });
    }

    private void builtLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    textView.setText("Present Location: "+location.getLatitude()+" / "+location.getLongitude());
                    Toast.makeText(MainActivity.this, location.getLatitude()+" / "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onLocationResult: "+location.getLatitude()+" / "+location.getLongitude());
                }
            }
        };

    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000*10);
        locationRequest.setFastestInterval(0);
        locationRequest.setSmallestDisplacement(0);
    }
}
