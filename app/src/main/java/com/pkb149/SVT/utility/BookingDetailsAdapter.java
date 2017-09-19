package com.pkb149.SVT.utility;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pkb149.SVT.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderGuru on 17-09-2017.
 */

public class BookingDetailsAdapter extends RecyclerView.Adapter<BookingDetailsAdapter.BookingDetailsViewHolder> {

    Context context;
    List<BookingDetailsCardViewData> cardViewDatas= new ArrayList<>();
    BookingDetailsAdapter.ListItemClickListener listener;
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, int code);
    }

    public BookingDetailsAdapter(List<BookingDetailsCardViewData> cardViewDatas, Context context, BookingDetailsAdapter.ListItemClickListener listener) {
        this.cardViewDatas = cardViewDatas;
        this.context = context;
        this.listener=listener;

    }

    @Override
    public BookingDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_details_card_view, parent, false);
        BookingDetailsViewHolder holder = new BookingDetailsViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(final BookingDetailsViewHolder holder, final int position) {


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.bookingId.setText(cardViewDatas.get(position).getBookingId());
        holder.merchantName.setText(cardViewDatas.get(position).getMerchant());
        holder.fromToLocation.setText(cardViewDatas.get(position).getFromLocation()+" -- "+cardViewDatas.get(position).getToLocation());
        holder.fromToTime.setText(cardViewDatas.get(position).getFromTime()+" -- "+cardViewDatas.get(position).getToTime());

        holder.callMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClick(position,1);
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
    public void insert(int position, BookingDetailsCardViewData data) {
        //list.add(position, data);
        //cardViewData.notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookingDetailsCardViewData data) {
        //int position = list.indexOf(data);
        //list.remove(position);
        // notifyItemRemoved(position);
    }
    public void clear() {
        cardViewDatas.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<BookingDetailsCardViewData>  data) {
        cardViewDatas.clear();
        cardViewDatas.addAll(data);
        notifyDataSetChanged();
    }

    class BookingDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView bookingId;
        TextView merchantName;
        TextView fromToLocation;
        TextView fromToTime;
        Button callMerchant;

        public BookingDetailsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.booking_details_cv);
            bookingId=(TextView) itemView.findViewById(R.id.booking_id_tv);
            merchantName=(TextView)itemView.findViewById(R.id.merchant_name_tv);
            fromToLocation=(TextView)itemView.findViewById(R.id.from_to_location);
            fromToTime=(TextView)itemView.findViewById(R.id.from_to_time);
            callMerchant=(Button) itemView.findViewById(R.id.call_merchant_button);
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