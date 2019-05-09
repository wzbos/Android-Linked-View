package cn.wzbos.android.widget.linked;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.view.View;

import java.util.List;

public interface ILinked {

    /**
     * get select values
     *
     * @return collection
     */
    List<List<IPickerData>> getSelectValues();

    /**
     * Reset data to the last selected state
     */
    void restore();

    /**
     * Reset data to the last default state
     */
    void reset();

    /**
     * get all options
     *
     * @return PickerView collection
     */
    int getPickerViewCount();

    /**
     * get option of index
     *
     * @return PickerView
     */
    PickerView getPickerView(int index);


    /**
     * add option
     *
     * @param option PickerView
     */
    void addPickerView(PickerView option);

    /**
     * set background
     *
     * @param color color
     */
    void setBackgroundColor(@ColorInt int color);

    /**
     * set data
     *
     * @param data collection
     */
    void setData(List<? extends IPickerData> data);

    /**
     * Set the visibility of divider
     *
     * @param val true:visible,false:gone
     */
    void setDivider(boolean val);

    /**
     * Sets confirm button the text to be displayed.
     *
     * @param text button text
     */
    void setConfirmButtonText(String text);

    /**
     * Set the visibility state of this Confirm button
     *
     * @param val true:visible,false:gone
     */
    void setConfirmButtonVisible(boolean val);

    /**
     * Register a callback to be invoked when all picked.
     *
     * @param listener The listener for picked.
     */
    void setOnPickedListener(OnPickedListener listener);

    /**
     * Setting the automatic create PickerView
     *
     * @param val true:automatic,false:manual
     */
    void setLinkedMode(boolean val);

    /**
     * Register a callback to be invoked when this item view clicked.
     *
     * @param listener The listener for PickerView item clicked.
     */
    void setOnPickerViewItemClickedListener(OnPickerViewItemClickedListener listener);

    /**
     * Register a callback to be invoked when auto create PickerView.
     *
     * @param listener The listener for create PickerView.
     */
    void setOnCreatePickerViewListener(OnCreatePickerViewListener listener);

    interface OnPickerViewItemClickedListener {
        void onPickerViewItemClicked(PickerView pickerView, int position, IPickerData data);
    }

    interface OnCreatePickerViewListener {
        void onCreatePickerView(PickerView prevView, int prevPosition, PickerView nextView, int nextPosition);
    }

    interface OnPickedListener {
        void onPicked(ILinked linkView, PickerResult result);
    }

    interface OnResetListener {
        void onReset(ILinked linkView);
    }

    interface OnCancelListener {
        void onCancel(ILinked linkView);
    }

    interface IView extends ILinked {

        /**
         * Sets reset button the text to be displayed.
         *
         * @param text button text
         */
        void setResetButtonText(String text);

        /**
         * Set the visibility state of reset button
         *
         * @param val true:visible,false:gone
         */
        void setResetButtonVisible(boolean val);

        /**
         * Register a callback to be invoked when this data reset.
         *
         * @param listener The listener for data reset.
         */
        void setOnResetListener(OnResetListener listener);
    }

    interface IPopupWindow extends ILinked {

        /**
         * Set the height of window ratio
         *
         * @param height window height
         */
        void setWindowHeight(int height);

        /**
         * Set the height of window ratio
         *
         * @param ratio window height(0.1~1.0)
         */
        void setWindowHeightRatio(double ratio);

        /**
         * Sets window the title to be displayed.
         *
         * @param title window title
         */
        void setTitle(CharSequence title);

        /**
         * Set the visibility state of title
         *
         * @param val true:visible,false:gone
         */
        void setTitleVisible(boolean val);

        /**
         * Set the text color of title
         *
         * @param color color
         */
        void setTextColor(@ColorInt int color);

        /**
         * Set the text color of title
         *
         * @param color ColorStateList
         */
        void setTextColor(ColorStateList color);

        /**
         * Sets cancel button the text to be displayed.
         *
         * @param text text
         */
        void setCancelButtonText(String text);

        /**
         * Set the visibility state of this cancel button
         *
         * @param val true:visible,false:gone
         */
        void setCancelButtonVisible(boolean val);

        /**
         * Register a callback to be invoked when cancel button clicked.
         *
         * @param listener The listener for cancel button clicked.
         */
        void setOnCancelListener(OnCancelListener listener);

        /**
         * Display the window from the center.
         */
        void showWindow();

        /**
         * Display the window from the parent view bottom.
         *
         * @param parent parent view
         */
        void showAtBottom(View parent);

        /**
         * Display the window from the parent view top.
         *
         * @param anchor the view on which to pin the popup window
         */
        void showAsDropDown(View anchor);
    }
}
