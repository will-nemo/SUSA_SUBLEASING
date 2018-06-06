package susa.lambert.william.susa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchQActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    public View nav_header;
    private Spinner location;
    private Spinner dates;
    private Button sButton;
    public TextView value;
    public int progressC;
    private String city = "", avail = "";

    // private UserPostsFrag userPostsFrag;
    //  private PinnedPostsFrag pinnedPostsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_q);

        String uEmail = "";
        final String uID;
        String uName = "";
        String action = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();         //gets the workout tag that was passed from
        //the start activity

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        value = (TextView)findViewById(R.id.title_normal);
        location = (Spinner) findViewById(R.id.spinner_location);
        dates = (Spinner) findViewById(R.id.spinner_availability);
        sButton = findViewById(R.id.button_search);
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                avail = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        uEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = firebaseFirestore.collection("users").document(uID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                setName(user.name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchQActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(progressC == 0){
                    progressC = 500;
                }

                Intent intent = new Intent(SearchQActivity.this, SearchFActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Location", city);
                bundle.putString("Availability", avail);
                bundle.putString("Price", Integer.toString(progressC));

                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

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
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(SearchQActivity.this, FeedActivity.class);
            startActivity(intent);
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent mIntent = new Intent(SearchQActivity.this, UserProfileActivity.class);
            startActivity(mIntent);
        } else if (id == R.id.nav_pinned) {
            Intent mIntent = new Intent(SearchQActivity.this, StoredPostActivity.class);
            startActivity(mIntent);
        } else if (id == R.id.nav_signout) {
            firebaseAuth.signOut();
            Intent oIntent = new Intent(SearchQActivity.this, MainActivity.class);
            startActivity(oIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setName(String name){ ((TextView) nav_header.findViewById(R.id.head_name)).setText(name); }
}

