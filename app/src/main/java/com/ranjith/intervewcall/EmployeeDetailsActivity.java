package com.ranjith.intervewcall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ranjith.intervewcall.ApiPackage.ApiClient;
import com.ranjith.intervewcall.adaptor.UserAdaptor;
import com.ranjith.intervewcall.model.UserModel;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailsActivity extends AppCompatActivity {

    private TextView emp_id;
    private TextView emp_name;
    private TextView emp_email;
    private TextView emp_address;
    private TextView emp_phone_no;
    private TextView emp_address_1;
    private TextView emp_address_2;
    private TextView company_name;
    private TextView company_website;
    private TextView title_back_btn;
    String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        emp_id = (TextView) findViewById(R.id.emp_id);
        emp_name = (TextView) findViewById(R.id.emp_name);
        emp_email = (TextView) findViewById(R.id.emp_email);
        emp_address = (TextView) findViewById(R.id.emp_address);
        emp_phone_no  =  (TextView) findViewById(R.id.emp_phone_no);
        emp_address_1 = (TextView) findViewById(R.id.emp_address_1);
        emp_address_2 = (TextView) findViewById(R.id.emp_address_2);
        company_name = (TextView) findViewById(R.id.company_name);
        company_website = (TextView) findViewById(R.id.company_website);
        title_back_btn = (TextView) findViewById(R.id.title_back_btn);

        Intent intent = getIntent();
        _id = intent.getStringExtra("EMP_ID");

        title_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getEmployeeById();
    }


    private void getEmployeeById(){
        if (!ApiClient.isNetworkAvailable(this)){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(EmployeeDetailsActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        Call<UserModel> call = ApiClient.getUserService().getSingleEmployee(_id);

        call.enqueue(new Callback<UserModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        UserModel user = response.body();
                        Log.e("succ", ""+response.code());
                        emp_id.setText("Employee Id: "+user.getId());
                        emp_name.setText("Name: "+user.getName());
                        emp_email.setText(user.getEmail().toLowerCase());
                        emp_phone_no.setText(user.getPhone());
                        //Address
                        emp_address_1.setText(user.getAddress().getSuite()+", "+user.getAddress().getStreet());
                        emp_address_2.setText(user.getAddress().getCity()+" - "+user.getAddress().getZipcode());
                        //company
                        company_name.setText("Company Name: " + user.getCompany().getName());
                        company_website.setText(user.getWebsite());

                        emp_email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onClickOpenEmail(user.getEmail().toLowerCase());
                            }
                        });
                        emp_phone_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPhoneNo(user.getPhone());
                            }
                        });

                        company_website.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openWebsite(user.getWebsite());
                            }
                        });

                    }
                } else {
                    Log.e("failed", ""+response.code());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("throw", ""+t.getMessage());
                progressDialog.dismiss();
            }
        });

    }

    private void openWebsite(String website) {
        Uri uri = Uri.parse(website);
////        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//        startActivity(new Intent(Intent.ACTION_VIEW,uri));
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://"+website));
        startActivity(i);
    }

    private void openPhoneNo(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    private void onClickOpenEmail(String email_id) {
        String[] emails = email_id.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,"");
        intent.putExtra(Intent.EXTRA_TEXT,"");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose An Email"));
    }
}