package com.example.pokladna.Debts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Debt> debts;
    private Activity activity;

    Animation translate_anim;

    //int position;

    CustomAdapter(Activity activity, Context context,List<Debt> debts )
    {
        this.activity = activity;
        this.context = context;
        this.debts = debts;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_debts, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //this.position = position;
        holder.itemID.setText(String.valueOf(debts.get(position).getId()));
        holder.itemName.setText(String.valueOf(debts.get(position).getName()));
        holder.itemAmount.setText(String.valueOf(debts.get(position).getAmmount()));
        holder.buyPrice.setText(String.valueOf(debts.get(position).getPrice()));
        holder.debtor.setText(String.valueOf(debts.get(position).getDebtor()));

    }

    @Override
    public int getItemCount() {
        return debts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemID,itemName,itemAmount,buyPrice, debtor;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemID = itemView.findViewById(R.id.itemID);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            buyPrice = itemView.findViewById(R.id.Price);
            debtor = itemView.findViewById(R.id.itemDebtor);
            constraintLayout = itemView.findViewById(R.id.layout);
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            constraintLayout.setAnimation(translate_anim);

        }
    }
}
