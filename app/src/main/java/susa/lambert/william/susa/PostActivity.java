package susa.lambert.william.susa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.view.PagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private TextView tText;
    private TextView dText;
    private TextView pText;
    private TextView aText;
    private TextView adText;
    private TextView eText;
    private FirebaseFirestore firebaseFirestore;
    private String timeOF = "";
    private String pUser = "";
    private String iPost = "";
    public String loc,desc,addr,uid,img,time, img2, img3,email, availabilty;
    public int pri, likes_digit;

    public String useremail;
    public int progressC;
    Uri post_image;
    Uri post_image2;
    Uri post_image3;
    String timeOf;
    String Uid;
    String addy;
    String des;
    String tit;
    String temp_image;
    String temp_image2;
    String temp_image3;
    private String city;
    private String avail;
    private String postID;
    private String[] mImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){                         //error checking for empty Bundle
            timeOF = bundle.getString("posttime");
            pUser = bundle.getString("userid");
            iPost = bundle.getString("postid");
        }

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        mImages = new String[3];

        tText = findViewById(R.id.text_title);
        dText = findViewById(R.id.text_description);
        pText = findViewById(R.id.text_price);
        aText = findViewById(R.id.text_availability);
        adText = findViewById(R.id.text_address);
        eText = findViewById(R.id.text_email);

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference docRef = firebaseFirestore.collection("posts").document(iPost);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserPost userPost = documentSnapshot.toObject(UserPost.class);
                setValues(userPost.title, userPost.desc, userPost.price,
                        userPost.availability, userPost.address, userPost.email);

                        progressC = userPost.price;
                        addy = userPost.address;
                        city = userPost.location;
                        des = userPost.getDesc();
                        Uid = userPost.userID;
                        time = userPost.timeOf;
                        temp_image = userPost.post_image;
                        temp_image2 = userPost.post_image2;
                        temp_image3 = userPost.post_image3;
                        useremail = userPost.email;
                        avail = userPost.availability;
                        tit = userPost.title;
                        postID = userPost.postID;

                        mImages[0] = temp_image;
                        mImages[1] = temp_image2;
                        mImages[2] = temp_image3;

                ViewPager viewPager = findViewById(R.id.container);
                ImagePagerAdapter adapter = new ImagePagerAdapter(PostActivity.this,mImages);
                viewPager.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






    }

    public void setValues(String title, String description, int price,
                          String availability, String address, String email){

        String p = Integer.toString(price);
        tText.setText(title);
        dText.setText(description);
        pText.setText(p);
        aText.setText(availability);
        adText.setText(address);
        eText.setText(email);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(PostActivity.this, FeedActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(PostActivity.this, SearchQActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_store) {

            Date date = new Date();

            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserPost userPost = new UserPost(progressC, addy, city, des, Uid, timeOF, temp_image,
                    temp_image2, temp_image3, useremail, avail, tit, postID);
            FirebaseFirestore dt = FirebaseFirestore.getInstance();
            FirebaseFirestore dl = FirebaseFirestore.getInstance();

            dt.collection("user-likes").document(userid).collection("likes")
                    .document(postID).set(userPost);


            final DocumentReference sfDocRef = dl.collection("posts").document(postID);

            dl.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                    double newnum = snapshot.getDouble("likes") + 1;
                    transaction.update(sfDocRef, "likes", newnum);

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Log.d(TAG, "Transaction success!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Transaction failure.", e);
                        }
                    });


            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private Context context;
        private String[] mImages;

        ImagePagerAdapter(Context context, String[] imageUrls) {
            this.context = context;
            this.mImages = imageUrls;
        }

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = PostActivity.this;
            ImageView imageView = new ImageView(context);

            Picasso.get()
                    .load(mImages[position])
                    .fit()
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}

