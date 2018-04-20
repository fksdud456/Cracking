package com.example.student.crackingtablet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
}
