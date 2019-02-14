package com.beblue.gfpf.test.bebluegfpftest.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class Util {

    public static void hideKeyboard(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(act.getWindow().getDecorView().getWindowToken(), 0);
    }

}
