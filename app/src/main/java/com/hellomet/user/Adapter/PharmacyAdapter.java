package com.hellomet.user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import com.hellomet.user.Model.Pharmacy;
import com.hellomet.user.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<Pharmacy> copyPharmacies;
    OnItemClickListener onItemClickListener;
    List<Pharmacy> pharmacies;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public PharmacyAdapter(Context context2, List<Pharmacy> pharmacies2) {
        this.context = context2;
        this.pharmacies = pharmacies2;
        this.copyPharmacies = new ArrayList(pharmacies2);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.iv_pharmacy, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_pharmacy_name.setText(this.pharmacies.get(position).getMeta_data().getName());
        holder.txtv_pharmacy_distance.setText(this.pharmacies.get(position).getMeta_data().getDistance());
        holder.txtv_pharmacy_address.setText(this.pharmacies.get(position).getMeta_data().getAddress());
        Picasso.with(this.context).load(this.pharmacies.get(position).getMeta_data().getImage_url()).into(holder.imgv_pharmacy_image);
        //Toast.makeText(this.context, this.pharmacies.get(position).getMeta_data().getImage_url(), Toast.LENGTH_SHORT).show();
    }

    public int getItemCount() {
        return this.pharmacies.size();
    }

    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */
            public Filter.FilterResults performFiltering(CharSequence constraint) {
                List<Pharmacy> filterPharmacyList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filterPharmacyList.addAll(PharmacyAdapter.this.copyPharmacies);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Pharmacy pharmacy : PharmacyAdapter.this.copyPharmacies) {
                        if (pharmacy.getMeta_data().getName().toLowerCase().trim().contains(filterPattern)) {
                            filterPharmacyList.add(pharmacy);
                        }
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = filterPharmacyList;
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence constraint, Filter.FilterResults results) {
                pharmacies.clear();
                pharmacies.addAll((Collection) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgv_pharmacy_image;
        TextView txtv_pharmacy_address;
        TextView txtv_pharmacy_distance;
        TextView txtv_pharmacy_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtv_pharmacy_name = (TextView) itemView.findViewById(R.id.txtv_pharmacy_name);
            this.txtv_pharmacy_distance = (TextView) itemView.findViewById(R.id.txtv_pharmacy_distance);
            this.imgv_pharmacy_image = (ImageView) itemView.findViewById(R.id.imgv_pharmacy_image);
            txtv_pharmacy_address = itemView.findViewById(R.id.txtv_pharmacy_address);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PharmacyAdapter.this.onItemClickListener.onItemClick(v, MyViewHolder.this.getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }
}
