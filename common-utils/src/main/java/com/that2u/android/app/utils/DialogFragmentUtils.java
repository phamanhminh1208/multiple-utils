package com.that2u.android.app.utils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogFragmentUtils {
    public static void showDialogFragment(FragmentManager fragmentManager, DialogFragment dialog,
                                          String tag, Runnable onShowFunc){
        if(dialog != null && !dialog.isAdded() && !dialog.isVisible()){
            dialog.show(fragmentManager, tag);

            if(onShowFunc != null){
                onShowFunc.run();
            }
        }
    }
}
