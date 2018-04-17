package com.example.haris.mapsgooglegoogleplaces;
import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    TextView txt_serviceok;
    TextView txt_servicenotok;

    TextView textlat;
    TextView textlon;
    TextView textbear;
    TextView textNE;

    TextView txt_savedtextlat;
    TextView txt_savedtextlon;
    TextView txt_savedtextbear;
    TextView txt_savedtextNE;


    // Ver 1.0.1  MediaPlayer mp;

    private static final String TAG ="MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //added toolbar
/*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
*/

/*
        if(!permissionGranted){
            checkPermissions();
        }
*/

        txt_serviceok = (TextView) findViewById(R.id.txt_serviceok);
        txt_servicenotok = (TextView) findViewById(R.id.txt_servicenotok);

        textlat = (TextView) findViewById(R.id.textlat);
        textlon = (TextView) findViewById(R.id.textlon);
        textbear = (TextView) findViewById(R.id.textbear);
        textNE = (TextView) findViewById(R.id.textNE);

        txt_savedtextlat = (TextView) findViewById(R.id.txt_savedtextlat);
        txt_savedtextlon = (TextView) findViewById(R.id.txt_savedtextlon);
        txt_savedtextbear = (TextView) findViewById(R.id.txt_savedtextbear);
        txt_savedtextNE = (TextView) findViewById(R.id.txt_savedtextNE);

        TextView textlocator = (TextView) findViewById(R.id.textlocator);
        textlocator.setVisibility(View.INVISIBLE);


        // get
        String savedtextlat= ((MyApplication) this.getApplication()).getsavedlatVariable();
        String savedtextlon= ((MyApplication) this.getApplication()).getsavedlonVariable();
        String savedtextbear= ((MyApplication) this.getApplication()).getsavedbearVariable();
        String savedtextNE= ((MyApplication) this.getApplication()).getsavedNEVariable();

        double dd_lat=0.0;
        double dd_lon=0.0;

        Log.d(TAG, "savedtextbear: recorded "+savedtextbear);
        boolean testsaveddate = true;

        if(savedtextlat==null) {
            savedtextlat = "None";
            testsaveddate = false;
        }else {
             dd_lat = Double.valueOf(savedtextlat.trim()).doubleValue();
        }

        if(savedtextlon==null) {
            savedtextlon = "None";
            testsaveddate = false;
        }else{
             dd_lon =Double.valueOf(savedtextlon.trim()).doubleValue();
        }

        if(savedtextbear==null) {
            savedtextbear = "None";
            testsaveddate = false;
        }else{
            final double d_bear =Double.valueOf(savedtextbear.trim()).doubleValue();

            final double d_lat= dd_lat;

            final double d_lon= dd_lon;

        }
        if(savedtextNE==null){
            savedtextNE = "None";
            testsaveddate = false;
        }
        txt_savedtextlat.setText(savedtextlat);
        txt_savedtextlon.setText(savedtextlon);
        txt_savedtextbear.setText(savedtextbear);
        txt_savedtextNE.setText(savedtextNE);

        // Ver 1.0.1   mp = MediaPlayer.create(this, R.raw.abc);

        ImageButton btnCompass = (ImageButton) findViewById(R.id.btnCompass);


        if(!testsaveddate){
                btnCompass.setVisibility(View.INVISIBLE);
        }
        final String s_savedtextbear =savedtextbear;
        final String d_lat =savedtextlat;
        final String d_lon =savedtextlon;

       if(testsaveddate) {
           btnCompass.setVisibility(View.VISIBLE);
           textlocator.setVisibility(View.VISIBLE);

           txt_serviceok.setVisibility(View.INVISIBLE);
           txt_servicenotok.setVisibility(View.INVISIBLE);
           textlat.setVisibility(View.VISIBLE);
           textlon.setVisibility(View.VISIBLE);
           textbear.setVisibility(View.VISIBLE);

           txt_savedtextlat.setVisibility(View.VISIBLE);
           txt_savedtextlon.setVisibility(View.VISIBLE);
           txt_savedtextbear.setVisibility(View.VISIBLE);
           txt_savedtextNE.setVisibility(View.VISIBLE);

           btnCompass.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View view){
                   Intent intent = new Intent (MainActivity.this, Compass.class);
                   intent.putExtra("My_Bearing", s_savedtextbear);
                  intent.putExtra("My_lat", d_lat);
                   intent.putExtra("My_lon", d_lon);


                   startActivity(intent);
               }
           });
    }else{
        txt_serviceok.setVisibility(View.INVISIBLE  );
        txt_servicenotok.setVisibility(View.INVISIBLE);
        textlat.setVisibility(View.INVISIBLE);
        textlon.setVisibility(View.INVISIBLE);
        textbear.setVisibility(View.INVISIBLE);
        textNE.setVisibility(View.INVISIBLE);

        txt_savedtextlat.setVisibility(View.INVISIBLE);
        txt_savedtextlon.setVisibility(View.INVISIBLE);
        txt_savedtextbear.setVisibility(View.INVISIBLE);
        txt_savedtextNE.setVisibility(View.INVISIBLE);

    }



        if(isServicesOK()){
            init();
        } else{

        }
        Boolean diagnostic_mode = false;
//        Boolean diagnostic_mode = true;

/*
        if(diagnostic_mode) {
            txt_serviceok.setVisibility(View.VISIBLE);
            txt_servicenotok.setVisibility(View.VISIBLE);
            textlat.setVisibility(View.VISIBLE);
            textlon.setVisibility(View.VISIBLE);
            textbear.setVisibility(View.VISIBLE);

            txt_savedtextlat.setVisibility(View.VISIBLE);
            txt_savedtextlon.setVisibility(View.VISIBLE);
            txt_savedtextbear.setVisibility(View.VISIBLE);
            txt_savedtextNE.setVisibility(View.VISIBLE);
        }else{
            txt_serviceok.setVisibility(View.INVISIBLE  );
            txt_servicenotok.setVisibility(View.INVISIBLE);
            textlat.setVisibility(View.INVISIBLE);
            textlon.setVisibility(View.INVISIBLE);
            textbear.setVisibility(View.INVISIBLE);
            textNE.setVisibility(View.INVISIBLE);

            txt_savedtextlat.setVisibility(View.INVISIBLE);
            txt_savedtextlon.setVisibility(View.INVISIBLE);
            txt_savedtextbear.setVisibility(View.INVISIBLE);
            txt_savedtextNE.setVisibility(View.INVISIBLE);

            }
*/

    }

    private void init(){
        txt_serviceok.setVisibility(View.INVISIBLE);
        txt_servicenotok.setVisibility(View.INVISIBLE);
        Log.d(TAG, "init: drawing btn map is working");

        ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    // Ver 1.0.1boolean playSong=true;
/*
    public void playAudio(View v){
        mp.start();
    }
*/

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make the map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            txt_serviceok.setVisibility(View.INVISIBLE);
            txt_servicenotok.setVisibility(View.INVISIBLE);
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
            txt_serviceok.setVisibility(View.INVISIBLE);
            txt_servicenotok.setVisibility(View.INVISIBLE);

            return false;
        }else{
            Toast.makeText(this, "You can't make requests", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "isServicesOK: You can't make requests");
        }
        txt_serviceok.setVisibility(View.INVISIBLE);
        txt_servicenotok.setVisibility(View.VISIBLE);
        return false;

    }

    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}