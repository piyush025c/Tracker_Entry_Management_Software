//Fragment that Manages Check-out process.

package com.piyush025.tracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Checkout extends Fragment {

    EditText qname;
    Button btn;

    DatabaseReference databaseReference,gout;

    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> checkin1 = new ArrayList<>();
    ArrayList<String> visitid1 = new ArrayList<>();
    ArrayList<String> email1 = new ArrayList<>();

    ListView listView;

    MyListAdapter2 arrayAdapter;     //Adapter for custom lisView.


    public  Checkout()
    { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.checkout,container,false);

        qname=(EditText)v.findViewById(R.id.qname);
        btn=(Button)v.findViewById(R.id.qbtn);
        qname.setText("");


        listView = (ListView)v.findViewById(R.id.lv2);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name=qname.getText().toString().trim();
                name1.clear();
                email1.clear();
                checkin1.clear();
                visitid1.clear();

                //Set the Adapter for the listView: Empty ListView.
                arrayAdapter = new MyListAdapter2(getActivity(),name1,email1,checkin1);
                listView.setAdapter(arrayAdapter);

                Log.e("TAG1",name);

                if(name.isEmpty())
                {
                    qname.setError("Please Enter Host Name!");
                    qname.requestFocus();
                    return;
                }

                listView.setVisibility(View.VISIBLE);

                Log.e("TAG2","ok");

                databaseReference= FirebaseDatabase.getInstance().getReference("VisitorDetail");        //Reference to the VisitorDetail node in the database.


                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {      //Iterates through all the children of VisitorDetail.


                        String name2=dataSnapshot.child("gname").getValue(String.class);
                        String checkout=dataSnapshot.child("gcheckout").getValue(String.class);



                        if(name2.toUpperCase().equals(name.toUpperCase())&&checkout.equals("NA"))   //Matches entries that have the specified name and checks if checkout is NA.
                        {
                            Log.e("TAG2",name2);
                            String email=dataSnapshot.child("gemail").getValue(String.class);
                            String checkin=dataSnapshot.child("gcheckin").getValue(String.class);

                            String id=dataSnapshot.child("visitid").getValue(String.class);

                            name1.add(name2);
                            email1.add(email);
                            checkin1.add(checkin);
                            visitid1.add(id);


                            //Populates the matching result into the listView.
                            arrayAdapter = new MyListAdapter2(getActivity(),name1,email1,checkin1);
                            listView.setAdapter(arrayAdapter);


                        }


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




            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {     //Select the matching enrty from result.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String visitor=visitid1.get(position);

                databaseReference=FirebaseDatabase.getInstance().getReference("VisitorDetail").child(visitor).child("gcheckout");    //Reference to gcheckout field of the selected entry.


                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Conform Check-out.");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Click OK to confirm your Chek-out.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {        //Request Confirmation.

                                Date d = new Date();    //Create Date instance to extract current date and time.
                                final String checkout = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.getTime()).toString();   ////Set checkout as current date and time in the specified format.

                                databaseReference.setValue(checkout);       //Update gcheckout field of the selected entry.

                                gout=FirebaseDatabase.getInstance().getReference("VisitorDetail").child(visitor);        //Reference to Checked-Out visitor

                                gout.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        //Extract Visit details.
                                        final String name= dataSnapshot.child("gname").getValue().toString();
                                        final String mob= dataSnapshot.child("gmob").getValue().toString();
                                        final String email= dataSnapshot.child("gemail").getValue().toString();
                                        final String checkin= dataSnapshot.child("gcheckin").getValue().toString();
                                        final String checkout= dataSnapshot.child("gcheckout").getValue().toString();
                                        final String hname= dataSnapshot.child("hname").getValue().toString();
                                        final String address= dataSnapshot.child("haddress").getValue().toString();

                                        //Create message.
                                        String message="Visit Details: \n"+"Name: "+name+"\n"+"Mob: "+mob+"\n"+"Checkin: "+checkin+"\nCheckout: "+checkout+"\nHost Name: "+hname+"\nAdress Visited :"+address;


                                        JavaMailAPI x=new JavaMailAPI(getContext(),email,"Visit Details.",message);       //Using JavaMail API to send message as email.

                                        x.execute();        //send email.
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                //Remove the entry from the listView.

                                name1.remove(position);
                                email1.remove(position);
                                checkin1.remove(position);
                                visitid1.remove(position);
                                arrayAdapter = new MyListAdapter2(getActivity(),name1,email1,checkin1);
                                listView.setAdapter(arrayAdapter);


                                dialog.dismiss();
                                qname.setText("");

                                Toast.makeText(getActivity(), "Successfully checked out!", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.show();



            }
        });

        return v;
    }
}
