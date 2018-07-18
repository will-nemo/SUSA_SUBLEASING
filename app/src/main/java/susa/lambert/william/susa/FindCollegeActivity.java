package susa.lambert.william.susa;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindCollegeActivity extends AppCompatActivity implements ResultsAdapter.ResultsAdapterListener {

    private static final String TAG = FindCollegeActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Results> resultList;
    private ResultsAdapter mAdapter;
    private SearchView searchView;
    private ArrayList<Results> data;

    private EditText cText;
    private TextView nText;
    public String sName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_college);


        cText = findViewById(R.id.text_enter);
        nText = findViewById(R.id.text_notfound);

        ImageButton sButton = (ImageButton) findViewById(R.id.search_button);

        recyclerView = findViewById(R.id.recycler_view);

        resultList = new ArrayList<>();
        mAdapter = new ResultsAdapter(this, resultList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);




        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cText.getText().toString().isEmpty()){
                    Toast.makeText(FindCollegeActivity.this, "Enter a College.", Toast.LENGTH_LONG).show();
                }else {
                    nText.setVisibility(View.INVISIBLE);
                    load(cText.getText().toString());
                }

            }
        });

    }

    private void load(final String college){
        //Retrofit class object generates an implementation of the TrainingPeaks interface
        //uses Gson for deserialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.data.gov/ed/collegescorecard/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //instantiates the API interface
        APIinterface request = retrofit.create(APIinterface.class);

        //Call object created from APIinterface obj to carryouy asyncronous request
        Call<JSONResponse> call = request.getJSON(college);

        //asyncronous request is executed by enqueue(), on Success onResponse() is executed
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                if (response.isSuccessful()) {

                    JSONResponse jsonResponse = response.body();        //Obtains the JSONResponse obj
                   data = new ArrayList<>(Arrays.asList(jsonResponse.getResults()));         //stores workoutDetailData in
                    //Result obj

                    if(!data.isEmpty()) {

                        School temp = data.get(0).getSchool();


                        resultList.clear();
                        resultList.addAll(data);


                        mAdapter = new ResultsAdapter(FindCollegeActivity.this, resultList, FindCollegeActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    }else{
                        nText.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    // error handling/ if not found then goes back to StartActivity
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(FindCollegeActivity.this, "Error: Workout Tag Not Found", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FindCollegeActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case 500:
                            Toast.makeText(FindCollegeActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(FindCollegeActivity.this, MainActivity.class);
                            startActivity(intent1);
                            break;
                        default:
                            Toast.makeText(FindCollegeActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(FindCollegeActivity.this, MainActivity.class);
                            startActivity(intent2);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                //if asyncronous request called by enqueue fails then onFailure() executed
                Log.d("Error",t.getMessage());
            }
        });

    }

    @Override
    public void onSchoolSelected(Results result) {
        final School temp = result.getSchool();
        String set = "college";

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FindCollegeActivity.this);
        View cView = getLayoutInflater().inflate(R.layout.dialog_college, null);

        TextView tv_cName = cView.findViewById(R.id.tv_cname);
        Button yButton = cView.findViewById(R.id.b_yes);
        Button nButton = cView.findViewById(R.id.b_no);


        mBuilder.setView(cView);
        tv_cName.setText(temp.getName());



        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        yButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore firebaseFirestore;
                FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();


                firebaseFirestore = FirebaseFirestore.getInstance();

                String uid = firebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference docRef = firebaseFirestore.collection("users").document(uid);

                docRef.update("college", temp.getName());

                Intent intent = new Intent(FindCollegeActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.cancel();
            }
        });
    }

}
