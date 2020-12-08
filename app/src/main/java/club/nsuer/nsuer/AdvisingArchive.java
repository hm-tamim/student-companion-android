package club.nsuer.nsuer;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class AdvisingArchive extends Fragment {

    private ProgressDialog progressDialog;
    private WebView webView;
    private SearchView searchView;
    private MainActivity main;
    private SimpleCursorAdapter mAdapter;
    private View v;

    private String uid = "uids";

    public AdvisingArchive() {
        // Required empty public constructor
    }

    public AdvisingArchive(String uid) {

        this.uid = uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        main = (MainActivity) getActivity();

    }


    private void startWebView(WebView webView, String url) {


        // progressDialog.show();


        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                main.findViewById(R.id.advisingProgressBar).setVisibility(View.VISIBLE);
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageFinished(WebView view, String url) {


                try {
                    main.findViewById(R.id.advisingProgressBar).setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    public void resetFragment() {


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        main.resetShadow();

        main.setActionBarTitle("Advising Archive");


        v = inflater.inflate(R.layout.advising_assistant, container, false);
        webView = (WebView) v.findViewById(R.id.advising_assistant_webview);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.AdvisingBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetFragment();

            }
        });


        startWebView(webView, "https://nsuer.club/advising-archive/index-app.php?uid=" + uid);

        return v;
    }

}
