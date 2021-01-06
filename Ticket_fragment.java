package com.example.trainbook;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.content.Intent;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class Ticket_fragment extends Fragment {

    private View view;
    //private RecyclerView recyclerView;
    private ListView listView;
    private MyListAdapter listAdapter;
    private List<Ticket> tickets = new ArrayList<>();
    private static final String TAG = "Ticket_fragment";
    private CallbackInterface anInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ticket_frag, container, false);
        listView = view.findViewById(R.id.ticket_list);
        //recyclerView = view.findViewById(R.id.ticket_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        int id = bundle.getInt(CallbackInterface.ID);

        listAdapter = new MyListAdapter(getContext(), R.layout.ticket_rowlayout);
        listView.setAdapter(listAdapter);
        Background background = new Background();

        background.execute(id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ticket ticket = tickets.get(position);
                Log.i(TAG, "onItemClick: ticket clicked"+ticket.toString());
                if (anInterface != null){
                    anInterface.ticketDetails(ticket);
                }
            }
        });


    }

    public void setAnInterface(CallbackInterface anInterface) {
        this.anInterface = anInterface;
    }

    private void getTicket(String s){
        Log.i(TAG, "getTicket: result "+s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("ticket");
            for (int i=0; i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Ticket ticket = new Ticket(object.getInt("id"),object.getString("type"),object.getString("coach"),
                        object.getString("origin"),object.getString("destination"),object.getInt("customer"),
                        simpleDateFormat.parse(object.getString("date")),object.getString("nation"),object.getInt("mobile"));
                listAdapter.add(ticket);
                tickets.add(ticket);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private class Background extends AsyncTask<Integer, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String result = "";
            try {
                int id = integers[0];
                URL url = new URL("http://10.0.2.2/projects/getTickets.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(String.valueOf(id));
                Log.i(TAG, "doInBackground: encoded data"+ data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line=bufferedReader.readLine()) != null){
                    stringBuilder.append(line+"\n");
                    Log.i(TAG, "doInBackground: "+line);
                }
                result = stringBuilder.toString().trim();
                Log.i(TAG, "doInBackground: result "+result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            getTicket(s);
            super.onPostExecute(s);
        }
    }







    private class MyListAdapter extends ArrayAdapter {
        List<Ticket> ticketList = new ArrayList<>();

        public MyListAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void add(Object object) {
            super.add(object);
            ticketList.add((Ticket)object);
        }

        @Override
        public int getPosition(Object item) {
            return ticketList.indexOf(item);
        }

        @Override
        public Object getItem(int position) {
            return ticketList.get(position);
        }
        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            View view0;
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view0 = layoutInflater.inflate(R.layout.ticket_rowlayout, parent, false);
            Holder holder = new Holder();
            holder.idTextView = view0.findViewById(R.id.ticket_id);
            holder.originTextView = view0.findViewById(R.id.ticket_origin);
            holder.destinationTextView = view0.findViewById(R.id.ticket_destination);

            holder.idTextView.setText(String.valueOf(ticketList.get(position).getId()));
            holder.originTextView.setText(ticketList.get(position).getOrigin());
            holder.destinationTextView.setText(ticketList.get(position).getDestination());





            return view0;
        }

        private class Holder {
            private TextView idTextView;
            private TextView originTextView;
            private TextView destinationTextView;

        }

    }









/*
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VHolder> {
        Context context;
        List<Ticket> ticketList = new ArrayList<>();
        VHolder vHolder;

        public MyAdapter(Context context, List<Ticket> ticketList) {
            this.context = context;
            this.ticketList = ticketList;
        }

        @NonNull
        @Override
        public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_rowlayout, parent, false);
            vHolder = new VHolder(view1);

            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull VHolder holder, int position) {
            vHolder.idTextView.setText(ticketList.get(position).getId());
            vHolder.originTextView.setText(ticketList.get(position).getOrigin());
            vHolder.destinationTextView.setText(ticketList.get(position).getDestination());
            vHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        private class VHolder extends RecyclerView.ViewHolder{
            TextView idTextView;
            TextView originTextView;
            TextView destinationTextView;
            RelativeLayout relativeLayout;


            public VHolder(@NonNull View itemView) {
                super(itemView);
                idTextView = itemView.findViewById(R.id.ticket_id);
                originTextView = itemView.findViewById(R.id.ticket_origin);
                destinationTextView = itemView.findViewById(R.id.ticket_destination);
                relativeLayout = itemView.findViewById(R.id.ticket_layout);


            }
        }
    }*/
}
