package com.example.trainbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Home extends AppCompatActivity
        implements CallbackInterface {

    user currentUser;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private Ticket_fragment ticketFragment;
    private Schedule_fragment scheduleFragment;
    private Help_fragment helpFragment;
    private ContactFragment contactFragment;

    private TicketView ticketView;
    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,  R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView =  findViewById(R.id.nav_view);
       // navigationView.inflateMenu(R.menu.activity_home_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {// Handle navigation view item clicks here.
                Log.i(TAG, "onNavigationItemSelected: called");
                itemSelected(item);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
       // navigationView.setNavigationItemSelectedListener(this);
        init();
        ticketFragment = new Ticket_fragment();
        helpFragment = new Help_fragment();
        scheduleFragment = new Schedule_fragment();
        contactFragment = new ContactFragment();
        ticketView = new TicketView();
        ticketFragment.setAnInterface(this);
    }

    private void init(){
        Bundle bundle = getIntent().getExtras();
        currentUser = new user(bundle.getInt(CallbackInterface.ID),
                bundle.getString(CallbackInterface.NAME),
                bundle.getString(CallbackInterface.EMAIL),
                bundle.getInt(CallbackInterface.MOBILE_NUMBER),
                bundle.getInt(CallbackInterface.AGE),
                bundle.getString(CallbackInterface.PASSWORD));
        firstFragment = new FirstFragment();
        firstFragment.setCallbackInterface(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_frame, firstFragment);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        itemSelected(item);
        return super.onOptionsItemSelected(item);
    }
    private void itemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.Booking:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Booking");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, firstFragment);
                fragmentTransaction.commit();
                break;
            }

            case R.id.Ticket:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Ticket");
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt(CallbackInterface.ID, currentUser.getId_number());
                ticketFragment.setAnInterface(this);
                ticketFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.home_frame, ticketFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.Schedule:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Schedule");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, scheduleFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.Help:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Help");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, helpFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_contact:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Contact");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, contactFragment);
                fragmentTransaction.commit();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



  /* @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.i(TAG, "onNavigationItemSelected: called");
        switch(item.getItemId()){

        case R.id.Booking:{
            Log.i(TAG, "onNavigationItemSelected: menuItem click Booking");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_frame, firstFragment);
            fragmentTransaction.commit();
            break;
        }

            case R.id.Ticket:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Ticket");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, ticketFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.Schedule:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Schedule");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, scheduleFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.Help:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Help");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, helpFragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_contact:{
                Log.i(TAG, "onNavigationItemSelected: menuItem click Contact");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_frame, contactFragment);
                fragmentTransaction.commit();
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }*/

    @Override
    public void firsttoSecond(Ticket ticket, int price) {
        Ticket ticket1 = ticket;
        ticket1.setCustomerId(currentUser.getId_number());
        Bundle bundle = new Bundle();
        bundle.putString(CallbackInterface.TRAIN_TYPE, ticket1.getType());
        bundle.putString(CallbackInterface.ORIGIN, ticket1.getOrigin());
        bundle.putString(CallbackInterface.DESTINATION, ticket1.getDestination());
        bundle.putString(CallbackInterface.COACH, ticket1.getCoach());
        bundle.putInt(CallbackInterface.ID, currentUser.getId_number());
        bundle.putInt(CallbackInterface.PRICE, price);
        secondFragment = new SecondFragment();
        secondFragment.setArguments(bundle);
        secondFragment.setCallbackInterface(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, secondFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void signUpSuccess() {
        //super.onBackPressed();
    }

    @Override
    public void ticketDetails(Ticket ticket) {

        Bundle bundle = new Bundle();
        bundle.putInt(CallbackInterface.ID, ticket.getId());
        bundle.putInt(CallbackInterface.CUSTOMER, ticket.getCustomerId());
        bundle.putString(CallbackInterface.ORIGIN, ticket.getOrigin());
        bundle.putString(CallbackInterface.DESTINATION, ticket.getDestination());
        bundle.putString(CallbackInterface.COACH, ticket.getCoach());
        bundle.putInt(CallbackInterface.MOBILE_NUMBER, ticket.getMobile());
        bundle.putString(CallbackInterface.DDATE, String.valueOf(ticket.getDate()));
        ticketView.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, ticketView);
        fragmentTransaction.addToBackStack("VIEW TICKET");
        fragmentTransaction.commit();

    }

    @Override
    public void viewTicket(Bundle bundle) {
        fragmentTransaction = fragmentManager.beginTransaction();
        ticketFragment.setAnInterface(this);
        ticketFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.home_frame, ticketFragment);
        fragmentTransaction.commit();
    }
}
