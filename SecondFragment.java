package com.example.trainbook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondFragment extends Fragment {
    DatePicker Date2;
    Spinner spinner;
    EditText editText;
    Button button;
    View view;
    Ticket ticket;
    int price;
    TextView textView;
    CallbackInterface callbackInterface;
    private static final String TAG = "SecondFragment";
    private String DDate;
    Date date;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        Date2 = view.findViewById(R.id.Date2);
        spinner = view.findViewById(R.id.Countries);
        editText = view.findViewById(R.id.editText4);
        button = view.findViewById(R.id.books);
        textView = view.findViewById(R.id.textView3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        price = bundle.getInt(CallbackInterface.PRICE);
        ticket = new Ticket();
        ticket.setCoach(bundle.getString(CallbackInterface.COACH));
        ticket.setCustomerId(bundle.getInt(CallbackInterface.ID));
        ticket.setOrigin(bundle.getString(CallbackInterface.ORIGIN));
        ticket.setDestination(bundle.getString(CallbackInterface.DESTINATION));
        ticket.setType(bundle.getString(CallbackInterface.TRAIN_TYPE));
        textView.setText("price: "+String.valueOf(price));


        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    date = simpleDateFormat.parse(year+"-"+monthOfYear+"-"+dayOfMonth);
                    Log.i(TAG, "onDateChanged: selected date "+ date);
                    DDate =simpleDateFormat.format(date);
                    Log.i(TAG, "onDateChanged: asString"+DDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Date2.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),dateChangedListener);

        String[] countries = new String[]{"Kenya", "Tanzania","Uganda"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.layout_spinner, R.id.tv_spinner, countries);
        spinner.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = spinner.getSelectedItem().toString();
                ticket.setMobile(Integer.parseInt(editText.getText().toString()));
                ticket.setDate(date);
                ticket.setNationality(country);
                Log.i(TAG, "onClick: ticket"+ticket.toString());
                Background background = new Background();
                background.execute(ticket);
            }
        });

    }

    public void setCallbackInterface(CallbackInterface callbackInterface) {
        this.callbackInterface = callbackInterface;
    }
    private void getResult(String s){
        if (s.equals("success")){
            Log.i(TAG, "getResult: "+s);
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            if (callbackInterface != null){
                Bundle bundle = new Bundle();
                bundle.putInt(CallbackInterface.ID, ticket.getCustomerId());
                callbackInterface.viewTicket(bundle);
            }
        }else {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        }


    }



    private class Background extends AsyncTask<Ticket, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Ticket... tickets) {
            String result ="";
            try {
                Ticket ticket = tickets[0];
                URL url = new URL("http://10.0.2.2/projects/book.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String data = URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(ticket.getType(),"UTF-8")+"&"+
                        URLEncoder.encode("coach", "UTF-8")+"="+URLEncoder.encode(ticket.getCoach(),"UTF-8")+"&"+
                        URLEncoder.encode("origin", "UTF-8")+"="+URLEncoder.encode(ticket.getOrigin(),"UTF-8")+"&"+
                        URLEncoder.encode("destination", "UTF-8")+"="+URLEncoder.encode(ticket.getDestination(),"UTF-8")+"&"+
                        URLEncoder.encode("customer", "UTF-8")+"="+URLEncoder.encode(String.valueOf(ticket.getCustomerId()),"UTF-8")+"&"+
                        URLEncoder.encode("nationality", "UTF-8")+"="+URLEncoder.encode(ticket.getNationality(),"UTF-8")+"&"+
                        URLEncoder.encode("date", "UTF-8")+"="+URLEncoder.encode(DDate,"UTF-8")+"&"+
                        URLEncoder.encode("mobile", "UTF-8")+"="+URLEncoder.encode(String.valueOf(ticket.getMobile()),"UTF-8");
                Log.i(TAG, "doInBackground: encoded data > "+data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.i(TAG, "doInBackground: after write");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = bufferedReader.readLine();
                result += line;
                Log.i(TAG, "doInBackground: result "+result);
                httpURLConnection.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            getResult(s);
            super.onPostExecute(s);
        }
    }
}
