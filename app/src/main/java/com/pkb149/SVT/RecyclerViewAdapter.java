package com.pkb149.SVT;

/**
 * Created by CoderGuru on 19-08-2017.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {

    Context context;
    List<CardViewData> cardViewDatas= new ArrayList<>();
    ListItemClickListener listener;
    public interface ListItemClickListener {
         void onListItemClick(int clickedItemIndex);
    }

    public RecyclerViewAdapter(List<CardViewData> cardViewDatas, Context context,ListItemClickListener listener) {
        this.cardViewDatas = cardViewDatas;
        this.context = context;
        this.listener=listener;

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        NewsViewHolder holder = new NewsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.availableFrom.append(cardViewDatas.get(position).getAvailableFrom());
        holder.lorryOwnerId.setText(cardViewDatas.get(position).getOnwerId());
        holder.numberPlate.setText(cardViewDatas.get(position).getNumberPlate());

        holder.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                listener.onListItemClick(position);
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
    public void insert(int position, CardViewData data) {
        //list.add(position, data);
        //cardViewData.notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(CardViewData data) {
        //int position = list.indexOf(data);
        //list.remove(position);
        // notifyItemRemoved(position);
    }
    public void clear() {
        cardViewDatas.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<CardViewData>  data) {
        cardViewDatas.clear();
        cardViewDatas.addAll(data);
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView numberPlate;
        Button bookNow;
        TextView lorryOwnerId;
        TextView availableFrom;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            numberPlate=(TextView) itemView.findViewById(R.id.number_plate);
            lorryOwnerId=(TextView)itemView.findViewById(R.id.owner_id);
            availableFrom=(TextView)itemView.findViewById(R.id.available_from);
            bookNow=(Button) itemView.findViewById(R.id.book_now);
            //itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            /*int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
            Uri uri = Uri.parse(cardViewDatas.get(clickedPosition).getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);*/
        }
    }

}