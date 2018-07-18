package susa.lambert.william.susa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText nText, cText;
    private TextView collegeTxt;
    private Button sButton, cButton;
    private String name = "", college = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();         //gets the workout tag that was passed from
        //the start activity

        if(bundle != null){                         //error checking for empty Bundle
            name = bundle.getString("name");
            college = bundle.getString("college");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        nText = findViewById(R.id.edit_name);
        cText = findViewById(R.id.edit_college);
        collegeTxt = findViewById(R.id.textViewCollege);
        sButton = findViewById(R.id.button_save);
        cButton = findViewById(R.id.button_change);

        nText.setText(name);
        cText.setText(college);
        collegeTxt.setText(college);

        Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(myToolbar);

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, FindCollegeActivity.class);
                startActivity(intent);
            }
        });

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = firebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference docRef = firebaseFirestore.collection("users").document(uid);

              if(!nText.getText().toString().equals(name) && !cText.getText().toString().equals(college) ){
                    docRef.update("name", nText.getText().toString());
                    docRef.update("college", cText.getText().toString());
                  Toast.makeText(EditProfileActivity.this, "Saving Changes", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                  startActivity(intent);

              }else if(!nText.getText().toString().equals(name)){
                    docRef.update("name", nText.getText().toString());
                  Toast.makeText(EditProfileActivity.this, "Saving Changes", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                  startActivity(intent);
              }else if(!cText.getText().toString().equals(college)){
                    docRef.update("college", cText.getText().toString());
                  Toast.makeText(EditProfileActivity.this, "Saving Changes", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                  startActivity(intent);
              }


            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
