package com.example.choudhary.mobilepass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by choudhary on 12/6/18.
 */

public class selectPassImageClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_gridlayout);

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        final Intent intent = getIntent();
        final String userstr = intent.getExtras().getString("user");
        final String emailstr = intent.getExtras().getString("email");
        final String passstr = intent.getExtras().getString("pass");
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(selectPassImageClass.this, "" + position,Toast.LENGTH_SHORT).show();

                intent.putExtra("id", position);
                intent.putExtra("user", userstr);
                intent.putExtra("email",emailstr);
                intent.putExtra("pass",passstr);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
