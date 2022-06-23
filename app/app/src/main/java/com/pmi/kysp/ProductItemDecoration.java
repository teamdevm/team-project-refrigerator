package com.pmi.kysp;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int bottomOffset;

    public ProductItemDecoration(int space, int bottomOffest) {
        this.space = space;
        this.bottomOffset = bottomOffest;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position % 2 == 1) {
            outRect.left = (int)(space/2);
            outRect.right = space;
        } else {
            outRect.left = space;
        }

        int dataSize = state.getItemCount();
        if (dataSize > 0 && position == dataSize - 1)
        {
            outRect.bottom = bottomOffset;
        }
    }


}