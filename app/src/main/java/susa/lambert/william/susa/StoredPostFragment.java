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
    public void performAction(String time, String uid){

        Intent intent = new Intent(getContext(), PostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("posttime", time);
        bundle.putString("userid", uid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        // All my posts

        return   databaseReference.collection("user-likes").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("like")
                .whereEqualTo("status", "VACANT")
                .orderBy("timeOf", Query.Direction.DESCENDING);
    }

}
