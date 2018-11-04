package club.nsuer.nsuer;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileAsync extends AsyncTask<String, Void, String> {




    private JSONParser.ParserListener listener;

    public interface ParserListener {
        void onSuccess(JSONObject result);
        void onFailure();
    }


    public void setListener(JSONParser.ParserListener listener) {
        this.listener = listener;
    }


    String sourceFileUri;
    File sourceFile;
    String upLoadServerUri;


    public UploadFileAsync(File sourceFile, String sourceFileUri, String upLoadServerUri) {
        this.sourceFile = sourceFile;
        this.sourceFileUri = sourceFileUri;
        this.upLoadServerUri = upLoadServerUri;
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            if (sourceFile.isFile()) {

                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);





                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\""  + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();


                    Log.d("TTTT", serverResponseMessage);


                    Log.d("TTTTA", sourceFileUri);



                    if (serverResponseCode == 200) {

                        // messageText.setText(msg);
                        //Toast.makeText(EditProfile.this, "Profile Picture Uploaded.", Toast.LENGTH_SHORT).show();

                        // recursiveDelete(mDirectory1);
                        onPostExecute("uploaded");

                    } else {

                        onPostExecute("failed");

                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {

        listener.onFailure();

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }




}



