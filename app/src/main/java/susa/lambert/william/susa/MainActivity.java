package susa.lambert.william.susa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

 //   private LoginButton loginButton;
  //  private CallbackManager callbackManager;
    private TextView info;
    //private DatabaseReference mDatabase;
    private FirebaseFirestore mDatabase;
    private Button RegisterButton;

    private TextView LoginText;
    private EditText Email;
    private EditText PasswordText;
    private EditText mUsername;
    private String mEmail;
    private String userN;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegisterButton= (Button) findViewById(R.id.button_login);
        LoginText= (TextView) findViewById(R.id.login_text);
        Email= (EditText) findViewById(R.id.user_email);
        mUsername = (EditText) findViewById(R.id.user_name);
        PasswordText = (EditText) findViewById(R.id.user_pw);
        progressDialog= new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        //button click listeners

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null) {
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("action", "HOME");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    private void registerUser(){
        String email= Email.getText().toString().trim();
        String password= PasswordText.getText().toString().trim();
        userN = mUsername.getText().toString().trim();
        String check = email;
        mEmail = email;
        if(TextUtils.isEmpty(email)){
            // email empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            // password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if(check.contains("@")){	//checks to see if there is an email entered - WJL
            check = check.split("@")[1];

            if(check.equalsIgnoreCase("my.fsu.edu") || check.equalsIgnoreCase("tcc.fl.edu")){		//makes sure user registering has a College Email - WJL

            }else{
                Toast.makeText(MainActivity.this, "College Email Required",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }//end of large if - WJL
        else{//made in case of false positive - WJL
            Toast.makeText(MainActivity.this, "Email Required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String id = user.getUid();
                            if (userN.isEmpty()) {
                                Toast.makeText(MainActivity.this, "dAuthentication faile bitch.",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                User mUser = new User(userN, mEmail);

                                mDatabase.collection("users").document(id).set(mUser);
                            }
                            Toast.makeText(MainActivity.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        progressDialog.hide();

    }


}

