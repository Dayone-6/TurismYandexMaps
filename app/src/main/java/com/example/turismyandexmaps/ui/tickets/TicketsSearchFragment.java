package com.example.turismyandexmaps.ui.tickets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.turismyandexmaps.databinding.FragmentSearchTicketsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketsSearchFragment extends Fragment {
    private FragmentSearchTicketsBinding binding;

    private RetrofitService service;
    private Call<Answer> call;
    protected static Ticket[] tickets;
    private final String token = "682633046022068f80e2f1a5085c8784";

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchTicketsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.button.setOnClickListener(v -> {
            if(!binding.from.getText().toString().equals("") && !binding.to.getText().toString().equals("")) {
                if (binding.from.getText().toString().length() <= 3 && binding.to.getText().toString().length() <= 3) {
                    if(binding.limit.getText().toString().equals("")){
                        binding.limit.setText("15");
                    }
                    if(binding.fromDate.getText().toString().equals("") && binding.toDate.getText().toString().equals("")){
                        call = service.getTicketsWithoutTime(binding.from.getText().toString(),
                                binding.to.getText().toString(),
                                binding.direct.isChecked(),
                                Integer.parseInt(binding.limit.getText().toString()),
                                token);
                    } else if(binding.fromDate.getText().toString().equals("")) {
                        call = service.getTicketsWithoutFromTime(binding.from.getText().toString(),
                                binding.to.getText().toString(),
                                binding.toDate.getText().toString(),
                                binding.direct.isChecked(),
                                Integer.parseInt(binding.limit.getText().toString()), token);
                    }else if(binding.toDate.getText().toString().equals("")){
                        call = service.getTicketsWithoutToTime(binding.from.getText().toString(),
                                binding.to.getText().toString(),
                                binding.fromDate.getText().toString(),
                                binding.direct.isChecked(),
                                Integer.parseInt(binding.limit.getText().toString()), token);
                    }else{
                        call = service.getTickets(binding.from.getText().toString(),
                                binding.to.getText().toString(),
                                binding.fromDate.getText().toString(),
                                binding.toDate.getText().toString(),
                                binding.direct.isChecked(),
                                Integer.parseInt(binding.limit.getText().toString()), token);
                    }
                    call.enqueue(new Callback<Answer>() {
                        @Override
                        public void onResponse(@NonNull Call<Answer> call, @NonNull Response<Answer> response) {
                            if (response.body() != null) {
                                tickets = response.body().data.toArray(new Ticket[0]);
                                Intent i = new Intent(requireContext(), TicketActivity.class);

                                startActivity(i);
                            }else{
                                Toast.makeText(requireContext(), "Ответ пустой!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Answer> call, @NonNull Throwable t) {
                            Toast.makeText(requireContext(), "Ошибка в получении билетов!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    IATACodeAlert();
                }
            }else{
                IATACodeAlert();
            }
        });
        return root;
    }

    private void IATACodeAlert(){
        Toast.makeText(requireContext(), "Певые 2 поля далжны быть заполнены " +
                "в формате IATA-кода", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.travelpayouts.com/aviasales/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitService.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
