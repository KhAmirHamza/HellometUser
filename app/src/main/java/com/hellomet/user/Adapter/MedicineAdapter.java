package com.hellomet.user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import com.hellomet.user.Model.Medicine;
import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<Medicine> copyMedicines;
    List<Medicine> medicines;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onItemAddToCartClick(MaterialButton materialButton, MaterialCardView materialCardView, int i);

        void onItemClick(View view, int i);
    }

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
        this.copyMedicines = new ArrayList(medicines);
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_medicine, parent, false));
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = medicines.get(position).getMeta_data().getName();
        String features = medicines.get(position).getMeta_data().getFeatures();
        String indication = medicines.get(position).getMeta_data().getIndication();
        String price = medicines.get(position).getMeta_data().getPrice();
        if (name==null){
            name = "...";
        }if (features==null){
            features = "...";
        }if (indication==null){
            indication = "...";
        }if (price==null){
            price = "...";
        }

            holder.txtv_medicine_name.setText(name);
            holder.txtv_medicine_features.setText(features);
            holder.txtv_medicine_indication.setText(indication);
            holder.txtv_medicine_price.setText("৳ " + price + " Tk");


        if (medicines.get(position).getMeta_data().getImage_url()!=null){
            Picasso.with(this.context).load(medicines.get(position).getMeta_data().getImage_url()).into(holder.imgv_medicine);
        }

        if (getAllMedicineFromCartInRoomDB() != null) {
            for (int i = 0; i < getAllMedicineFromCartInRoomDB().size(); i++) {
                if (medicines.get(position).getId().equalsIgnoreCase(getAllMedicineFromCartInRoomDB().get(i).getMedicineId())) {
                    holder.btn_medicine_add_to_cart.setText("Remove");
                }
            }
        }
    }

    public int getItemCount() {
        return medicines.size();
    }

    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */
            public Filter.FilterResults performFiltering(CharSequence constraint) {
                List<Medicine> filterMedicineList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filterMedicineList.addAll(copyMedicines);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Medicine medicine : copyMedicines) {
                        if (medicine.getMeta_data().getName().toLowerCase().trim().contains(filterPattern)) {
                            filterMedicineList.add(medicine);
                        }
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = filterMedicineList;
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence constraint, Filter.FilterResults results) {
                medicines.clear();
                medicines.addAll((Collection) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btn_medicine_add_to_cart;
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
            btn_medicine_add_to_cart = (MaterialButton) itemView.findViewById(R.id.btn_medicine_add_or_remove);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            btn_medicine_add_to_cart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onItemClickListener.onItemAddToCartClick(btn_medicine_add_to_cart, cardview_medicine_list, MyViewHolder.this.getAdapterPosition());
                    if (btn_medicine_add_to_cart.getText().toString().equalsIgnoreCase("Add to Cart")) {
                        btn_medicine_add_to_cart.setText("Remove");
                        insertMedicineToCartInRoomDB(medicines, getAdapterPosition());
                        return;
                    }
                    MyViewHolder.this.cardview_medicine_list.setStrokeWidth(0);
                    btn_medicine_add_to_cart.setText("Add to Cart");
                    deleteMedicineFromCartInRoomDB(medicines.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    /* access modifiers changed from: private */
    public void insertMedicineToCartInRoomDB(List<Medicine> medicines2, int position) {
        Medicine.MetaData selectedMedicineMetaData = medicines2.get(position).getMeta_data();
        MedicineDatabase.getInstance(this.context).medicineDao().insertMedicine(new MedicineEntityRoom.Medicine(medicines2.get(position).getId(), new MedicineEntityRoom.MetaData(selectedMedicineMetaData.getName(), selectedMedicineMetaData.getImage_url(), selectedMedicineMetaData.getFeatures(), selectedMedicineMetaData.getBrand(), selectedMedicineMetaData.getIndication(), selectedMedicineMetaData.getPrice(), selectedMedicineMetaData.getDescription())));
    }

    private List<MedicineEntityRoom.Medicine> getAllMedicineFromCartInRoomDB() {
        return MedicineDatabase.getInstance(this.context).medicineDao().getAllMedicine();
    }

    /* access modifiers changed from: private */
    public void deleteMedicineFromCartInRoomDB(String medicineId) {
        MedicineDatabase.getInstance(this.context).medicineDao().deleteMedicine(medicineId);
    }
}
