package susa.lambert.william.susa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchFeedFragment extends FeedFragment {
    public static final String TAG = SearchFeedFragment.class.getCanonicalName();

    public SearchFeedFragment() {
        // Required empty public constructor
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
                .orderBy("timeOf", Query.Direction.DESCENDING);
    }

}
