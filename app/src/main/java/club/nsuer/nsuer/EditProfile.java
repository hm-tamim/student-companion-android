package club.nsuer.nsuer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class EditProfile extends AppCompatActivity {


    private ImageView img;
    private String imgPath;
    private SessionManager session;


    private String name = "Username";
    private String email;
    private String gender;
    private String uid;
    private String memberID;
    private String picture = "no";
    private String cgpa = "";
    private String credit = "";
    private String semester = "0";
    private String dept = "0";

    private String pictureNew = "0";

    private String uploadURL = "";

    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 8888;

    private EditText nameInput;
    private EditText cgpaInput;
    private EditText creditInput;
    private Spinner deptInput;
    private Spinner semesterInput;

    private Context context;



    private void openImage(){



        if (ContextCompat.checkSelfPermission(EditProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditProfile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            openSelector();



        }



    }



    private void openSelector(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }


    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openSelector();

                return;



            } else {
                // User refused to grant permission.
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);


        context = this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 0));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);


        String url = getIntent().getStringExtra("URL");
        String title  = "Edit Profile";
        setTitle(title);




        ImageView dp = (ImageView) findViewById(R.id.profilePicEdit);



        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            MainActivity.getInstance().logoutUser();
        }

        name = session.getName();
        uid = session.getUid();
        email = session.getEmail();
        gender = session.getGender();
        memberID = session.getMemberID();
        picture = session.getPicture();

        cgpa = session.getCgpa();
        credit = session.getCredit();
        semester = session.getSemester();
        dept = session.getDepartment();




        FloatingActionButton settingsFB = findViewById(R.id.settingsButton);

        settingsFB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, SettingsActivity.class);
                intent.putExtra("FROM_ACTIVITY", "EDIT_PROFILE");
                startActivity(intent);
            }
        });








        String destinationPaths = getFilesDir().getPath() + File.separator + "images";

        File dpp = new File(destinationPaths, "tamim.jpg");



        if (picture != null && !picture.equals("0")){

            if(!dpp.exists()) {


                Picasso.get()
                        .load("https://nsuer.club/images/profile_picture/" + picture)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(dp);
            } else {
                Picasso.get()
                        .load(dpp)
                        .fit()
                        .transform(new CircleTransform())
                        .centerCrop(Gravity.TOP)
                        .into(dp);
            }
        }



        // Toast.makeText(getApplicationContext(), dept, Toast.LENGTH_LONG).show();





        ImageView dpBtn = findViewById(R.id.dpEditbutton);

        img = findViewById(R.id.profilePicEdit);

        dpBtn.bringToFront();


        nameInput = findViewById(R.id.editProfileName);
        cgpaInput = findViewById(R.id.editProfileCgpa);
        creditInput = findViewById(R.id.editProfileCredits);

        deptInput = findViewById(R.id.editProfileDept);

        semesterInput = findViewById(R.id.editProfileSemester);


        if(picture.equals("0"))
            pictureNew = "0";
        else
            pictureNew = picture;



        if(!name.equals(""))
            nameInput.setText(name);

        if(!cgpa.equals(""))
            cgpaInput.setText(cgpa);

        if(!credit.equals(""))
            creditInput.setText(credit);

        if(!dept.equals("")) {

            int indexd = Integer.parseInt(dept);
            deptInput.setSelection(indexd);
        }

        if(!semester.equals("")) {

            int indexs = Integer.parseInt(semester);
            semesterInput.setSelection(indexs);
        }



        //String selectedVal = getResources().getStringArray(R.array.values)[spinner.getSelectedItemPosition()];




        dpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openImage();



            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                openImage();

            }
        });

        uploadURL = "https://nsuer.club/apps/edit-profile/upload.php?uid="+ uid;




        FloatingActionButton btn = findViewById(R.id.editProfileButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if(!Utils.isNetworkAvailable(EditProfile.this)) {
                    Toast.makeText(EditProfile.this, "Internet connection required.", Toast.LENGTH_SHORT).show();
                    return;
                }



                String nameNew = nameInput.getText().toString();
                String cgpaNew = cgpaInput.getText().toString();
                String creditNew = creditInput.getText().toString();

                String deptNew = String.valueOf(deptInput.getSelectedItemPosition());
                String semesterNew = String.valueOf(semesterInput.getSelectedItemPosition());

                if(cgpaNew.equals(""))
                    cgpaNew = "0";
                if(creditNew.equals(""))
                    creditNew = "0";
                if(semesterNew.equals(""))
                    semesterNew = "0";



                double creditDouble = Double.valueOf(creditNew);

                int creditNewInt = (int) creditDouble;

                creditNew = String.valueOf(creditNewInt);



                session.setName(nameNew);
                session.setEmail(email);
                session.setUid(uid);
                session.setMemberID(memberID);
                session.setGender(gender);
                session.setPicture(pictureNew);
                session.setCredit(creditNew);
                session.setCgpa(cgpaNew);
                session.setDepartment(deptNew);
                session.setSemester(semesterNew);



                HashMap<String, String> parametters = new HashMap<String, String>();

                parametters.put("uid", uid);
                parametters.put("name", nameNew);
                parametters.put("cgpa", cgpaNew);
                parametters.put("credit", creditNew);
                parametters.put("semester", semesterNew);
                parametters.put("dept", deptNew);

                JSONParser parser = new JSONParser("https://nsuer.club/apps/edit-profile/edit.php", "POST", parametters);


                parser.setListener(new JSONParser.ParserListener() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        Toast.makeText(context,"Profile updated!",Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onFailure() {

                    }
                });


                parser.execute();



                finishNow();

            }
        });



    }


    public boolean checkFieldsRequired(ViewGroup viewGroup){

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                checkFieldsRequired((ViewGroup) view);
            else if (view instanceof EditText) {
                EditText edittext = (EditText) view;
                if (edittext.getText().toString().trim().equals("")) {
                    edittext.setError("Required!");
                }
            }
        }

        return false;
    }


    @Override
    public void onBackPressed(){

        finishNow();

    }


    private void finishNow(){

        Intent intent = new Intent(EditProfile.this,
                MainActivity.class);
        startActivity(intent);

        finish();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                Intent intent = new Intent(EditProfile.this, SettingsActivity.class);
                intent.putExtra("FROM_ACTIVITY", "EDIT_PROFILE");
                startActivity(intent);
        } else if (id == R.id.action_logout){

            MainActivity  main = MainActivity.getInstance();

            main.logoutUser();
        }


        return super.onOptionsItemSelected(item);
    }












    @TargetApi(19)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getData() != null && resultCode == RESULT_OK) {
            boolean isImageFromGoogleDrive = false;
            Uri uri = data.getData();

            if (DocumentsContract.isDocumentUri(this, uri)) {
                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        imgPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if(TextUtils.isEmpty(rawEmulatedStorageTarget))
                        {
                            if(TextUtils.isEmpty(rawExternalStorage))
                            {
                                rv.add("/storage/sdcard0");
                            }
                            else
                            {
                                rv.add(rawExternalStorage);
                            }
                        }
                        else
                        {
                            String rawUserId;
                            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
                            {
                                rawUserId = "";
                            }
                            else
                            {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try
                                {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                }
                                catch(NumberFormatException ignored)
                                {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if(TextUtils.isEmpty(rawUserId))
                            {
                                rv.add(rawEmulatedStorageTarget);
                            }
                            else
                            {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
                        {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);
                        for (int i = 0; i < temp.length; i++)   {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if(tempf.exists()) {
                                imgPath = temp[i] + "/" + split[1];
                            }
                        }
                    }
                }
                else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId( Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = { column };
                    try {
                        cursor = this.getContentResolver().query(contentUri, projection, null, null,
                                null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            imgPath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
                else if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{ split[1] };

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = { column };

                    try {
                        cursor = this.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            imgPath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
                else if("com.google.android.apps.docs.storage".equals(uri.getAuthority()))   {
                    isImageFromGoogleDrive = true;
                }
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Cursor cursor = null;
                String column = "_data";
                String[] projection = { column };

                try {
                    cursor = this.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        imgPath = cursor.getString(column_index);
                    }
                }
                finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imgPath = uri.getPath();
            }

            if(isImageFromGoogleDrive)  {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else    {
                File f = new File(imgPath);


                Bitmap gbit =  BitmapFactory.decodeResource(getResources(), R.drawable.default_user_pic);;

                File compressedImageFile = f;

                try {
                      compressedImageFile = new Compressor(this).compressToFile(f);

                      gbit = new Compressor(this).compressToBitmap(f);

                }catch (Exception e){

                }






                String destinationPath = getFilesDir().getPath() + File.separator + "images";

                FileOutputStream fileOutputStream = null;
                File file = new File(destinationPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File myFile = new File(destinationPath, "tamim.jpg");


                if(!myFile.exists()) {

                    try {

                        myFile.createNewFile();

                    } catch (Exception e){}
                }

                try {

                    FileOutputStream out = new FileOutputStream(myFile,false);
                    gbit.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                Picasso.get()
                        .load(compressedImageFile)
                        .transform(new CircleTransform())
                        .fit()
                        .centerCrop(Gravity.TOP)
                        .into(img);







                UploadFileAsync parser = new UploadFileAsync(compressedImageFile, imgPath, uploadURL);

                pictureNew = memberID+".jpeg";


                Picasso.get().invalidate(myFile);

                parser.setListener(new JSONParser.ParserListener() {
                    @Override
                    public void onSuccess(JSONObject result) {




                    }


                    @Override
                    public void onFailure() {

                    }
                });


            parser.execute();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }






}
