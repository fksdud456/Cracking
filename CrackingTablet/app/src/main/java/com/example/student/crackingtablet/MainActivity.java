package com.example.student.crackingtablet;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

//implements OnMapReadyCallback
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private final String TAG = "MainActivity:::";

    private LinearLayout l_home, l_chart, l_management, l_map, container_h, container_m;
    private WebView webView_chart;
    private GoogleMap mMap;
    private HashMap<String, User> allUserH;
    private ArrayList<String> loginUser;
    private ArrayList<String> connUser;
    private ArrayList<String> dataUser;
    private ArrayList<Location1> list;

    private UserAdapter userAdapter; //home login user list
    private UserGridAdapter userGridAdapter; // management user grid view
    private Intent connIntent;
    private Intent dataIntent;


    String id;
    GridView gridView;
    ImageButton btn_disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        connIntent = new Intent(this, ConnService.class);
        startService(connIntent);

        dataIntent = new Intent(this, DataService.class);
        startService(dataIntent);

        gridView = findViewById(R.id.grid_manage);

        makeUI();

        UpdateManagementList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String command = intent.getStringExtra("command");
        Log.d(TAG, "onNewIntent ::  " + command);

        if (command.equals("connservice")) {
            userGridAdapter.setOptionDisableAll();

            connUser.clear();
            loginUser.clear();

            String res = intent.getStringExtra("conn");
            Util.getStringListFromJSON(connUser, res);

            for (String id : connUser)
                userGridAdapter.setOptionEnable(id, User.CONNECTION);

            res = intent.getStringExtra("login");
            Util.getStringListFromJSON(loginUser, res);

            for (String id : loginUser)
                userGridAdapter.setOptionEnable(id, User.LOGIN);

            userGridAdapter.notifyDataSetChanged();
        } else if (command.equals("dataservice")) {

            userGridAdapter.setMotionDisableAll();
            dataUser.clear();

            String res = intent.getStringExtra("data");
            Log.d(TAG, res);
            Util.getStringListFromJSON(dataUser, res);

            for (String id : dataUser)
                userGridAdapter.setOptionEnable(id, User.MOTION);

            userGridAdapter.notifyDataSetChanged();
        }

        super.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            l_home.setVisibility(View.VISIBLE);
            l_map.setVisibility(View.INVISIBLE);
            l_chart.setVisibility(View.INVISIBLE);
            l_management.setVisibility(View.INVISIBLE);
            UpdateHomeLayout();
        } else if (id == R.id.nav_map) {
            l_home.setVisibility(View.INVISIBLE);
            l_map.setVisibility(View.VISIBLE);
            l_chart.setVisibility(View.INVISIBLE);
            l_management.setVisibility(View.INVISIBLE);
            requestMyLocation();
        } else if (id == R.id.nav_management) {
            l_home.setVisibility(View.INVISIBLE);
            l_map.setVisibility(View.INVISIBLE);
            l_chart.setVisibility(View.INVISIBLE);
            l_management.setVisibility(View.VISIBLE);
            UpdateManagementList();
        } else if (id == R.id.nav_chart) {
            l_home.setVisibility(View.INVISIBLE);
            l_map.setVisibility(View.INVISIBLE);
            l_chart.setVisibility(View.VISIBLE);
            l_management.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeUI() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        l_home = findViewById(R.id.l_home);
        l_map = findViewById(R.id.l_map);
        l_chart = findViewById(R.id.l_chart);
        l_management = findViewById(R.id.l_management);
        webView_chart = findViewById(R.id.webView_chart);
        container_h = findViewById(R.id.container_h);
        container_m = findViewById(R.id.container_m);

        webView_chart.setWebViewClient(new WebViewClient());
        webView_chart.getSettings().setJavaScriptEnabled(true);
        webView_chart.loadUrl(Util.wcURL + "/chart.do?comm=w");

        l_home.setVisibility(View.VISIBLE);
        l_chart.setVisibility(View.INVISIBLE);
        l_management.setVisibility(View.INVISIBLE);
        l_map.setVisibility(View.INVISIBLE);

        allUserH = new HashMap<>();
        loginUser = new ArrayList<>();
        connUser = new ArrayList<>();
        dataUser = new ArrayList<>();
        list = new ArrayList<Location1>();

        getAllUser();

        ArrayList<User> loginUser = getLoginUser();
        userAdapter = new UserAdapter(loginUser, this, container_h);
        ListView listView = findViewById(R.id.list_manage);
        listView.setAdapter(userAdapter);

        userGridAdapter = new UserGridAdapter(new ArrayList<User>(), this, container_m);
        gridView = (GridView) findViewById(R.id.grid_manage);
        gridView.setAdapter(userGridAdapter);
    }

    private void getAllUser() {
        try {
            ReceiveData receiveData = new ReceiveData("/alluser.do");
            receiveData.addParameter("?comm=t");
            String res = receiveData.execute().get();
            Log.d(TAG, "getAllUser . . . res : " + res);

            if (res != null && !res.equals("")) {
                allUserH.clear();
                Util.getAllFromJSON(allUserH, res);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<User> getLoginUser() {
        Log.d(TAG, "UpdateHomeLayout . . .");

        ReceiveData receiveData = new ReceiveData("/loginuser.do");
        receiveData.addParameter("?comm=t");

        String res = "";
        try {
            res = receiveData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<User> list = new ArrayList<>();
        loginUser.clear();
        if (res == null || res.equals("")) {
            Toast.makeText(this, "No one has logined", Toast.LENGTH_SHORT);
        } else {
            Util.getStringListFromJSON(loginUser, res);
            for (String id : loginUser) {
                list.add(allUserH.get(id));
            }
        }
        return list;
    }

    private void UpdateHomeLayout() {
        Log.d(TAG, "UpdateHomeLayout()");

        userAdapter.setList(getLoginUser());
        userAdapter.notifyDataSetChanged();
    }

    private void UpdateManagementList() {
        Log.d(TAG, "UpdateManagementList()");
        getAllUser();
        userGridAdapter.setList(Util.getAllUser(allUserH, loginUser, connUser, dataUser));
        userGridAdapter.notifyDataSetChanged();

        if (list.size() == 0) {
            Toast.makeText(this, "No user", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

  /*      LatLng sydney = new LatLng(-34, 127.0266826);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        //requestMyLocation();
    }

    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            return;
        }

        mMap.setMyLocationEnabled(true);

        ReceiveData recvData = new ReceiveData("/location.do?comm=t");

        String res = null;
        try {
            res = recvData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        list.clear();
        Util.getLocationFromJSON(list, res);


        for (int i = 0; i < list.size(); i++) {
            double latitude = Double.parseDouble(list.get(i).lat);
            double longitude = Double.parseDouble(list.get(i).lon);

            if (latitude > 40 || latitude < 34 || longitude > 130 || longitude < 125) {
                SendData sendData = new SendData("/disconnect.do?comm=t&id=" + list.get(i).id);
                sendData.execute();
                continue;
            }
            LatLng m1 = new LatLng(Double.parseDouble(list.get(i).lat), Double.parseDouble(list.get(i).lon));
            mMap.addMarker(new MarkerOptions().position(m1).title(list.get(i).id));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 10));
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        stopService(connIntent);
        stopService(dataIntent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        super.onDestroy();
    }
}
