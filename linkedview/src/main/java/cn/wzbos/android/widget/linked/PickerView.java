package cn.wzbos.android.widget.linked;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Picker View
 */
public class PickerView extends RecyclerView {

    protected LinearLayoutManager linearLayoutManager;
    protected PickerAdapter adapter;
    private static final String TAG = "PickerView";

    Integer checkedColor;
    Integer unCheckedColor;
    Integer stateBackground = R.drawable.wzblinkedview_item_background;
    Integer checkedBkgColor;
    Integer unCheckedBkgColor;
    Integer stateIconResId = R.drawable.wzblinkedview_item_checked;
    Integer checkedIconResId;
    Integer unCheckedIconResId;
    Integer textSize;
    Integer textAlignment;
    boolean isMultiSelect;
    private boolean isShowIcon;
    List<? extends IPickerData> data;
    float weight = 1;
    int width;
    List<String> defaultIds;
    boolean isShowPickCount;

    public PickerView(@NonNull Context context) {
        super(context);
        initViews(context);
    }

    public PickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public OnItemClickedListener onItemClickedListener;

    public interface OnItemClickedListener {
        void onItemClicked(PickerView pickerView, int position, IPickerData data);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }


    protected void initViews(Context context) {
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(true);
        //reset all animator duration
        RecyclerView.ItemAnimator animator = getItemAnimator();
        if (animator != null) {
            animator.setAddDuration(0);
            animator.setChangeDuration(0);
            animator.setRemoveDuration(0);
        }

        this.adapter = new PickerAdapter();
        setAdapter(adapter);
    }

    /**
     * 设置列表选项的宽度等份
     *
     * @param weight 等份
     */
    public PickerView setWeight(float weight) {
        this.weight = weight;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).weight = weight;
                requestLayout();
            }
        }
        return this;
    }

    /**
     * 设置列表选项的宽度
     *
     * @param width 宽度
     */
    public PickerView setWidth(int width) {
        this.width = width;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = width;
            requestLayout();
        }
        return this;
    }

    PickerDecoration divider;

    /**
     * 设置显示的分割线
     *
     * @param showDivider 显示：true,不显示：false
     */
    public PickerView setShowDivider(boolean showDivider) {
        if (showDivider) {
            if (divider == null)
                divider = new PickerDecoration(getContext(), PickerDecoration.VERTICAL_LIST, R.drawable.wzblinkedview_divider_vertical);
            addItemDecoration(divider);
        } else {
            removeItemDecoration(divider);
        }
        return this;
    }

    /**
     * 设置显示选中的数量
     *
     * @param showPickCount 显示：true,不显示：false
     */
    public PickerView setShowPickCount(boolean showPickCount) {
        isShowPickCount = showPickCount;
        if (isShowPickCount)
            refreshPickCount();
        else
            adapter.notifyDataSetChanged();
        return this;
    }


    public PickerView setStateBackground(@DrawableRes int stateBackground) {
        this.stateBackground = stateBackground;
        this.checkedBkgColor = null;
        this.unCheckedBkgColor = null;
        return this;
    }

    /**
     * 设置选项状态背景颜色
     *
     * @param checkedBkgColor   选中的背景颜色
     * @param unCheckedBkgColor 未选中的背景颜色
     */
    public PickerView setStateBackground(@ColorInt int checkedBkgColor, @ColorInt int unCheckedBkgColor) {
        this.stateBackground = null;
        this.checkedBkgColor = checkedBkgColor;
        this.unCheckedBkgColor = unCheckedBkgColor;
        return this;
    }

    /**
     * 设置选项文本颜色
     *
     * @param checkedColor 选中的文本颜色
     * @param checkedColor 未选中的文本颜色
     */
    public PickerView setTextColor(@ColorInt int checkedColor, @ColorInt int unCheckedColor) {
        this.checkedColor = checkedColor;
        this.unCheckedColor = unCheckedColor;
        return this;
    }


    /**
     * 设置是否显示状态图标
     *
     * @param showIcon 显示：true,不显示：false
     */
    public PickerView setShowIcon(boolean showIcon) {
        //Log.d(TAG, "setShowIcon:" + showIcon);
        isShowIcon = showIcon;
        adapter.notifyDataSetChanged();
        return this;
    }

    /**
     * 设置是否显示状态图标资源
     *
     * @param stateIconResId 资源ID
     */
    public PickerView setStateIconRes(@DrawableRes int stateIconResId) {
        this.stateIconResId = stateIconResId;
        this.checkedIconResId = null;
        this.unCheckedIconResId = null;
        return this;
    }

    /**
     * 设置选项的状态图标
     *
     * @param checkedIconResId   选中的图标资源ID
     * @param unCheckedIconResId 未选中的图标资源ID
     */
    public PickerView setStateIcon(@DrawableRes int checkedIconResId, @DrawableRes int unCheckedIconResId) {
        this.stateIconResId = null;
        this.checkedIconResId = checkedIconResId;
        this.unCheckedIconResId = unCheckedIconResId;
        return this;
    }

    /**
     * 设置选项文本字体大小
     *
     * @param textSize 单位sp
     */
    public PickerView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    @IntDef({
            View.TEXT_ALIGNMENT_INHERIT,
            View.TEXT_ALIGNMENT_GRAVITY,
            View.TEXT_ALIGNMENT_CENTER,
            View.TEXT_ALIGNMENT_TEXT_START,
            View.TEXT_ALIGNMENT_TEXT_END,
            View.TEXT_ALIGNMENT_VIEW_START,
            View.TEXT_ALIGNMENT_VIEW_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextAlignment {
    }

    /**
     * 设置选项文本对其方向
     *
     * @param textAlignment 单位sp
     */
    public PickerView setTxtAlignment(@TextAlignment int textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    /**
     * 设置是否为多选
     *
     * @param multiSelect 多选:true，单选:false
     */
    public PickerView setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
        return this;
    }


    public PickerView setData(List<? extends IPickerData> data) {
        this.data = data;
        this.mLastPosition = -1;
        if (data != null) {
            for (int i = 0; i < this.data.size(); i++) {
                IPickerData d = this.data.get(i);

                if (d.isSelected()) {
                    mLastPosition = i;
                }
            }
        }
        if (this.adapter != null)
            this.adapter.notifyDataSetChanged();
        return this;
    }


    public PickerView setDefaultIds(List<String> defaultIds) {
        this.defaultIds = defaultIds;
        return this;
    }

    public PickerView setDefaultIds(String defaultValue, String split) {
        if (!TextUtils.isEmpty(defaultValue)) {
            String[] val = defaultValue.split(split);
            this.defaultIds = Arrays.asList(val);
        }
        return this;
    }

    public List<String> getDefaultIds() {
        return this.defaultIds;
    }


    public List<IPickerData> getSelectedValues() {
        List<IPickerData> values = new ArrayList<>();
        if (data != null) {
            for (IPickerData d : data) {
                if (d.isSelected()) {
                    values.add(d);
                }
            }
        }
        return values;
    }

    /**
     * 数据重置到上次选中状态
     */
    public void restore() {
        if (data == null)
            return;

        for (IPickerData d : data) {
            d.setSelected(defaultIds != null && defaultIds.contains(d.getId()));
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 数据重置到默认值状态
     */
    public void reset() {
        if (data == null)
            return;

        for (IPickerData d : data) {
            d.setSelected(false);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 检查当前列表是否选择结束(注意：多选永远返回false)
     */
    public boolean isSelectedOver() {
        if (isMultiSelect)
            return false;

        return isSelected();
    }

    /**
     * 是否选中
     */
    public boolean isSelected() {
        if (data != null) {
            for (IPickerData d : data) {
                if (d.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }


    public void refreshPickCount() {
        if (adapter != null) {
            if (data != null && !isMultiSelect) {
                if (mLastPosition > -1 && mLastPosition < data.size()) {
                    ViewHolder viewHolder = findViewHolderForLayoutPosition(mLastPosition);
                    if (viewHolder != null) {
                        ((PickerViewHolder) viewHolder).refreshPickCount();
                    }
                    return;
                }

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelected()) {
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }


    /**
     * Picker Adapter
     * Created by wuzongbo on 2018/1/2.
     */
    class PickerAdapter extends Adapter<PickerViewHolder> {

        @NonNull
        @Override
        public PickerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wzblinkedview_picker_item, viewGroup, false);
            return new PickerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PickerViewHolder holder, int position) {
            IPickerData bean = data.get(position);
            holder.bind(bean);
        }

        @Override
        public int getItemCount() {
            if (data == null)
                return 0;
            return data.size();
        }
    }

    int mLastPosition = -1;

    /**
     * Picker Item ViewHolder
     * Created by wuzongbo on 2018/1/2.
     */
    class PickerViewHolder extends ViewHolder implements OnClickListener {
        private CheckedTextView tvName;
        private IPickerData bean;

        PickerViewHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);

        }

        void bind(IPickerData bean) {
            this.bean = bean;

            if (checkedColor != null && unCheckedColor != null) {
                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_checked};
                states[1] = new int[]{-android.R.attr.state_checked};
                int[] colors = new int[]{checkedColor, unCheckedColor};
                tvName.setTextColor(new ColorStateList(states, colors));
            }

            if (textSize != null)
                tvName.setTextSize(textSize);

            if (textAlignment != null)
                tvName.setTextAlignment(textAlignment);

            if (stateBackground != null) {
                tvName.setBackgroundResource(stateBackground);
            } else if (checkedBkgColor != null && unCheckedBkgColor != null) {
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(checkedBkgColor));
                stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, new ColorDrawable(unCheckedBkgColor));
                tvName.setBackground(stateListDrawable);
            } else {
                tvName.setBackground(null);
            }

            this.tvName.setChecked(bean.isSelected());

            if (isShowIcon) {
                if (stateIconResId != null) {
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, stateIconResId, 0);
                } else if (checkedIconResId != null && unCheckedIconResId != null) {
                    StateListDrawable drawable = new StateListDrawable();
                    drawable.addState(new int[]{android.R.attr.state_checked}, getResources().getDrawable(checkedIconResId));
                    drawable.addState(new int[]{-android.R.attr.state_checked}, getResources().getDrawable(unCheckedIconResId));
                    tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                } else {
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            } else {
                tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            int count;
            if (isShowPickCount && bean.isSelected() && (count = getNextCount()) > 0) {
                tvName.setText(bean.getName() + "(" + count + ")");
            } else {
                tvName.setText(bean.getName());
            }
        }

        void refreshPickCount() {
            int count;
            if (isShowPickCount && bean.isSelected() && (count = getNextCount()) > 0) {
                tvName.setText(bean.getName() + "(" + count + ")");
            } else {
                tvName.setText(bean.getName());
            }
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if (isMultiSelect) {
                boolean val = !bean.isSelected();
                this.bean.setSelected(val);
                this.tvName.setChecked(val);
            } else {
                if (mLastPosition > -1) {
                    //Log.d(TAG, "Picker:" + mLastPosition + " = false");
                    data.get(mLastPosition).setSelected(false);
                    adapter.notifyItemChanged(mLastPosition);
                }
                //Log.d(TAG, "Picker:" + getAdapterPosition() + " = true");
                this.bean.setSelected(true);
                this.tvName.setChecked(true);
                mLastPosition = position;
            }


            if (onItemClickedListener != null)
                onItemClickedListener.onItemClicked(PickerView.this, position, bean);
        }

        private int getNextCount() {
            int count = 0;
            if (bean.nodes() != null) {
                for (IPickerData b : bean.nodes()) {
                    if (b.isSelected()) {
                        count += 1;
                    }
                }
            }
            return count;
        }
    }
}
