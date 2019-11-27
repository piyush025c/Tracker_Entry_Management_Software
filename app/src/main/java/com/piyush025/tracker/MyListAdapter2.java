//Adapter for mylist2(Custom ListView)

package com.piyush025.tracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter2 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> email;
    private final ArrayList<String> checkin;



    public MyListAdapter2(Activity context,ArrayList<String> name,ArrayList<String> email,ArrayList<String> checkin)
    {
        super(context, R.layout.mylist2,name);

        this.context=context;
        this.name=name;
        this.email=email;
        this.checkin=checkin;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist2, null,true);

        TextView vname = (TextView) rowView.findViewById(R.id.vname);
        TextView vemail = (TextView) rowView.findViewById(R.id.vemail);
        TextView vcheckin = (TextView) rowView.findViewById(R.id.vcheckin);

        vname.setText(name.get(position));
        vemail.setText(email.get(position));
        vcheckin.setText(checkin.get(position));
        return rowView;
    }

}
