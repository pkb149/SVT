package com.pkb149.SVT.LorryOwnerFlow;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkb149.SVT.LoginActivity;
import com.pkb149.SVT.MainActivity;
import com.pkb149.SVT.R;
import com.pkb149.SVT.utility.PrefManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class AddTruck extends AppCompatActivity {
    int REQUEST_IMAGE_CAPTURE=100;
    int PICK_IMAGE=101;
    private Uri imageToUploadUri;
    PrefManager prefManager;
    android.support.v7.app.AlertDialog ad;
    @Bind(R.id.truck_registration_number_et) EditText _truckRegistrationNumber;
    @Bind(R.id.truck_registered_owner_name_et) EditText _truckRegisteredOwnerName;
    @Bind(R.id.driver_name_et) EditText _driverName;
    @Bind(R.id.driver_mobile_number_et) EditText _driverMobileNumber;
    @Bind(R.id.upload_rc_button) Button _uploadRC;
    @Bind(R.id.upload_dl_button) Button _uploadDL;
    @Bind(R.id.permit_tv) TextView _permitTV;
    @Bind(R.id.submit_for_verification_button) Button _submit;
    final String[] select_permit = {
            "National Permit", "Bihar", "Jharkhand", "Karnataka",
            "Andhra Pradesh", "Telangana"};
    static boolean[] checkedItems = {false, false, false, false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck);
        prefManager = new PrefManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        _permitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTruck.this,R.style.MyDialogTheme);
                builder.setTitle("Permitted States:");
                //builder.setView(npView);
                builder.setMultiChoiceItems(select_permit, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which]=isChecked;
                        if((which==0)&&(isChecked==true)){
                            ListView v = ((AlertDialog)dialog).getListView();
                            int i = 0;
                            while(i < select_permit.length) {
                                v.setItemChecked(i, true);
                                checkedItems[i]=true;
                                i++;
                            }
                        }
                        else if((which==0)&&(isChecked==false)){
                            ListView v = ((AlertDialog)dialog).getListView();
                            int i = 0;
                            while(i < select_permit.length) {
                                v.setItemChecked(i, false);
                                checkedItems[i]=false;
                                i++;
                            }
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String states=null;
                        int i=0;
                        while(i<checkedItems.length){
                            if(checkedItems[i]){
                                if(states==null){
                                    states=select_permit[i];
                                    if(i==0){
                                        break;
                                    }
                                }
                                else {
                                    Log.e(select_permit[i],"");
                                    states=states.concat(", " + select_permit[i]);
                                }
                            }
                            i++;
                        }
                        if(states!=null){
                            _permitTV.setText(states);
                        }
                        else
                        {
                            _permitTV.setText("select permit");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                final AlertDialog ad=builder.create();
                ad.getWindow().setBackgroundDrawableResource(R.color.primary_dark);
                builder.show();
                /*Button nbutton=ad.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(getResources().getColor(R.color.white));
                Button pbutton=ad.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(getResources().getColor(R.color.white));*/
            }
        });

        _uploadRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        AddTruck.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View npView = inflater.inflate(R.layout.file_picker_dialog, null);


                npView.findViewById(R.id.camera_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        File f = new File(Environment.getExternalStorageDirectory(), "IMG_"+timeStamp+".jpg");
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        imageToUploadUri = Uri.fromFile(f);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });


                npView.findViewById(R.id.gallery_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        //Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                        if (pickIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(pickIntent, PICK_IMAGE);
                        }
                    }
                });
                npView.findViewById(R.id.file_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("*/*");
                        if (getIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(getIntent, PICK_IMAGE);
                        }

                    }
                });


                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddTruck.this);
                builder.setView(npView);
                ad=builder.create();
                ad.getWindow().setBackgroundDrawableResource(R.color.primary_dark);
                ad.show();
            }
        });

        _uploadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        AddTruck.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View npView = inflater.inflate(R.layout.file_picker_dialog, null);

                npView.findViewById(R.id.camera_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        File f = new File(Environment.getExternalStorageDirectory(), "IMG_"+timeStamp+".jpg");
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        imageToUploadUri = Uri.fromFile(f);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });


                npView.findViewById(R.id.gallery_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        //Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                        if (pickIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(pickIntent, PICK_IMAGE);
                        }
                    }
                });
                npView.findViewById(R.id.file_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("*/*");
                        if (getIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(getIntent, PICK_IMAGE);
                        }

                    }
                });
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddTruck.this);
                builder.setView(npView);
                ad=builder.create();
                ad.getWindow().setBackgroundDrawableResource(R.color.primary_dark);
                ad.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_history);
        item.setVisible(false);
        //item.setTitle("bhak");
        return true;
    }
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_history);
        item.setTitle("bhak");
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            //TODO Log user Out
            prefManager.clearLoggedIn();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return true;
        }
        else if(id==android.R.id.home){
            onBackPressed();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if(imageToUploadUri != null){
                Uri selectedImage = imageToUploadUri;
                Toast.makeText(this,selectedImage.toString(),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            String selectedImage = data.getData().getPath();
            Toast.makeText(this,selectedImage.toString(),Toast.LENGTH_LONG).show();
        }
        if(ad!=null){
            ad.dismiss();
        }
    }
}


