package com.that2u.android.app.utils;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class ReviewUtils {
    private static final String NEVER_SHOW_RATE_APP_KEY = "neverShowRateAppDialog";

    public static void showReviewDialog(Activity activity){
        if(activity == null){
            return;
        }

        ReviewManager reviewManager = ReviewManagerFactory.create(activity);

        Task<ReviewInfo> request =reviewManager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if(task.isSuccessful()){
                    ReviewInfo reviewInfo = task.getResult();

                    if(reviewInfo != null) {
                        Task<Void> flow = reviewManager.launchReviewFlow(activity, reviewInfo);
                        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }else{
                    showNormalReviewDialog(activity);
                }
            }
        });
    }

    public static void showNormalReviewDialog(Activity activity){
        if(activity != null && !activity.isDestroyed() && !activity.isFinishing() &&
                !SharedPreferenceUtil.getBooleanValue(activity, NEVER_SHOW_RATE_APP_KEY, false)) {
            new MaterialAlertDialogBuilder(activity)
                    .setTitle(R.string.rateAppDialogTitle)
                    .setMessage(R.string.rateAppDialogContent)
                    .setPositiveButton(R.string.rateNow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PlayStoreUtil.openAppPage(activity);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.remindMeLater, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNeutralButton(R.string.noThank, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //mark as not show again
                            SharedPreferenceUtil.setBooleanValue(activity, NEVER_SHOW_RATE_APP_KEY, true);

                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
    }
}
