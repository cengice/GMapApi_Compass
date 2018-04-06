package com.example.haris.mapsgooglegoogleplaces;
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

        mp = MediaPlayer.create(this, R.raw.abc);

        if(isServicesOK()){
            init();
        }
    }

    private void init(){
        txt_serviceok.setVisibility(View.VISIBLE);
        txt_servicenotok.setVisibility(View.INVISIBLE);

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
            txt_serviceok.setVisibility(View.INVISIBLE);
            txt_servicenotok.setVisibility(View.VISIBLE);
            return false;
        }else{
            Toast.makeText(this, "You can't make requests", Toast.LENGTH_SHORT).show();
            txt_serviceok.setVisibility(View.INVISIBLE);
            txt_servicenotok.setVisibility(View.VISIBLE);
            return false;
        }

    }
}