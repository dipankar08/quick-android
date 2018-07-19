package in.co.dipankar.quickandorid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import in.co.dipankar.quickandorid.R;

public class DialogUtils {
    public interface FeedbackCallback{
        void onSubmit(float rating, String message);
        void onDismiss();
    }
    public static void showFeedbackDialog(Activity context, final FeedbackCallback callback ) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = ((Activity)context).getLayoutInflater().inflate(R.layout.rating_dialog_view, null);
        final EditText mFeedback = (EditText) mView.findViewById(R.id.feedback);
        final RatingBar mRating = mView.findViewById(R.id.rating);
        final TextView mResult = mView.findViewById(R.id.result);
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating >=5){
                    mResult.setText("Outstanding work!");
                } else if(rating >=4){
                    mResult.setText("Good Job - Love it");
                } else if(rating >=3){
                    mResult.setText("Meet expectations");
                } else if(rating >=2){
                    mResult.setText("Needs improvements");
                } else{
                    mResult.setText("Useless app");
                }
            }
        });
        mBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(callback != null){
                    callback.onSubmit(mRating.getRating(),mFeedback.getText().toString());
                }
            }
        });

        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(callback != null){
                    callback.onDismiss();
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public interface ReportCallback{
        void onSubmit(String type, String message);
        void onDismiss();
    }

    public static void showReportDialog(Activity context, final ReportCallback callback ) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = ((Activity)context).getLayoutInflater().inflate(R.layout.feedback_dialog_view, null);
        final EditText mFeedback = (EditText) mView.findViewById(R.id.feedback);
        final Spinner mSpinner = mView.findViewById(R.id.type);


        String types[] = {"Bugs", "Feature Request", "Others", };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,   android.R.layout.simple_spinner_item, types);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mSpinner.setAdapter(spinnerArrayAdapter);
        mBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(callback != null){
                   callback.onSubmit(mSpinner.getSelectedItem().toString(),mFeedback.getText().toString());
                }
            }
        });
        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(callback != null){
                    callback.onDismiss();
                }
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public static void showAboutDialog(Context context, String text){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = ((Activity)context).getLayoutInflater().inflate(R.layout.about_dialog_view, null);
        final TextView mText = (TextView) mView.findViewById(R.id.data);
        mText.setText(text);
        mBuilder.setPositiveButton("Ok", null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public  interface PlayStoreRateDialogCallback{
        void onSubmit();
        void onDismiss();
    }

    public static void showPlayStoreRateAlert(final Context context, final PlayStoreRateDialogCallback callback){
        new AlertDialog.Builder(context)
                .setTitle("Rate this app!")
                .setMessage("Will appreciate if you could give some feedback and  5-star rating in playStore.")
                .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AndroidUtils.RateIt(context);
                        if(callback != null){
                            callback.onSubmit();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(callback != null){
                            callback.onDismiss();
                        }
                    }
                }).show();
    }


    public  interface AppUpgradeCallback{
        void onSubmit();
        void onDismiss();
    }

    public static void showAppUpgradeAlert(final Context context, final AppUpgradeCallback callback){
        new AlertDialog.Builder(context)
                .setTitle("Please upgrade this app")
                .setMessage("We are consistently working on this app by fixing issues or adding new feature. Recently, I have upgraded this app, so please consider upgrading it. ")
                .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AndroidUtils.RateIt(context);
                        if(callback != null){
                            callback.onSubmit();
                        }
                    }
                })
                .setNegativeButton("Not now", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(callback != null){
                            callback.onDismiss();
                        }
                    }
                }).show();
    }
}
