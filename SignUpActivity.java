package com.example.trainbook;

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
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    private TextView Sign;
    private EditText Names;
    private EditText ID;
    private EditText Email2;
    private EditText Mobile1;
    private EditText Age1;
    private EditText Pass;
    private EditText Confirm;
    private Button Sign1;
    private DateFormat dateFormat;

    private static final String TAG = "SignUpActivity";

    final String SUCCESS = "success";
    final String ERROR1 = "error registering";
    final String ERROR2 = "error connecting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i(TAG, "oncreate");
        Sign =  findViewById(R.id.Sign);
        Names =  findViewById(R.id.Fullnames);
        ID =  findViewById(R.id.id_number);
        Email2 =  findViewById(R.id.Email);
        Mobile1 = findViewById(R.id.Mobile);
        Age1 =  findViewById(R.id.Age);
        Pass =  findViewById(R.id.password);
        Confirm =  findViewById(R.id.confirm);
        Sign1 =  findViewById(R.id.SIGNUP);


     Sign1.setOnClickListener(new View.OnClickListener()

      {
        @Override
        public void onClick (View v){
        user user = new user(Integer.parseInt(ID.getText().toString()),
                Names.getText().toString(),
                Email2.getText().toString(),
                Integer.parseInt(Mobile1.getText().toString()),
                Integer.parseInt(Age1.getText().toString()),
                Pass.getText().toString());
        Log.i(TAG, "The user is" + user.getFullnames());

        Background background = new Background();
        background.execute(user);
         }
      });

    }
    private void getResult(String result){
        Log.i(TAG, "getResult: "+result);
        switch (result){
            case SUCCESS:{
                Intent intent = new Intent(this, LOGIN.class);
                startActivity(intent);
            }
            case ERROR1:{
                Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
            }
            case ERROR2:{
                Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
            }
            default: Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
        }
    }
     private class Background extends AsyncTask<user, Void, String> {
         @Override
         protected void onPreExecute(){super.onPreExecute(); }
         @Override
         protected String doInBackground(user... users){
             String result= "";
             try{
                 user user = users[0];
                 URL url = new URL("http://10.0.2.2/projects/register.php");
                 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                 httpURLConnection.setRequestMethod("POST");
                 httpURLConnection.setDoInput(true);
                 httpURLConnection.setDoOutput(true);
                 OutputStream outputStream = httpURLConnection.getOutputStream();
                 BufferedWriter bufferedWriter =  new BufferedWriter(new OutputStreamWriter(outputStream));
                 String data = URLEncoder.encode("id_number","UTF-8") + "="+URLEncoder.encode(String.valueOf(user.getId_number()),"UTF-8")
                         +"&"+URLEncoder.encode("names","UTF-8") + "="+URLEncoder.encode(user.getFullnames(),"UTF-8")
                         +"&"+URLEncoder.encode("email","UTF-8") + "="+URLEncoder.encode(user.getEmail(),"UTF-8")+"&"+
                         URLEncoder.encode("mobile","UTF-8") + "="+URLEncoder.encode(String.valueOf(user.getMobile_number()),"UTF-8")
                         +"&"+URLEncoder.encode("age","UTF-8") + "="+URLEncoder.encode(String.valueOf(user.getAge()),"UTF-8")+"&"+
                         URLEncoder.encode("password","UTF-8") + "="+URLEncoder.encode(user.getPassword(),"UTF-8");
                 Log.i(TAG, "doInBackground: encoded data: "+data);
                 bufferedWriter.write(data);
                 bufferedWriter.flush();
                 bufferedWriter.close();
                 outputStream.close();

                 InputStream inputStream = httpURLConnection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                 result += bufferedReader.readLine();
                 Log.i("TRACK", result);
                 Log.i(TAG, "doInBackground: result is: "+result);
             }catch (MalformedURLException e){
                 Log.i(TAG, "doInBackground: malformedURLException :"+e);
                 e.printStackTrace();
             }catch(IOException e){
                 e.printStackTrace();
                 Log.i(TAG, "doInBackground: IOException :"+e);
             }
             return result;
         }
         @Override
         protected void onPostExecute (String result){
             super.onPostExecute(result);
             getResult(result);
             Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
         }
     }
    }



