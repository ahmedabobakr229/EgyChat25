package com.example.documents.egychat2.Settings_Pakage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.documents.egychat2.Auth_System.MainPage;
import com.example.documents.egychat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private CircleImageView mDisplayImage;
    private TextView mName, mStatus;
    private Button statusBtn, mImageBtn, AllUsers, logout_btn;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDilog;
    //  private StorageReference mStorageRef;

    // random name for images
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDisplayImage = findViewById(R.id.settings_image);
        mName = findViewById(R.id.setting_display_name);
        mStatus = findViewById(R.id.settings_display_statue);
        statusBtn = findViewById(R.id.setting_status_btn);
        mImageBtn = findViewById(R.id.setting_img_btn);

        AllUsers = findViewById(R.id.setting_all_users);
        logout_btn = findViewById(R.id.setting_logout);

        AllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, UsersActivity.class);
                startActivity(i);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Settings.this, MainPage.class);
                startActivity(i);
            }
        });

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, Status.class);
                startActivity(i);
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Settings.this);

            }
        });


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Toast.makeText(Settings.this , dataSnapshot.toString() , Toast.LENGTH_LONG).show();

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if (!image.equals("default")) {

                    Picasso.with(Settings.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.app).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(Settings.this).load(image).placeholder(R.drawable.app).into(mDisplayImage);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(Settings.this);


            //  Toast.makeText(SettingActivity.this , ImageURL,Toast.LENGTH_LONG).show();
        }

        //profile image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDilog = new ProgressDialog(Settings.this);
                mProgressDilog.setTitle("Uploading Image...");
                mProgressDilog.setMessage("Please wait until uploading your image");
                mProgressDilog.setCanceledOnTouchOutside(false);
                mProgressDilog.show();

                final Uri resultUri = result.getUri();

                //   File thumb_filePath=new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();
                Uri imageUri = data.getData();

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(this);
                // uploading photo to page all users
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //  imageUri.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumb").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();
                                mUserDatabase.child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mProgressDilog.dismiss();
                                                if (task.isSuccessful()) {
                                                    // Intent selfIntent = new Intent(Settings.this, Settings.class);
                                                    //startActivity(selfIntent);
                                                    Toast.makeText(Settings.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(Settings.this, "Error Occured..." + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                mUserDatabase.child("thumb_image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mProgressDilog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Intent selfIntent = new Intent(Settings.this, Settings.class);
                                                    startActivity(selfIntent);
                                                    Toast.makeText(Settings.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(Settings.this, "Error Occured..." + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

}


