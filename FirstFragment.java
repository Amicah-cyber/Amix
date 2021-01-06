package com.example.trainbook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

public class FirstFragment extends Fragment {

    private View view;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Button next;
    private String trainType;
    private String coach;
    private static final String TAG = "FirstFragment";
    private static final String ERROR = "failed to connect";


    private CallbackInterface callbackInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        spinner1 = view.findViewById(R.id.spinner);
        spinner2 = view.findViewById(R.id.spinner2);
        spinner3 = view.findViewById(R.id.spinner3);
        spinner4 = view.findViewById(R.id.spinner4);
        next = view.findViewById(R.id.Next);
        setAdapters();
        return view;
    }
    private void setAdapters(){
        String[] trains = new String[]{"Madaraka","Jamhuri"};
        String[] places = new String[]{"Mombasa","Nairobi","Mariakani"};
        String[] coaches = new String[]{"first_class", "second_class"};

        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(getContext(),R.layout.layout_spinner, R.id.tv_spinner,trains);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(getContext(), R.layout.layout_spinner, R.id.tv_spinner, places);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(getContext(), R.layout.layout_spinner, R.id.tv_spinner, coaches);

        spinner1.setAdapter(arrayAdapter1);
        spinner2.setAdapter(arrayAdapter2);
        spinner3.setAdapter(arrayAdapter2);
        spinner4.setAdapter(arrayAdapter3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: method clicked");
                trainType = spinner1.getSelectedItem().toString();
                String origin = spinner2.getSelectedItem().toString();
                String destination = spinner3.getSelectedItem().toString();
                coach = spinner4.getSelectedItem().toString();
                Background background = new Background();
                background.execute(coach, origin, destination);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    public void setCallbackInterface(CallbackInterface callbackInterface) {
        this.callbackInterface = callbackInterface;
    }
    private void goNext(String s){
        Log.i(TAG, "goNext: result " +s);
        if (s.equals(ERROR)){

        }else{
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                JSONObject object = jsonArray.getJSONObject(0);
                Ticket ticket = new Ticket();
                ticket.setType(trainType);
                ticket.setOrigin(object.getString("origin"));
                ticket.setDestination(object.getString("destination"));
                ticket.setCoach(coach);
                if (callbackInterface != null){
                    callbackInterface.firsttoSecond(ticket, object.getInt("price"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class Background extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String coach = strings[0];
            String origin = strings[1];
            String destination = strings[2];
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2/projects/get_price.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String data = URLEncoder.encode("class", "UTF-8")+"="+URLEncoder.encode(coach,"UTF-8")
                        +"&"+URLEncoder.encode("origin", "UTF-8")+"="+URLEncoder.encode(origin, "UTF-8")
                        +"&"+URLEncoder.encode("destination", "UTF-8")+"="+URLEncoder.encode(destination,"UTF-8");
                Log.i(TAG, "doInBackground: encoded data :" +data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                result += line;
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();



            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            goNext(s);
            super.onPostExecute(s);
        }
    }


}
