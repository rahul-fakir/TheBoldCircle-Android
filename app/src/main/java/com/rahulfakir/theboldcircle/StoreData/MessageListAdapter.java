package com.rahulfakir.theboldcircle.StoreData;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rahulfakir.theboldcircle.R;
import com.rahulfakir.theboldcircle.StoreData.MessageObject;

import java.util.List;

/**
 * Created by rahul.fakir on 2016/05/28.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {

    private List<MessageObject> messageList;
    private ViewGroup parent;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserMessage, tvUserDateStamp, tvStoreMessage, tvStoreDateStamp;
        public RelativeLayout lytStoreMessageHolder, lytUserMessageHolder;

        public MyViewHolder(View view) {
            super(view);
            tvUserMessage = (TextView) view.findViewById(R.id.tvUserMessage);
            tvUserDateStamp = (TextView) view.findViewById(R.id.tvUserDateStamp);
            tvStoreMessage = (TextView) view.findViewById(R.id.tvStoreMessage);
            tvStoreDateStamp = (TextView) view.findViewById(R.id.tvStoreDateStamp);
            lytStoreMessageHolder = (RelativeLayout) view.findViewById(R.id.lytStoreMessage);
            lytUserMessageHolder = (RelativeLayout) view.findViewById(R.id.lytUserMessage);
        }
    }


    public MessageListAdapter(List<MessageObject> messageList) {
        this.messageList = messageList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageObject message = messageList.get(position);
        if (message.getMessageType() == 0) {
            holder.tvUserMessage.setText(message.getMessage());
            holder.tvUserDateStamp.setText(message.getDateStamp());
            holder.lytStoreMessageHolder.setVisibility(View.GONE);

        } else {
            holder.tvStoreMessage.setText(message.getMessage());
            holder.tvStoreDateStamp.setText(message.getDateStamp());
            holder.lytUserMessageHolder.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}