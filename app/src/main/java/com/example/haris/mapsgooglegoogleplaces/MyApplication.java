package com.example.haris.mapsgooglegoogleplaces;

import android.app.Application;

/**
 * Created by Haris on 4/7/2018.
 */

public class MyApplication extends Application {

    private String savedlatVariable;
    private String savedlonVariable;
    private String savedbearVariable;
    private String savedNEVariable;

    public String getsavedlatVariable() {
        return savedlatVariable;
    }

    public String getsavedlonVariable() {
        return savedlonVariable;
    }
    public String getsavedbearVariable() {
        return savedbearVariable;
    }
    public String getsavedNEVariable() {
        return savedNEVariable;
    }

    public void setsavedlatVariable(String savedlatVariable) {
        this.savedlatVariable = savedlatVariable;
    }

    public void setsavedlonVariable(String savedlonVariable) {
        this.savedlonVariable = savedlonVariable;
    }

    public void setsavedbearVariable(String savedbearVariable) {
        this.savedbearVariable = savedbearVariable;
    }

    public void setsavedNEVariable(String savedNEVariable) {
        this.savedNEVariable = savedNEVariable;
    }

}

