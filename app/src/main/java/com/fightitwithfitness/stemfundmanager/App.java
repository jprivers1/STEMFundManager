package com.fightitwithfitness.stemfundmanager;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by jpriv on 9/27/2017.
 */

public class App extends Application {
    public static App sInstance;

    //App instance
    public static App get() {
        if(sInstance == null){
            sInstance = new App();
        }
        return sInstance;
    }

    ProgressDialog dialog;

    //Show Loading dialog
    public void initLoadingDialog(Context context) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.setMessage(context.getResources().getString(R.string.loading_dialog));
            dialog.show();
        }
    }

    //Dismiss loading dialog
    public void stopLoadingDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = null;
    }

    //Check network connectivity
    public static boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}