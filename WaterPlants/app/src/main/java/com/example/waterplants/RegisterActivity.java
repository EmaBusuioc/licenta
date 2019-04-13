package com.example.waterplants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextName;
    private CardView CardViewReg;
    private ProgressDialog ProgressDlg;
    private FirebaseAuth firebaseAuth;
    public String email, password,name;
    private static final String TAG = "RegisterActivity";
    private ImageView user_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user_pic=(ImageView)findViewById(R.id.flw_png);

        firebaseAuth=FirebaseAuth.getInstance();
        EditTextEmail=(EditText)findViewById(R.id.editUsername2);
        EditTextPassword=(EditText)findViewById(R.id.EditPassword2);
        EditTextName=(EditText)findViewById(R.id.editName);
        ProgressDlg= new ProgressDialog(this);
        CardViewReg=(CardView)findViewById(R.id.cardViewReg);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(new Intent(RegisterActivity.this, DrawerActivity.class));
            }
        });


        CardViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(registerUser() ){

                    startActivity(new Intent(RegisterActivity.this, FirstChild.class));
                }


            }
        });

    }



    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email,0);

        mDatabase.child(userId).setValue(user);

        // mDatabase.child(userId).child("Flori").setValue("Hello");

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }








        private boolean registerUser(){

        email=EditTextEmail.getText().toString().trim();
        password=EditTextPassword.getText().toString().trim();
        name=EditTextName.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this,"Your email is not valid. @xxx.xxx missing.",Toast.LENGTH_SHORT).show();
            return false;}

        if(TextUtils.isEmpty((password))){
            Toast.makeText(this,"Please enter password.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()<6){
            Toast.makeText(this,"Please enter password of 6 or more characters and at least 1 number",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((name))){
            Toast.makeText(this,"Please enter name.",Toast.LENGTH_SHORT).show();
            return false;
        }
        ProgressDlg.setMessage("Register User ...");
        ProgressDlg.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signInWithEmailAndPassword(email,password);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            writeNewUser(user.getUid(), name,email);

                        }else{
                            Toast.makeText(RegisterActivity.this,"Could not register. please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }

}
