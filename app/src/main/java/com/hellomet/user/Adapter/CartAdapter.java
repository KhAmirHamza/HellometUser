package com.hellomet.user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<MedicineEntityRoom.Medicine> medicines;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemAddToCartClick(MaterialButton materialButton, MaterialCardView materialCardView, int i);

        void onItemClick(View v, MedicineEntityRoom.Medicine medicine, int adapterPosition);
    }

    public CartAdapter(Context context2, List<MedicineEntityRoom.Medicine> medicines2) {
        this.context = context2;
        this.medicines = medicines2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.iv_medicine, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_medicine_name.setText(this.medicines.get(position).getMetaData().getName());
        holder.txtv_medicine_features.setText(this.medicines.get(position).getMetaData().getFeatures());
        holder.txtv_medicine_indication.setText(this.medicines.get(position).getMetaData().getIndication());
        holder.txtv_medicine_price.setText("৳ " + this.medicines.get(position).getMetaData().getPrice() + " Tk");
        Picasso.with(this.context).load(this.medicines.get(position).getMetaData().getImage_url()).into(holder.imgv_medicine);
        holder.btn_medicine_remove_from_cart.setText("Remove");
    }

    public int getItemCount() {
        return this.medicines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btn_medicine_remove_from_cart;
        MaterialCardView cardview_medicine_list;
        ImageView imgv_medicine;
        TextView txtv_medicine_features;
        TextView txtv_medicine_indication;
        TextView txtv_medicine_name;
        TextView txtv_medicine_price;

        public MyViewHolder(View itemView) {
            super(itemView);
           txtv_medicine_name = (TextView) itemView.findViewById(R.id.txtv_medicine_name);
           txtv_medicine_features = (TextView) itemView.findViewById(R.id.txtv_medicine_features);
           txtv_medicine_indication = (TextView) itemView.findViewById(R.id.txtv_medicine_indication);
           txtv_medicine_price = (TextView) itemView.findViewById(R.id.txtv_medicine_price);
           cardview_medicine_list = (MaterialCardView) itemView.findViewById(R.id.cardview_medicine_list);
           imgv_medicine = (ImageView) itemView.findViewById(R.id.imgv_medicine);
           btn_medicine_remove_from_cart = (MaterialButton) itemView.findViewById(R.id.btn_medicine_add_or_remove);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CartAdapter.this.onItemClickListener
                            .onItemClick(
                                    v,
                                    CartAdapter.this.medicines.get(MyViewHolder.this.getAdapterPosition()),
                                    MyViewHolder.this.getAdapterPosition());
                }
            });

            this.btn_medicine_remove_from_cart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onItemClickListener.onItemAddToCartClick(MyViewHolder.this.btn_medicine_remove_from_cart, MyViewHolder.this.cardview_medicine_list, MyViewHolder.this.getAdapterPosition());
                    deleteMedicineFromCartInRoomDB(medicines.get(MyViewHolder.this.getAdapterPosition()).medicineId);
                    //Toast.makeText(context, medicines.get(getAdapterPosition()).medicineId, Toast.LENGTH_SHORT).show();
                    medicines.remove(MyViewHolder.this.getAdapterPosition());
                    notifyItemRemoved(MyViewHolder.this.getAdapterPosition());
                    notifyItemRangeChanged(MyViewHolder.this.getAdapterPosition(), CartAdapter.this.medicines.size());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    /* access modifiers changed from: private */
    public void deleteMedicineFromCartInRoomDB(String medicineId) {
        MedicineDatabase.getInstance(context).medicineDao().deleteMedicine(medicineId);
    }
}
