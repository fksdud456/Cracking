package com.example.student.crackingtablet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Util {
    public static ArrayList<User> getListFromJSON(ArrayList<User> list, String josnString) {
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
        return list;
    }

    public static ArrayList<String> getStringListFromJSON(ArrayList<String> list, String josnString) {
        try {
            JSONArray ja = new JSONArray(josnString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                // jo.toString("img")
                int imgNum = 0;
                list.add(jo.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HashMap<String, User> getListFromJSON(HashMap<String, User> list, String josnString) {
        try {
            JSONArray ja = new JSONArray(josnString);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                // jo.toString("img")
                int imgNum = 0;
                list.put(jo.getString("id"),
                        new User(jo.getString("id"),
                                "",
                                jo.getString("name"),
                                jo.getString("registdate"),
                                imgNum, 0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
