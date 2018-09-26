package com.example.choudhary.mobilepass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Button registranionbtn, authenicatioinbtn;
    ConnectionClass connectionClass2;
    public static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registranionbtn = (Button) findViewById(R.id.registrationBTN);
        authenicatioinbtn = (Button) findViewById(R.id.authenticationBTN);

        connectionClass2 = new ConnectionClass();

        registranionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter==0)
                {
                    startActivity(new Intent(MainActivity.this,registerClass.class));
                }
                else if(counter==1) {
                    Toast.makeText(getBaseContext(),"You are Already Registered",Toast.LENGTH_SHORT).show();
                }
            }
        });

        authenicatioinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String z="";
                try {
                    Connection con = connectionClass2.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {
                        String query = "select * from demoregister where name = '"+registerClass.namestrHash+"' and count='1' ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.absolute(1)) {
                            Intent intent = new Intent(getApplicationContext(),authenticationClass.class);
                            startActivity(intent);
                        }
                        else
                        {
                            z="Firstly Login in Site";
                        }
                    }
                } catch (Exception ex) {
                    z = "Exceptions" + ex;
                }
                Toast.makeText(getBaseContext(),z,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
