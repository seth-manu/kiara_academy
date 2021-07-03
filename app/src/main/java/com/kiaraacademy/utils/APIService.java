package com.kiaraacademy.utils;


import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.request.RequestSignup;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService {
    @Multipart
    @POST(APIConstants.API_SIGN_UP)
    Call<RequestSignup> registerUser(@Part MultipartBody.Part file,
                                     @Part("password") RequestBody password,
                                     @Part("mobile_number") RequestBody mobileNumber,
                                     @Part("country_code") RequestBody countryCode,
                                     @Part("email_id") RequestBody emailId,
                                     @Part("first_name") RequestBody firstName,
                                     @Part("last_name") RequestBody lastName,
                                     @Part("class") RequestBody classes);
}
