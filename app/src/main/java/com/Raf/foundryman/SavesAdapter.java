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

public class SavesAdapter extends RecyclerView.Adapter<SavesAdapter.SavesViewHolder> {

    Context context;
    ArrayList<Integer> ammount = new ArrayList<>();
    ArrayList<String> projectName, materialType, materialName;
    ArrayList<Double> totalWeight;


    public SavesAdapter(Context cont, ArrayList<String> name, ArrayList<String> matType,  ArrayList<String> matName,
                        ArrayList<Double> totWeight){
        context = cont;
        projectName = name;
        materialType = matType;
        materialName = matName;
        totalWeight = totWeight;

    }

    @NonNull
    @Override
    public SavesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.saves_row, parent, false);
        return new SavesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavesViewHolder holder,final int position) {

        holder.text1.setText(String.valueOf(projectName.get(position)));
        holder.text2.setText(String.valueOf(materialType.get(position)));
        holder.text3.setText(String.valueOf(materialName.get(position)));
        holder.text4.setText(String.valueOf(totalWeight.get(position)));


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int index = Support.recLinePosition(position);
//                Support.removeFeederLine(index);
//                notifyItemRemoved(index);
//                FeedersActivity.setTotalFeederMass();
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectName.size();
    }

    public class SavesViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2, text3, text4;
        Button remove;
        public SavesViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.save_name_text);
            text2 = itemView.findViewById(R.id.save_mat_type_text);
            text3 = itemView.findViewById(R.id.save_mat_name_text);
            text4 = itemView.findViewById(R.id.save_weight_text);
            remove = itemView.findViewById(R.id.save_remove_btn);
        }
    }
}
