package com.example.eds.geolocation;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Eds on 20/04/2017.
 */

public class MyPrefsActivity extends PreferenceActivity {
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
