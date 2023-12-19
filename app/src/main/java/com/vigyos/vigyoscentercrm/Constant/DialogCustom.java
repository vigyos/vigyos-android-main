package com.vigyos.vigyoscentercrm.Constant;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class DialogCustom {

    public interface AlertDialogListener {
        void onPositiveButtonClick();
    }

    public static Dialog dialog;

    public static void showAlertDialog(Activity activity, String title, String msg, String action, boolean backCancel ,final AlertDialogListener listener) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(backCancel);
        dialog.setContentView(R.layout.dialog_user_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView titleText = dialog.findViewById(R.id.title);
        titleText.setText(title);
        TextView details = dialog.findViewById(R.id.details);
        details.setText(msg);
        TextView buttonText = dialog.findViewById(R.id.buttonText);
        buttonText.setText(action);
        dialog.findViewById(R.id.enable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                dismissAlertDialog();
                if (listener != null) {
                    listener.onPositiveButtonClick();
                }
            }
        });
        dialog.show();
    }

    public static void dismissAlertDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}