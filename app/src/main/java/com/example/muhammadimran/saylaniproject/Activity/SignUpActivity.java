package com.example.muhammadimran.saylaniproject.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muhammadimran.saylaniproject.R;
import com.example.muhammadimran.saylaniproject.SignUp.SignUpModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Button signup;
    private EditText first_name, last_name, email, password, conferm_password;
    private Spinner bloodGroup, Mebership_type;
    public static final int REQUEST = 1;
    private Uri mImageUri = null;
    private String BloodGroup;
    private String MembershipType;
    private SignUpModel signUpModel;
    private DatabaseReference mdDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);
        mdDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        imageButton = (ImageButton) findViewById(R.id.selectImage);
        first_name = (EditText) findViewById(R.id.firstName);
        last_name = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        conferm_password = (EditText) findViewById(R.id.Conferm_Password);
        bloodGroup = (Spinner) findViewById(R.id.BloodGroup);
        Mebership_type = (Spinner) findViewById(R.id.Required);
        signup = (Button) findViewById(R.id.signUp);
        //  --progressDialog
        progressDialog = new ProgressDialog(this);

        SelectImage();
        BloodGroup();
        MembershipType();
        SignUpClick();
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

    public void BloodGroup() {
        bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGroup = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SignUpActivity.this, "Plz Select Item", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void MembershipType() {
        Mebership_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MembershipType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SignUpActivity.this, "Plz Select Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SignUpClick() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("SigningUp...");
                progressDialog.setMessage("Plz wait.");
                progressDialog.show();
                final String F_name = first_name.getText().toString();
                final String L_name = last_name.getText().toString();
                final String emails = email.getText().toString();
                final String Password = password.getText().toString();
                final String Conferm_Password = conferm_password.getText().toString();


                if ((F_name.equals("") || L_name.equals("") || Password.equals("") || Conferm_Password.equals("")) || BloodGroup.equals("") || MembershipType.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Sorry Enter All fields", Toast.LENGTH_SHORT).show();
                }

                if (mImageUri == null) {
                    Toast.makeText(SignUpActivity.this, "Plz SelecImage after creating profile!!!", Toast.LENGTH_SHORT).show();
                } else if (mImageUri != null) {
                    StorageReference storage = mStorage.child("Images").child(mImageUri.getLastPathSegment().toString());
                    storage.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUrl = taskSnapshot.getDownloadUrl().toString();
                            mAuth.createUserWithEmailAndPassword(emails, Conferm_Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    signUpModel = new SignUpModel(imageUrl, F_name, L_name, emails, BloodGroup, MembershipType, Password, Conferm_Password);
                                    mdDatabase.child("User-Data").child(mAuth.getCurrentUser().getUid()).setValue(signUpModel);
                                    first_name.setText("");
                                    last_name.setText("");
                                    email.setText("");
                                    password.setText("");
                                    conferm_password.setText("");
                                    finish();
                                    progressDialog.dismiss();
                                }
                            });

                        }
                    });
                }


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
                    .start(SignUpActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                Log.d("TAG", "val : " + mImageUri);

                Glide.with(SignUpActivity.this).load(mImageUri).into(imageButton);
                // ImageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
