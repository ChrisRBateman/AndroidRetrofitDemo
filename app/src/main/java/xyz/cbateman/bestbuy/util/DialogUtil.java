package xyz.cbateman.bestbuy.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import xyz.cbateman.bestbuy.R;

/**
 * DialogUtil class has some general purpose dialog windows.
 */
public class DialogUtil {

    /**
     * Display a dialog window with message and OK button.
     *
     * @param context the Context displaying this dialog window
     * @param message the displayed message
     */
    public static void showOKMessage(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        Resources res = context.getResources();
        alertDialog.setPositiveButton(res.getString(R.string.ok_text), (dialog, which) -> {});

        alertDialog.create();
        alertDialog.show();
    }
}
