package com.example.waterplants;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.waterplants.Model.Flower;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFlower extends AppCompatActivity  {

    private static int RESULT_LOAD_IMAGE = 1;
    private CircleImageView profile_image;
    private Spinner specie;
    private FirebaseAuth auth;

    String sp;
    int intervall;
    private CardView CardViewReg;
    public String name,species;
    private FirebaseUser user;
    private TextView nm;
    private DatabaseReference mDatabase;
    public ProgressDialog ProgressDlg;
    private ImageView photo;
    private TextView interv;

    long maxid;
    private StorageTask uploadTask;
    private Uri selectedImage;
    private  String mUri;
    private StorageReference mStorageRef;
    private static List<String> specs=null;
    static ArrayAdapter<String> ad;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flower);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        profile_image=(CircleImageView)findViewById(R.id.flw_pic);
        ProgressDlg= new ProgressDialog(AddFlower.this);
        CardViewReg=(CardView)findViewById(R.id.CardViewFLW);
        interv=findViewById(R.id.editInterval);
        nm=(TextView)findViewById(R.id.editFlw);
        specie=(Spinner) findViewById(R.id.editSpecie) ;
        specs=new ArrayList<>();
        specs.add("Alta");
        specs.add("Azalee");
        specs.add("Cactus");
        specs.add("Orhidee");



        ad=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,specs);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specie.setAdapter(ad);
        specie.setSelection(0);

        specie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        mDatabase.child("ExtraFlowers").child(specie.getSelectedItem().toString())
                                .child("interval").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    intervall=dataSnapshot.getValue(int.class);
                                    interv.setText(Integer.toString(intervall));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        photo=(ImageView)findViewById(R.id.flw_prf);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        mDatabase.child(user.getUid()).child("nrFlori").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=dataSnapshot.getValue(int.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFlower.this, FirstChild.class));
            }
        });

        CardViewReg.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                ProgressDlg.setMessage("Înregistrare floare ...");
                ProgressDlg.show();
                Flower flower= new Flower(sp,nm.getText().toString(),null,
                        Integer.parseInt(interv.getText().toString()),maxid, mUri);
                mDatabase.child(user.getUid()).child("nrFlori").setValue(maxid+1);
                mDatabase.child(user.getUid()).child("qFlowers").child(nm.getText().toString()).setValue(flower);
                ProgressDlg.dismiss();
                Intent intn=new Intent(AddFlower.this, FirstChild.class);
                startActivity(intn);


            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }



    private void openImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver= AddFlower.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(selectedImage !=null){
            final StorageReference fileRefrence= mStorageRef.child("Plants").child(nm.getText().toString() + "." +getFileExtension(selectedImage));
            uploadTask=fileRefrence.putFile(selectedImage) ;
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRefrence.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        mUri = downloadUri.toString();
                        pd.dismiss();
                    }else{
                        toastMessage("Failed!");
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage(e.getMessage());
                    pd.dismiss();
                }
            });
        } else{
            toastMessage("No image selected");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && data.getData()!=null) {
            selectedImage = data.getData();
            profile_image.setImageURI(selectedImage);

            if(uploadTask != null && uploadTask.isInProgress()){
                toastMessage("Upload in progress");
            }else{
                uploadImage();
            }

        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
