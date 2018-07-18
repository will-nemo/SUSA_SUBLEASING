package susa.lambert.william.susa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIinterface {

    //@Headers({"Accept: application/json"})      //Required HTTP request header
    @GET("schools.json?api_key=1UnIIvdT0Pm3ZmLE4HpNf4NKBG4INOnd2zCfECZX")
    Call<JSONResponse> getJSON(@Query("school.name") String schoolName);
    //When getJSON method called it performs the GET Request with the workoutTag passed in
}
