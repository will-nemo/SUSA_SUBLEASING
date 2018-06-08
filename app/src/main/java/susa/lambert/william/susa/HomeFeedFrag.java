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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeFeedFrag extends FeedFragment {
    public static final String TAG = HomeFeedFrag.class.getCanonicalName();

    public HomeFeedFrag() {
        // Required empty public constructor
    }

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        // All my posts
        return   databaseReference.collection("posts")
                .whereEqualTo("status", "VACANT")
                .orderBy("timeOf", Query.Direction.DESCENDING);
    }

    @Override
    public void performAction(String time, String uid, String postID){

        Intent intent = new Intent(getContext(), PostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("posttime", time);
        bundle.putString("userid", uid);
        bundle.putString("postid", postID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
