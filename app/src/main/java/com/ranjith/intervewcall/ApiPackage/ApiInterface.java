package com.ranjith.intervewcall.ApiPackage;

import com.ranjith.intervewcall.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    //GetAllUsers
    @GET("users")
    Call<List<UserModel>> getAllUsers();

    @GET("users/{id}")
    Call<UserModel> getSingleEmployee(@Path("id") String id);

}
