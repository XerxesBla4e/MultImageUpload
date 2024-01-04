package com.wkdeveloper.multipleimage_uploaded.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wkdeveloper.multipleimage_uploaded.Interface.OnItemClicked;
import com.wkdeveloper.multipleimage_uploaded.Models.ItemModel;
import com.wkdeveloper.multipleimage_uploaded.R;

import java.util.List;
import java.util.Random;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context context;
    public List<ItemModel> itemOrderList;

    OnItemClicked onItemClicked;

    public ItemAdapter(Context context, List<ItemModel> itemOrderList) {
        this.context = context;
        this.itemOrderList = itemOrderList;
    }

    public void setItemList(List<ItemModel> itemOrderList) {
        this.itemOrderList = itemOrderList;
        notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClicked onItemClickedlistener) {
        this.onItemClicked = onItemClickedlistener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel itemModel = itemOrderList.get(position);
        holder.bind(itemModel);
    }

    @Override
    public int getItemCount() {
        return itemOrderList.size();
    }

   public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemNameTextView;
        private TextView itemDescriptionTextView;
        private ImageView itemImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && onItemClicked != null) {
                ItemModel itemModel = itemOrderList.get(pos);
                onItemClicked.goToViewItemDetails(itemModel);
            }
        }

        public void bind(ItemModel item) {
            itemNameTextView.setText(item.getItemName());
            itemDescriptionTextView.setText(item.getItemDesc());

            // Check if there are any image URLs
            List<String> imageUrls = item.getImageUrls();
            if (!imageUrls.isEmpty()) {
                // Load a random image URL into the ImageView using Picasso
                int randomIndex = getRandomNumber(imageUrls.size());
                String randomImageUrl = imageUrls.get(randomIndex);
                Glide.with(itemNameTextView.getContext()).load(randomImageUrl).into(itemImageView);
            }
        }

        private int getRandomNumber(int max) {
            // Generate a random number between 0 (inclusive) and max (exclusive)
            Random random = new Random();
            return random.nextInt(max);
        }


    }

}
