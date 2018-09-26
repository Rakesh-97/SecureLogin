package com.example.choudhary.mobilepass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by choudhary on 12/6/18.
 */

public class registerClass extends Activity {

    TextView imagename;
    EditText username,email,pass;
    Button register, selectimagebtn, Available_btn, showpw;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    static String namestrr;
    static String namestrHash;
    private static int pixels[][];
    private static char Data[];
    private static int Width,Height;
    private static int Binary[];
    private static int Length=0;
    public static String intentData="";
    private static final int SECOND_ACTIVITY_REQUEST_CODE= 0;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imagename = (TextView) findViewById(R.id.selectedIMGNAME);
        username= (EditText) findViewById(R.id.usernameET);
        email= (EditText) findViewById(R.id.emailET);
        pass= (EditText) findViewById(R.id.pinET);
        register= (Button) findViewById(R.id.registerBTN);
        selectimagebtn = (Button) findViewById(R.id.selectImageBTN);
        Available_btn = (Button) findViewById(R.id.check_availability);
        showpw = (Button) findViewById(R.id.show_password);

        selectimagebtn.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View v) {

              Intent intent = new Intent(getApplicationContext(), selectPassImageClass.class);
              intent.putExtra("user", username.getText().toString());
              intent.putExtra("email",email.getText().toString());
              intent.putExtra("pass",pass.getText().toString());
              startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        connectionClass = new ConnectionClass();

        Available_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namestrr=username.getText().toString();
                String namestrHash = get_SHA_512_SecureText(namestrr,"india");
                String z = "";
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {
                        String query = "select * from demoregister where name = '"+namestrHash+"' ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.absolute(1)) {
                            z="User Name Already Taken";
                        }
                        else
                        {
                            z = "User Name is Available";
                        }
                    }
                } catch (Exception ex) {
                    z = "Exceptions in Available btn" + ex;
                    }
                Toast.makeText(getApplicationContext(), z,Toast.LENGTH_LONG).show();
                Log.d("DDDDDDDDDDDDDDDDDDDDDDD",z);
            }
        });

        showpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                if(count%2==1)
                {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        progressDialog=new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailValid(email.getText().toString())){
                    Doregister doregister = new Doregister();
                    doregister.execute("");
                    namestrr=username.getText().toString();
                    namestrHash = get_SHA_512_SecureText(namestrr,"india");
                    String passstr=pass.getText().toString();
                    String passstrHash = get_SHA_512_SecureText(passstr,"india");
                    String imagenamee = String.valueOf(imagename.getText());
                    server_side_implementation(namestrHash,passstrHash,imagenamee);
                    MainActivity.counter=1;
                }

                else {
                    Toast.makeText(getBaseContext(),"Please Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                username.setText(data.getStringExtra("user"));
                email.setText(data.getStringExtra("email"));
                pass.setText(data.getStringExtra("pass"));
                int position = data.getExtras().getInt("id");
                imagename.setText("img"+String.valueOf(position)+".png");
                ImageAdapter imageAdapter = new ImageAdapter(this);
                ImageView imageView = (ImageView) findViewById(R.id.selectedIMG);
                imageView.setVisibility(View.VISIBLE);
                imagename.setVisibility(View.VISIBLE);
                imageView.setImageResource(imageAdapter.mThumbIds[position]);
            }
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class Doregister extends AsyncTask<String,String,String>
    {

        String namestr=username.getText().toString();
        String emailstr=email.getText().toString();
        String passstr=pass.getText().toString();
        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (namestr.trim().equals("") || emailstr.trim().equals("") || passstr.trim().equals("") || imagename.getText().toString() == "")
                z = "Please enter all fields....";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {
                        String query = "insert into demoregister values('" + namestrHash + "','" + emailstr + "','0','0')";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        z = "Registered successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }
            return z;

        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                startActivity(new Intent(registerClass.this,MainActivity.class));
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
                    intentData = data.toString();

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("key",intentData);
                    editor.commit();

                    Log.d("tagggggg","RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                    Log.d("Servercalled Qiiii:- ",intentData);


                }catch(Exception e)
                {
                    Log.d("Exceptionnnnnnn",e.toString());
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
