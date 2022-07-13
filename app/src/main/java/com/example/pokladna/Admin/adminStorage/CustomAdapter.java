package com.example.pokladna.Admin.adminStorage;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Item> items;
    private Activity activity;

    Animation translate_anim;

    //int position;

    CustomAdapter(Activity activity, Context context,List<Item> items)
    {
        this.activity = activity;
        this.context = context;
        this.items = items;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adm_storage_row, parent, false);
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
        holder.profileView.setText(String.valueOf(items.get(position).getProfile()));
        holder.taxView.setText(String.valueOf(items.get(position).getTax()));


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id",String.valueOf(items.get(position).getId()));
                intent.putExtra("name",items.get(position).getName().toLowerCase(Locale.ROOT));
                intent.putExtra("amount",String.valueOf(items.get(position).getAmmount()));
                intent.putExtra("buy",String.valueOf(items.get(position).getBuy()));
                intent.putExtra("sell",String.valueOf(items.get(position).getSell()));
                intent.putExtra("tax",String.valueOf(items.get(position).getTax()));
                intent.putExtra("profile",String.valueOf(items.get(position).getProfile()));
                //context.startActivity(intent);
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemID,itemName,itemAmount,buyPrice, sellPrice, profileView, taxView;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemID = itemView.findViewById(R.id.itemID);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            buyPrice = itemView.findViewById(R.id.Price);
            sellPrice = itemView.findViewById(R.id.sellPrice);
            profileView = itemView.findViewById(R.id.textViewProfile);
            taxView = itemView.findViewById(R.id.tax);
            constraintLayout = itemView.findViewById(R.id.theLayout);
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            constraintLayout.setAnimation(translate_anim);

        }
    }
}
