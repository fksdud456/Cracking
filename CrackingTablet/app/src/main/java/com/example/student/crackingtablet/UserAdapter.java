package com.example.student.crackingtablet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

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
        vw = inflater.inflate(R.layout.user_home, container, true);

        ImageView img = vw.findViewById(R.id.imageView_profile_h);
        TextView id = vw.findViewById(R.id.tv_id_h);
        TextView name = vw.findViewById(R.id.tv_name_h);
        TextView date = vw.findViewById(R.id.tv_loc_h);
        Log.d("getView :: ", "index" + i+" size: "+list.size());
        User user = list.get(i);
        id.setText(user.getId());
        name.setText(user.getName());
        date.setText(user.getRdate());
        int imgNum = R.drawable.humanhead;

        if (user.getImg() != 0)
            imgNum = user.getImg();

        img.setImageResource(imgNum);
        return vw;
    }

    public void setList(ArrayList<User> list) {
        this.list = list;
    }
}
