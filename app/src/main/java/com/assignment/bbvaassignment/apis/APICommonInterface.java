package com.assignment.bbvaassignment.apis;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APICommonInterface {


	@GET("/maps/api/place/textsearch/json?")
	Call<Map<String,Object>> getPlaces(@QueryMap Map<String, String> params);
}
