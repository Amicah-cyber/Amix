package com.example.trainbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import static android.widget.Toast.*;

public class  LOGIN extends AppCompatActivity {
    private TextView textView;
    private EditText Id;
    private Button Loginbtn;
    private EditText pass;
    final String ERROR1 = "invalid login";
    final String ERROR2 = "error connecting";
    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginbtn =  findViewById(R.id.Loginbtn);
        Id =  findViewById(R.id.id_number);
        pass =  findViewById(R.id.password);
        textView =  findViewById(R.id.textView);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LOGIN.this, SignUpActivity.class));
            }
        });
    }
        private void logIn(){
            int id_number = Integer.parseInt(Id.getText().toString());
            String password = pass.getText().toString();
            Background background = new Background();
            Log.i(TAG,String.valueOf(id_number));
            background.execute(String.valueOf(id_number), password);
        }

        private void getObject(String s){

            if (s.equals(ERROR1)|| s.equals (ERROR2)){
               Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
               return;
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                    JSONObject object = jsonArray.getJSONObject(0);

                    user user = new user (object.getInt("id"),
                        object.getString("names"),
                        object.getString("email"),
                        object.getInt("mobile_number"),
                        object.getInt("age"),
                        object.getString("password"));
                    Toast.makeText(getBaseContext(),user.getFullnames(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, Home.class);
                    intent.putExtra(CallbackInterface.ID, user.getId_number());
                    intent.putExtra(CallbackInterface.NAME, user.getFullnames());
                    intent.putExtra(CallbackInterface.EMAIL, user.getEmail());
                    intent.putExtra(CallbackInterface.MOBILE_NUMBER, user.getMobile_number());
                    intent.putExtra(CallbackInterface.AGE, user.getAge());
                    intent.putExtra(CallbackInterface.PASSWORD, user.getPassword());
                    startActivity(intent);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

        private class Background extends AsyncTask<String,Void,String>{

            @Override
            protected void onPreExecute() {super.onPreExecute();}

            @Override
            protected String doInBackground(String... strings){
                int id_number = Integer.parseInt(strings[0]);
                String password = strings[1];
                String result = "";
                try{
                    URL url = new URL("http://10.0.2.2/projects/Login.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    String data = URLEncoder.encode("id_number", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id_number),"UTF-8")+"&"+
                            URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    Log.i(TAG, "doInBackground: encoded data: "+data);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line=bufferedReader.readLine()) !=null){
                        stringBuilder.append(line +"\n");
                        result = stringBuilder.toString().trim();
                    }
                    Log.i(TAG, "doInBackground: return result: "+ result);
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

                return result;
            }
            @Override
            protected void onPostExecute(String s){
                getObject(s);
                super.onPostExecute(s);
            }
        }
    }

