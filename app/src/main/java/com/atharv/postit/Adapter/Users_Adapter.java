package com.atharv.postit.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atharv.postit.Model.Users_Model;
import com.atharv.postit.R;

import java.util.List;

public class Users_Adapter extends RecyclerView.Adapter<Users_Adapter.Users_ViewHolder> {

    public interface OnUserClickedListener {
        void onUserClicked(Users_Model users_model);
    }

    public interface OnUserLongClickedListener {
        void onUserLongClicked(Users_Model users_model);
    }

    List<Users_Model> user_List;
    OnUserClickedListener listener;
    OnUserLongClickedListener longClickedListener;

    public Users_Adapter(List<Users_Model> user_List, OnUserClickedListener listener, OnUserLongClickedListener longClickedListener) {
        this.user_List = user_List;
        this.listener = listener;
        this.longClickedListener = longClickedListener;
    }

    @NonNull
    @Override
    public Users_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View user = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_layout,parent,false);
        return new Users_ViewHolder(user);
    }

    @Override
    public void onBindViewHolder(@NonNull Users_ViewHolder holder, int position) {
        holder.userName.setText(user_List.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return user_List.size();
    }


    public class Users_ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        //ImageView userImage;

        public Users_ViewHolder(View userView) {
            super(userView);

            userName = userView.findViewById(R.id.username_textView);
            //userImage = userView.findViewById(R.id.user_imageView);

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClicked(user_List.get(getAdapterPosition()));
                }
            });
           /* userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClicked(user_List.get(getAdapterPosition()));
                }
            }); */

            userName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickedListener.onUserLongClicked(user_List.get(getAdapterPosition()));
                    return false;
                }
            });

           /* userImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickedListener.onUserLongClicked(user_List.get(getAdapterPosition()));
                    return false;
                }
            }); */
        }
    }
}
