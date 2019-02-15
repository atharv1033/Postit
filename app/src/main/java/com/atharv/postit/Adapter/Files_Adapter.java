package com.atharv.postit.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atharv.postit.Model.File_Model;
import com.atharv.postit.R;
import java.util.List;

public class Files_Adapter extends RecyclerView.Adapter<Files_Adapter.Files_ViewHolder> {


    public interface OnFileClickedListner {
        void onFileClicked(File_Model file_model);
    }

    List<File_Model> files_list;
    OnFileClickedListner listner;

    public Files_Adapter(List<File_Model> files_list, OnFileClickedListner listner) {
        this.files_list = files_list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Files_Adapter.Files_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View file = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_list_layout,parent,false);
        return new Files_ViewHolder(file);
    }

    @Override
    public void onBindViewHolder(@NonNull Files_Adapter.Files_ViewHolder holder, int position) {
        holder.fileName.setText(files_list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return files_list.size();
    }

    public class Files_ViewHolder extends RecyclerView.ViewHolder {

        TextView fileName;

        public Files_ViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName_textView);

            fileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onFileClicked(files_list.get(getAdapterPosition()));
                }
            });
        }
    }
}
