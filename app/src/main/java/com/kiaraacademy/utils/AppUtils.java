package com.kiaraacademy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kiaraacademy.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppUtils {
    public static final InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    };

    private AppUtils() {
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            // Dismiss Dialog
            progressDialog.dismiss();
        }
    }

    //Show Progress Dialog
    public static ProgressDialog generateProgressDialog(Context context, int title, String message, boolean isDeterminent, boolean isCancelable) {
        return (ProgressDialog.show(context, context.getString(title), message, isDeterminent, isCancelable));
    }

    public static void showAlertToEnableMobileData(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("").setMessage(context.getString(R.string.str_enable_mobile_data)).setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.str_dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorRed));
            }
        });
        alertDialog.show();
    }

    /**
     * reduces the size of the image
     */
    public static Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int maxSize = 512;
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static void showImageConfirmationDialog(final Context context, final ConfimationListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("").setMessage(context.getString(R.string.str_image_confirmation)).setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.str_okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOkClick();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(context.getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelCLick();
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorGreen));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorRed));
            }
        });

        alertDialog.show();
    }


    public static void showErrorToast(Context context) {
        showToast(context, context.getString(R.string.str_something_went_wrong));
    }

    public static void showConfirmationDialog(String msg, Context loginActivity, final ConfimationListener confimationListener) {
        final Dialog confirmationDialog = new Dialog(loginActivity);
        confirmationDialog.setContentView(R.layout.alert_confirmation_dialog);
        confirmationDialog.setCancelable(false);
        TextView tvTitleMsg = confirmationDialog.findViewById(R.id.tv_title_otp);
        if (msg != null)
            tvTitleMsg.setText(msg);

        Button btnOK = confirmationDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confimationListener.onOkClick();
                confirmationDialog.dismiss();
            }
        });
        Button btnCancel = confirmationDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });
        confirmationDialog.show();
    }
}