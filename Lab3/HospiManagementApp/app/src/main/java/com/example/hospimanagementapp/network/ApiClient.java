package com.example.hospimanagementapp.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final AppointmentAPI appointmentAPI;

    public ApiClient(Context ctx) {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(ctx))  // Mocked for Lab 2
                .addInterceptor(log)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mock.hms.local/") // dummy; intercepted
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        appointmentAPI = retrofit.create(AppointmentAPI.class);
    }

    public AppointmentAPI appointmentAPI() {
        return appointmentAPI;
    }
}
