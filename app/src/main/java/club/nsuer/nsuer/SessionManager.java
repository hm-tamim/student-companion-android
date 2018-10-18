package club.nsuer.nsuer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "NSUerClubLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_CGPA = "willShowCgpa";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    public void setShowCgpa(boolean willShowCgpa) {

        editor.putBoolean(KEY_IS_CGPA, willShowCgpa);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean willShowCgpa(){

        return pref.getBoolean(KEY_IS_CGPA, true);
    }

    public void setShowWeather(boolean willShowWeather) {

        editor.putBoolean("willShowWeather", willShowWeather);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean willShowWeather(){

        return pref.getBoolean("willShowWeather", true);
    }

}