package com.example.student.crackingtablet;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

//implements OnMapReadyCallback
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private final String TAG = "MainActivity:::";
    private final String wcURL = "http://70.12.114.144/wc";
    private LinearLayout l_home, l_chart, l_management, l_map, container_h, container_m;
    private WebView webView_chart;
    private GoogleMap mMap;
    private HashMap<String, User> allUserH;
    private ArrayList<User> allUser;
    private ArrayList<String> loginUser;
    private ArrayList<String> connUser;


    private UserAdapter userAdapter; //home 화면 리스트
    private UserAdapter userAdapter;
    private Intent connIntent;

    private boolean first = true;
    private boolean flag = true;
    private ReceiveData connectionReceiver;
    String id;
    UserGridAdapter gridAdapter;
    GridView gridView;
    ImageButton btn_disconnect;

    Runnable r = new Runnable() {
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //좌표를 가져오기
                Log.d("connectionReceiver ::", "run");
                connectionReceiver = new ReceiveData(wcURL + "/connection.do");
                connectionReceiver.addParameter("?comm=t");
                try {
                    connectionReceiver.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    };

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

        gridView = findViewById(R.id.grid_manage);

        first = true;
        makeUI();
        first = false;
        UpdateManagementList();

    }



    @Override
    protected void onNewIntent(Intent intent) {
        String command = intent.getStringExtra("command");
        // connection.do
        if (command.equals("coon")) {
            //String command = intent.getStringExtra("command");
            String res = intent.getStringExtra("res");
            connUser.clear();
            Util.getStringListFromJSON(connUser, res);
            Log.d(TAG, ":::::::::::::::::::::::::::::::: conn " + res);
        }

        super.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        webView_chart.loadUrl(wcURL + "chart");

        l_home.setVisibility(View.VISIBLE);
        l_chart.setVisibility(View.INVISIBLE);
        l_management.setVisibility(View.INVISIBLE);
        l_map.setVisibility(View.INVISIBLE);

        allUserH = new HashMap<>();
        allUser = new ArrayList<>();
        loginUser = new ArrayList<>();
        connUser = new ArrayList<>();

        getAllUser();
        UpdateHomeLayout();
    }

    private void getAllUser() {
        try {
            ReceiveData receiveData = new ReceiveData(wcURL + "/alluser.do");
            receiveData.addParameter("?comm=t");
            String res = receiveData.execute().get();
            Log.d(TAG, "getAllUser . . . res : " + res);

            if (res != null && !res.equals("")) {
                allUserH.clear();
                allUser.clear();
                Util.getAllFromJSON(allUserH, res);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void UpdateHomeLayout() {
        Log.d(TAG, "UpdateHomeLayout . . .");

        ReceiveData receiveData = new ReceiveData(wcURL + "/loginuser.do");
        receiveData.addParameter("?comm=t");

        try {
            String res = receiveData.execute().get();
            Log.d(TAG, "UpdateHomeLayout . . . res : " + res);
            loginUser.clear();
            if (res != null && !res.equals("")) {
                Util.getStringListFromJSON(loginUser, res);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<User> list = new ArrayList<>();
        for (String id : loginUser) {
            list.add(allUserH.get(id));
        }

        if (first) {
            userAdapter = new UserAdapter(list, this, container_h);
            ListView listView = findViewById(R.id.list_manage);
            listView.setAdapter(userAdapter);
        } else {
            userAdapter.setList(list);
            userAdapter.notifyDataSetChanged();
        }

        if (list.size() == 0) {
            Toast.makeText(this, "No one has logined", Toast.LENGTH_SHORT);
        }
    }

    private void UpdateManagementList() {
        Util.setAllUser(allUserH, allUser, loginUser, connUser);
        //Log.d(TAG, allUser.toString());
        UserGridAdapter userGridAdapter = new UserGridAdapter(allUser, this, container_m);

        gridView = (GridView)findViewById(R.id.grid_manage);
        Toast.makeText(MainActivity.this, "좀 되라", Toast.LENGTH_LONG).show();
        gridView.setAdapter(userGridAdapter);

    }



    public void onDisconnectUser(View v) {


       // Log.d("checkid#####", id);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onDestroy() {
        stopService(connIntent);
        super.onDestroy();
    }

}
