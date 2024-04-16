package com.example.sbstest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class io_RecyclerViewAdapter extends RecyclerView.Adapter<io_RecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Order> orderArrayList;


    public io_RecyclerViewAdapter(Context context, ArrayList<Order>orderArrayList, RecyclerViewInterface recyclerViewInterface){
        this.context=context;
        this.orderArrayList=orderArrayList;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public io_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(context);
        View view= inflater. inflate(R.layout.recycler_view_row,parent, false);
        return new io_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull io_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.orderNumber.setText(orderArrayList.get(position).getOrderID());
        holder.orderDetails.setText(orderArrayList.get(position).toString());

    }


    //Get number of incoming orders
    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView orderNumber, orderDetails, bookDetails;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderTextView);
            orderDetails  = itemView.findViewById(R.id.detailsTextView);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
