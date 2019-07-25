package com.example.waterplants.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waterplants.FirstChild;
import com.example.waterplants.R;
import com.example.waterplants.User;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    CircleImageView image_profile;
    TextView username;
    TextView email;

    DatabaseReference reference;
    FirebaseUser fuser;

     ImageView ic_edit;

    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseAuth auth;
    private StorageReference mStorageRef;
    private  Uri selectedImage;
    private StorageTask uploadTask;
    public String userID;
    private DatabaseReference  mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        image_profile = view.findViewById(R.id.prof_img);
        username = view.findViewById(R.id.userTV);
        email=view.findViewById(R.id.email);

         ic_edit = view.findViewById(R.id.vector_edit_username);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(fuser.getUid());


        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userID = fuser.getUid();
        mDatabase = reference;

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                if (user.getImageURL().equals("default")) {
                    image_profile.setImageResource(R.drawable.def_user);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                    //username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        ic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
                View mview=getLayoutInflater().inflate(R.layout.dialog_username,null);
                final EditText usernm=mview.findViewById(R.id.username);


                mBuilder.setView(mview).setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(!usernm.getText().toString().isEmpty()){
                            HashMap<String, Object> map =new HashMap<>();
                            map.put("username", usernm.getText().toString());
                            mDatabase.updateChildren(map);
                            toastMessage("Editare reușită");
                            dialog.cancel();  }
                        else {
                            toastMessage("Câmpul pentru nume este gol!");
                            dialog.cancel();
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                            }
                        });
                final AlertDialog dialog=mBuilder.create();
                dialog.show();


            }
        });
        return view;
    }

        private void openImage() {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(i.ACTION_GET_CONTENT);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        private void uploadImage(){
            final ProgressDialog pd=new ProgressDialog(getContext());
            pd.setMessage("Se încarcă...");
            pd.show();
            if(selectedImage !=null){
                final StorageReference fileRefrence= mStorageRef.
                        child(userID + "." +getFileExtension(selectedImage));
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
                            String mUri = downloadUri.toString();

                            HashMap<String, Object> map =new HashMap<>();
                            map.put("imageURL", mUri);
                            mDatabase.updateChildren(map);
                            pd.dismiss();
                        }else{
                            toastMessage("Încercare eșuată!");
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
                toastMessage("Nicio imagine selectată");
            }
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_LOAD_IMAGE &&
                    resultCode == RESULT_OK && null != data && data.getData()!=null) {
                selectedImage = data.getData();
                image_profile.setImageURI(selectedImage);

                if(uploadTask != null && uploadTask.isInProgress()){
                    toastMessage("Acțiune în desfășurare");
                }else{
                    uploadImage();
                }

            }
        }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver= getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void toastMessage(String message){
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }

    }

