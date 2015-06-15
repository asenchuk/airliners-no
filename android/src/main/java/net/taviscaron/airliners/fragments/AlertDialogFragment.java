package net.taviscaron.airliners.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import net.taviscaron.airliners.R;

/**
 * Alert dialog
 * @author Andrei Senchuk
 */
public class AlertDialogFragment extends DialogFragment {
    private static final String DEFAULT_TAG = "alert";
    public static final String POSITIVE_BUTTON_ARG = "positiveButton";
    public static final String POSITIVE_BUTTON_ID_ARG = "positiveButtonId";
    public static final String NEGATIVE_BUTTON_ARG = "negativeButton";
    public static final String NEGATIVE_BUTTON_ID_ARG = "negativeButtonId";
    public static final String TITLE_ARG = "title";
    public static final String TITLE_ID_ARG = "titleId";
    public static final String MESSAGE_ARG = "message";
    public static final String MESSAGE_ID_ARG = "messageId";

    public interface OnAlertDialogListener {
        public void positiveButtonClicked(AlertDialogFragment dialog);
        public void negativeButtonClicked(AlertDialogFragment dialog);
    }

    public static AlertDialogFragment createAlert(Context context, int messageId) {
        return createDialog(context, 0, messageId, R.string.button_ok, 0);
    }

    public static AlertDialogFragment createDialog(Context context, int title, int message, int positiveButton, int negativeButton) {
        Bundle args = new Bundle();

        if(title != 0) {
            args.putInt(TITLE_ID_ARG, title);
        }

        if(message != 0) {
            args.putInt(MESSAGE_ID_ARG, message);
        }

        if(positiveButton != 0) {
            args.putInt(POSITIVE_BUTTON_ID_ARG, positiveButton);
        }

        if(negativeButton != 0) {
            args.putInt(NEGATIVE_BUTTON_ID_ARG, negativeButton);
        }

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String title = getArgumentString(TITLE_ARG, TITLE_ID_ARG);
        if(title != null) {
            builder.setTitle(title);
        }

        String message = getArgumentString(MESSAGE_ARG, MESSAGE_ID_ARG);
        if(message != null) {
            builder.setMessage(message);
        }

        String positiveButton = getArgumentString(POSITIVE_BUTTON_ARG, POSITIVE_BUTTON_ID_ARG);
        if(positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    positiveButtonClicked();
                }
            });
        }

        String negativeButton = getArgumentString(NEGATIVE_BUTTON_ARG, NEGATIVE_BUTTON_ID_ARG);
        if(negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    negativeButtonClicked();
                }
            });
        }

        return builder.create();
    }

    private void positiveButtonClicked() {
        Activity activity = getActivity();
        if(activity instanceof OnAlertDialogListener) {
            ((OnAlertDialogListener)activity).positiveButtonClicked(this);
        }
    }

    private void negativeButtonClicked() {
        Activity activity = getActivity();
        if(activity instanceof OnAlertDialogListener) {
            ((OnAlertDialogListener)activity).negativeButtonClicked(this);
        }
    }

    private String getArgumentString(String stringKey, String idKey) {
        String result = null;
        Bundle args = getArguments();
        if(args != null) {
            Context context = getActivity();
            int id = args.getInt(idKey);
            if(id != 0 && context != null) {
                result = context.getString(id);
            } else if(stringKey != null) {
                result = args.getString(stringKey);
            }
        }
        return result;
    }

    public void show(FragmentManager fm) {
        Fragment f = fm.findFragmentByTag(DEFAULT_TAG);
        if(f != null) {
            fm.beginTransaction().remove(f).commit();
        }

        show(fm, DEFAULT_TAG);
    }
}
