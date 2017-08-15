package com.sourcey.materiallogindemo.utility;

/**
 * Created by CoderGuru on 14-08-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "loginInfo";

    private static final String isloggedIn = "IsLoggedIn";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoggedIn(int sessionId) {
        editor.putInt(isloggedIn, sessionId);
        editor.commit();
    }
    public void clearLoggedIn(){
        editor.putInt(isloggedIn, 0);
        editor.commit();
    }

    public boolean isLoggedIn() {
        Log.e("login preference value",Integer.toString(pref.getInt(isloggedIn,0)));
        return !(pref.getInt(isloggedIn, 0)==0);
    }

}