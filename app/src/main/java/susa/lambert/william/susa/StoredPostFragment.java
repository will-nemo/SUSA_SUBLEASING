package susa.lambert.william.susa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StoredPostFragment extends FeedFragment{

    public static final String TAG =StoredPostFragment.class.getCanonicalName();

    public StoredPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void performAction(String time, String uid, String postID){

        Intent intent = new Intent(getContext(), PostedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("posttime", time);
        bundle.putString("userid", uid);
        bundle.putString("postid", postID);
        bundle.putString("action", "stored");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        // All my posts

        return   databaseReference.collection("user-likes").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("likes")
                .whereEqualTo("status", "VACANT")
                .orderBy("timeOf", Query.Direction.DESCENDING);
    }

}
