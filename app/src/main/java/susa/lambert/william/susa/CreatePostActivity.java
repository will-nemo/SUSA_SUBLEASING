package susa.lambert.william.susa;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Random;

public class CreatePostActivity extends AppCompatActivity {

    public TextView value, descChar, titleChar;
    private Uri mdownloadUri;
    private EditText address, title, desc;
    private FirebaseAuth firebaseAuth;
    private Spinner location;
    private Spinner dates;
    private Button mSelectImage;
    private boolean location_assert= false;
    private boolean dates_assert = false;
    private String city;
    private String avail;
    private ImageView image_1;
    private ImageView image_2;
    private ImageView image_3;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT= 2;
    Uri post_image;
    Uri post_image2;
    Uri post_image3;
    boolean image1= false;
    boolean image2= false;
    boolean image3= false;
    public String useremail;
    public int progressC;
    public boolean idFree;
    FirebaseFirestore databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        idFree = false;
        value = (TextView)findViewById(R.id.title_normal);
        address = (EditText) findViewById(R.id.edit_address);
        title = (EditText) findViewById(R.id.edit_title);
        desc = (EditText) findViewById(R.id.edit_description);
        firebaseAuth= FirebaseAuth.getInstance();
        location = (Spinner) findViewById(R.id.spinner_location);
        dates = (Spinner) findViewById(R.id.spinner_availability);
        descChar = (TextView) findViewById(R.id.text_chardesc);
        titleChar = (TextView) findViewById(R.id.text_titlechars);
        useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();



        databaseReference= FirebaseFirestore.getInstance();

        desc.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString().length() >= 130)
                    descChar.setTextColor(Color.RED);
                else
                    descChar.setTextColor(Color.parseColor("#FF512DA8"));

                // this will show characters remaining
                descChar.setText(140 - s.toString().length() + "/140");

            }
        });

        title.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString().length() >= 30)
                    titleChar.setTextColor(Color.RED);
                else
                    titleChar.setTextColor(Color.parseColor("#FF512DA8"));

                // this will show characters remaining
                titleChar.setText(40 - s.toString().length() + "/40");

            }
        });

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = adapterView.getItemAtPosition(i).toString();
                location_assert= true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                avail = adapterView.getItemAtPosition(i).toString();
                dates_assert= true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSelectImage= (Button) findViewById(R.id.button_upload) ;
        mdownloadUri= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
        post_image= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
        post_image2 = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
        post_image3 = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
        image_1= (ImageView) findViewById(R.id.image_1);
        image_2=  (ImageView)   findViewById(R.id.image_2);
        image_3=    (ImageView) findViewById(R.id.image_3);

        mStorage= FirebaseStorage.getInstance().getReference();

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(adapter);

        ArrayAdapter<CharSequence> dAdapter = ArrayAdapter.createFromResource(this,
                R.array.dates, android.R.layout.simple_spinner_item);

        dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dates.setAdapter(dAdapter);

        Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2); // initiate the Seek bar

       simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                value.setText("Price: $" + progressChanged);
                progressC = 0;
                progressC = progressChanged;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            if(location_assert && FormComplete() && dates_assert ) {
                postLease();
                Intent intent = new Intent(CreatePostActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(CreatePostActivity.this ,"Please Fill in fields" ,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){

            //mProgressDialog.setMessage("Uploading ...");
            Uri uri= data.getData();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            StorageReference filepath= mStorage.child(user.getUid()).child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreatePostActivity.this, "Upload Done", Toast.LENGTH_LONG).show();

                    mdownloadUri= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");

                    mdownloadUri = taskSnapshot.getDownloadUrl();
                    if(!image1) {
                        Picasso.get().load(mdownloadUri).fit().centerCrop().into(image_1);
                        post_image= mdownloadUri;
                        post_image2= mdownloadUri;
                        post_image3= mdownloadUri;

                        image1= true;
                    }
                    else if (!image2){
                        Picasso.get().load(mdownloadUri).fit().centerCrop().into(image_2);
                        post_image2= mdownloadUri;
                        image2= true;
                    }
                    else if (!image3){
                        Picasso.get().load(mdownloadUri).fit().centerCrop().into(image_3);
                        post_image3= mdownloadUri;
                        image3=true;
                    }
                    else
                    {
                        Toast.makeText(CreatePostActivity.this, "Upload Limit", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private boolean FormComplete(){
        String Address= address.getText().toString().trim();

        String Title = title.getText().toString().trim();
        String Description= desc.getText().toString().trim();

        if(TextUtils.isEmpty(Address)|| TextUtils.isEmpty(Description)|| TextUtils.isEmpty(Title)){
            return false;
        }
        return true;
    }

    public static class PostSentFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Post Sent");
            builder.setMessage("Sublease Posted");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private void postLease(){
        String timeOf;
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String addy= address.getText().toString().trim();
        String des = desc.getText().toString().trim();
        String tit = title.getText().toString().trim();

        if(progressC == 0){
            progressC = 500;
        }

        String temp_image= post_image.toString();
        String temp_image2= post_image2.toString();
        String temp_image3= post_image3.toString();

        //Date date = new Date();
        //timeOf = date.toString();

        long tsLong = System.currentTimeMillis()/1000;
       // String ts = tsLong.toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String postID = getRandomHexString(25);


        UserPost userPost = new UserPost(progressC, addy, city, des, Uid, tsLong, temp_image,
                temp_image2, temp_image3, useremail, avail, tit, postID);

        databaseReference.collection("posts").document(postID).set(userPost);
        Toast.makeText(this, "Posted", Toast.LENGTH_LONG).show();
    }


    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
}
