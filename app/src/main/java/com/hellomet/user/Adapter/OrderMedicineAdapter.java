package com.hellomet.user.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineEntityRoom;
import java.util.List;

public class OrderMedicineAdapter extends RecyclerView.Adapter<OrderMedicineAdapter.MyVieHolder> {
    private static final String TAG = "OrderMedicineAdapter";
    Context context;
    int[] medicineSelectedCount;
    List<MedicineEntityRoom.Medicine> medicines;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int[] iArr, int i);
    }

    public OrderMedicineAdapter(Context context2, List<MedicineEntityRoom.Medicine> medicines2) {
        this.context = context2;
        this.medicines = medicines2;
        this.medicineSelectedCount = new int[medicines2.size()];
        for (int i = 0; i < medicines2.size(); i++) {
            this.medicineSelectedCount[i] = 1;
        }
    }

    public MyVieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyVieHolder(LayoutInflater.from(this.context).inflate(R.layout.iv_selected_medicine, parent, false));
    }

    public void onBindViewHolder(MyVieHolder holder, int position) {
        holder.txtv_medicine_name.setText(this.medicines.get(position).getMetaData().getName());
        holder.txtv_medicine_features.setText(this.medicines.get(position).getMetaData().getFeatures());
        holder.txtv_medicine_price.setText("৳ " + this.medicines.get(position).getMetaData().getPrice());
    }

    public int getItemCount() {
        return this.medicines.size();
    }

    public class MyVieHolder extends RecyclerView.ViewHolder {
        ImageView imgv_medicine_add;
        ImageView imgv_medicine_remove;
        TextView txtv_medicine_features;
        TextView txtv_medicine_name;
        TextView txtv_medicine_price;
        TextView txtv_selected_medicine_count;

        public MyVieHolder(View itemView) {
            super(itemView);
            this.txtv_medicine_name = (TextView) itemView.findViewById(R.id.txtv_medicine_name);
            this.txtv_medicine_features = (TextView) itemView.findViewById(R.id.txtv_medicine_features);
            this.txtv_medicine_price = (TextView) itemView.findViewById(R.id.txtv_medicine_price);
            this.txtv_selected_medicine_count = (TextView) itemView.findViewById(R.id.txtv_selected_medicine_count);
            this.imgv_medicine_remove = (ImageView) itemView.findViewById(R.id.imgv_medicine_remove);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imgv_medicine_add);
            this.imgv_medicine_add = imageView;
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()] = OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()] + 1;
                    Log.d(OrderMedicineAdapter.TAG, "onClick: " + OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()]);
                    MyVieHolder.this.txtv_selected_medicine_count.setText("" + OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()]);
                    OrderMedicineAdapter.this.onItemClickListener.onItemClick(v, OrderMedicineAdapter.this.medicineSelectedCount, MyVieHolder.this.getAdapterPosition());
                    OrderMedicineAdapter.this.notifyDataSetChanged();
                }
            });
            this.imgv_medicine_remove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(OrderMedicineAdapter.TAG, "onClick: " + OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()]);
                    if (OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()] > 0) {
                        OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()] = OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()] - 1;
                        MyVieHolder.this.txtv_selected_medicine_count.setText("" + OrderMedicineAdapter.this.medicineSelectedCount[MyVieHolder.this.getAdapterPosition()]);
                        OrderMedicineAdapter.this.onItemClickListener.onItemClick(v, OrderMedicineAdapter.this.medicineSelectedCount, MyVieHolder.this.getAdapterPosition());
                        OrderMedicineAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }
}
