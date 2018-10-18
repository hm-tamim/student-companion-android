package club.nsuer.nsuer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NoticeBrowser extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_notice_browser);


        String url = getIntent().getStringExtra("URL");
        String type = getIntent().getStringExtra("TYPE");


        String title  = type.substring(0, 1).toUpperCase() + type.substring(1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WebView webView = (WebView) findViewById(R.id.webView);



        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);






        setTitle(title);

        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("pdf")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    onBackPressed();
                }
                return true;
            }

//
//                                     @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                         super.onPageStarted(view, url, favicon);
//                                         progressBar.setVisibility(ProgressBar.VISIBLE);
//                                         webView.setVisibility(View.INVISIBLE);
//                                     }

            @Override public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                progressBar.setVisibility(ProgressBar.GONE);
                webView.setVisibility(View.VISIBLE);
                // isWebViewLoadingFirstPage=false;


                StringBuilder extraStyles = new StringBuilder();
                extraStyles.append("javascript:(function extra(){");
                if(1==1){
                    extraStyles.append(
                            "document.getElementsByClassName('header-middle')[0].innerHTML = '';document.getElementsByClassName('footer')[0].outerHTML = ''"
                    );
                }
                extraStyles.append("})();");

                webView.loadUrl(extraStyles.toString());



            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//
//                document.getElementsByTagName('html')[0].innerHTML+='<style>.header-middle{display: none}</style>


            }


        });








        webView.loadUrl(url);





        // webView.setHorizontalScrollBarEnabled(false);



//        Button btnLogin;
//
//        // Login button Click Event
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//
//                    // Prompt user to enter credentials
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter the credentials!", Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//
//        });

    }


    @Override
    public void onBackPressed(){

            finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout){

            MainActivity  main = MainActivity.getInstance();

                    main.logoutUser();
        }


        return super.onOptionsItemSelected(item);
    }








}
