package com.example.haris.mapsgooglegoogleplaces;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {

    TextView txt_serviceok;
    TextView txt_servicenotok;

    TextView textlat;
    TextView textlon;
    TextView textbear;

    TextView txt_savedtextlat;
    TextView txt_savedtextlon;
    TextView txt_savedtextbear;
    TextView txt_savedtextNE;

    Button btnMap;


    private EditText mSearchText;

    MediaPlayer mp;

    private static final String TAG ="MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //added toolbar
/*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
*/

        txt_serviceok = (TextView) findViewById(R.id.txt_serviceok);
        txt_servicenotok = (TextView) findViewById(R.id.txt_servicenotok);

        textlat = (TextView) findViewById(R.id.textlat);
        textlon = (TextView) findViewById(R.id.textlon);
        textbear = (TextView) findViewById(R.id.textbear);

        txt_savedtextlat = (TextView) findViewById(R.id.txt_savedtextlat);
        txt_savedtextlon = (TextView) findViewById(R.id.txt_savedtextlon);
        txt_savedtextbear = (TextView) findViewById(R.id.txt_savedtextbear);
        txt_savedtextNE = (TextView) findViewById(R.id.txt_savedtextNE);

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

        mp = MediaPlayer.create(this, R.raw.abc);

        Button btnCompass = (Button) findViewById(R.id.btnCompass);

        if(!testsaveddate){
                btnCompass.setVisibility(View.INVISIBLE);
        }

       if(testsaveddate) {
           btnCompass.setVisibility(View.VISIBLE);
           btnCompass.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View view){
                   Intent intent = new Intent (MainActivity.this, Compass.class);
//                   intent.putExtra("My_Bearing", s_savedtextbear);
 /*                  intent.putExtra("My_lat", d_lat);
                   intent.putExtra("My_lon", d_lon);
*/

                   startActivity(intent);
               }
           });
       }





        if(isServicesOK()){
            init();
        } else{

        }

    }

    private void init(){
        txt_serviceok.setVisibility(View.VISIBLE);
        txt_servicenotok.setVisibility(View.INVISIBLE);
        Log.d(TAG, "init: drawing btn map is working");

        Button btnMap = (Button) findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    //boolean playSong=true;
    public void playAudio(View v){
        mp.start();
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make the map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            txt_serviceok.setVisibility(View.VISIBLE);
            txt_servicenotok.setVisibility(View.INVISIBLE);
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
            txt_serviceok.setVisibility(View.VISIBLE);
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

}