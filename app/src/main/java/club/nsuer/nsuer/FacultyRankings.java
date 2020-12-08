package club.nsuer.nsuer;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class FacultyRankings extends Fragment implements CoursesList {


    private ProgressDialog progressDialog;
    private WebView webView;
    private SearchView searchView;
    private MainActivity main;
    private SimpleCursorAdapter mAdapter;
    private View v;
    private String uid = "uids";


    public FacultyRankings() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        main = (MainActivity) getActivity();

        uid = main.getUid();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        final String[] from = new String[]{"courseName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View row = super.getView(position, convertView, parent);
                if (position % 2 == 0)
                    row.setBackgroundResource(android.R.color.background_light);
                else
                    row.setBackgroundResource(android.R.color.background_light);
                return row;
            }
        };


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
                try {


                    main.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                } catch (Exception e){

                    Log.e("Faculty Rankings", e.toString());

                }
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageFinished(WebView view, String url) {


                try {
                    main.findViewById(R.id.progressBar).setVisibility(View.GONE);
                } catch (Exception e){

                }

            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.search_button, menu);


        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();


        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(true);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String suggestion = cursor.getString(0);//2 is the index of col containing suggestion name.
                searchView.setQuery(SUGGESTIONS[Integer.parseInt(suggestion)], true);//setting suggestion
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startWebView(webView, "https://nsuer.club/apps/faculty-rankings.php?uid="+uid+"&query=" + query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                populateAdapter(newText);
                return false;

            }
        });

    }

    public void resetFragment() {


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        main.resetShadow();

        main.setActionBarTitle("Faculty Rankings");


        v = inflater.inflate(R.layout.faculty_rankings, container, false);
        webView = (WebView) v.findViewById(R.id.faculty_rankings_webview);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.rankingBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetFragment();

            }
        });




        startWebView(webView, "https://nsuer.club/apps/faculty-rankings.php?uid="+uid);

        return v;
    }


    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "courseName"});

        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }



















    /*
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();




        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);


        webView.loadUrl("https://nsuer.club/apps/faculty-rankings.php");

        webView.setWebViewClient(new WebViewClient() {



            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
            public void onPageFinished(WebView view, String url) {


                CookieSyncManager.getInstance().sync();

            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());


        */

}
