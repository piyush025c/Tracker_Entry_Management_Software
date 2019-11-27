//Fragment that manages Check-in process.

package com.piyush025.tracker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checkin extends Fragment {

    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> mob1 = new ArrayList<>();
    ArrayList<String> address1 = new ArrayList<>();
    ArrayList<String> email1 = new ArrayList<>();

    ListView listView;

    DatabaseReference databaseReference;

    MyListAdapter1 arrayAdapter;        //Adapter for custom lisView.

    EditText gname,gmob,gemail;


    public  Checkin()
    { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.checkin,container,false);

        listView = (ListView)v.findViewById(R.id.lv1);

        gname=(EditText)v.findViewById(R.id.gname);
        gmob=(EditText)v.findViewById(R.id.gmob);
        gemail=(EditText)v.findViewById(R.id.gemail);

        databaseReference= FirebaseDatabase.getInstance().getReference("Host");         //Reference to the Host node in the database.

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {      //Iterates through all the children of Host.

                String name=dataSnapshot.child("name").getValue(String.class);
                String mob=dataSnapshot.child("mob").getValue(String.class);
                String address=dataSnapshot.child("address").getValue(String.class);
                String email=dataSnapshot.child("email").getValue(String.class);


                //Store required information using array list.
                name1.add(name);
                mob1.add(mob);
                address1.add(address);
                email1.add(email);

                //Set the Adapter for the listView.
                arrayAdapter = new MyListAdapter1(getActivity(),name1,mob1,email1,address1);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()       //Selecting a Host from the listView
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String name = gname.getText().toString().trim();
                final String mob = gmob.getText().toString().trim();
                final String email = gemail.getText().toString().trim();

                //Validation of Guest details starts.

                if(name.isEmpty())
                {
                    gname.setError("Name is Required!");
                    gname.requestFocus();
                    return;
                }

                if(mob.isEmpty())
                {
                    gmob.setError("Mobile Numeber is Required!");
                    gmob.requestFocus();
                    return;
                }

                String regx = "^[6789][0-9]{9}$";
                Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(mob);
                if(!matcher.find())
                {
                    gmob.setError("Enter a Valid phone number!");
                    gmob.requestFocus();
                    return;
                }

                if(email.isEmpty())
                {
                    gemail.setError("Email is Required!");
                    gemail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    gemail.setError("Enter a valid email!");
                    gemail.requestFocus();
                    return;
                }

                //Validation Ends.

                final String hname=name1.get(position);
                final String hmob=mob1.get(position);
                final String hemail=email1.get(position);
                final String haddress=address1.get(position);

                Date d = new Date();    //Create Date instance to extract current date and time.
                final String checkin = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.getTime()).toString(); //Set checkin as current date and time in the specified format.

                final String  checkout="NA";    //Set checkout to NA.

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Conform Check-in.");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Click OK to confirm your Chek-in.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener()       //Request Confirmation.
                        {
                            public void onClick(DialogInterface dialog, int which) {

                                saveVisitor(name,mob,email,hname,checkin,checkout,hemail,hmob,haddress);        //Save guest Check-in details.
                                dialog.dismiss();

                                String message="Visitor Details: \n"+"Name: "+name+"\n"+"Mob: "+mob+"\n"+"Email: "+email+"\n"+"Chekin: "+checkin;   //Create a message to be sent.

                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)        //Check if SMS permission is granted or not.
                                {
                                    try
                                    {
                                        SmsManager smsMgrVar = SmsManager.getDefault();     //Using SmsManager API to send SMS.
                                        smsMgrVar.sendTextMessage(hmob, null, message, null, null);     //Send message.

                                    }
                                    catch (Exception ErrVar)
                                    {
                                        Toast.makeText(getContext(),ErrVar.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        ErrVar.printStackTrace();
                                    }
                                }
                                else
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                    {
                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                                    }
                                }

                                JavaMailAPI x=new JavaMailAPI(getContext(),hemail,"New Visitor!",message);   //Using JavaMail API to send message as email.

                                x.execute();    //send email.


                                //Reset the EditText Fields.
                                gname.setText("");
                                gemail.setText("");
                                gmob.setText("");

                            }
                        });
                alertDialog.show();


            }
        });

        return v;
    }

    private void saveVisitor(String name, String mob, String email, String hname, String checkin, String checkout, String hemail, String hmob, String haddress) {


        DatabaseReference df=FirebaseDatabase.getInstance().getReference("VisitorDetail").push();       //Produces a unique node under VisitorDetail Node in the database.

        Visitor visitor=new Visitor(name,mob,email,checkin,checkout,hname,hemail,hmob,haddress,df.getKey());        //Creates Visitor instance to store check-in details of guest.
        df.setValue(visitor);       //visitor instance node is added under VisitorDetail Node of database.
    }
}
