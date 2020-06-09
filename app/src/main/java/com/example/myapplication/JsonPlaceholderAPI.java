package com.example.myapplication;

import com.example.myapplication.dto.Massage;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderAPI {

    @GET("shoutbox/messages")
    Call<List<Massage>> getPost();

    @PUT("shoutbox/message/{id}")
    Call<ResponseBody> sendPost(@Path("id") String id, @Body Massage msg);

    @POST("shoutbox/message")
    Call<ResponseBody> sendMsg(@Body Massage msg);

    @DELETE("shoutbox/message/{id}")
    Call<ResponseBody> sendMsg(@Path("id") String id);

}

