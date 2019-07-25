package com.example.waterplants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private CardView CardViewLogin;
    private TextView TextViewRegister;
    private ProgressDialog ProgressDlg;
    private FirebaseAuth firebaseAuth;
    public String email, password;
    private static final String TAG = "MainActivity";
    private FirebaseUser usr;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        EditTextEmail=(EditText)findViewById(R.id.editUsername);
        EditTextPassword=(EditText)findViewById(R.id.EditPassword);
        ProgressDlg= new ProgressDialog(this);
        mDatabase=FirebaseDatabase.getInstance().getReference("ExtraFlowers");

        CardViewLogin=(CardView)findViewById(R.id.cardView);
        CardViewLogin.setOnClickListener(this);
        TextViewRegister=(TextView)findViewById(R.id.textRegister);
        TextViewRegister.setOnClickListener(this);

         usr=FirebaseAuth.getInstance().getCurrentUser();
        if(usr!=null){
            updateUI();
        }
    }



    public void updateUI(  ){

        startActivity(new Intent(this, FirstChild.class));
    }

    @Override
    public void onClick(View v) {
        email=EditTextEmail.getText().toString().trim();
        password=EditTextPassword.getText().toString().trim();

        if(v==TextViewRegister){
            startActivity(new Intent(this, RegisterActivity.class));
            //registerUser();
        }
        if(v==CardViewLogin){
            //open login activity here
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Inserați email.",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty((password))){
                Toast.makeText(this,"Inserați parola.",Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Logare cu succes, deschide activitatea următoare cu informațiile utilizatorului
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                updateUI();
                            } else {
                                // Dacă logarea eșuează, afișează mesaj.
                                Log.w(TAG, "Logare eșuată", task.getException());
                                Toast.makeText(MainActivity.this, "Eroare la autentificare. Verificați rețea",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}

