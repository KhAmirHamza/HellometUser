package com.hellomet.user.View.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hellomet.user.View.InitialActivity;
import com.hellomet.user.View.WebviewActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.hellomet.user.Adapter.OrderListAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Constants;
import com.hellomet.user.Model.Order;
import com.hellomet.user.Model.Profile;
import com.hellomet.user.R;
import com.hellomet.user.View.SignInActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;
import static com.hellomet.user.Constants.ORDER_COMPLETED;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    MaterialToolbar account_toolbar;
    ImageView imgv_profile_image;
    SharedPreferences sharedPreferences;
    TextView txtv_email;
    TextView txtv_name;
    TextView txtv_phone_number;
    RecyclerView recy_completed_order;
    ProgressBar progressbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = getActivity().getSharedPreferences("AUTHENTICATION", 0);
        imgv_profile_image = view.findViewById(R.id.imgv_profile_image);
        txtv_name = view.findViewById(R.id.txtv_name);
        txtv_email =  view.findViewById(R.id.txtv_email);
        txtv_phone_number =  view.findViewById(R.id.txtv_phone_number);

        progressbar = view.findViewById(R.id.progressbar);

        MaterialToolbar materialToolbar = (MaterialToolbar) view.findViewById(R.id.account_toolbar);
        account_toolbar = materialToolbar;
        materialToolbar.setTitle((CharSequence) "Account");
        account_toolbar.inflateMenu(R.menu.account);

        String userPhoneNumber = sharedPreferences.getString("USER_PHONE_NUMBER", "null");
        if (!userPhoneNumber.equalsIgnoreCase("null")) {
            Log.d(TAG, "onCreateView: userPhoneNumber: " + userPhoneNumber);
        }
        else{
            //startActivity(new Intent(getActivity(), SignInActivity.class));
        }/*else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            //Toast.makeText(getContext(), userPhoneNumber, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreateView: Firebase: userPhoneNumber: " + userPhoneNumber);
        } else if (userPhoneNumber == null){
            Log.d(TAG, "onCreateView: userPhoneNumber is null");
            Toast.makeText(getContext(), "Please Sign In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return view;
        }*/


        if (userPhoneNumber!=null) {
            progressbar.setVisibility(View.VISIBLE);
            ApiClient.getInstance(Constants.MAIN_URL)
                    .getUserProfileUsingPhoneNumber(userPhoneNumber).enqueue(new Callback<Profile>() {
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    progressbar.setVisibility(View.GONE);
                    if (!response.isSuccessful()) {
                        //Toast.makeText(getContext(), "ResponseError:" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: ResponseError: "+response.errorBody().toString());
                    } else{
                        if (response.body() == null) {
                            Log.d(TAG, "onResponse: Nothing Found!");
                            //Toast.makeText(getContext(), "Nothing Found.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d(TAG, "onResponse: Data: "+ response.body().getMetaData().getImage_url());
                        Picasso.with(getContext())
                                .load(response.body().getMetaData().getImage_url())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .placeholder(R.drawable.person)
                                .into(imgv_profile_image);
                        txtv_name.setText(response.body().getMetaData().getName());
                        txtv_email.setText(response.body().getMetaData().getEmail());
                        txtv_phone_number.setText(response.body().getMetaData().getPhone_number());
                    }
                }
                public void onFailure(Call<Profile> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    //Toast.makeText(AccountFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                }
            });
        }
        account_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.sign_out) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut();
                        if (!sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null")) {
                            sharedPreferences.edit().remove("USER_PHONE_NUMBER").apply();
                            sharedPreferences.edit().remove("USER_NAME").apply();
                            sharedPreferences.edit().remove("USER_ID").apply();
                            startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            getActivity().finish();
                        } else {
                            startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            getActivity().finish();
                        }
                    } else if (!sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null")) {
                        sharedPreferences.edit().remove("USER_PHONE_NUMBER").apply();
                        sharedPreferences.edit().remove("USER_NAME").apply();
                        sharedPreferences.edit().remove("USER_ID").apply();
                        startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "You are not registered!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }
                } else if (item.getItemId() == R.id.help) {
                    startActivity(new Intent(getContext(), WebviewActivity.class)
                            .putExtra("url", "https://www.hellomet.com/help-center/"));
                }
                return false;
            }
        });

        recy_completed_order = view.findViewById(R.id.recy_completed_order);
        recy_completed_order.setHasFixedSize(true);
        progressbar.setVisibility(View.VISIBLE);
        if (userPhoneNumber.equalsIgnoreCase("null")){
            Toast.makeText(getContext(), "Please Sign in.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("You are not registered, Please sign in");
            builder.setPositiveButton("SIgn In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            builder.show();

        }else {
            ApiClient.getInstance(MAIN_URL).getOrdersByStatus(userPhoneNumber, ORDER_COMPLETED).enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    progressbar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            OrderListAdapter completedOrderListAdapter = new OrderListAdapter(getActivity(), response.body());
                            recy_completed_order.setAdapter(completedOrderListAdapter);
                        }else {
                            Toast.makeText(getContext(), "Nothing Found!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Response Error: "+ response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Error: "+ response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: Error: "+ t.getLocalizedMessage());
                }
            });
        }
        return view;
    }
}
