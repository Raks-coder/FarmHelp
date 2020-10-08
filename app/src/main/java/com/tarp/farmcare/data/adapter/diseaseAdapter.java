package com.tarp.farmcare.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.tarp.farmcare.R;
import com.tarp.farmcare.data.model.Diseases;

import java.util.ArrayList;

public abstract class diseaseAdapter extends FirebaseRecyclerAdapter<Diseases, diseaseAdapter.diseaseViewholder> {

//    FirebaseRecyclerOptions<Diseases> disease;
    public ArrayList<Integer> id = new ArrayList<>();
    public ArrayList<String> diseaseName = new ArrayList<>();
    public ArrayList<String> diseaseDescription = new ArrayList<>();
    public ArrayList<String> diseaseDescriptionPn = new ArrayList<>();
    public ArrayList<String> diseaseDescriptionTn = new ArrayList<>();

    public diseaseAdapter(@NonNull FirebaseRecyclerOptions<Diseases> options, DiseaseItemClickListener itemClickListener) {
        super(options);
        this.mClickListener = itemClickListener;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull diseaseViewholder holder, int position, @NonNull Diseases diseases ) {

        holder.name.setText(diseases.getName());
        holder.type.setText(diseases.getType());
        id.add(diseases.getId());
        diseaseName.add(diseases.getName());
        diseaseDescription.add(diseases.getDescription_en());
        diseaseDescriptionPn.add(diseases.getDescription_pn());
        diseaseDescriptionTn.add(diseases.getDescription_tn());

    }

    private DiseaseItemClickListener mClickListener;
    ProgressBar loadingProgressBar;

    public interface DiseaseItemClickListener {
        void onDiseaseItemClick(int position, ArrayList<Integer> id, ArrayList<String> diseaseName, ArrayList<String> diseaseDescription, ArrayList<String> diseaseDescriptionPn, ArrayList<String> diseaseDescriptionTn);
    }



    @NonNull
    @Override
    public diseaseViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_item, parent, false);
        return new diseaseAdapter.diseaseViewholder(view);
    }
    
    class diseaseViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, type;
        
        public diseaseViewholder(@NonNull View itemView)
        {
            super(itemView);

            name= itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onDiseaseItemClick(getAdapterPosition(), id, diseaseName, diseaseDescription, diseaseDescriptionPn, diseaseDescriptionTn);
        }
    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        onLoaded();
    }

    public abstract void onLoaded();

}
