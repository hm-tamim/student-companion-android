package club.nsuer.nsuer;

import android.app.Activity;
import android.app.ProgressDialog;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LoginActivity extends Activity {
    private static final String TAG = "Login";
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkForget;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private MainActivity main;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = getApplicationContext();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkForget = (Button) findViewById(R.id.btnLinkForget);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Session manager
        session = new SessionManager(context);

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(context,
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(context,
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnLinkForget.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Uri uri = Uri.parse("https://nsuer.club/user/reset.php");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        String memberID = user.getString("memberID");

                        String gender = user.getString("gender");

                        String picture = user.getString("picture");

                        String cgpa = user.getString("cgpa");

                        String credit = user.getString("credit");

                        String semester = user.getString("semester");

                        String dept = user.getString("dept");

                        String bgroup = user.getString("bgroup");


                        String type = user.getString("type");
                        String expire = user.getString("expire");


                        if (type == null || type.equals("")) {
                            session.setPremium(false);
                            session.setExpireDate("0");
                        } else {
                            session.setPremium(true);
                            session.setExpireDate(expire);

                        }


                        if (bgroup == null) {
                            bgroup = "-1";
                        } else {

                            FirebaseMessaging.getInstance().subscribeToTopic("BLOOD." + bgroup);

                        }


                        String phone = user.getString("phone");
                        String address = user.getString("address");
                        // Inserting row in users table

                        session.setName(name);
                        session.setEmail(email);
                        session.setUid(uid);
                        session.setMemberID(memberID);
                        session.setGender(gender);
                        session.setPicture(picture);
                        session.setCredit(credit);
                        session.setCgpa(cgpa);
                        session.setDepartment(dept);
                        session.setSemester(semester);
                        session.setBloodGroup(bgroup);

                        if (phone != null)
                            session.setPhone(phone);
                        if (address != null)
                            session.setAddress(address);

                        // Create login session
                        session.setLogin(true);

                        // Launch main activity


                        Toast.makeText(context, "Syncing account data...", Toast.LENGTH_SHORT).show();

                        syncCourse(memberID, LoginActivity.this);
                        Utils.syncSchedule(memberID, context);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void syncCourse(String memID, final Context context) {


        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("memID", memID);


        JSONParser parser = new JSONParser("https://nsuer.club/apps/sync-data.php", "GET", parametters);


        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                CoursesDatabase db = Room.databaseBuilder(context,
                        CoursesDatabase.class, "courses").allowMainThreadQueries().build();

                db.coursesDao().nukeTable();

                BooksDatabase dbBooks = Room.databaseBuilder(context,
                        BooksDatabase.class, "books").allowMainThreadQueries().build();

                dbBooks.booksDao().nukeTable();

                FacultiesDatabase dbFaculties = Room.databaseBuilder(context,
                        FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();
                dbFaculties.facultiesDao().nukeTable();


                try {
                    JSONArray obj = result.getJSONArray("dataArray");


                    for (int i = 0; i < obj.length(); i++) {


                        JSONObject data = obj.getJSONObject(i);

                        String course = data.getString("course");
                        String section = data.getString("section");
                        String faculty = data.getString("faculty");
                        String startTime = data.getString("startTime");
                        startTime = timeConverter(startTime, 24);
                        String endTime = data.getString("endTime");
                        endTime = timeConverter(endTime, 24);
                        String day = data.getString("day");
                        String room = data.getString("room");

                        CoursesEntity arrData = new CoursesEntity();

                        arrData.setCourse(course);
                        arrData.setFaculty(faculty);
                        arrData.setSection(section);
                        arrData.setStartTime(startTime);
                        arrData.setEndTime(endTime);
                        arrData.setRoom(room);
                        arrData.setDay(day);
                        db.coursesDao().insertAll(arrData);


                        // add books


                        String books = data.getString("books");

                        if (books != null && !books.isEmpty() && !books.equals("null")) {

                            BooksEntity booksEntity = new BooksEntity();
                            booksEntity.setCourse(course);
                            booksEntity.setBooks(books);
                            dbBooks.booksDao().insertAll(booksEntity);
                        }

                        // faculty


                        String name = data.getString("name");

                        if (name != null && !name.isEmpty() && !name.equals("null")) {
                            String rank = data.getString("rank");
                            String image = data.getString("image");
                            String initial = trim(data.getString("initial"));
                            String email = data.getString("email");
                            String phone = data.getString("phone");
                            String ext = data.getString("ext");
                            String department = data.getString("dept");
                            String office = data.getString("office");
                            String url = data.getString("url");

                            FacultiesEntity facultiesEntity = new FacultiesEntity();
                            facultiesEntity.setName(name);
                            facultiesEntity.setRank(rank);
                            facultiesEntity.setImage(image);
                            facultiesEntity.setInitial(initial);
                            facultiesEntity.setCourse(course);
                            facultiesEntity.setSection(section);
                            facultiesEntity.setEmail(email);
                            facultiesEntity.setPhone(phone);
                            facultiesEntity.setExt(ext);
                            facultiesEntity.setDepartment(department);
                            facultiesEntity.setOffice(office);
                            facultiesEntity.setUrl(url);

                            if (dbFaculties.facultiesDao().findByInitial(initial) == null)
                                dbFaculties.facultiesDao().insertAll(facultiesEntity);

                        }


                    }
                } catch (JSONException e) {

                    Log.e("JSON", e.toString());
                }


                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();

            }

            @Override
            public void onFailure() {


            }
        });
        parser.execute();


    }

    public String trim(String str) {
        int len = str.length();
        int st = 0;

        char[] val = str.toCharArray();

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return str.substring(st, len);
    }


    public String timeConverter(String time, int type) {

        try {
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            if (type == 24) {
                String t24 = date24Format.format(date12Format.parse(time));
                return t24;
            } else {
                String t12 = date12Format.format(date24Format.parse(time));
                return t12;
            }
        } catch (final ParseException e) {
            e.printStackTrace();

            return "nothing";
        }

    }

}