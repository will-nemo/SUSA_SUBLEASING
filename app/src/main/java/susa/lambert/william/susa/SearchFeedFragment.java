package susa.lambert.william.susa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchFeedFragment extends FeedFragment {
    public static final String TAG = SearchFeedFragment.class.getCanonicalName();

    public SearchFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void performAction(long time, String uid, String postID){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final long timeT = time;
        final String uidT = uid;
        final String postIDT = postID;

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference docRef = firebaseFirestore.collection("post-likes")
                .document(postID).collection("liked-by").document(userid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userID")) {

                        Intent intent = new Intent(getContext(), PostActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("posttime", timeT);
                        bundle.putString("userid", uidT);
                        bundle.putString("postid", postIDT);
                        bundle.putString("action", "contains");
                        bundle.putString("user", "no");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(getContext(), PostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("posttime", timeT);
                    bundle.putString("userid", uidT);
                    bundle.putString("postid", postIDT);
                    bundle.putString("action", "nope");
                    bundle.putString("user", "no");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public Query getQuery(FirebaseFirestore db) {

        Bundle args = getArguments();



        String loc = args.get("Location").toString();
        String p = args.get("Price").toString();
        String a = args.get("Availability").toString();

        int price = 0;
        price = Integer.parseInt(p);

        if(loc.equals("") && a.equals("")  ){

            return db.collection("posts")
                    .whereEqualTo("status", "VACANT")
                    .whereLessThan("price", price+1);

        }else if(!loc.isEmpty() && a.equals("")){
            return db.collection("posts")
                    .whereEqualTo("location", loc)
                    .whereEqualTo("status", "VACANT")
                    .whereLessThan("price", price+1);
        }else if(loc.isEmpty() && !a.equals("")) {

            return db.collection("posts")
                    .whereEqualTo("availability", a)
                    .whereEqualTo("status", "VACANT")
                    .whereLessThan("price", price+1);
        }else if(!loc.isEmpty() && !a.equals("")) {

            return db.collection("posts")
                    .whereEqualTo("availability", a)
                    .whereEqualTo("location", loc)
                    .whereEqualTo("status", "VACANT")
                    .whereLessThan("price", price+1);
        }


        // All my posts
        return   db.collection("posts")
                .whereEqualTo("status", "VACANT")
                .orderBy("timeOf", Query.Direction.ASCENDING);
    }

}
