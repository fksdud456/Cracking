package com.example.student.crackingtablet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UserGridAdapter extends BaseAdapter {

    HashMap<String, Integer> map;
    ArrayList<User> list;
    Context context;
    LinearLayout container;
    int status;
    ImageView btn_disconnect;

    public UserGridAdapter() {

    }

    public void setList(ArrayList<User> list) {
        this.list = list;
    }

    public ArrayList<User> getList() {
        return list;
    }

    public UserGridAdapter(ArrayList<User> list, final Context context, LinearLayout container) {
        map = new HashMap<>();
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
        map.put(list.get(i).getId(), i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vw = inflater.inflate(R.layout.user_management, container, true);

        ImageView img = vw.findViewById(R.id.imageView_profile_m);
        ImageView data = vw.findViewById(R.id.imageView_data);
        TextView id = vw.findViewById(R.id.tv_id_m);
        TextView name = vw.findViewById(R.id.tv_name_m);
        TextView date = vw.findViewById(R.id.tv_loc_m);
        btn_disconnect = vw.findViewById(R.id.btn_disconnect);

        User user = list.get(i);

        //id 설정
        if (user.isOptionEnabled(User.CONNECTION)) {
            id.setText(user.getId() + " [ connected ] ");
        } else {
            id.setText(user.getId());
        }

        name.setText(user.getName());
        date.setText(user.getRdate());
        int imgNum = R.drawable.humanhead;
        if (user.getImg() != 0)
            imgNum = user.getImg();
        img.setImageResource(imgNum);

        if (user.isOptionEnabled(User.LOGIN)){
            vw.setBackgroundResource(R.color.white);
            btn_disconnect.setImageResource(R.drawable.delete2);
        } else {
            vw.setBackgroundResource(R.color.common_google_signin_btn_text_light_disabled);
        }

        if (user.isOptionEnabled(User.MOTION)){
            data.setImageResource(R.drawable.pulse2);
        }

        clickDisconnect(user.getId());

        return vw;
    }

    public void clickDisconnect(String id){
        final String idz = id;
        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder((Activity)view.getContext());
                builder.setTitle("Alert");
                builder.setMessage("Are you sure you want to quit this sub app?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendData sendData = new SendData(MainActivity.wcURL+"/disconnect.do?comm=t&id=" + idz);
                        sendData.execute();
                        setOptionDisable(idz, User.CONNECTION|User.LOGIN|User.MOTION);
                    }
                });

                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setOptionEnable(String id, int status) {
        int index = map.get(id);
        User user = list.get(index);
        user.optionEnable(status);

        list.set(index, user);
        notifyDataSetChanged();
    }

    public void setOptionDisable(String id, int status) {
        Log.d("setOptionDisable" , ":: "+ id);
        int index = map.get(id);
        User user = list.get(index);
        user.optionDisable(status);

        list.set(index, user);
        notifyDataSetChanged();
    }

    public void setOptionDisableAll() {
        Log.d("setOptionDisableAll" , ":: ");

        for(User user : list) {
            user.optionDisable(User.MOTION | User.LOGIN | User.CONNECTION);
            list.set(map.get(user.getId()), user);
        }
    }

    public void setMotionDisableAll() {
        Log.d("setOptionDisableAll" , ":: ");

        for(User user : list) {
            user.optionDisable(User.MOTION);
            list.set(map.get(user.getId()), user);
        }
    }
}
