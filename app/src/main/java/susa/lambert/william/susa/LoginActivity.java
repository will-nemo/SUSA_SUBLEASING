package susa.lambert.william.susa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText PasswordText;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button LoginButton;
    public ImageView backImage, logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent= getIntent();
        backImage = (ImageView) findViewById(R.id.imageView5);
        logoImage = (ImageView) findViewById(R.id.imageView6);
        Uri mImage= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/image_background");
        Picasso.get().load(mImage).transform(new CircleTransform()).fit().centerCrop().into(backImage);
        logoImage.setVisibility(View.VISIBLE);
        progressDialog= new ProgressDialog(this);
        Email= (EditText) findViewById(R.id.login_email);
        PasswordText = (EditText) findViewById(R.id.login_Password);
        mAuth = FirebaseAuth.getInstance();
        LoginButton= (Button) findViewById(R.id.button_login);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }


    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null) {
            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("action", "HOME");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }


    private void loginUser(){
        String email= Email.getText().toString().trim();
        String password= PasswordText.getText().toString().trim();


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

        progressDialog.setMessage("Signing in....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this,FeedActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("action", "HOME");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this, "Wrong Email or Password.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });

    }

}
