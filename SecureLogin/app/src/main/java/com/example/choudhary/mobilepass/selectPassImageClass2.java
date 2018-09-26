package com.example.choudhary.mobilepass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class selectPassImageClass2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_gridlayout);

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(selectPassImageClass2.this, "" + position,Toast.LENGTH_SHORT).show();

                Intent Iauth = new Intent();
                Iauth.putExtra("id",position);
                setResult(RESULT_OK, Iauth);
                finish();
            }
        });
    }
}
