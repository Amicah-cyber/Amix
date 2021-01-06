package com.example.trainbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TicketView extends Fragment {

    private View view;
    private TextView idTV;
    private TextView coachTV;
    private TextView customerTV;
    private TextView originTV;
    private TextView destTV;
    private TextView dateTV;
    private TextView numberTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ticket_view, container, false);
        idTV = view.findViewById(R.id.TextId);
        coachTV = view.findViewById(R.id.CoachId);
        customerTV = view.findViewById(R.id.CustomId);
        originTV = view.findViewById(R.id.OriginId);
        destTV = view.findViewById(R.id.DestId);
        dateTV = view.findViewById(R.id.DepartId);
        numberTV = view.findViewById(R.id.MobileId);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        idTV.setText(String.valueOf(bundle.getInt(CallbackInterface.ID)));
        coachTV.setText(bundle.getString(CallbackInterface.COACH));
        customerTV.setText(String.valueOf(bundle.getInt(CallbackInterface.CUSTOMER)));
        originTV.setText(bundle.getString(CallbackInterface.ORIGIN));
        destTV.setText(bundle.getString(CallbackInterface.DESTINATION));
        dateTV.setText(bundle.getString(CallbackInterface.DDATE));
        numberTV.setText(String.valueOf(bundle.getInt(CallbackInterface.MOBILE_NUMBER)));




        super.onActivityCreated(savedInstanceState);
    }
}
