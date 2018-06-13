package susa.lambert.william.susa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public abstract class FeedFragment extends Fragment {

    public class UserPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        Context mContext;
        String loc,desc,addr,uid,img,time, img2, img3,email, availabilty, postID;
        int pri, likes_digit;

        public UserPostHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            performAction(time, uid, postID);
        }

        public void setEmail(String email){
            this.email = email;
        }


        public void setPostID(String postID){this.postID = postID;}

        public void setUid(String id){
            this.uid =id ;
        }
        public void setPrice(int price){
            pri = price;
            TextView post_addy= (TextView)mView.findViewById(R.id.post_address);
            post_addy.setText(Integer.toString(price));
        }

        public void setTitle(String title){
            addr = title;
            TextView post_addy= (TextView)mView.findViewById(R.id.post_id);
           // TextView post_addy= (TextView)mView.findViewById(R.id.post_id);
            post_addy.setText(title);
        }

        public void setImage(Context ctx,String image){
            this.img = image;
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }
        public void setImage2(Context ctx,String image){
            this.img2 = image;
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image2);
            Picasso.get().load(image).into(post_image);
        }
        public void setImage3(Context ctx,String image){
            this.img3 = image;
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image3);
            Picasso.get().load(image).into(post_image);
        }

        public void setLocation(String city){
            TextView post_city= (TextView)mView.findViewById(R.id.post_location);
            loc = city;
            post_city.setText(city);
        }
        public void setLikes(int likes){
            this.likes_digit= likes;
          //  TextView like_num = (TextView)mView.findViewById(R.id.likes_num);
         //   like_num.setText(Integer.toString(likes));
        }

        public void setAvailability(String avail){
            TextView post_avail= (TextView)mView.findViewById(R.id.post_availability);
            availabilty = avail;
            post_avail.setText(avail);
        }
        public void setDesc(String s){
            desc = s;
        }
        public void setTime(String s){
            this.time = s;
        }

    }

    FirestoreRecyclerAdapter<UserPost, UserPostHolder> firestoreRecyclerAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseFirestore fDatabase;
    private Query query;

    public FeedFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mRecycler = rootView.findViewById(R.id.my_recyclerView);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);


        fDatabase = FirebaseFirestore.getInstance();

        query = getQuery(fDatabase);

        fetchData();


    }


    @Override
    public void onStart() {
        super.onStart();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(FirebaseFirestore databaseReference);

    public abstract void performAction(String time, String uid, String poatID);

    public void fetchData(){

        FirestoreRecyclerOptions<UserPost> options = new FirestoreRecyclerOptions.Builder<UserPost>()
                .setQuery(query, UserPost.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<UserPost, UserPostHolder>(options) {
            @Override
            protected void onBindViewHolder(UserPostHolder viewHolder, int position, UserPost model) {
                viewHolder.setPrice(model.getPrice());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getContext() ,model.getPost_image());
                viewHolder.setImage2(getContext() ,model.getPost_image2());
                viewHolder.setImage3(getContext() ,model.getPost_image3());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUid(model.getUserID());
                viewHolder.setTime(model.getTimeOf());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setLikes(model.getLikes());
                viewHolder.setAvailability(model.getAvailability());
                viewHolder.setPostID(model.getPostID());
            }

            @Override
            public UserPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserPostHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false));
            }
        };


        mRecycler.setAdapter(firestoreRecyclerAdapter);
    }



}
