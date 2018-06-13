package susa.lambert.william.susa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment {

    private static final int GALLERY_INTENT= 2;
    private TextView eText, cText, nText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageButton iButton;
    private Uri imageURL, mdownloadUri;
    private ImageView profileImage;
    private StorageReference mStorage;
    private String newImage = "";
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
        cText = rootview.findViewById(R.id.text_college);
        iButton = rootview.findViewById(R.id.imageButton);

        mdownloadUri = Uri.parse("android.resource://"+rootview.getContext().getPackageName()+"/drawable/no_image_available");
        imageURL = Uri.parse("android.resource://"+rootview.getContext().getPackageName()+"/drawable/no_image_available");

        profileImage = rootview.findViewById(R.id.imageView);

        mStorage= FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        uEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = firebaseFirestore.collection("users").document(uID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                setInfo(user.name, user.college, user.pImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;

        eText.setText(uEmail);

        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        return rootview;
    }

    public void setInfo(String name, String college, String image){
        cText.setText(college);
        nText.setText(name);

        if(!image.equals("DEFAULT"))
        Picasso.get().load(image).transform(new CircleTransform()).fit().centerCrop().into(profileImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){

            //mProgressDialog.setMessage("Uploading ...");
            Uri uri= data.getData();
            final FirebaseUser user = firebaseAuth.getCurrentUser();

            StorageReference filepath = mStorage.child(user.getUid()).child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    DocumentReference docRef = firebaseFirestore.collection("users").document(user.getUid());


                    Toast.makeText(getContext(), "Profile Image Changed", Toast.LENGTH_LONG).show();

                    mdownloadUri= Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/no_image_available");

                    mdownloadUri = taskSnapshot.getDownloadUrl();
                        Picasso.get().load(mdownloadUri).transform(new CircleTransform()).fit().centerCrop().into(profileImage);

                    imageURL = mdownloadUri;

                    docRef.update("pImage", imageURL.toString());
                }
            });

        }
    }


}
