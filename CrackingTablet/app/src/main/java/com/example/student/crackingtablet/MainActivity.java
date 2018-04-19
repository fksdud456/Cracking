package com.example.student.crackingtablet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String wcURL = "70.12.114.144/wc";

    private LinearLayout l_home, l_chart, l_management, container_h, container_m;
    private WebView webView_chart;

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
        webView_chart.loadUrl(wcURL);

        l_home.setVisibility(View.VISIBLE);
        l_chart.setVisibility(View.INVISIBLE);
        l_management.setVisibility(View.INVISIBLE);
    }

    private void UpdateHomeList() {
        // Get online userlist from database
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("fksdud456", "1234", "란영", "20180808", R.drawable.heart, 1));
        UserAdapter userAdapter = new UserAdapter(list,this, container_h);
        ListView listView = findViewById(R.id.list_manage);
        listView.setAdapter(userAdapter);
    }

    private void UpdateManagementList() {
        // Get online userlist from database
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("fksdud456", "1234", "란영", "20180808", R.drawable.heart, 1));
        list.add(new User("dkdkdk234", "1234", "란영2", "20180809", R.drawable.heart, 0));
        UserGridAdapter userGridAdapter = new UserGridAdapter(list,this, container_m);
        GridView gridView = findViewById(R.id.grid_mange);
        gridView .setAdapter(userGridAdapter);
    }

    public void onDisconnectUser(View v) {
        Toast.makeText(this, "Disconnect USER" , Toast.LENGTH_SHORT).show();
    }

}
