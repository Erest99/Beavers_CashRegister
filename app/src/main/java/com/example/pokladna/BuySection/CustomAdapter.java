package com.example.pokladna.BuySection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Item> items;

    Animation translate_anim;
    //int position;

    CustomAdapter(Context context,List<Item> items)
    {
        this.context = context;
        this.items = items;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_buy, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //this.position = position;
        holder.itemID.setText(String.valueOf(items.get(position).getId()));
        holder.itemName.setText(String.valueOf(items.get(position).getName()));
        holder.itemAmount.setText(String.valueOf(items.get(position).getAmmount()));
        holder.buyPrice.setText(String.valueOf(items.get(position).getBuy()));
        holder.sellPrice.setText(String.valueOf(items.get(position).getSell()));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyMoreActivity.class);
                intent.putExtra("id",String.valueOf(items.get(position).getId()));
                intent.putExtra("name",items.get(position).getName());
                intent.putExtra("amount",String.valueOf(items.get(position).getAmmount()));
                intent.putExtra("buy",String.valueOf(items.get(position).getBuy()));
                intent.putExtra("sell",String.valueOf(items.get(position).getSell()));
                intent.putExtra("tax",String.valueOf(items.get(position).getTax()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemID,itemName,itemAmount,buyPrice, sellPrice;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemID = itemView.findViewById(R.id.itemID);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            buyPrice = itemView.findViewById(R.id.Price);
            sellPrice = itemView.findViewById(R.id.sellPrice);
            constraintLayout = itemView.findViewById(R.id.storageAdminLayout);
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            constraintLayout.setAnimation(translate_anim);
        }
    }
}
