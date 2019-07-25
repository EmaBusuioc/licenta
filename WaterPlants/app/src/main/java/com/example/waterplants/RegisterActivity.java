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
import android.support.v7.widget.Toolbar;


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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //toolbar arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });


        CardViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(registerUser() ){


                }


            }
        });

    }



    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email,0,"default",0);

        mDatabase.child(userId).setValue(user);

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }








        private boolean registerUser(){

        email=EditTextEmail.getText().toString().trim();
        password=EditTextPassword.getText().toString().trim();
        name=EditTextName.getText().toString().trim();
            final int[] st = new int[1];

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vă rugăm să introduceți email.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this,"Emailul nu este valid. Lipsește @xxx.xxx .",Toast.LENGTH_SHORT).show();
            return false;}

        if(TextUtils.isEmpty((password))){
            Toast.makeText(this,"Vă rugăm să introduceți parolă",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()<6){
            Toast.makeText(this,"Parola trebuie să fie de cel puțin șase caractere" +
                    " și macar un număr",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((name))){
            Toast.makeText(this,"Introduceți nume ",Toast.LENGTH_SHORT).show();
            return false;
        }
        ProgressDlg.setMessage("Înregistrare utilizator ...");
        ProgressDlg.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Înregistrare finalizată",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signInWithEmailAndPassword(email,password);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            writeNewUser(user.getUid(), name,email);
                            ProgressDlg.dismiss();
                            Intent intn=new Intent(RegisterActivity.this, FirstChild.class);
                            intn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intn);

                            st[0] =1;
                        }else{
                            Toast.makeText(RegisterActivity.this,"Înregistrarea nu s-a putut finaliza. Verificați conexiunea la internet",Toast.LENGTH_SHORT).show();
                            ProgressDlg.dismiss();
                            st[0] =0;
                        }
                    }
                });
        if(st[0] ==1)
            return true;
        else
            return false;
    }

}
