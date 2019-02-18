package com.atharv.postit.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atharv.postit.Model.Channels_Model;
import com.atharv.postit.R;

import java.util.List;

public class Channels_Adapter extends RecyclerView.Adapter<Channels_Adapter.Channels_ViewHolder> {

    public interface OnChannelClickedListener {
        void onChannelClicked(Channels_Model channels_model);
    }

    public interface OnChannelLongClickedListener {
        void onChannelLongClicked(Channels_Model channels_model);
    }

    List<Channels_Model> channels_List;
    OnChannelClickedListener listener;
    OnChannelLongClickedListener longClickedListener;

    public Channels_Adapter(List<Channels_Model> channels_List, OnChannelClickedListener listener, OnChannelLongClickedListener longClickedListener) {
        this.channels_List = channels_List;
        this.listener = listener;
        this.longClickedListener = longClickedListener;
    }

    @NonNull
    @Override
    public Channels_Adapter.Channels_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View channel = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_layout,parent,false);
        return new Channels_ViewHolder(channel);
    }

    @Override
    public void onBindViewHolder(@NonNull Channels_Adapter.Channels_ViewHolder holder, int position) {
        holder.name_TextView.setText(channels_List.get(position).getName());
        holder.subject_TextView.setText(channels_List.get(position).getSubject());
        holder.topic_TextView.setText(channels_List.get(position).getTopic());
        holder.owner_TextView.setText(channels_List.get(position).getOwner());
    }

    @Override
    public int getItemCount() {
        return channels_List.size();
    }

    public class Channels_ViewHolder extends RecyclerView.ViewHolder {

        TextView name_TextView,subject_TextView,topic_TextView,owner_TextView;

        public Channels_ViewHolder(View channelView) {
            super(channelView);
            name_TextView = channelView.findViewById(R.id.name_textView);
            subject_TextView = channelView.findViewById(R.id.subject_textView);
            topic_TextView = channelView.findViewById(R.id.topic_textView);
            owner_TextView = channelView.findViewById(R.id.owner_textView);

            name_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                    }
            });
            subject_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                }
            });
            topic_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                }
            });

            name_TextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickedListener.onChannelLongClicked(channels_List.get(getAdapterPosition()));
                    return true;
                }
            });

            subject_TextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickedListener.onChannelLongClicked(channels_List.get(getAdapterPosition()));
                    return true;
                }
            });
            topic_TextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickedListener.onChannelLongClicked(channels_List.get(getAdapterPosition()));
                    return true;
                }
            });

        }
    }
}
