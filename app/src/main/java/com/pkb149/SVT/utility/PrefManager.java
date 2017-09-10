package com.pkb149.SVT.utility;

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

    public void setLoggedIn(String sessionId) {
        editor.putString(isloggedIn, sessionId);
        editor.commit();
    }
    public void clearLoggedIn(){
        editor.putString(isloggedIn, null);
        editor.commit();
    }

    public boolean isLoggedIn() {

        Log.e("login preference value",": "+pref.getString(isloggedIn,null));
        return !(pref.getString(isloggedIn, null)==null);
    }
    public String getUserType(){
        return pref.getString(isloggedIn,null).split("_")[0];

    }

}