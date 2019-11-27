//Class for RegisterHost Activity that allows to register new hosts.

package com.piyush025.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterHost extends AppCompatActivity {

    EditText hname,hemail,hmob,haddress;
    Button hregister;

    TextView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_host);

        hname=(EditText) findViewById(R.id.hname);
        hemail=(EditText) findViewById(R.id.hemail);
        hmob=(EditText) findViewById(R.id.hmob);
        haddress=(EditText) findViewById(R.id.haddress);
        hregister=(Button)findViewById(R.id.hregister);
        back=(TextView)findViewById(R.id.back1);




        back.setOnClickListener(new View.OnClickListener()  //Go back
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Track.class);
                startActivity(i);
            }
        });

        hregister.setOnClickListener(new View.OnClickListener() //Submitting the Host details
        {
            @Override
            public void onClick(View v) {

                registerHost();
            }
        });
    }

    private void registerHost()         //Validates all the fields and only the proceeds to register a host to the database.
    {

        final String name = hname.getText().toString().trim();
        final String email = hemail.getText().toString().trim();
        final String mob = hmob.getText().toString().trim();
        final String address = haddress.getText().toString().trim();


        //Validation Starts.

        if(name.isEmpty())
        {
            hname.setError("Please Enter Host Name!");
            hname.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            hemail.setError("Email is Required!");
            hemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            hemail.setError("Enter a valid Email!");
            hemail.requestFocus();
            return;
        }

        if(mob.isEmpty())
        {
            hmob.setError("Mobile Numeber is Required!");
            hmob.requestFocus();
            return;
        }

        String regx = "^[6789][0-9]{9}$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mob);
        if(!matcher.find())
        {
            hmob.setError("Enter a Valid phone number!");
            hmob.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            haddress.setError("Address is Required!");
            haddress.requestFocus();
            return;
        }



        //Validation Ends.

        saveHost(name,email,mob,address);



    }

    private void saveHost(String name, String email, String mob, String address) {

        Host host=new Host(name,email,mob,address);     //Creating Host Instance.

        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference("Host").push();  //Produces a unique node under Host Node in the database.

        databaseReference.setValue(host);       //host instance node is added under Host Node of database.


        Toast.makeText(this, "Host Registration Successful.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), Track.class);
        startActivity(i);
    }
}
