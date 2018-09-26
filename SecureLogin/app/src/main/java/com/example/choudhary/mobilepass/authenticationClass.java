package com.example.choudhary.mobilepass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;


public class authenticationClass extends Activity {

    TextView imagenameinauth, usernameinauth;
    Button selectimagebtninauth, loginbtninauth, showpasswordbtn;
    EditText pininauth;
    ImageView selectedimageinauth;
    ConnectionClass connectionClass2;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    ProgressDialog progressDialog;
    public boolean isTokenmatched ;
    int count=0;

    public static String intentData1 = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        imagenameinauth = (TextView) findViewById(R.id.selectedIMGNAMEinAUTH);
        selectimagebtninauth = (Button) findViewById(R.id.selectImageBTNinAUTH);
        loginbtninauth = (Button) findViewById(R.id.LoginBTNinAUTH);
        pininauth = (EditText) findViewById(R.id.pinedittextinAUTH);
        selectedimageinauth = (ImageView) findViewById(R.id.selectedIMGinAUTH);
        usernameinauth = (TextView) findViewById(R.id.usernameTxtView);
        usernameinauth.setText(registerClass.namestrr);
        progressDialog = new ProgressDialog(this);
        showpasswordbtn = (Button) findViewById(R.id.show_password_in_auth);
        connectionClass2 = new ConnectionClass();

        selectimagebtninauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), selectPassImageClass2.class);
                startActivityForResult(I, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        showpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                if(count%2==1)
                {
                    pininauth.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    pininauth.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        loginbtninauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namestrrinauth = usernameinauth.getText().toString();
                String namestrHashinauth = get_SHA_512_SecureText(namestrrinauth,"india");
                String passstrinauth = pininauth.getText().toString();
                String passstrHashinauth = get_SHA_512_SecureText(passstrinauth,"india");
                String imagenamee = String.valueOf(imagenameinauth.getText());

                server_side_implementation(namestrHashinauth,passstrHashinauth,imagenamee);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Dologin dologin = new Dologin();
                        dologin.execute();
                    }
                }, 1000);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                int position = data.getExtras().getInt("id");
                imagenameinauth.setText("img" + String.valueOf(position) + ".png");
                ImageAdapter imageAdapter = new ImageAdapter(this);
                ImageView imageView = (ImageView) findViewById(R.id.selectedIMGinAUTH);
                imageView.setVisibility(View.VISIBLE);
                imagenameinauth.setVisibility(View.VISIBLE);
                imageView.setImageResource(imageAdapter.mThumbIds[position]);
            }
        }
    }


    public class Dologin extends AsyncTask<String, String, Void> {

        String passstrinauth = pininauth.getText().toString();
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (passstrinauth.trim().equals("")){
                z = "Please enter all fields....";
            }
            else {
                try {
                    String namestrrinauth = usernameinauth.getText().toString();
                    String namestrHashinauth = get_SHA_512_SecureText(namestrrinauth,"india");
                    String passstrinauth = pininauth.getText().toString();
                    Connection con = connectionClass2.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {
                        String query = "update demoregister set isAut = '1' where name = '"+ namestrHashinauth +"' and count='1' ";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        z = "Authenticate successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_SHORT).show();
            if (isTokenmatched && isSuccess) {
                Intent intent = new Intent(authenticationClass.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "PassImage or Password is incorrect! ", Toast.LENGTH_SHORT).show();
                pininauth.setText("");
                imagenameinauth.setText("");
                selectedimageinauth.setImageResource(0);
            }
            progressDialog.hide();

        }
    }

    public void server_side_implementation(final String namestrHash, final String passstrHash, final String imagename){

        new Thread(new Runnable() {
            public void run() {

                try{

                    URL url = new URL("http://10.0.2.2:8080/Backend_scripts/ForRegestrationServlet");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("user", namestrHash);
                    params.put("pass", passstrHash);
                    params.put("PassImage",imagename);

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder data = new StringBuilder();
                    for (int c; (c = in.read()) >= 0;) {
                        data.append((char)c);
                    }
                    intentData1 = data.toString();

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String gettoken = pref.getString("key", "");
                    Log.d("String in SharedPref",gettoken);

                    if (intentData1.equals(gettoken)){
                        isTokenmatched = true;
                        Log.d("tagggggg","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                        Log.d("Server Qi in Auth:- ",intentData1);
                    }

                    else {
                        isTokenmatched = false;
                    }

                }catch(Exception e)
                {
                    Log.d("Exception",e.toString());
                }

            }
        }).start();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String get_SHA_512_SecureText(String passwordToHash, String   salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

}