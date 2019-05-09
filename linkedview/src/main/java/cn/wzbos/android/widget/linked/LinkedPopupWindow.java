package cn.wzbos.android.widget.linked;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

/**
 * Linked Picker PopupWindow
 * Created by wuzongbo on 2019/4/21.
 */
public class LinkedPopupWindow extends PopupWindow implements ILinked.IPopupWindow {
    private LinkedView picker;
    private LinearLayout llWin;
    private LinearLayout titleLayout;
    private TextView tvOK;
    private TextView tvCancel;
    private TextView tvTitle;
    private TextView btnCancel;
    private OnPickedListener onPickedListener;
    private OnCancelListener onCancelListener;
    private final double DEFAULT_RATIO = 0.5;//默认高度
    private Context context;

    public LinkedPopupWindow(Context context) {
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.wzblinkedview_linked_popup, null);
        picker = rootView.findViewById(R.id.picker);
        llWin = rootView.findViewById(R.id.llWin);
        titleLayout = rootView.findViewById(R.id.titleLayout);
        tvCancel = rootView.findViewById(R.id.tvCancel);
        tvOK = rootView.findViewById(R.id.tvOK);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        setContentView(rootView);
        setWindowStyle();
        initView(rootView);
    }

    /**
     * 设置当前窗体样式
     */
    private void setWindowStyle() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x11ffffff);
        super.setBackgroundDrawable(dw);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private void initView(View rootView) {
        picker.setConfirmButtonVisible(false);
        picker.setResetButtonVisible(false);

        tvOK.setOnClickListener(v -> {
            PickerResult result = picker.onPicked();
            if (onPickedListener != null)
                onPickedListener.onPicked(LinkedPopupWindow.this, result);
            dismiss();
        });

        tvCancel.setOnClickListener(v -> onCancel());


        llWin.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int height = llWin.getHeight();
            int maxHeight = (int) ((float) Utils.getScreenHeight(context) * DEFAULT_RATIO);
            if (height > maxHeight) {
                setWindowHeightRatio(DEFAULT_RATIO);
                llWin.requestLayout();
            }
        });
        picker.setOnPickedListener((linkView, selectIds) -> {
            if (titleLayout.getVisibility() != View.VISIBLE && btnCancel.getVisibility() != View.VISIBLE) {
                dismiss();

                if (onPickedListener != null)
                     onPickedListener.onPicked(LinkedPopupWindow.this, selectIds);
            }

        });
        rootView.findViewById(R.id.bkgWin).setOnClickListener(v -> onCancel());
    }


    private void onCancel() {
        dismiss();
        if (onCancelListener != null)
            onCancelListener.onCancel(LinkedPopupWindow.this);
    }


    @Override
    public void restore() {
        picker.restore();
    }

    @Override
    public void reset() {
        picker.reset();
    }

    @Override
    public int getPickerViewCount() {
        return picker.getPickerViewCount();
    }

    @Override
    public PickerView getPickerView(int index) {
        return picker.getPickerView(index);
    }

    @Override
    public List<List<IPickerData>> getSelectValues() {
        return picker.getSelectValues();
    }

    @Override
    public void addPickerView(PickerView option) {
        picker.addPickerView(option);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        this.picker.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        this.picker.setBackground(background);
    }

    @Override
    public Drawable getBackground() {
        return this.picker.getBackground();
    }

    @Override
    public void setData(List<? extends IPickerData> data) {
        this.picker.setData(data);
    }


    @Override
    public void setOnPickedListener(OnPickedListener listener) {
        this.onPickedListener = listener;
    }

    @Override
    public void setLinkedMode(boolean val) {
        picker.setLinkedMode(val);
    }

    @Override
    public void setOnPickerViewItemClickedListener(OnPickerViewItemClickedListener listener) {
        picker.setOnPickerViewItemClickedListener(listener);
    }

    @Override
    public void setOnCreatePickerViewListener(OnCreatePickerViewListener intercept) {
        picker.setOnCreatePickerViewListener(intercept);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener) {
        this.onCancelListener = listener;
    }


    @Override
    public void setConfirmButtonText(String text) {
        tvOK.setText(text);
        picker.setConfirmButtonText(text);
    }

    @Override
    public void setConfirmButtonVisible(boolean val) {
        tvOK.setVisibility(val ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setCancelButtonVisible(boolean val) {
        if (titleLayout.getVisibility() == View.VISIBLE) {
            tvCancel.setVisibility(val ? View.VISIBLE : View.GONE);
        } else {
            btnCancel.setVisibility(val ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setDivider(boolean val) {
        picker.setDivider(val);
    }

    @Override
    public void setWindowHeightRatio(double ratio) {
        llWin.getLayoutParams().height = (int) ((float) Utils.getScreenHeight(context) * ratio);
    }

    @Override
    public void setWindowHeight(int height) {
        llWin.getLayoutParams().height = height;
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    @Override
    public void setTitleVisible(boolean val) {
        titleLayout.setVisibility(val ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setTextColor(@ColorInt int color) {
        tvCancel.setTextColor(color);
        tvOK.setTextColor(color);
    }


    @Override
    public void setTextColor(ColorStateList color) {
        tvCancel.setTextColor(color);
        tvOK.setTextColor(color);
    }

    @Override
    public void setCancelButtonText(String text) {
        tvCancel.setText(text);
        btnCancel.setText(text);
    }

//    private void setStyle() {
//        if (picker.getPickerViews().size() > 1) {
//            btnCancel.setVisibility(View.GONE);
//            titleLayout.setGravity(View.VISIBLE);
//        } else {
//            btnCancel.setVisibility(View.VISIBLE);
//            titleLayout.setGravity(View.GONE);
//        }
//    }

    public void showWindow() {
        llWin.setGravity(Gravity.CENTER_VERTICAL);
        View view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        super.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void showAtBottom(View parent) {
        ((FrameLayout.LayoutParams) llWin.getLayoutParams()).gravity = Gravity.BOTTOM;
        View view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        super.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void showAsDropDown(View anchor) {
        llWin.setGravity(Gravity.TOP);
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        int height = Utils.getScreenHeight(anchor.getContext()) - location[1];
        setHeight(height);
        super.showAsDropDown(anchor);
    }

}
