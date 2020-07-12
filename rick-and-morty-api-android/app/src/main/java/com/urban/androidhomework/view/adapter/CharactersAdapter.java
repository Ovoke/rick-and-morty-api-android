package com.urban.androidhomework.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.urban.androidhomework.R;
import com.urban.androidhomework.model.CharacterData;

import java.util.ArrayList;
import java.util.List;


public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.ViewHolder> {

    private OnItemClickListener itemClickListener;
    private List<CharacterData> list;
    private Context context;

    public CharactersAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView characterImageView;
        TextView nameTextView, specieTextView, locationTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            characterImageView = itemView.findViewById(R.id.character_image);
            nameTextView = itemView.findViewById(R.id.name);
            specieTextView = itemView.findViewById(R.id.specie);
            locationTextView = itemView.findViewById(R.id.location);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<CharacterData> getCurrentList() {
        return list;
    }

    public void addList(List<CharacterData> mList){
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();
    }

    private CharacterData getItem(int position){
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
                .inflate(R.layout.character_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterData character = getItem(position);
        if(character == null) return;

        String name = character.getName();
        String imageURL = character.getImage();
        String specie = character.getSpecies();
        String locationName = character.getLocation().getName();

        holder.nameTextView.setText(name);
        holder.specieTextView.setText(specie);
        holder.locationTextView.setText(locationName);
        holder.itemView.setOnClickListener(v -> itemClickListener.onClick(position, v));

        //loads an empty bitmap as a placeholder for consistency
        Bitmap bitmap = Bitmap.createBitmap(100, 80, Bitmap.Config.ARGB_8888);
        Canvas canvas  = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#B8B8B8"));
        Glide.with(context).load(imageURL)
                .apply(new RequestOptions()
                        .placeholder(new BitmapDrawable(context.getResources(), bitmap))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter())
                .into(holder.characterImageView);
    }
}
