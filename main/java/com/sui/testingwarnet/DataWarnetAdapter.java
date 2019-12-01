package com.sui.testingwarnet;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DataWarnetAdapter extends ArrayAdapter<Data_User> {
    private Activity context;
    private List<Data_User> WarnetList;
    public DataWarnetAdapter(Activity context, List<Data_User> warnetList){
        super(context, R.layout.format_list_warnet, warnetList);
        this.context = context;
        this.WarnetList = warnetList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.format_list_warnet,null,true);

        TextView userName = (TextView)listView.findViewById(R.id.username_user);
        TextView userEmail = (TextView)listView.findViewById(R.id.email_user);

        Data_User testing = WarnetList.get(position);
        userName.setText(testing.getNama());
        userEmail.setText(testing.getEmail());
        return listView;
    }
}
