package com.assignment.bbvaassignment.apis;


import com.assignment.bbvaassignment.constants.MyConstants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

	private static final APIRequestHandler instance = new APIRequestHandler();
	APICommonInterface mInterfaces = createService();

	private APIRequestHandler() {

	}

	public static APIRequestHandler getInstance() {
		return instance;
	}

	public static APICommonInterface createService() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(MyConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		return retrofit.create(APICommonInterface.class);
	}

	public Call<Map<String,Object>> getPlaces(HashMap<String, String> paramsMap) {
		return mInterfaces.getPlaces(paramsMap);
	}
}
