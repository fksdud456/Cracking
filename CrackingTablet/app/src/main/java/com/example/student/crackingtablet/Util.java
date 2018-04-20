package com.example.student.crackingtablet;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Util {
    private static final String TAG = "Util ::: ";
    public static final int CONN = 1;
    public static final int LOGIN = 1;
    public static final int DISCONN = 0;
    public static final int NLOGIN = 0;

    public static void getListFromJSON(ArrayList<User> list, String josnString) {
        try {
            JSONArray ja = new JSONArray(josnString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                // jo.toString("img")
                int imgNum = 0;
                list.add(new User(jo.getString("id"), "", jo.getString("name"), jo.getString("registdate"), imgNum, 0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public static void getStringListFromJSON(ArrayList<String> list, String josnString) {
        try {
            JSONArray ja = new JSONArray(josnString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                Log.d("getStringFromJSON",jo.getString("id"));
                list.add(jo.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public static void getAllFromJSON(HashMap<String, User> userMap,  String josnString) {
        User user;
        try {
            JSONArray ja = new JSONArray(josnString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                // jo.toString("img")
                int imgNum = 0;
                user= new User(jo.getString("id"),
                        "",
                        jo.getString("name"),
                        jo.getString("registdate"),
                        imgNum, 0);
                userMap.put(jo.getString("id"),
                        user);
                Log.d(TAG, "getAllFromJSON :: "  + user.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public static void setAllUser(HashMap<String, User> listH,  ArrayList<User> all, ArrayList<String> login, ArrayList<String> conn) {
        User user;
        for(String id : login) {
            user = listH.get(id);
            user.setLogin(LOGIN);

            listH.put(id, user);
        }
        for(String id : conn) {
            user = listH.get(id);
            user.setConn(CONN);

            listH.put(id, user);
        }

        Log.d("listH.keySet()", listH.keySet().toString() +"" + listH.keySet().size());
        all.clear();
        for ( String key : listH.keySet() ) {
            all.add(listH.get(key));
        }

        return;
    }
}
