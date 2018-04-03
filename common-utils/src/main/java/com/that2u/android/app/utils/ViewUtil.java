package com.that2u.android.app.utils;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by phama on 2016-07-12.
 */
public class ViewUtil {
    public static int getIdByName(Context context, String name){
        return context.getResources().getIdentifier(name, "id", context.getPackageName());
    }

    public static View getViewInListView(AbsListView listView, int position){
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
