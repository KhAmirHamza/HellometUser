package com.hellomet.user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import com.hellomet.user.Model.Order;
import com.hellomet.user.R;
import com.hellomet.user.View.ChatActivity;

import java.util.Date;
import java.util.List;

import static com.hellomet.user.Constants.CHAT_USER_PHARMACY;
import static com.hellomet.user.Constants.CHAT_USER_RIDER;
import static com.hellomet.user.Constants.ORDER_ACTIVE;
import static com.hellomet.user.Constants.ORDER_CANCELLED;
import static com.hellomet.user.Constants.ORDER_COMPLETED;
import static com.hellomet.user.Constants.ORDER_ON_THE_WAY;
import static com.hellomet.user.Constants.ORDER_PENDING;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private static final String TAG = "OrderListAdapter";
    Context context;
    int[] detailsVisibility;
    List<Order> orders;

    OnItemClickListener onItemClickListener;

    public OrderListAdapter(Context context2, List<Order> orders2) {
        this.context = context2;
        this.orders = orders2;
        this.detailsVisibility = new int[orders2.size()];
        for (int i = 0; i < orders2.size(); i++) {
            this.detailsVisibility[i] = 0;
        }
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.iv_order_list, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_order_id.setText(orders.get(position).getId());

        String status = orders.get(position).getMetaData().getStatus();
        //Toast.makeText(context, "AdapterPosition: "+position, Toast.LENGTH_SHORT).show();

        if (status.equalsIgnoreCase(ORDER_PENDING)) {
            Log.d(TAG, "onBindViewHolder: Pending: AdapterPosition: "+ position);
            holder.txtv_order_status.setText(status);
            holder.txtv_order_status.setBackground(context.getResources().getDrawable(R.drawable.bg_txtv_pending));
        } else if (status.equalsIgnoreCase(ORDER_ACTIVE)) {
            Log.d(TAG, "onBindViewHolder: Active: AdapterPosition: "+ position);
            holder.txtv_order_status.setText(status);
            holder.txtv_order_status.setBackground(context.getResources().getDrawable(R.drawable.bg_txtv_active));
        }else if (status.equalsIgnoreCase(ORDER_ON_THE_WAY)) {
            Log.d(TAG, "onBindViewHolder: OnTheWay: AdapterPosition: "+ position);
            holder.txtv_order_status.setText(status);
            holder.txtv_order_status.setBackground(context.getResources().getDrawable(R.drawable.bg_txtv_ontheway));
        } else if (status.equalsIgnoreCase(ORDER_COMPLETED)) {
            Log.d(TAG, "onBindViewHolder: Completed: AdapterPosition: "+ position);
            holder.txtv_order_status.setText(status);
            holder.txtv_order_status.setBackground(context.getResources().getDrawable(R.drawable.bg_txtv_completed));
        }else if (status.equalsIgnoreCase(ORDER_CANCELLED)) {
            Log.d(TAG, "onBindViewHolder: Cancelled: AdapterPosition: "+ position);
            holder.txtv_order_status.setText(status);
            holder.txtv_order_status.setBackground(context.getResources().getDrawable(R.drawable.bg_txtv_cancelled));
        }

        holder.txtv_order_price.setText("৳ " + this.orders.get(position).getMetaData().getTotalPrice());
        holder.txtv_order_date.setText(new Date(Long.parseLong(this.orders.get(position).getMetaData().getCreatedAt())).toString());
        holder.txtv_pharmacy_name.setText("Pharmacy: " + this.orders.get(position).getMetaData().getPharmacyName());
        holder.txtv_payment_method.setText("Payment Method: " + this.orders.get(position).getMetaData().getPaymentMethod());
        holder.txtv_requirement.setText("Requirement: " + this.orders.get(position).getMetaData().getRequirement());

        List<Order.PrescriptionImageURL> prescriptionImageUrls = orders.get(position).getPrescriptionImageUrls();



        if (prescriptionImageUrls.size()>0 && prescriptionImageUrls.get(0).getPrescriptionImageUrl()!=null){
            holder.imgv_prescription.setVisibility(View.VISIBLE);
            holder.recy_order_details.setVisibility(View.GONE);
            //Toast.makeText(context, prescriptionImageUrls.get(0), Toast.LENGTH_SHORT).show();
            Picasso.with(context).load(prescriptionImageUrls.get(0).getPrescriptionImageUrl()).placeholder(R.drawable.progress_animation).into(holder.imgv_prescription);

        }else if (orders.get(position).getItems().size()>0){
            holder.recy_order_details.setVisibility(View.VISIBLE);
            holder.imgv_prescription.setVisibility(View.GONE);
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this.context, this.orders.get(position).getItems());
            Log.d(TAG, "MyViewHolder: orderItemSize: " + this.orders.get(position).getItems().size());
            holder.recy_order_details.setAdapter(orderItemAdapter);
        }
        if (this.detailsVisibility[position] == 0) {
            holder.relative_layout_details.setVisibility(View.GONE);
        } else {
            holder.relative_layout_details.setVisibility(View.VISIBLE);
        }
    }

    public int getItemCount() {
        return this.orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recy_order_details;
        RelativeLayout relative_layout_details;
        TextView txtv_order_date;
        TextView txtv_order_id;
        TextView txtv_order_price;
        TextView txtv_order_status;
        TextView txtv_payment_method;
        TextView txtv_pharmacy_name;
        TextView txtv_requirement;
        ImageView imgv_prescription;
        MaterialButton btn_chat_with_rider,btn_chat_with_pharmacy;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtv_order_id = itemView.findViewById(R.id.txtv_order_id);
            txtv_order_status = itemView.findViewById(R.id.txtv_order_status);
            txtv_order_price = (TextView) itemView.findViewById(R.id.txtv_order_price);
            txtv_order_date = (TextView) itemView.findViewById(R.id.txtv_order_date);
            txtv_pharmacy_name = (TextView) itemView.findViewById(R.id.txtv_pharmacy_name);
            txtv_payment_method = (TextView) itemView.findViewById(R.id.txtv_payment_method);
            txtv_requirement = (TextView) itemView.findViewById(R.id.txtv_requirement);
            imgv_prescription = itemView.findViewById(R.id.imgv_prescription);
            btn_chat_with_rider = itemView.findViewById(R.id.btn_chat_with_rider);
            btn_chat_with_pharmacy = itemView.findViewById(R.id.btn_chat_with_pharmacy);


            relative_layout_details =  itemView.findViewById(R.id.relative_layout_details);
            RecyclerView recyclerView = itemView.findViewById(R.id.recy_order_details);
            recy_order_details = recyclerView;
            recyclerView.setHasFixedSize(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (detailsVisibility[getAdapterPosition()] == 0) {
                        detailsVisibility[getAdapterPosition()] = 1;
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        detailsVisibility[getAdapterPosition()] = 0;
                        notifyItemChanged(getAdapterPosition());

                    }

                    /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your Code
                        }
                    }, 1000);*/
                }
            });
/*            txtv_order_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onItemClickListener.onItemClick(v, getAdapterPosition());
                    if (orders.get(getAdapterPosition()).getMeta_data().getStatus().equalsIgnoreCase(ORDER_ACTIVE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setCancelable(false)
                                .setTitle("Please Make Sure...")
                                .setMessage("It means you have received Medicines. Do you want to Complete this Order?")
                                .setPositiveButton("Complete Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //todo...
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                                        String USER_ID = sharedPreferences.getString("USER_ID","null");
                                        String USER_Name = sharedPreferences.getString("USER_Name","null");
                                        String USER_PHONE_NUMBER = sharedPreferences.getString("USER_PHONE_NUMBER","null");

                                        Order order = orders.get(getAdapterPosition());
                                        order.getMeta_data().setStatus("Completed");
                                        order.getMeta_data().setDeliveryman_id(USER_ID);
                                        order.getMeta_data().setDeliveryman_name(USER_Name);
                                        order.getMeta_data().setDeliveryman_phone_number(USER_PHONE_NUMBER);

                                        //Updating Order with some new value.....

                                        ApiClient.getInstance(MAIN_URL).updateOrder(order.getId(), order).enqueue(new Callback<Order>() {
                                            @Override
                                            public void onResponse(Call<Order> call, Response<Order> response) {
                                                if (response.isSuccessful()){
                                                    if (response.body()==null){
                                                        Toast.makeText(context, "Can Not Get Data.", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(context, "Order Complete Successfully", Toast.LENGTH_SHORT).show();
                                                        //orders.get(getAdapterPosition()).getMeta_data().setStatus("Active");
                                                        orders.remove(getAdapterPosition());
                                                        notifyDataSetChanged();
                                                    }
                                                }else {
                                                    Toast.makeText(context, "ResponseError: "+ response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Order> call, Throwable t) {
                                                Toast.makeText(context, "Error: "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //todo...

                                    }
                                });
                        builder.show();
                    }
                }
            });*/

            btn_chat_with_rider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class)
                            .putExtra("user_id", orders.get(getAdapterPosition()).getMetaData().getUserId())
                            .putExtra("order_id", orders.get(getAdapterPosition()).getId())
                            .putExtra("chat_with", CHAT_USER_RIDER);

                    Log.d(TAG, "onCreate: user_id: "+orders.get(getAdapterPosition()).getMetaData().getUserId());
                    Log.d(TAG, "onCreate: order_id: "+orders.get(getAdapterPosition()).getId());
                    Log.d(TAG, "onCreate: chat_with: "+CHAT_USER_PHARMACY);

                    context.startActivity(intent);
                }
            });

            btn_chat_with_pharmacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class)
                            .putExtra("user_id", orders.get(getAdapterPosition()).getMetaData().getUserId())
                            .putExtra("order_id", orders.get(getAdapterPosition()).getId())
                            .putExtra("chat_with", CHAT_USER_PHARMACY);

                    Log.d(TAG, "onCreate: user_id: "+orders.get(getAdapterPosition()).getMetaData().getUserId());
                    Log.d(TAG, "onCreate: order_id: "+orders.get(getAdapterPosition()).getId());
                    Log.d(TAG, "onCreate: chat_with: "+CHAT_USER_PHARMACY);

                    context.startActivity(intent);
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
