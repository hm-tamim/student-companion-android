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
    private static final String PREF_NAME = "NSUerAppLogin2";

    private static final String KEY_IS_LOGGEDIN = "isloggedIn";
    private static final String KEY_IS_CGPA = "willShowCgpa";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    // user info


    public String getName() {

        return pref.getString("name", "0.0");
    }

    public void setName(String name) {

        editor.putString("name", name);
        editor.commit();

    }

    public boolean isPremium() {

        return pref.getBoolean("premium", false);
    }

    public void setPremium(boolean premium) {
        editor.putBoolean("premium", premium);
        editor.commit();
    }


    public boolean showAdvisingTools() {

        return pref.getBoolean("AdvisingTools", false);
    }

    public void setAdvisingTools(boolean premium) {
        editor.putBoolean("AdvisingTools", premium);
        editor.commit();
    }

    public String getExpireDate() {

        return pref.getString("getExpireDate", "0");
    }

    public void setExpireDate(String name) {

        editor.putString("getExpireDate", name);
        editor.commit();

    }


    public String getEmail() {

        return pref.getString("email", "name@northsouth@edu");
    }

    public void setEmail(String name) {

        editor.putString("email", name);
        editor.commit();

    }


    public String getMemberID() {

        return pref.getString("memberID", "0.0");
    }

    public void setMemberID(String name) {

        editor.putString("memberID", name);
        editor.commit();

    }

    public String getUid() {

        return pref.getString("uid", "0.0");
    }

    public void setUid(String name) {

        editor.putString("uid", name);
        editor.commit();

    }

    public String getPicture() {

        return pref.getString("picture", "0");
    }

    public void setPicture(String name) {

        editor.putString("picture", name);
        editor.commit();

    }

    public String getGender() {

        return pref.getString("gender", "male");
    }

    public void setGender(String gender) {

        editor.putString("gender", gender);
        editor.commit();

    }

    public String getCgpa() {

        return pref.getString("cgpa", "0.0");

    }

    public void setCgpa(String cgpa) {
        editor.putString("cgpa", cgpa);
        editor.commit();
    }

    public String getCredit() {

        return pref.getString("credit", "0.0");
    }

    public void setCredit(String credit) {

        editor.putString("credit", credit);
        editor.commit();
    }

    public String getDepartment() {

        return pref.getString("department", "0");
    }

    public void setDepartment(String department) {

        editor.putString("department", department);
        editor.commit();
    }

    public String getSemester() {

        return pref.getString("semester", "1");
    }

    public void setSemester(String semester) {

        editor.putString("semester", semester);
        editor.commit();
    }

    public String getPhone() {

        return pref.getString("phone", "");
    }

    public void setPhone(String phone) {

        editor.putString("phone", phone);
        editor.commit();
    }

    public String getAddress() {

        return pref.getString("address", "");
    }

    public void setAddress(String address) {

        editor.putString("address", address);
        editor.commit();
    }

    public String getBloodGroup() {

        return pref.getString("bloodgroup", "-1");
    }

    public void setBloodGroup(String bloodgroup) {

        editor.putString("bloodgroup", bloodgroup);
        editor.commit();
    }


    // settings

    public void setShowCgpa(boolean willShowCgpa) {

        editor.putBoolean(KEY_IS_CGPA, willShowCgpa);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean willShowCgpa() {

        return pref.getBoolean(KEY_IS_CGPA, true);
    }

    public void setShowWeather(boolean willShowWeather) {

        editor.putBoolean("willShowWeather", willShowWeather);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean willShowWeather() {

        return pref.getBoolean("willShowWeather", true);
    }

}