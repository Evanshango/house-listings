package com.evans.multiimageupload.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evans.multiimageupload.R;
import com.evans.multiimageupload.models.House;
import com.evans.multiimageupload.models.ImageUrl;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseHolder> {

    private List<House> mHouses;
    private HouseItemClick mHouseItemClick;

    public HouseAdapter(List<House> houses, HouseItemClick houseItemClick) {
        mHouses = houses;
        mHouseItemClick = houseItemClick;
    }

    @NonNull
    @Override
    public HouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.house_item, parent, false);
        return new HouseHolder(view, mHouseItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseHolder holder, int position) {
        House house = mHouses.get(position);
        holder.bind(house);
    }

    @Override
    public int getItemCount() {
        return mHouses.size();
    }

    class HouseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView houseCard;
        ImageView image;
        TextView description;
        HouseItemClick mHouseItemClick;

        HouseHolder(@NonNull View itemView, HouseItemClick houseItemClick) {
            super(itemView);
            houseCard = itemView.findViewById(R.id.houseCard);
            mHouseItemClick = houseItemClick;
            image = itemView.findViewById(R.id.houseImg);
            description = itemView.findViewById(R.id.houseDescription);
            houseCard.setOnClickListener(this);
        }

        private void bind(House house) {
            List<ImageUrl> imageUrlList = house.getImageUrls();
            String imgUrl = imageUrlList.get(0).getImgUrl();
            description.setText(house.getDesc());
            Glide.with(itemView.getContext()).load(imgUrl).into(image);
        }

        @Override
        public void onClick(View v) {
            mHouseItemClick.itemClick(mHouses.get(getAdapterPosition()));
        }
    }

    public interface HouseItemClick{

        void itemClick(House house);
    }
}
