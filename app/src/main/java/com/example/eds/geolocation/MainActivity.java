package com.example.eds.geolocation;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public double lat;
    public double lon;
    MyLocationNewOverlay locationOverlay;
    ArrayList<String> A1= new ArrayList<>(), A2 = new ArrayList<>(), A3 = new ArrayList<>(),
        A4 = new ArrayList<>(), A5= new ArrayList<>();
    MapView mv;
        ItemizedIconOverlay<OverlayItem> items;
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this,
                PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location mgr1 = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mv = (MapView)findViewById(R.id.map1);
        mv. getController().setCenter(new GeoPoint(51.05,-0.72));
        mv.setBuiltInZoomControls(true);
        mv. getController().setZoom(16);

        if (mgr1 !=null){
            updateloc(mgr1);
        }


        try {
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);


        } catch (SecurityException e) {
            System.out.println("Error" + e);

        }

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>(){
            public boolean onItemLongPress (int i, OverlayItem item){
                Toast.makeText(MainActivity.this,"Press" + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onItemSingleTapUp (int i, OverlayItem item){
                Toast.makeText(MainActivity.this, "Tap" + item.getSnippet(), Toast.LENGTH_SHORT).show();
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

        return false;

    }
    public void onActivityResult (int requestCode, int resultCode,Intent intent){
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                Bundle extras = intent.getExtras();



                    String name= extras.getString("com.example.tx1");
                    String type = extras.getString("com.example.tx2");
                    String des = extras.getString("com.example.tx3");
                   // A1.add(name);
                   // A2.add(type);
                   // A3.add(des);
                   // A4.add(lat);
                    //A5.add(lon);

                    items = new ItemizedIconOverlay<OverlayItem>(this, new
                            ArrayList<OverlayItem>(), markerGestureListener);
                    OverlayItem Item = new OverlayItem(name,type,des,
                            new GeoPoint(lon,lat));
                    items.addItem(Item);
                    mv.getOverlays().add(items);

                }
                }

        }



    public void updateloc(Location location){
        GeoPoint Location = new GeoPoint
                (location.getLatitude(), location.getLongitude());
        mv.getController().setCenter(Location);

    }

private LocationListener myLocationListener=
        new LocationListener() {
            @Override
            public void onLocationChanged(Location newloc) {
                updateloc(newloc);

                lat = newloc.getLatitude();
                lon = newloc.getLongitude();


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
    public void onDestroy(){
        super.onDestroy();

    }


}
