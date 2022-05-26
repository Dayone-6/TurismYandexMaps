package com.example.turismyandexmaps.ui.tickets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismyandexmaps.R;

public class TicketActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_tickets);

        ((ListView)findViewById(R.id.tickets_view)).setAdapter(new TicketsAdapter(
                getApplicationContext(), R.layout.ticket, TicketsSearchFragment.tickets));

        super.onCreate(savedInstanceState);
    }

}
