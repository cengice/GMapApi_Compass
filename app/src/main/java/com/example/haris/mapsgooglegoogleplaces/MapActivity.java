package com.example.haris.mapsgooglegoogleplaces;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Haris on 3/7/2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

/*
    ImageView img_north;
    ImageView img_mec;
    ImageView img_compass;
*/

    TextView txt_azimuth;
    TextView txt_bearing;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setCompassEnabled(true);

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //mMap.setMapStyle(GoogleMap.MAP_TYPE_HYBRID);
            //mMap.setMapStyle(MapStyleOptions )
            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }

    }
    //public float bearing=0f;
    //public String s_bearing = Float.toString(bearing);

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16f;
    private static final double DEFAULT_LAT = 38.2864; //38.328732;
    private static final double DEFAULT_LON = -85.5062;//-85.764771;
    private float currentDegree = 0f;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private  GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //private ImageView compass;
    //private SensorManager mSensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_map);


        getLocationPermission();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the urrent devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "oncomplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            //currentLocation.setLatitude(DEFAULT_LAT);
                            //currentLocation.setLongitude(DEFAULT_LON);
                            CalculateBearing(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                        }else{
                            Log.d(TAG, "oncomplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }catch (SecurityException e){
            Log.e(TAG, "ggetDeviceLocation: securityException: " + e.getMessage());
        }
    }

    private void CalculateBearing (LatLng latLng) {
        Log.d(TAG, "PrepareCompass:moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

/*
        img_north = (ImageView) findViewById(R.id.img_north);
        img_mec = (ImageView) findViewById(R.id.img_mec);
        img_compass = (ImageView) findViewById(R.id.img_compass);

        img_north.setVisibility(View.GONE);
        img_mec.setVisibility(View.GONE);//img_mec.setVisibility(View.VISIBLE);
        img_compass.setVisibility(View.GONE);//img_compass.setVisibility(View.VISIBLE);
*/

        txt_azimuth = (TextView) findViewById(R.id.txt_azimuth);
        txt_bearing = (TextView) findViewById(R.id.txt_bearing);
        //mRotation ( latLng.latitude, latLng.longitude, float bearing);
        double PI = Math.PI;
        float bearing=0f;
        final double lat = latLng.latitude;
        final double lon = latLng.longitude;

        double latk = 21.4225*PI/180.0;
        double longk = 39.8264*PI/180.0;
        double phi = lat*PI/180.0;
        double lambda = lon*PI/180.0;
//        if (lat<0.0 ||lon<0.0){
//            Log.d(TAG, "current_locatino: Non-numeric entry/entries");
//            return;
//        }
        if (lat>90.0 ||lat<(-90.0)){
            Log.d(TAG, "current_locatino: Latitude must be between -90 and 90 degrees");
            return;
        }
        if (lon>180.0 ||lon<(-180.0)){
            Log.d(TAG, "current_locatino: Longitude must be between -180 and 180 degrees");
            return;
        }
        if (lat==21.4 && lon==39.8){
            Log.d(TAG, "current_locatino: Any Direction");
        }
        double qiblad = 180.0/PI*Math.atan2(Math.sin(longk-lambda),
                Math.cos(phi)*Math.tan(latk)-Math.sin(phi)*Math.cos(longk-lambda));

        bearing = Math.round(qiblad);
        final String s_bearing = Float.toString(bearing);
        //s_bearing = Float.toString(bearing);

        txt_azimuth.setText("lat: " +latLng.latitude + "   lng: " + latLng.longitude );
        txt_bearing.setText(s_bearing +" degrees East of North" );
        if (bearing<0.0){
            txt_bearing.setText(s_bearing +" degrees West of North" );

        }

//        img_north.setRotation( 0);
//        img_north.setVisibility(View.VISIBLE);

//        img_compass.setRotation( 0);
//        img_compass.setVisibility(View.INVISIBLE);

//        img_mec.setRotation( bearing);
//        img_mec.setVisibility(View.VISIBLE);

        Button btnHome = (Button) findViewById(R.id.btnhome);
        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (MapActivity.this , MainActivity.class);

                startActivity(intent);
            }
        });


        Button btnCompass = (Button) findViewById(R.id.btnCompass);
        btnCompass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent (MapActivity.this, Compass.class);
                intent.putExtra("My_Bearing", s_bearing);
                intent.putExtra("My_lat", lat);
                intent.putExtra("My_lon", lon);


                startActivity(intent);
            }
        });
    }

    private void moveCamera (LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera:moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void initMap(){
        Log.d(TAG, "initMap: initialize");
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting_locations_permissions ");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called_onRequestPermissionsResult ");
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length>0){
                    for ( int i=0; i<grantResults.length; i++ ){
                        if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: Permissions failed ");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permissions granted ");
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }

        }
    }


}