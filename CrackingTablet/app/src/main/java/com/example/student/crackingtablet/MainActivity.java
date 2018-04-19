package com.example.student.crackingtablet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String wcURL = "http://70.12.114.144/wc";

    private LinearLayout l_home, l_chart, l_management, container_h, container_m;
    private WebView webView_chart;
    private boolean flag = true;
    private ReceiveData connectionReceiver;

    Runnable r = new Runnable() {
        @Override
        public void run() {
            while(flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //좌표를 가져오기
                Log.d("connectionReceiver ::" , "run");
                connectionReceiver = new ReceiveData(wcURL+"/connection.do");
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

        makeUI();
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
            l_chart.setVisibility(View.INVISIBLE);
            l_management.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_map) {

        } else if (id == R.id.nav_management) {
            l_home.setVisibility(View.INVISIBLE);
            l_chart.setVisibility(View.INVISIBLE);
            l_management.setVisibility(View.VISIBLE);
            UpdateManagementList();
        } else if (id == R.id.nav_chart) {
            l_home.setVisibility(View.INVISIBLE);
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
        l_home = findViewById(R.id.l_home);
        l_chart = findViewById(R.id.l_chart);
        l_management = findViewById(R.id.l_management);
        webView_chart = findViewById(R.id.webView_chart);
        container_h = findViewById(R.id.container_h);
        container_m = findViewById(R.id.container_m);

        UpdateHomeList();

        webView_chart.setWebViewClient(new WebViewClient());
        webView_chart.getSettings().setJavaScriptEnabled(true);
        webView_chart.loadUrl(wcURL + "chart");

        l_home.setVisibility(View.VISIBLE);
        l_chart.setVisibility(View.INVISIBLE);
        l_management.setVisibility(View.INVISIBLE);
    }

    private void UpdateHomeList() {
        ArrayList<User> list = new ArrayList<>();
        ReceiveData receiveData = new ReceiveData(wcURL + "/connection.do");
        receiveData.addParameter("?comm=t");

        try {
            String connectedIds = receiveData.execute().get();
            Toast.makeText(this, connectedIds, Toast.LENGTH_LONG).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        UserAdapter userAdapter = new UserAdapter(list, this, container_h);
        ListView listView = findViewById(R.id.list_manage);
        listView.setAdapter(userAdapter);
    }

    private void UpdateManagementList() {
        // Get online userlist from database
        ArrayList<User> list = new ArrayList<>();
        ReceiveData receiveData = new ReceiveData(wcURL + "/alluser.do");
        receiveData.addParameter("?comm=t");
        try {
            String allUser = receiveData.execute().get();
            Util.getListFromJSON(list, allUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        UserGridAdapter userGridAdapter = new UserGridAdapter(list, this, container_m);
        GridView gridView = findViewById(R.id.grid_mange);
        gridView.setAdapter(userGridAdapter);
    }

    public void onDisconnectUser(View v) {

        Toast.makeText(this, "Disconnect USER", Toast.LENGTH_SHORT).show();
    }
}
