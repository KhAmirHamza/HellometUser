package com.hellomet.user.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.hellomet.user.Adapter.MedicineAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Medicine;
import com.hellomet.user.R;
import com.hellomet.user.View.MedicineDetailsActivity;
import java.util.List;

import com.hellomet.user.View.PrescriptionActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class MedicineFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    EditText edt_medicine_search;
    MedicineAdapter medicineAdapter;
    String pharmacyId;
    String pharmacyName;
    RecyclerView recy_medicine;
    MaterialToolbar toolbar_medicine;
    MaterialButton material_button_Upload_prescription;

    ProgressBar progressbar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // int forcrashvalue = Integer.parseInt("fds");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        MaterialToolbar materialToolbar = (MaterialToolbar) view.findViewById(R.id.toolbar_medicine);
        toolbar_medicine = materialToolbar;
        materialToolbar.setTitle((CharSequence) "Medicine");
        toolbar_medicine.inflateMenu(R.menu.medicine);

        progressbar = view.findViewById(R.id.progressbar);
        edt_medicine_search = (EditText) view.findViewById(R.id.edt_medicine_search);
        material_button_Upload_prescription = view.findViewById(R.id.material_button_Upload_prescription);
        RecyclerView recyclerView = view.findViewById(R.id.recy_medicine);
        recy_medicine = recyclerView;
        recyclerView.setHasFixedSize(true);
        recy_medicine.setLayoutManager(new LinearLayoutManager(getContext()));

        progressbar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(MAIN_URL).getMedicines().enqueue(new Callback<List<Medicine>>() {
            public void onResponse(Call<List<Medicine>> call, final Response<List<Medicine>> response) {
                progressbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    //Toast.makeText(MedicineFragment.this.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    if (response.body().size()<1){
                        return;
                    }
                    medicineAdapter = new MedicineAdapter(getContext(), response.body());
                    recy_medicine.setAdapter(medicineAdapter);
                    medicineAdapter.setOnItemClickListener(new MedicineAdapter.OnItemClickListener() {
                        public void onItemClick(View view, int position) {
                            startActivity(new Intent(MedicineFragment.this.getContext(), MedicineDetailsActivity.class).putExtra("id", ((Medicine) ((List) response.body()).get(position)).getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        public void onItemAddToCartClick(MaterialButton materialButton, MaterialCardView materialCardView, int position) {
                        }
                    });
                   edt_medicine_search.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            edt_medicine_search.setCursorVisible(true);
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
                            medicineAdapter.getFilter().filter(s);
                        }

                        public void afterTextChanged(Editable s) {
                            edt_medicine_search.setCursorVisible(false);
                        }
                    });
                }else {
                    Log.d(TAG, "onResponse: Response Error: " + response.errorBody().toString());
                }
            }

            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: Error: " + t.getMessage());
            }
        });
        this.toolbar_medicine.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), "Search CLicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        material_button_Upload_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PrescriptionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return view;
    }
}
