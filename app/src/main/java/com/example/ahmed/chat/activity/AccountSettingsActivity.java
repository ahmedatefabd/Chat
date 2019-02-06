package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsActivity extends AppCompatActivity {

    @BindView(R.id.image)
    CircleImageView imageView;
    @BindView(R.id.username_TV)
    TextView usernameTV;
    @BindView(R.id.statuse_TV)
    TextView statuseTV;
    @BindView(R.id.change_image_btn)
    Button changeImageBtn;
    @BindView(R.id.change_statuse_btn)
    Button changeStatuseBtn;
    private Toolbar toolbar;

    private DatabaseReference getDatabaseReference;
    private FirebaseAuth mAuth;
    private final static int Gallery_pick = 1;
    private StorageReference storageReference;
    private AllUsers allUsers;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.Settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AccountSettings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AccountSettingsActivity.this);
        editor = sharedPreferences.edit();

        allUsers = new AllUsers();
        mAuth = FirebaseAuth.getInstance();
        String online_user_id = mAuth.getCurrentUser().getUid();
        getDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS).child(online_user_id);
        storageReference = FirebaseStorage.getInstance().getReference().child(Constant.ExtraBranch.PROFILE_IMAGES);

        getDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(Constant.ExtraBranch.USER_NAME).getValue().toString();
                String statuse = dataSnapshot.child(Constant.ExtraBranch.USER_STATUSE).getValue().toString();
                String image = dataSnapshot.child(Constant.ExtraBranch.USER_IMAGE).getValue().toString();
                String thumb_image = dataSnapshot.child(Constant.ExtraBranch.USER_THUMB_IMAGE).getValue().toString();
                usernameTV.setText(name);
                statuseTV.setText(statuse);
                Picasso.get().load(image).placeholder(R.drawable.user).into(imageView);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

        changeStatuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_statuse = statuseTV.getText().toString().trim();
                Intent intent = new Intent(AccountSettingsActivity.this, StatusActivity.class);
                intent.putExtra(Constant.ExtraBranch.USER_STATUSE, old_statuse);
                startActivity(intent);
            }
        });
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null){
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filrpath = storageReference.child(user_id + ".jpg");
                filrpath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AccountSettingsActivity.this,
                                    "Saving your profile image to Firebase", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            getDatabaseReference.child(Constant.ExtraBranch.USER_IMAGE).setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(AccountSettingsActivity.this,
                                                    "Image uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(AccountSettingsActivity.this,
                                    "Error Saving", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}