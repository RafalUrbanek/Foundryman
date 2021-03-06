package com.Raf.foundryman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FeedersAdapter extends RecyclerView.Adapter<FeedersAdapter.FeedersViewHolder> {

    Context context;
    ArrayList<Integer> ammount;
    ArrayList<String> sleeve;
    ArrayList<Double> diameter, height, modulus, mass;


    public FeedersAdapter(Context cont, ArrayList<Integer> noOf, ArrayList<String> slv, ArrayList<Double> dia,
                          ArrayList<Double> h, ArrayList<Double> mod, ArrayList<Double> m){
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
    public void onBindViewHolder(@NonNull FeedersViewHolder holder,final int position) {

        holder.text1.setText(String.valueOf(ammount.get(position)));
        holder.text2.setText(String.valueOf(sleeve.get(position)));
        holder.text3.setText(String.valueOf(diameter.get(position)));
        holder.text4.setText(String.valueOf(height.get(position)));
        holder.text5.setText(String.valueOf(modulus.get(position)));
        holder.text6.setText(String.valueOf(mass.get(position)));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Support.recLinePosition(position);
                Support.removeFeederLine(index);
                notifyItemRemoved(index);
                FeedersActivity.setTotalFeederMass();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ammount.size();
    }

    public class FeedersViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2, text3, text4, text5, text6;
        Button remove;
        public FeedersViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.lineAmmount);
            text2 = itemView.findViewById(R.id.save_name_text);
            text3 = itemView.findViewById(R.id.save_mat_type_text);
            text4 = itemView.findViewById(R.id.lineHeight);
            text5 = itemView.findViewById(R.id.save_mat_name_text);
            text6 = itemView.findViewById(R.id.save_weight_text);
            remove = itemView.findViewById(R.id.save_remove_btn);
        }
    }
}
