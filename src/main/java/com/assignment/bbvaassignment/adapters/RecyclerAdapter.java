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
        View view = inflater.inflate(R.layout.emg_contacts_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlacesDTO current = data.get(position);
        holder.title.setText(current.getName());

        if(current.getFormatted_address() != null && !current.getFormatted_address().isEmpty()) {
            holder.subTitle.setText(current.getFormatted_address());
            holder.subTitle.setVisibility(View.VISIBLE);
        } else {
            holder.subTitle.setText("");
            holder.subTitle.setVisibility(View.GONE);
        }

        if(current.getPlaceRating() != null && !current.getPlaceRating().isEmpty()) {
            holder.rating.setText(current.getPlaceRating());
            holder.rating.setVisibility(View.VISIBLE);
        } else {
            holder.rating.setText("");
            holder.rating.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, rating, subTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            rating = (TextView) itemView.findViewById(R.id.rating);
            subTitle=(TextView)itemView.findViewById(R.id.subTitle);
        }
    }
}