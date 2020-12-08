package club.nsuer.nsuer;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class BuySellCreate extends AppCompatActivity {


    private ImageView img;
    private String imgPath;
    private SessionManager session;
    private String username;
    private String titlez;
    private String uid;
    private String memberID;
    private String picture = "no";
    private String pricez;
    private int categoryz;
    private String descriptionz;
    private String imgURLz;
    private String timeStampz;
    private String msgID = "0";
    private String pictureNew = "0";
    private String uploadURL = "";
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 8888;
    private EditText titleInput;
    private EditText priceInput;
    private EditText descriptionInput;
    private Spinner categorySpinner;
    private Context context;
    private int currentRadio = -1;
    private File compressedImageFile;
    private Boolean isImageSelected = false;
    private boolean isEdit = false;
    private boolean isEditImageSelected = false;


    private void openImage() {


        if (ContextCompat.checkSelfPermission(BuySellCreate.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BuySellCreate.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BuySellCreate.this,
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


    private void openSelector() {

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

        setContentView(R.layout.buysell_create_activity);


        context = this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(findViewById(R.id.mainBar), "elevation", 0));
        findViewById(R.id.mainBar).setStateListAnimator(stateListAnimator);


        String url = getIntent().getStringExtra("URL");
        String title = "Create an ad";
        setTitle(title);


        ImageView dp = (ImageView) findViewById(R.id.profilePicEdit);


        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            MainActivity.getInstance().logoutUser();
        }


        username = session.getName();
        uid = session.getUid();
        memberID = session.getMemberID();


        String destinationPaths = getFilesDir().getPath() + File.separator + "images";


        ImageView dpBtn = findViewById(R.id.dpEditbutton);

        img = findViewById(R.id.image);

        dpBtn.bringToFront();


        titleInput = findViewById(R.id.title);
        priceInput = findViewById(R.id.price);
        descriptionInput = findViewById(R.id.description);

        categorySpinner = findViewById(R.id.category);


        final RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);


                if (index == 0)
                    priceInput.setText("FREE");
                else if (index == 1)
                    priceInput.setText("EXCHANGE");

            }
        });

        priceInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                group.clearCheck();


            }
        });

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


        Intent intent = getIntent();


        if (intent.hasExtra("title")) {

            isEdit = true;


            titlez = intent.getStringExtra("title");
            pricez = intent.getStringExtra("price");
            categoryz = intent.getIntExtra("category", 0);
            descriptionz = intent.getStringExtra("description");
            imgURLz = intent.getStringExtra("image");
            timeStampz = intent.getStringExtra("timestamp");

            msgID = intent.getStringExtra("msgID");

            titleInput.setText(titlez);
            priceInput.setText(pricez);
            descriptionInput.setText(descriptionz);

            categorySpinner.setSelection(categoryz);


            Picasso.get()
                    .load(imgURLz)
                    .fit()
                    .centerCrop(Gravity.TOP)
                    .into(img);


        }


        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);


        FloatingActionButton btn = findViewById(R.id.submitButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (!Utils.isNetworkAvailable(BuySellCreate.this)) {
                    Toast.makeText(BuySellCreate.this, "Internet connection required.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!checkFieldsRequired(viewGroup)) {

                    return;

                }


                if (!isEdit) {
                    if (!isImageSelected) {
                        Toast.makeText(BuySellCreate.this, "Select an image for the ad", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                final ProgressDialog dialog = ProgressDialog.show(BuySellCreate.this, "",
                        "Posting ad...", true);
                dialog.show();

                String titleS = titleInput.getText().toString();
                String descriptionS = descriptionInput.getText().toString();
                String priceS = priceInput.getText().toString();

                String categoryS = String.valueOf(categorySpinner.getSelectedItemPosition());

                HashMap<String, String> parametters = new HashMap<String, String>();

                parametters.put("uid", uid);
                parametters.put("title", titleS);
                parametters.put("price", priceS);
                parametters.put("description", descriptionS);
                parametters.put("category", categoryS);
                parametters.put("msgID", msgID);

                if (isEdit) {
                    parametters.put("doUpdate", "1");

                } else
                    parametters.put("doUpdate", "0");

                if (isEditImageSelected) {
                    parametters.put("doUpdateImage", "1");

                } else
                    parametters.put("doUpdateImage", "0");


                JSONParser parser = new JSONParser("https://nsuer.club/apps/buy-sell/create-ad.php", "GET", parametters);


                parser.setListener(new JSONParser.ParserListener() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        dialog.dismiss();

                        String timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000L);
                        try {

                            timeStamp = result.getString("msg");
                        } catch (Exception e) {

                            Log.e("JSON", e.toString());

                        }


                        uploadURL = "https://nsuer.club/apps/buy-sell/upload.php?uid=" + uid + "&time=" + timeStamp;


                        if (!isEdit)
                            uploadImage();
                        else if (isEdit && isEditImageSelected) {

                            uploadImage();
                        } else {

                            onBackPressed();

                        }


                        //Toast.makeText(context,"Profile updated!",Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onFailure() {

                    }
                });


                parser.execute();


                //  finishNow();

            }
        });


    }


    public boolean checkFieldsRequired(ViewGroup viewGroup) {


        boolean bool = true;

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                checkFieldsRequired((ViewGroup) view);
            else if (view instanceof EditText) {
                EditText edittext = (EditText) view;
                if (edittext.getText().toString().trim().equals("")) {
                    edittext.setError("Required!");
                    bool = false;
                }
            }
        }

        return bool;
    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }


    private void finishNow() {

        Intent intent = new Intent(BuySellCreate.this,
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
            Intent intent = new Intent(BuySellCreate.this, SettingsActivity.class);
            intent.putExtra("FROM_ACTIVITY", "EDIT_PROFILE");
            startActivity(intent);
        } else if (id == R.id.action_logout) {

            MainActivity main = MainActivity.getInstance();

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
                    } else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                            if (TextUtils.isEmpty(rawExternalStorage)) {
                                rv.add("/storage/sdcard0");
                            } else {
                                rv.add(rawExternalStorage);
                            }
                        } else {
                            String rawUserId;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                rawUserId = "";
                            } else {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                } catch (NumberFormatException ignored) {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if (TextUtils.isEmpty(rawUserId)) {
                                rv.add(rawEmulatedStorageTarget);
                            } else {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);
                        for (int i = 0; i < temp.length; i++) {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if (tempf.exists()) {
                                imgPath = temp[i] + "/" + split[1];
                            }
                        }
                    }
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};
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
                } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
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
                    String[] selectionArgs = new String[]{split[1]};

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};

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
                } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                    isImageFromGoogleDrive = true;
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Cursor cursor = null;
                String column = "_data";
                String[] projection = {column};

                try {
                    cursor = this.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        imgPath = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imgPath = uri.getPath();
            }

            if (isImageFromGoogleDrive) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {


                File f = new File(imgPath);


                Bitmap gbit = BitmapFactory.decodeResource(getResources(), R.drawable.default_user_pic);
                ;


                Compressor compressor = new Compressor(context);
                compressor.setMaxWidth(700);
                compressor.setQuality(80);
                compressor.setMaxHeight(1200);


                compressedImageFile = f;

                try {
                    compressedImageFile = compressor.compressToFile(f);

                    gbit = compressor.compressToBitmap(f);

                } catch (Exception e) {

                }


                isImageSelected = true;

                isEditImageSelected = true;


                Picasso.get()
                        .load(compressedImageFile)
                        .fit()
                        .centerCrop(Gravity.TOP)
                        .into(img);


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {

        final ProgressDialog dialog = ProgressDialog.show(BuySellCreate.this, "",
                "Uploading photo...", true);

        UploadFileAsync parser = new UploadFileAsync(compressedImageFile, imgPath, uploadURL);

        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {


            }


            @Override
            public void onFailure() {

                dialog.dismiss();

                onBackPressed();

                Toast.makeText(context, "You ad is posted, it might take few hours to get approved", Toast.LENGTH_LONG).show();

            }
        });


        parser.execute();

    }


}

