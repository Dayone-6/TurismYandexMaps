package com.example.turismyandexmaps.ui.tickets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.turismyandexmaps.R;

public class TicketsAdapter extends ArrayAdapter<Ticket> {

    public TicketsAdapter(@NonNull Context context, int resource, Ticket[] data) {
        super(context, resource, data);
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ticket ticket = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ticket, null);
        }
        ((TextView)convertView.findViewById(R.id.ticket_cities)).setText(ticket.origin + " -> " + ticket.destination);
        ((TextView)convertView.findViewById(R.id.ticket_airports)).setText(ticket.origin_airport + " -> " + ticket.destination_airport);
        ((TextView)convertView.findViewById(R.id.ticket_date_from)).setText("Прилет:\n" + ticket.departure_at);
        ((TextView)convertView.findViewById(R.id.ticket_date_to)).setText("Возвращение:\n" + ticket.return_at);
        ((TextView)convertView.findViewById(R.id.ticket_airline)).setText("Авиакомпания(IATA-код):\n" + ticket.airline);
        ((TextView)convertView.findViewById(R.id.ticket_flight_number)).setText("Номер рейса:\n" + ticket.flight_number);
        ((TextView)convertView.findViewById(R.id.ticket_transfers_from)).setText("Пересадки туда: " + ticket.transfers);
        ((TextView)convertView.findViewById(R.id.ticket_transfers_to)).setText("Пересадки обратно: " + ticket.return_transfers);
        ((TextView)convertView.findViewById(R.id.ticket_flight_duration)).setText("Длительность полета(часы):\n" + ticket.duration / 60);
        ((TextView)convertView.findViewById(R.id.ticket_price)).setText("Цена билета: " + ticket.price + " рублей.");
        return convertView;
    }
}
