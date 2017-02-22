package com.example.muhammadimran.saylaniproject.SignUp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muhammadimran.saylaniproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {

    private ImageButton imageButton;
    private EditText first_name, last_name, email, password, conferm_password;
    private Spinner bloodGroup, Mebership_type;
    private int REQUEST = 1;
    private Uri mImageUri = null;
    private String BloodGroup;
    private String MembershipType;
    private View view;
    DatabaseReference mdDatabase;
    StorageReference mStorage;
    FirebaseAuth mAuth;

    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mdDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        imageButton = (ImageButton) view.findViewById(R.id.selectImage);
        first_name = (EditText) view.findViewById(R.id.firstName);
        last_name = (EditText) view.findViewById(R.id.email);
        email = (EditText) view.findViewById(R.id.Email);
        password = (EditText) view.findViewById(R.id.Password);
        conferm_password = (EditText) view.findViewById(R.id.Conferm_Password);
        bloodGroup = (Spinner) view.findViewById(R.id.BloodGroup);
        Mebership_type = (Spinner) view.findViewById(R.id.Required);


        SelectImage();
        BloodGroup(view);
        MembershipType(view);
        SignUpClick(view);
        return view;

    }

    public void SelectImage() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), REQUEST);
            }
        });
    }

    public void BloodGroup(View view) {
        bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGroup = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Plz Select Item", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void MembershipType(View view) {
        Mebership_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MembershipType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Plz Select Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SignUpClick(View view) {

        final String F_name = first_name.getText().toString();
        final String L_name = last_name.getText().toString();
        final String emails = email.getText().toString();
        final String Password = password.getText().toString();
        final String Conferm_Password = conferm_password.getText().toString();


        if ((F_name.equals("") || L_name.equals("") || Password.equals("") || Conferm_Password.equals("")) || BloodGroup.equals("") || MembershipType.equals("")) {
            Toast.makeText(getActivity(), "Sorry Enter All fields", Toast.LENGTH_SHORT).show();
        }

        StorageReference storage = mStorage.child("Images").child(mImageUri.getLastPathSegment().toString());
        storage.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imageUrl = taskSnapshot.getDownloadUrl().toString();
                SignUpModel signUpModel = new SignUpModel(imageUrl, F_name, L_name, emails, BloodGroup, MembershipType, Password, Conferm_Password);
                mdDatabase.child("User-Data").child(mAuth.getCurrentUser().getUid()).setValue(signUpModel);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getActivity());

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                Log.d("TAG", "val : " + mImageUri);

                Glide.with(getActivity()).load(mImageUri).into(imageButton);
                // ImageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
