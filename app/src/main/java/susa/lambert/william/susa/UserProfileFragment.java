package susa.lambert.william.susa;

import android.content.Context;
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


public class UserProfileFragment extends Fragment {

    private TextView nText;
    private TextView eText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_user_profile, container, false);

        String uEmail = "";
        final String uID;

        nText = rootview.findViewById(R.id.text_name);
        eText = rootview.findViewById(R.id.text_email);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;

        eText.setText(uEmail);
        return rootview;
    }

    public void setName(String name){ nText.setText(name); }


}
