package com.hellomet.user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hellomet.user.Model.Order;
import com.hellomet.user.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {
    Context context;
    List<Order.Item> items;

    public OrderItemAdapter(Context context2, List<Order.Item> items2) {
        this.context = context2;
        this.items = items2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.iv_order_items, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_medicine_name.setText(this.items.get(position).getName() + " || " + this.items.get(position).getBrand());
        holder.txtv_medicine_features.setText(this.items.get(position).getFeatures());
        holder.txtv_medicine_sub_total.setText(this.items.get(position).getPrice() + " Tk || " + this.items.get(position).getQuantity() + " Pcs || SubTotal: " + this.items.get(position).getSubTotal() + " Tk");
    }

    public int getItemCount() {
        return this.items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtv_medicine_features;
        TextView txtv_medicine_name;
        TextView txtv_medicine_sub_total;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtv_medicine_name = (TextView) itemView.findViewById(R.id.txtv_medicine_name);
            this.txtv_medicine_features = (TextView) itemView.findViewById(R.id.txtv_medicine_features);
            this.txtv_medicine_sub_total = (TextView) itemView.findViewById(R.id.txtv_medicine_sub_total);
        }
    }
}
