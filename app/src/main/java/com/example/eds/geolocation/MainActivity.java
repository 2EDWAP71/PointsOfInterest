package com.example.eds.geolocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public double latitude = 51.05;
    public double longitude = -0.72;
    public int uploaded = 0;
    LocationManager mgr;

    ArrayList<String> A1 = new ArrayList<>(), A2 = new ArrayList<>(), A3 = new ArrayList<>(),
            A4 = new ArrayList<>(), A5 = new ArrayList<>();
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this,
                PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location mgr1 = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mv = (MapView) findViewById(R.id.map1);
        mv.getController().setCenter(new GeoPoint(51.05, -0.72));
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);

        if (mgr1 != null) {
            updateloc(mgr1);
        }
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    public void onClick (View view){

    }
        int item = A1.size();
        boolean save = false;
        if (view.getId()==R.id.save)

    {
        save = true;
        try {
            BufferedWriter pw = new BufferedWriter(new FileWriter(Environment
                    .getExternalStorageDirectory().getAbsolutePath() + "file.txt", true));
            for (int i = 0; i < item; i++) {
                String B1 = A1.get(i), B2 = A2.get(i), B3 = A3.get(i), B4 = A4.get(i),
                        B5 = A5.get(i);
                pw.write("," + B1 + "," + B2 + "," + B3 + "," + B4 + "," + B5);
                uploaded = uploaded + i;
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("I/O Error" + e);
        }
    }








    public void onResume() {
        super.onResume();
        try {
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                    (getApplicationContext());
            //HttpURLConnection conn = null;


            String file = preferences.getString("File", "none");

            if (file.equals("Save")) {
                new AlertDialog.Builder(this).setMessage("Working(Saved)");

            }
            if (file.equals("NoSave")) {
                new AlertDialog.Builder(this).setMessage("Working (No save)");
            }
            }catch(SecurityException e){
                System.out.println("Error" + e);

            }





    markerGestureListener=new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()

    {
        public boolean onItemLongPress ( int i, OverlayItem item){
        Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean onItemSingleTapUp(int i, OverlayItem item) {
        Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
        return true;
    }

};
}

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.addaninterest){
                Intent intent = new Intent(this,AddInterest.class);
                startActivityForResult(intent, 1);
                return true;
        }
        if (item.getItemId() == R.id.prefs){
           Intent intent = new Intent(this,MyPrefsActivity.class);
           startActivityForResult(intent, 2);
            return true;
        }

        return false;

    }  public void updateloc(Location location){
        GeoPoint Location = new GeoPoint
                (location.getLatitude(), location.getLongitude());
        mv.getController().setCenter(Location);
        mv.invalidate();


    }

    protected LocationListener myLocationListener=
            new LocationListener() {
                @Override
                public void onLocationChanged(Location newlocation) {
                    updateloc(newlocation);
                    longitude = newlocation.getLongitude();
                    latitude = newlocation.getLatitude();



                }

                @Override
                public void onStatusChanged(String s, int status, Bundle extras) {
                    //TODO Auto-Generated method stub
                }


                @Override
                public void onProviderEnabled(String provider) {
                    //TODO Auto-Generated method stub
                }

                @Override
                public void onProviderDisabled(String provider) {
                    //TODO Auto-Generated method stub
                }
            };
    public void onActivityResult (int requestCode, int resultCode,Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String l1 = latitude.toString();
                String l2 = longitude.toString();
                Bundle extras = intent.getExtras();


                String name = extras.getString("com.example.tx1");
                String type = extras.getString("com.example.tx2");
                String des = extras.getString("com.example.tx3");


                A1.add(name);
                A2.add(des);
                A3.add(type);
                A4.add(l1);
                A5.add(l2);

                    items = new ItemizedIconOverlay<OverlayItem>(this, new
                            ArrayList<OverlayItem>(), markerGestureListener);
                    OverlayItem Item = new OverlayItem(name, des,
                            new GeoPoint(latitude, longitude));

                    items.addItem(Item);
                    mv.getOverlays().add(items);

                }

            }

        }







    public void onDestroy(){
        super.onDestroy();
        int item = A1.size();
            try {
                BufferedWriter pw = new BufferedWriter(new FileWriter(Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "file.txt", true));
                for (int i = 0; i < item; i++) {
                    String B1 = A1.get(i), B2 = A2.get(i), B3 = A3.get(i), B4 = A4.get(i),
                            B5 = A5.get(i);
                    pw.write("," + B1 + "," + B2 + "," + B3 + "," + B4 + "," + B5);
                }
                pw.close();
            } catch (IOException e) {
                System.out.println("ERROR" + e);
            }

        finish();
    }


}
