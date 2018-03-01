package com.madarabia;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import am.appwise.components.ni.NoInternetDialog;

public class MainActivity extends AppCompatActivity
{

    private static final String PAGE_URL = "http://madarabia.com";
    public ProgressBar mProgress;
    private Context context;
    NoInternetDialog noInternetDialog;
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        noInternetDialog = new NoInternetDialog.Builder(context).build();

        mWebView =  findViewById(R.id.webview);
        mProgress = findViewById(R.id.progressbar);

        mWebView.clearHistory();
        mWebView.clearCache(true);



        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        //improve webview performance
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);

        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        mWebView.onCheckIsTextEditor();


        mWebView.setWebChromeClient(new WebChromeClient() {

            // this will be called on page loading progress

            @Override

            public void onProgressChanged(WebView view, int newProgress) {

                //super.onProgressChanged(view, newProgress);


                mProgress.setProgress(newProgress);

                // hide the progress bar if the loading is complete

                if (newProgress == 100) {

  				/* call after laoding splash.html  */
                    mWebView.loadUrl("javascript:_fully_loaded()");
                    mWebView.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);

                }

            }




        });

        mWebView.setWebViewClient(new MyCustomWebViewClient());

        /* load splash screen */
        mWebView.loadUrl(PAGE_URL);

        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus())
                        {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        noInternetDialog.onDestroy();

    }

    private class MyCustomWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {


            mWebView.loadUrl("javascript:document.getElementById(\"firstname\").focus();");

            //hide loading image
            findViewById(R.id.imageSplash).setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            //show webview
            mWebView.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            //If there is history, then the canGoBack method will return ‘true’//
            return true;
        }

        //If the button that’s been pressed wasn’t the ‘Back’ button, or there’s currently no
        //WebView history, then the system should resort to its default behavior and return
        //the user to the previous Activity//
        return super.onKeyDown(keyCode, event);


    }




}


