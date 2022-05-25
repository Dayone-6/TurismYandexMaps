package com.example.turismyandexmaps.ui.tickets;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("v3/prices_for_dates")
    Call<Answer> getTickets(@Query("origin") String from,
                            @Query("destination") String to,
                            @Query("departure_at") String from_time,
                            @Query("return_at") String to_time,
                            @Query("direct") boolean direct,
                            @Query("limit") int tickets_limit,
                            @Query("token") String token);

    @GET("v3/prices_for_dates")
    Call<Answer> getTicketsWithoutFromTime(@Query("origin") String from,
                                           @Query("destination") String to,
                                           @Query("return_at") String to_time,
                                           @Query("direct") boolean direct,
                                           @Query("limit") int tickets_limit,
                                           @Query("token") String token);

    @GET("v3/prices_for_dates")
    Call<Answer> getTicketsWithoutToTime(@Query("origin") String from,
                                         @Query("destination") String to,
                                         @Query("departure_at") String from_time,
                                         @Query("direct") boolean direct,
                                         @Query("limit") int tickets_limit,
                                         @Query("token") String token);

    @GET("v3/prices_for_dates")
    Call<Answer> getTicketsWithoutTime(@Query("origin") String from,
                                       @Query("destination") String to,
                                       @Query("direct") boolean direct,
                                       @Query("limit") int tickets_limit,
                                       @Query("token") String token);
}
