package com.example.waterplants;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlantProfileActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private CircleImageView profile_image;
    private TextView username;

    private EditText tvD;

    private Button btnD;
    private Button btnAnuleaza;
    private Button btnUda;
    private TextView ETinterval;
    private  final Calendar cc=Calendar.getInstance();
   private Calendar data=Calendar.getInstance();
    private Date dt;
    int hour, minute,year1,month1,day1;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private TextView mDisplayDate;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private static Context context;
    private static int RESULT_LOAD_IMAGE = 1;
    private StorageReference mStorageRef;
    private Uri selectedImage;
    private StorageTask uploadTask;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String usernm;
    private DatabaseReference mDataBase;
    private DatabaseReference mDatabase;
    private DatabaseReference DeleteDB;
    private Calendar calen=Calendar.getInstance();
    private int id;
    private  int interv;
    private Date fbDate=new Date(Calendar.getInstance().getTimeInMillis());
    private Date tamp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_profile);

        username=(TextView)findViewById(R.id.txtPlant);
        profile_image=(CircleImageView)findViewById(R.id.flw_prf);
        tvD=(EditText) findViewById(R.id.tvData);

        btnD=findViewById(R.id.btnData);
        btnAnuleaza=findViewById(R.id.btnAnuleazaAlarma);
        ETinterval=findViewById(R.id.EdInt);
        btnUda=findViewById(R.id.btnUdaAcum);


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDataBase=FirebaseDatabase.getInstance().getReference(user.getUid());
        PlantProfileActivity.context=getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.pf_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlantProfileActivity.this, FirstChild.class));
            }
        });
        intent=getIntent();
        usernm=intent.getStringExtra("plantName");
        username.setText(usernm);

        profile_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openImage();
             }
         });
        mDatabase=mDataBase.child("qFlowers").child(usernm);
        mDataBase.child("qFlowers").child(usernm).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Flower flower =dataSnapshot.getValue(Flower.class);

                if(flower != null) {
                    username.setText(flower.getFloare());
                    // toastMessage(Double.toString(stamp));
                    fbDate = flower.getNext_watering();
                    interv = flower.getInterval();
                    ETinterval.setText(Integer.toString(interv));
                    id = (int) flower.getId();
                    getData();
                    if (flower.getImageUrl().equals("default")) {
                        profile_image.setImageResource(R.drawable.icon_f);
                    } else {
                        Glide.with(getContext()).load(flower.getImageUrl()).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAnuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm(id);
                toastMessage("Alarmă anulată");
                Calendar c=Calendar.getInstance();
                Date d=new Date((c.getTimeInMillis()));
                mDataBase.child("qFlowers").child(usernm).child("next_watering").setValue(null);


            }
        });
        btnUda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                tamp=c.getTime();
                mDatabase.child("last_watering").setValue(tamp);
                mDataBase.child("pompa").setValue(1);
                new Thread(){
                    public void run(){
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mDataBase.child("pompa").setValue(0);
                    }
                }.start();
                toastMessage("Udarea a început");
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm(id);
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(PlantProfileActivity.this,
                        PlantProfileActivity.this,year,month,day);
                datePickerDialog.show();
            }
        });


    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                year1=i;
                month1=i1+1;
                day1=i2;
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(PlantProfileActivity.this,
                        (TimePickerDialog.OnTimeSetListener)PlantProfileActivity.this,hour,minute,
                        DateFormat.is24HourFormat(PlantProfileActivity.this));
                timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {


        int month=month1-1;
        cc.set(year1,month,day1,i,i1);
        cc.set(Calendar.SECOND,0);
        data.set(year1,month1-1,day1,i,i1);
        data.set(Calendar.SECOND,0);
        dt=data.getTime();
        mDatabase.child("next_watering").setValue(dt);
        tvD.setText(year1+"/"+month1 +"/"+year1 +" "+i+":"+ i1);
        new Thread(){
            @Override
            public void run() {
                startAlarm(cc);
            }
        }.start();
        toastMessage("Alarma a fost setată");

    }

    public static Context getContext(){
        return PlantProfileActivity.context;
    }

    private void openImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //i.setType("image/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver= getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(selectedImage !=null){
            final StorageReference fileRefrence= mStorageRef.child("Plants").child(usernm + "." +getFileExtension(selectedImage));
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
                        map.put("imageUrl", mUri);
                        mDataBase.child("qFlowers").child(usernm).updateChildren(map);
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

    private void getData(){

//        calen.setTimeInMillis(stamp.getTime());
        if(fbDate!=null){
            calen.setTime(fbDate);
            int year = calen.get(Calendar.YEAR);
            int month = calen.get(Calendar.MONTH);
            int date = calen.get(Calendar.DATE);
            int hour=calen.get(Calendar.HOUR_OF_DAY);
            int minute=calen.get(Calendar.MINUTE);
            int mth=month+1;
            if(minute<10){
                tvD.setText(date+"/"+mth +"/"+year +" "+hour+":0"+ minute);
            }else{
                tvD.setText(date+"/"+mth +"/"+year +" "+hour+":"+ minute);
            }

        }else{
            tvD.setText("Dată nesetată");
        }


//        Calendar p=Calendar.getInstance();
//        long end = p.getTimeInMillis();
//        long start = calen.getTimeInMillis();
//        long a= TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));

    }


    private void startAlarm(Calendar c){

        AlarmManager alarmManager=(AlarmManager)getSystemService(PlantProfileActivity.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        Bundle bun = new Bundle();
        bun.putString("plantName",usernm);
        bun.putString("Inter",Integer.toString(interv));
        bun.putString("ID",Integer.toString(id));
        intent.putExtras(bun);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //interv*1000*60*24
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY*interv,pendingIntent);
    }
    private void cancelAlarm(int id){
        //toastMessage("Cancel id:"+id);
        AlarmManager alarmManager=(AlarmManager)getSystemService(PlantProfileActivity.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        intent.putExtra("plantName",usernm);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,intent,0);
        alarmManager.cancel(pendingIntent);
    }


}
