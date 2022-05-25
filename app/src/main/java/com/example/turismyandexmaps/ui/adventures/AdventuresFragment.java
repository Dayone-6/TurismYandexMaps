package com.example.turismyandexmaps.ui.adventures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.turismyandexmaps.MainActivity;
import com.example.turismyandexmaps.R;
import com.example.turismyandexmaps.databinding.FragmentAdventuresBinding;

import java.util.ArrayList;

public class AdventuresFragment extends Fragment {

    private FragmentAdventuresBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdventuresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.adventuresList;
        binding.addAdventure.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddAdventureActivity.class);
            startActivity(i);
        });
        try {
            ArrayList<Adventure> all_adventures = new ArrayList<>();
            all_adventures.addAll(MainActivity.adventures);
            all_adventures.addAll(MainActivity.new_adventures);

            AdventuresListAdapter adapter = new AdventuresListAdapter(requireContext(),
                    R.layout.adventure, all_adventures.toArray(new Adventure[0]));

            listView.setAdapter(adapter);
        }catch (NullPointerException e){

            AdventuresListAdapter adapter = new AdventuresListAdapter(requireContext(),
                    R.layout.adventure, new Adventure[0]);

            listView.setAdapter(adapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class Adventure{
        public Bitmap picture;
        public String title, description, picture_path;

        public Adventure(Bitmap picture, String title, String description, String picture_path){
            this.description = description;
            this.title = title;
            this.picture = picture;
            this.picture_path = picture_path;
        }
    }

    static class AdventuresListAdapter extends ArrayAdapter<Adventure> {

        public AdventuresListAdapter(@NonNull Context context, int resource, Adventure[] data) {
            super(context, resource, data);
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Adventure adventure = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adventure, null);
            }
            ((TextView)convertView.findViewById(R.id.desc)).setText(adventure.description);
            ((TextView)convertView.findViewById(R.id.from_to)).setText(adventure.title);
            ((ImageView)convertView.findViewById(R.id.picture)).setImageBitmap(adventure.picture);
            return convertView;
        }
    }
}