package susa.lambert.william.susa;

import com.google.gson.annotations.SerializedName;


public class JSONResponse {

    @SerializedName("results")
    private Results[] results;
    //object of type Results pulls from the field workoutDetailData

    public Results[] getResults() {
        return results;
    }
    //getter that returns the Results object

}
