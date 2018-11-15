package com.asana.git.graphql.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * RecViewItemDecoration is used to add space around the
 * recycler view item
 *
 * @author Gowsik K C
 * @version 1.0 ,10/28/2018
 * @since 1.0
 */
public class RecViewItemDecoration extends RecyclerView.ItemDecoration {


    private final int space;

    /**
     * Constructor to receive the space value
     *@param space int value of the margin
     */
    public RecViewItemDecoration(int space) {
        this.space = space;

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {


        //applies top margin only for first item
        if (parent.getChildAdapterPosition(view) == 0) {

            outRect.set(space, space, space, space);
        } else {

            outRect.set(space, 0, space, space);
        }

    }
}

