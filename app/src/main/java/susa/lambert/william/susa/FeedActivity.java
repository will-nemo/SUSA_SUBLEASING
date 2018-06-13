package susa.lambert.william.susa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    public View nav_header;

    private HomeFeedFrag homeFeedFrag;
    // private UserPostsFrag userPostsFrag;
  //  private PinnedPostsFrag pinnedPostsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        String uEmail = "";
        final String uID;
        String uName = "";
        String action = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();         //gets the workout tag that was passed from
        //the start activity

        if(bundle != null){                         //error checking for empty Bundle
            action = bundle.getString("action");
        }else{
            action = "HOME";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        uEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = firebaseFirestore.collection("users").document(uID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                setInfo(user.name, user.pImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_feed, null);


       //sets the text for the users email in navigation header
        ((TextView) nav_header.findViewById(R.id.head_email)).setText(uEmail);


        navigationView.addHeaderView(nav_header);
        navigationView.setNavigationItemSelectedListener(this);


            homeFeedFrag = new HomeFeedFrag();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, homeFeedFrag)
                    .addToBackStack(HomeFeedFrag.TAG)
                    .commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post) {
            Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
            startActivity(intent);
            return true;
        } if (id == R.id.action_search) {
            Intent intent = new Intent(FeedActivity.this, SearchQActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent mIntent = new Intent(FeedActivity.this, UserProfileActivity.class);
            startActivity(mIntent);

        } else if (id == R.id.nav_pinned) {
            Intent mIntent = new Intent(FeedActivity.this, StoredPostActivity.class);
            startActivity(mIntent);
        } else if (id == R.id.nav_signout) {
            firebaseAuth.signOut();
            Intent oIntent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(oIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setInfo(String name, String image){
        ((TextView) nav_header.findViewById(R.id.head_name)).setText(name);


        ImageView imageView = ((ImageView) nav_header.findViewById(R.id.imageView));

        if(!image.equals("DEFAULT"))
            Picasso.get().load(image).transform(new CircleTransform())
                    .fit().centerCrop().into(imageView);
    }
}
