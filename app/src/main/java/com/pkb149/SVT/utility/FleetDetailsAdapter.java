package com.pkb149.SVT.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pkb149.SVT.BookingHistory;
import com.pkb149.SVT.LorryOwnerFlow.SingleLorryDetail;
import com.pkb149.SVT.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderGuru on 17-09-2017.
 */


public class FleetDetailsAdapter extends RecyclerView.Adapter<FleetDetailsAdapter.FleetViewHolder> {

    Context context;
    List<FleetDetailsCardViewData> cardViewDatas= new ArrayList<>();
    FleetDetailsAdapter.ListItemClickListener listener;
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, int code);
    }

    public FleetDetailsAdapter(List<FleetDetailsCardViewData> cardViewDatas, Context context, FleetDetailsAdapter.ListItemClickListener listener) {
        this.cardViewDatas = cardViewDatas;
        this.context = context;
        this.listener=listener;

    }

    @Override
    public FleetDetailsAdapter.FleetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fleet_details_card_view, parent, false);
        FleetViewHolder holder = new FleetViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(final FleetViewHolder holder, final int position) {


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.driverName.append(cardViewDatas.get(position).getDriverName());
        holder.driverMobNumber.setText(cardViewDatas.get(position).getDriverMobileNumber());
        holder.numberPlate.setText(cardViewDatas.get(position).getNumberPlate());

        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClick(position,1);
            }
        });
        holder.callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClick(position,2);
            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return cardViewDatas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, FleetDetailsCardViewData data) {
        //list.add(position, data);
        //cardViewData.notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(FleetDetailsCardViewData data) {
        //int position = list.indexOf(data);
        //list.remove(position);
        // notifyItemRemoved(position);
    }
    public void clear() {
        cardViewDatas.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<FleetDetailsCardViewData>  data) {
        cardViewDatas.clear();
        cardViewDatas.addAll(data);
        notifyDataSetChanged();
    }

    class FleetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView numberPlate;
        TextView driverName;
        TextView driverMobNumber;
        Button callDriver;
        Button moreDetails;

        public FleetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.fleet_details_CV);
            numberPlate=(TextView) itemView.findViewById(R.id.number_plate_tv);
            driverName=(TextView)itemView.findViewById(R.id.driver_name_tv);
            driverMobNumber=(TextView)itemView.findViewById(R.id.driver_mobile_number_tv);
            callDriver=(Button) itemView.findViewById(R.id.call_driver);
            moreDetails=(Button) itemView.findViewById(R.id.more_details);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            /*int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
            Intent intent = new Intent(context, SingleLorryDetail.class);
            context.startActivity(intent);*/
        }
    }

}