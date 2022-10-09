package com.hellomet.user.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hellomet.user.Adapter.OrderListAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Order;
import java.util.List;

import com.hellomet.user.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    RecyclerView recy_order;
    ProgressBar progressbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recy_order);
        recy_order = recyclerView;

        progressbar = view.findViewById(R.id.progressbar);
        recyclerView.setHasFixedSize(true);
        recy_order.setLayoutManager(new LinearLayoutManager(getContext()));
        String phone_number = getContext().getSharedPreferences("AUTHENTICATION", 0).getString("USER_PHONE_NUMBER", "null");
        if (phone_number == null || phone_number.equalsIgnoreCase("null")) {
            Log.d(TAG, "onCreateView: Phone Number is not Found.");
        } else {
            Log.d(TAG, "onCreateView: Phone Number: "+ phone_number);
            progressbar.setVisibility(View.VISIBLE);
            ApiClient.getInstance(MAIN_URL).getOrders(phone_number).enqueue(new Callback<List<Order>>() {
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    progressbar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        recy_order.setAdapter(new OrderListAdapter(getActivity(), response.body()));
                    }else{
                        Log.d(TAG, "onResponse: ResponseError: " + response.errorBody().toString());
                    }
                }
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: Error: " + t.getMessage());
                }
            });
        }
        return view;
    }
}
