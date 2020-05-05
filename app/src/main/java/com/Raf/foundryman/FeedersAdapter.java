package com.Raf.foundryman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedersAdapter extends RecyclerView.Adapter<FeedersAdapter.FeedersViewHolder> {

    Context context;
    int[] ammount;
    String[] sleeve;
    double[] diameter, height, modulus, mass;

    public FeedersAdapter(Context cont, int[] noOf, String[] slv, double[] dia,
                          double[] h, double[] mod, double[] m){
        context = cont;
        ammount = noOf;
        sleeve = slv;
        diameter = dia;
        height = h;
        modulus = mod;
        mass = m;

    }
    @NonNull
    @Override
    public FeedersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new FeedersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedersViewHolder holder, int position) {
        holder.text1.setText(String.valueOf(ammount[position]));
        holder.text2.setText(sleeve[position]);
        holder.text3.setText(String.valueOf(diameter[position]));
        holder.text4.setText(String.valueOf(height[position]));
        holder.text5.setText(String.valueOf(modulus[position]));
        holder.text6.setText(String.valueOf(mass[position]));
    }

    @Override
    public int getItemCount() {
        return ammount.length;
    }

    public class FeedersViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2, text3, text4, text5, text6;
        public FeedersViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.lineAmmount);
            text2 = itemView.findViewById(R.id.lineSleeve);
            text3 = itemView.findViewById(R.id.lineDia);
            text4 = itemView.findViewById(R.id.lineHeight);
            text5 = itemView.findViewById(R.id.lineMod);
            text6 = itemView.findViewById(R.id.lineMass);
        }
    }
}
