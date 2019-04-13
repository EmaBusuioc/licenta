package com.example.waterplants;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class FirstChild extends AppCompatActivity  {
  //  private Spinner mySpinner;
    private CardView CardViewCal;
    private ImageView usr_img;
    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseAuth auth;
    private StorageReference mStorageRef;
    private  Bitmap myBitmap;
    private static final String TAG = "FirstChild";

    private  Uri selectedImage;
    private Button show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_child);

        show=(Button)findViewById(R.id.output) ;
        usr_img=(ImageView)findViewById(R.id.user_image);
        CardViewCal=(CardView)findViewById(R.id.CardView_calendar);
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //checkFilePermissions();

        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();
        DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
        String ur=mDatabase.child(userID).child("img").getKey();
        show.setText(ur);

        Uri downloadURI=Uri.parse(ur);

        usr_img.setImageURI(downloadURI);
//       // downloadURI = mStorageRef.child("images/users/" + userID  + ".jpg").getDownloadUrl().getResult();
//        Picasso.with(getBaseContext()).load("https://firebasestorage.googleapis.com/v0/b/myappauth-7f952.appspot.com/o/images%2Fusers%2FU09NHpzwPXQROgwCZlrZWm9eUou1.jpg?alt=media&token=ff41ec66-0485-460c-8dab-1edf22b98741").into(usr_img);
        usr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //i.setType("image/*");
               startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        CardViewCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference();
//                myRef.setValue("Hello, World!");
            }
        });

    }

    public  void downloadDirect(StorageReference imageRef, ImageView imageView) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            usr_img.setImageURI(selectedImage);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            FirebaseUser user = auth.getCurrentUser();
            String userID = user.getUid();

            DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(userID).child("img").setValue(selectedImage.toString());
//                myRef.setValue("Hello, World!");
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };

//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//             myBitmap  = BitmapFactory.decodeFile(picturePath);
//            try {
//                ExifInterface exif = new ExifInterface(picturePath);
//                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                Log.d("EXIF", "Exif: " + orientation);
//                Matrix matrix = new Matrix();
//                if (orientation == 6) {
//                    matrix.postRotate(90);
//                }
//                else if (orientation == 3) {
//                    matrix.postRotate(180);
//                }
//                else if (orientation == 8) {
//                    matrix.postRotate(270);
//                }
//                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
//            }
//            catch (Exception e) {
//
//            }
//            usr_img.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 120, 120, false));
//            //usr_img.setImageBitmap(myBitmap);
//            FirebaseUser user = auth.getCurrentUser();
//            String userID = user.getUid();
//            StorageReference storageReference = mStorageRef.child("images/users/" + userID  + ".jpg");
//            storageReference.putFile(selectedImage). addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    toastMessage("Upload Success");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    toastMessage("Upload Failed");
//                }
//            })
//          ;
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
