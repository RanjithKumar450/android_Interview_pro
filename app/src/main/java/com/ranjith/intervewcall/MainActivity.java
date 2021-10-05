package com.ranjith.intervewcall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ranjith.intervewcall.ApiPackage.ApiClient;
import com.ranjith.intervewcall.adaptor.UserAdaptor;
import com.ranjith.intervewcall.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this));



        getUsers();
    }

    private void getUsers(){
        if (!ApiClient.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        Call<List<UserModel>> call = ApiClient.getUserService().getAllUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.e("succ", ""+response.code());
                        UserAdaptor adaptor = new UserAdaptor(response.body(),MainActivity.this);
                        recycler_view.setAdapter(adaptor);
                    }
                } else {
                    Log.e("failed", ""+response.code());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.e("throw", t.getMessage());
                progressDialog.dismiss();
            }
        });

    }
}