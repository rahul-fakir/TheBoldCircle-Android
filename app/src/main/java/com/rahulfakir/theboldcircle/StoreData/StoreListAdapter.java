package com.rahulfakir.theboldcircle.StoreData;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rahulfakir.theboldcircle.R;

import java.util.List;


public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.MyViewHolder> {

    private List<StoreObject> storeList;
    private static Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStoreName, tvStoreID, tvStorePhone, tvStoreEmail, tvStoreAddress,
                tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday, tvPublicHoliday;
        public Button btnStoreInteraction;

        public MyViewHolder(View view) {
            super(view);
            tvStoreID = (TextView) view.findViewById(R.id.tvStoreID);
            tvStoreName = (TextView) view.findViewById(R.id.tvStoreName);
            tvStorePhone = (TextView) view.findViewById(R.id.tvStorePhone);
            tvStoreEmail = (TextView) view.findViewById(R.id.tvStoreEmail);
            tvStoreAddress = (TextView) view.findViewById(R.id.tvStoreAddress);
            tvMonday = (TextView) view.findViewById(R.id.tvHoursMonday);
            tvTuesday = (TextView) view.findViewById(R.id.tvHoursTuesday);
            tvWednesday = (TextView) view.findViewById(R.id.tvHoursWednesday);
            tvThursday = (TextView) view.findViewById(R.id.tvHoursThursday);
            tvFriday = (TextView) view.findViewById(R.id.tvHoursFriday);
            tvSaturday = (TextView) view.findViewById(R.id.tvHoursSaturday);
            tvSunday = (TextView) view.findViewById(R.id.tvHoursSunday);
            tvPublicHoliday = (TextView) view.findViewById(R.id.tvHoursPublicHoliday);
            btnStoreInteraction = (Button) view.findViewById(R.id.btnStoreInteraction);
        }
    }


    public StoreListAdapter(List<StoreObject> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StoreObject storeObjects = storeList.get(position);
        holder.tvStoreName.setText(storeObjects.getName());
        holder.tvStoreID.setText(storeObjects.getID());
        holder.tvStorePhone.setText(storeObjects.getPhoneNumber());
        holder.tvStoreEmail.setText(storeObjects.getEmail());
        holder.tvStoreAddress.setText(storeObjects.getAddress());


        String[] hours = new String[8];
        TextView[] views = {holder.tvMonday, holder.tvTuesday, holder.tvWednesday, holder.tvThursday, holder.tvFriday, holder.tvSaturday, holder.tvSunday, holder.tvPublicHoliday};        for (int i = 0; i < hours.length; i++){
            hours[i] = storeObjects.getStringHours(i, 0) + " - " + storeObjects.getStringHours(i, 1);
            if (hours[i].equals("-1:00 - -1:00")) {
                hours[0] = "closed";
            }
            views[i].setText(hours[0]);
        }

                holder.btnStoreInteraction.setText("CHAT WITH STORE");
                holder.btnStoreInteraction.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                       Intent intent = new Intent(context, StoreChatActivity.class);
                        intent.putExtra("storeID", storeObjects.getID());
                        ((Activity) context).startActivity(intent);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }



}