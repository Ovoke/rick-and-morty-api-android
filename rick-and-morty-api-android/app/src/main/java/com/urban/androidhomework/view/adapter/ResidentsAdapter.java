package com.urban.androidhomework.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urban.androidhomework.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ResidentsAdapter extends RecyclerView.Adapter<ResidentsAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    public ResidentsAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
        }
    }

    public void addList(List<String> mList){
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();
    }

    private String getItem(int position){
        return list.get(position);
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.resident_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String characterName = getItem(position);
        if(characterName == null) return;

        holder.nameTextView.setText(context.getString(R.string.resident_name, characterName));
    }
}
