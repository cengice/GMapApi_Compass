package com.example.haris.mapsgooglegoogleplaces;

/**
 * Created by Haris on 3/31/2018.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

public class Compass extends AppCompatActivity implements SensorEventListener {


    String My_Bearing = null;
    int My_Bearing_int = 0;
    private static final String TAG = "Compass";

    double lat = 0.0;
    double lon = 0.0;
    private static final float DEFAULT_ZOOM = 16f;
//    ImageView img_north;
    ImageView img_compass;
    ImageView img_mec;

    TextView txt_compass;
    TextView txt_bearing;
    //    SupportMapFragment mapFragment;
    private Location currentLocation;
    private GoogleMap mMap;

    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        Log.d(TAG, "moveCamera: lane 69 onCreate started");

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        img_north = (ImageView) findViewById(R.id.img_north);
        img_compass = (ImageView) findViewById(R.id.img_compass);
        img_mec = (ImageView) findViewById(R.id.img_mec);

//        img_north.setVisibility(View.INVISIBLE);
        img_compass.setVisibility(View.VISIBLE);
        img_mec.setVisibility(View.VISIBLE);

        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        txt_bearing = (TextView) findViewById(R.id.txt_bearing);

        Log.d(TAG, "moveCamera: lane 82 unbundle started");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            My_Bearing = extras.getString("My_Bearing");
            My_Bearing_int =  Math.round(Float.parseFloat(My_Bearing));
            lat = getIntent().getDoubleExtra("My_lat", 0.0);
            lon = getIntent().getDoubleExtra("My_lon", 0.0);
            Log.d(TAG, "moveCamera: lane 91 moving the camera to: lat: " + lat + ", lng: " + lon);
            latLng = new LatLng(lat, lon);
            Log.d(TAG, "moveCamera: lane 92 moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

            ////Task location = mFusedLocationProviderClient.setLastLocation();

            //currentLocation.setLatitude(lat);
            //currentLocation.setLongitude(lon);

            moveCamera(new LatLng(lat, lon),   DEFAULT_ZOOM);


            //SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

            moveCamera(new LatLng(lat,lon), DEFAULT_ZOOM);

        }



/*
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (Compass.this , MainActivity.class);

                startActivity(intent);
            }
        });
*/


/*
        Button btnHMap = (Button) findViewById(R.id.btnMap);
        btnHMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (Compass.this , MapActivity.class);

                startActivity(intent);
            }
        });
*/

        String s_lat = Double.toString(lat);
        String s_lon = Double.toString(lon);

        //latLng.longitude = lon;
        //latLng.setLongitude(lon);

        txt_compass.setText(s_lat + "   "+s_lon);
        txt_bearing.setText(My_Bearing +" degrees East of North" );

        start();
    }


    private void moveCamera (LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera:lane 137 moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude+ " , zoom" +zoom);
        //if (mMap!= null) {
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        //}
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    //private static final String TAG = "Compass";

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        int my_mecc = My_Bearing_int - mAzimuth;


//        img_north.setRotation(0);
//        img_north.setVisibility(View.INVISIBLE);

        img_compass.setRotation( -mAzimuth);
        img_compass.setVisibility(View.VISIBLE);

        img_mec.setRotation(my_mecc);
        img_mec.setVisibility(View.VISIBLE);

        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";


        txt_compass.setText(mAzimuth + "° " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }


}



