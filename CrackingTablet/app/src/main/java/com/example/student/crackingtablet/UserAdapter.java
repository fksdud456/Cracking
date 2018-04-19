package com.example.student.crackingtablet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    ArrayList<User> list;
    Context context;
    LinearLayout container;

    public UserAdapter() {
    }

    public UserAdapter(ArrayList<User> list, Context context, LinearLayout container) {
        this.list = list;
        this.context = context;
        this.container = container;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vw = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vw = inflater.inflate(R.layout.user, container, true);

        ImageView img = vw.findViewById(R.id.imageView_profile_m);
        TextView id = vw.findViewById(R.id.tv_id_m);
        TextView name = vw.findViewById(R.id.tv_name_m);
        TextView date = vw.findViewById(R.id.tv_loc_m);

        id.setText(list.get(i).getId());
        name.setText(list.get(i).getName());
        date.setText(list.get(i).getRdate());
       // img.setImageResource(list.get(i));

        return vw;
    }
}
