package club.nsuer.nsuer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkForget;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private MainActivity main;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkForget = (Button) findViewById(R.id.btnLinkForget);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

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
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
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
     * */
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
                        // Create login session
                        session.setLogin(true);

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


                        // Inserting row in users table
                        db.addUser(name, email, uid, memberID, gender, picture, cgpa, credit, semester, dept);

                        // Launch main activity


                        syncCourse(memberID, LoginActivity.this);









                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
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

    private void syncCourse(String memID, final Context context){





        HashMap<String, String> parametters = new HashMap<String, String>();

        parametters.put("memID", memID);


        JSONParser parser = new JSONParser("https://nsuer.club/app/courses/get-all-courses.php", "GET", parametters);



        parser.setListener(new JSONParser.ParserListener() {
            @Override
            public void onSuccess(JSONObject result) {

                CoursesDatabase db = Room.databaseBuilder(context,
                        CoursesDatabase.class, "courses").allowMainThreadQueries().build();

                BooksDatabase dbBooks = Room.databaseBuilder(context,
                        BooksDatabase.class, "books").allowMainThreadQueries().build();
                FacultiesDatabase dbFaculties = Room.databaseBuilder(context,
                        FacultiesDatabase.class, "faculties").allowMainThreadQueries().build();




                try {
                    JSONArray obj = result.getJSONArray("dataArray");

                    String firstCourse = null;
                    String firstSection = null;


                    for (int i = 0; i < obj.length(); i++) {


                        JSONObject data = obj.getJSONObject(i);

                        // Add books

                        if(i>0) {

                            if (data.has("books")) {

                                int id = data.getInt("id");
                                String course = data.getString("course");
                                String books = data.getString("books");

                                BooksEntity arrData = new BooksEntity();

                                arrData.setCourse(course);
                                arrData.setBooks(books);
                                dbBooks.booksDao().insertAll(arrData);

                                continue;
                            }
                        }



                        // Add faculties

                        if(i>0) {

                            if (data.has("initial")) {



                                int id = data.getInt("id");


                                String name = data.getString("name");
                                String rank = data.getString("rank");

                                String image = data.getString("image");

                                String initial  = trim(data.getString("initial"));

                                String course = db.coursesDao().getCourseByFaculty(initial);

                                String section = db.coursesDao().getSectionByFaculty(trim(initial));

                                String email = data.getString("email");
                                String phone = data.getString("phone");
                                String ext = data.getString("ext");

                                String department = data.getString("dept");
                                String office = data.getString("office");
                                String url = data.getString("url");




                                FacultiesEntity arrData = new FacultiesEntity();

                                arrData.setName(name);
                                arrData.setRank(rank);
                                arrData.setImage(image);
                                arrData.setInitial(initial);
                                arrData.setCourse(course);
                                arrData.setSection(section);
                                arrData.setEmail(email);
                                arrData.setPhone(phone);
                                arrData.setExt(ext);
                                arrData.setDepartment(department);
                                arrData.setOffice(office);
                                arrData.setUrl(url);

                                dbFaculties.facultiesDao().insertAll(arrData);
                                continue;
                            }
                        }



                        int id = data.getInt("id");
                        String course = data.getString("course");
                        String section = data.getString("section");
                        String faculty = data.getString("faculty");


                        if(i==0)
                            firstCourse = course;

                        if(i==0)
                            firstSection = section;


                        String startTime = data.getString("startTime");
                        startTime = timeConverter(startTime,24);

                        String endTime = data.getString("endTime");
                        endTime = timeConverter(endTime,24);

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



    public String timeConverter(String time, int type){

        try {
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            if(type == 24) {
                String t24 = date24Format.format(date12Format.parse(time));
                return t24;
            }
            else {
                String t12 = date12Format.format(date24Format.parse(time));
                return t12;
            }
        } catch (final ParseException e) {
            e.printStackTrace();

            return "nothing";
        }

    }

}