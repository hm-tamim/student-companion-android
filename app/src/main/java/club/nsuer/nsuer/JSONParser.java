package club.nsuer.nsuer;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class JSONParser extends AsyncTask<Void, Void, JSONObject> {

    private ParserListener listener;

    public interface ParserListener {
        void onSuccess(JSONObject result);
        void onFailure();
    }

    private HashMap<String, String> params;
    private String method;
    private String url;
    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    JSONArray jAry = null;
    StringBuilder sbParams;
    String paramsString;

    public JSONParser(String url, String method, HashMap<String, String> params) {
        this.url = url;
        this.method = method;
        this.params = params;
        result = new StringBuilder();
    }

    public void setListener(ParserListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        if (method.equals("POST")) {
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setReadTimeout(1000000);
                conn.setConnectTimeout(1500000);
                conn.connect();
                paramsString = sbParams.toString();
                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("GET")) {
            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }

            Log.d("URL",url);
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setConnectTimeout(1500000);
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        /* executed on main thread */
        if (listener != null) {
            if (jsonObject != null) {
                listener.onSuccess(jsonObject);
            } else {
                listener.onFailure();
            }
        }
    }
}