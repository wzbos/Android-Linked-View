package cn.wzbos.android.widget.linked;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


class PickerDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;
    private float mOrientation;
    private float mLeftMargin;
    private float mRightMargin;
    private float mTopMargin;
    private float mBottomMargin;
    private boolean hasStartEndSpace = true;

    static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private static final int[] ATRRS = new int[]{
            android.R.attr.listDivider
    };

    PickerDecoration(Context context, int orientation) {
        this.mContext = context;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }

    PickerDecoration(Context context, int orientation, int drawableId, boolean hasStartEndSpace) {
        this.mContext = context;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        this.hasStartEndSpace = hasStartEndSpace;
        ta.recycle();
        setOrientation(orientation);
    }


    public PickerDecoration(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public PickerDecoration setMargin(float leftMargin, float rightMargin, float topMargin, float bottomMargin) {
        mLeftMargin = leftMargin;
        mRightMargin = rightMargin;
        mTopMargin = topMargin;
        mBottomMargin = bottomMargin;

        return this;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(canvas, parent, state);
        } else {
            drawHorizontalLine(canvas, parent, state);
        }
    }

    private void drawHorizontalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        int count = 0;
        if (parent.getAdapter() != null) {
            count = parent.getAdapter().getItemCount();
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            if (i + 1 < childCount && parent.getChildAt(i + 1).getVisibility() != View.VISIBLE)
                continue;

            if (!hasStartEndSpace) {
                int index = parent.getChildAdapterPosition(child);
                if (index == 0 || index == count - 1) {
                    continue;
                }
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left + Utils.dp2px(mContext, mLeftMargin),
                    top + Utils.dp2px(mContext, mTopMargin),
                    right - Utils.dp2px(mContext, mRightMargin),
                    bottom - Utils.dp2px(mContext, mBottomMargin));
            this.mDivider.draw(canvas);
        }
    }

    private void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        int count = 0;
        if (parent.getAdapter() != null) {
            count = parent.getAdapter().getItemCount();
        }
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (i + 1 < childCount && parent.getChildAt(i + 1).getVisibility() != View.VISIBLE)
                continue;

            if (!hasStartEndSpace) {
                int index = parent.getChildAdapterPosition(child);
                if (index == 0 || index == count - 1) {
                    continue;
                }
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            this.mDivider.setBounds(left +
                            Utils.dp2px(mContext, mLeftMargin),
                    top + Utils.dp2px(mContext, mTopMargin),
                    right - Utils.dp2px(mContext, mRightMargin),
                    bottom - Utils.dp2px(mContext, mBottomMargin));
            this.mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.mOrientation == HORIZONTAL_LIST) {
            outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        }
    }
}
