//Adapter for mylist1(Custom ListView)

package com.piyush025.tracker;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter1 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> mob;
    private final ArrayList<String> address;
    private final ArrayList<String> email;

    public MyListAdapter1(Activity context,ArrayList<String> name,ArrayList<String> mob,ArrayList<String> email,ArrayList<String> address)
    {
         super(context, R.layout.mylist1,name);

        this.context=context;
        this.name=name;
        this.address=address;
        this.email=email;
        this.mob=mob;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist1, null,true);

        TextView lname = (TextView) rowView.findViewById(R.id.lname);
        TextView laddress = (TextView) rowView.findViewById(R.id.laddress);
        TextView lemail = (TextView) rowView.findViewById(R.id.lemail);
        TextView lmob = (TextView) rowView.findViewById(R.id.lmob);

        lname.setText(name.get(position));
        laddress.setText(address.get(position));
        lemail.setText(email.get(position));
        lmob.setText(mob.get(position));
        return rowView;
    }

}
