package com.Raf.foundryman;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SavesAdapter extends RecyclerView.Adapter<SavesAdapter.SavesViewHolder> {

    Context context;
    ArrayList<String> projectName, materialType, materialName;


    public SavesAdapter(Context cont, ArrayList<String> name, ArrayList<String> matType,  ArrayList<String> matName){
        context = cont;
        projectName = name;
        materialType = matType;
        materialName = matName;

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


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Support.saveLinePosition(position);
                MemAccess.delete(context, projectName.get(index));
                MemAccess.removeFromProjectList(context, projectName.get(index));
                Support.removeSaveLine(index);
                notifyItemRemoved(index);
            }
        });

        holder.load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemAccess.importProject(context, projectName.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectName.size();
    }

    public class SavesViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2, text3;
        ImageButton load, remove;
        public SavesViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.save_name_text);
            text2 = itemView.findViewById(R.id.save_mat_type_text);
            text3 = itemView.findViewById(R.id.save_mat_name_text);
            load = itemView.findViewById(R.id.save_load_btn);
            remove = itemView.findViewById(R.id.save_remove_btn);
        }
    }
}
