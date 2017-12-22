package com.assignment.bbvaassignment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assignment.bbvaassignment.R;
import com.assignment.bbvaassignment.models.PlacesDTO;

import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    List<PlacesDTO> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapter(Context context, List<PlacesDTO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.places_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlacesDTO current = data.get(position);
        holder.placeName.setText("Name : "+current.getName());

        if(current.getFormatted_address() != null && !current.getFormatted_address().isEmpty()) {
            holder.placeAddress.setText(current.getFormatted_address());
            holder.placeAddress.setVisibility(View.VISIBLE);
        } else {
            holder.placeAddress.setText("");
            holder.placeAddress.setVisibility(View.GONE);
        }

        if(current.getPlaceRating() != null && !current.getPlaceRating().isEmpty()) {
            holder.placeRating.setText(current.getPlaceRating());
            holder.placeRating.setVisibility(View.VISIBLE);
        } else {
            holder.placeRating.setText("");
            holder.placeRating.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeRating, placeAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.place_name);
            placeRating = (TextView) itemView.findViewById(R.id.place_rating);
            placeAddress=(TextView)itemView.findViewById(R.id.place_address);
        }
    }
}