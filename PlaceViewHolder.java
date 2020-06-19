package com.example.ebihartourism.ViewHolder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebihartourism.Interfaces.itemClickListener;
import com.example.ebihartourism.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static TextView textPlaceName;
    public static TextView textPlaceSpecification;
    public static TextView txtPlaceSpeciality;
    public ImageView imageView;
    public itemClickListener listener;

    public PlaceViewHolder(View itemView){
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.place_image_show);
        textPlaceName = (TextView) itemView.findViewById(R.id.place_name_textView_homepage);
        textPlaceSpecification = (TextView) itemView.findViewById(R.id.place_specification_textView_homepage);
        txtPlaceSpeciality = (TextView) itemView.findViewById(R.id.place_speciality_textView_homepage);

    }

    public void setItemClickListener(itemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);

    }
}
