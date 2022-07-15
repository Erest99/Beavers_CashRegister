package com.example.pokladna.SellSection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Item> items;
    private List<Item> cart = new ArrayList<>();
    private Activity activity;
    private int price = 0;
    Animation translate_anim;

    public List<Item> getCart() {
        return cart;
    }


    CustomAdapter(Activity activity, Context context,List<Item> items)
    {
        this.activity = activity;
        this.context = context;
        this.items = items;
        for (Item i:items)
        {
            Item it = new Item(i.getId(),i.getName(),i.getBuy(),i.getSell(),0,i.getTax(),i.getProfile());
            cart.add(it);
        }


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_sell, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.itemID.setText(String.valueOf(items.get(position).getId()));
        holder.itemName.setText(String.valueOf(items.get(position).getName()));
        holder.itemAmount.setText(String.valueOf(items.get(position).getAmmount()));
        holder.sellPrice.setText(String.valueOf(items.get(position).getSell()));
        holder.piece.setText(String.valueOf(holder.pieces));

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.pieces<items.get(position).getAmmount())
                {
                    holder.pieces++;
                    cart.get(position).setAmmount(holder.pieces);
                    holder.piece.setText(String.valueOf(holder.pieces));

                    price += items.get(position).getSell();
                    Sell.priceTv.setText(String.valueOf(price));
                }

            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.pieces > 0)
                {
                    holder.pieces--;
                    cart.get(position).setAmmount(holder.pieces);
                    holder.piece.setText(String.valueOf(holder.pieces));

                    price -= items.get(position).getSell();
                    Sell.priceTv.setText(String.valueOf(price));
                }

            }
        });




    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemID,itemName,itemAmount, sellPrice, piece;
        ConstraintLayout constraintLayout;
        Button plusButton, minusButton;

        int pieces = 0;

        public MyViewHolder(@NonNull View itemView)
        {

            super(itemView);
            itemID = itemView.findViewById(R.id.itemID);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            sellPrice = itemView.findViewById(R.id.sellPrice);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            piece = itemView.findViewById(R.id.peacesTv);

            constraintLayout = itemView.findViewById(R.id.sellLayout2);
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            //constraintLayout.setAnimation(translate_anim);

        }
    }
}
