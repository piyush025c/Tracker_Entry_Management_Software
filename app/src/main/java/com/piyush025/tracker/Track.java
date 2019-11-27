//Interface that allows user to take a choice i.e. manage visitor entry or register a new host.

package com.piyush025.tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Track extends AppCompatActivity {

    Button manage;

    TextView registerHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)  //Request Permission for SMS at the start itself.
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
        }


        manage=(Button)findViewById(R.id.manage);
        registerHost=(TextView)findViewById(R.id.registerHost);

        manage.setOnClickListener(new View.OnClickListener() // Takes user to Home Activity where guest entries can be managed.
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
            }
        });

        registerHost.setOnClickListener(new View.OnClickListener() //Takes user to RegisterHost Activity where a new host can be registered.
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), RegisterHost.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed()
    {

        Toast.makeText(this, "Press Home Button to exit app.", Toast.LENGTH_SHORT).show();
    }
}
