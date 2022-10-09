package com.hellomet.user.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.hellomet.user.Adapter.CartAdapter;
import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;
import com.hellomet.user.View.MedicineDetailsActivity;
import com.hellomet.user.View.PharmacyActivity;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    MaterialButton btn_order_all;
    RecyclerView recy_cart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btn_order_all = (MaterialButton) view.findViewById(R.id.btn_order_all);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recy_cart);
        recy_cart = recyclerView;
        recyclerView.setHasFixedSize(true);
        recy_cart.setLayoutManager(new LinearLayoutManager(getContext()));
        List<MedicineEntityRoom.Medicine> medicines = new ArrayList<>();
        if (getAllMedicineFromCartInRoomDB() != null) {
            medicines = getAllMedicineFromCartInRoomDB();
        }
        CartAdapter cartAdapter = new CartAdapter(getContext(), medicines);
        recy_cart.setAdapter(cartAdapter);
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            public void onItemClick(View view, MedicineEntityRoom.Medicine medicine, int position) {
                startActivity(new Intent(getContext(), MedicineDetailsActivity.class).putExtra("id", medicine.getMedicineId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }


            public void onItemAddToCartClick(MaterialButton materialButton, MaterialCardView materialCardView, int position) {
               recy_cart.removeViewAt(position);
            }
        });
        btn_order_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PharmacyActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return view;
    }

    private List<MedicineEntityRoom.Medicine> getAllMedicineFromCartInRoomDB() {
        return MedicineDatabase.getInstance(getContext()).medicineDao().getAllMedicine();
    }
}
