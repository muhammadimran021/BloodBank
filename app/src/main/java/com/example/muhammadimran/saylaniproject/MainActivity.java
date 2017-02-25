package com.example.muhammadimran.saylaniproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadimran.saylaniproject.Activity.VolunteerNavigationDrawer;
import com.example.muhammadimran.saylaniproject.Activity.SignUpActivity;
import com.example.muhammadimran.saylaniproject.Activity.Member;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        email = (EditText) findViewById(R.id.user_Email);
        password = (EditText) findViewById(R.id.user_password);

        // --ProgressDialog
        progressDialog = new ProgressDialog(MainActivity.this);


    }

    public void loginClick(View view) {
        progressDialog.setTitle("SigningIn");
        progressDialog.setMessage("plz Wait.");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (authResult != null) {
                    mDatabase.child("User-Data").child(authResult.getUser().getUid()).child("membership").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("TAg", dataSnapshot.getValue().toString());
                            if (dataSnapshot.getValue().equals("volunteer")) {
                                Intent volunteer = new Intent(MainActivity.this, VolunteerNavigationDrawer.class);
                                startActivity(volunteer);
                                finish();
                                progressDialog.dismiss();
                            } else {
                                Intent intent = new Intent(MainActivity.this, Member.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "error in signingIn...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void noAccount(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

}
