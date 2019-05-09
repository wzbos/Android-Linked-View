package cn.wzbos.android.widget.linked;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Linked Picker View
 * Created by wuzongbo on 2019/4/27.
 */
public class LinkedView extends RelativeLayout implements ILinked.IView, PickerView.OnItemClickedListener {
    private final static String TAG = "LinkedView";
    private Button btnReset;
    private Button btnConfirm;
    private LinearLayout pickerLayout;
    private OnPickedListener onPickedListener;
    private OnResetListener onResetListener;
    private Context context;
    private List<List<String>> defaultIds;
    private boolean isLinkedMode = false;
    private OnCreatePickerViewListener onCreatePickerViewListener;
    private OnPickerViewItemClickedListener onPickerViewItemClickedListener;

    public void setDefaultIds(List<List<String>> defaultIds) {
        this.defaultIds = defaultIds;
    }

    public LinkedView(Context context) {
        super(context);
        initViews(context);
    }

    public LinkedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public LinkedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    private void initViews(Context context) {
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.wzblinkedview_linked_view, this);

        btnReset = rootView.findViewById(R.id.btnReset);
        btnConfirm = rootView.findViewById(R.id.btnConfirm);
        pickerLayout = rootView.findViewById(R.id.pickerLayout);


        btnReset.setOnClickListener(v -> reset());
        btnConfirm.setOnClickListener(v -> onPicked());
    }


    @Override
    public void restore() {
        //Log.d(TAG, "<<< restore >>>");
        int count = getPickerViewCount();

        if (isLinkedMode) {

            if (defaultIds != null && defaultIds.size() > 0) {
                restore(data, defaultIds.get(0), 0);
            } else {
                restore(data, null, 0);
            }

            setData(data);
        } else {
            for (int i = 0; i < count; i++) {
                getPickerView(i).restore();
            }
        }
    }

    public void restore(List<? extends IPickerData> nodes, List<String> ids, int position) {
        //Log.d(TAG, "restore,position:" + position);
        if (nodes == null)
            return;

        int nextPosition = position + 1;

        for (IPickerData d : nodes) {
            if (ids != null && ids.contains(d.getId())) {
                //Log.d(TAG, d.getName() + " = true");
                d.setSelected(true);
                if (d.nodes() != null) {
                    if (defaultIds != null && defaultIds.size() > nextPosition) {
                        restore(d.nodes(), defaultIds.get(nextPosition), nextPosition);
                    }
                }
            } else {
                d.setSelected(false);

                if (d.nodes() != null)
                    restore(d.nodes(), null, nextPosition);
            }
        }


    }


    @Override
    public void reset() {
        //Log.d(TAG, "<<< reset >>>");
        int count = getPickerViewCount();

        if (isLinkedMode) {
            reset(data);

            if (count > 0)
                getPickerView(0).adapter.notifyDataSetChanged();

            for (int i = 1; i < count; i++) {
                getPickerView(i).setVisibility(GONE);
            }
        } else {
            for (int i = 0; i < count; i++) {
                getPickerView(i).reset();
            }
        }

        if (onResetListener != null)
            onResetListener.onReset(this);
    }


    private void reset(List<? extends IPickerData> nodes) {
        if (nodes != null) {
            for (IPickerData d : nodes) {
                d.setSelected(false);
                if (d.nodes() != null)
                    reset(d.nodes());
            }
        }
    }

    @Override
    public List<List<IPickerData>> getSelectValues() {
        List<List<IPickerData>> result = new ArrayList<>();
        for (int i = 0; i < getPickerViewCount(); i++) {
            PickerView pickerView = getPickerView(i);
            if (pickerView.getVisibility() == VISIBLE) {
                result.add(pickerView.getSelectedValues());
            }
        }
        return result;
    }

    protected PickerResult onPicked() {
        //Log.d(TAG, "onPicked");
        PickerResult result = new PickerResult(getSelectValues());
        defaultIds = result.getIdArray();
        for (int i = 0; i < getPickerViewCount(); i++) {
            if (i < defaultIds.size()) {
                getPickerView(i).setDefaultIds(defaultIds.get(i));
            } else {
                getPickerView(i).setDefaultIds(null);
            }
        }
        if (onPickedListener != null)
            onPickedListener.onPicked(this, result);
        return result;
    }

    @Override
    public void setOnPickedListener(OnPickedListener listener) {
        this.onPickedListener = listener;
    }

    @Override
    public void setOnResetListener(OnResetListener listener) {
        this.onResetListener = listener;
    }

    @Override
    public void setConfirmButtonText(String text) {
        btnConfirm.setText(text);
    }

    @Override
    public void setResetButtonText(String text) {
        btnReset.setText(text);
    }

    @Override
    public void setResetButtonVisible(boolean val) {
        btnReset.setVisibility(val ? VISIBLE : GONE);
    }

    @Override
    public void setConfirmButtonVisible(boolean val) {
        btnConfirm.setVisibility(val ? VISIBLE : GONE);
    }

    /**
     * 设置分割线
     */
    @Override
    public void setDivider(boolean val) {
        if (val) {
            pickerLayout.setDividerDrawable(getResources().getDrawable(R.drawable.wzblinkedview_divider_horizontal));
        } else {
            pickerLayout.setDividerDrawable(null);
        }
    }

    @Override
    public int getPickerViewCount() {
        return pickerLayout.getChildCount();
    }

    @Override
    public PickerView getPickerView(int index) {
        return (PickerView) pickerLayout.getChildAt(index);
    }

    List<? extends IPickerData> data;

    @Override
    public void setData(List<? extends IPickerData> data) {
        //Log.d(TAG, "<<< setData >>>");
        this.data = data;
        if (getPickerViewCount() > 0)
            pickerLayout.removeAllViews();

        if (data != null) {
            this.isLinkedMode = true;
            this.defaultIds = getDefaultValues();
            autoCreate(null, -1, data);
        }
    }

    private List<List<String>> getDefaultValues() {
        List<List<IPickerData>> result = new ArrayList<>();
        getDefaultValues(data, 0, result);
        PickerResult pickerResult = new PickerResult(result);
        //Log.d(TAG, "getDefaultValues:" + pickerResult.toString());
        return pickerResult.getIdArray();

    }

    private void getDefaultValues(List<? extends IPickerData> data, int position, List<List<IPickerData>> result) {
        List<IPickerData> values;
        if (result.size() > position) {
            values = result.get(position);
        } else {
            values = new ArrayList<>();
            result.add(values);
        }
        for (IPickerData pickerData : data) {
            if (pickerData.isSelected()) {
                values.add(pickerData);
            }

            if (pickerData.nodes() != null) {
                getDefaultValues(pickerData.nodes(), position + 1, result);
            }
        }
    }

    private void autoCreate(PickerView prevOption, int prevLevel, List<? extends IPickerData> data) {
        if (!isLinkedMode)
            return;
        //Log.d(TAG, "autoCreate,prevLevel:" + prevLevel);
        int nextLevel = prevLevel + 1;
        PickerView nextPicker = createNextPickerView(prevOption, prevLevel, nextLevel, data);
        for (IPickerData pickerData : data) {
            if (pickerData.isSelected()) {
                if (pickerData.nodes() != null) {
                    autoCreate(nextPicker, prevLevel + 1, pickerData.nodes());
                    break;
                }
            }
        }
    }

    private PickerView createNextPickerView(PickerView prevOption, int prevLevel, int nextLevel, List<? extends IPickerData> data) {
        //Log.d(TAG, "createNextPickerView,nextLevel=" + nextLevel);
        PickerView nextPicker = new PickerView(context);
        nextPicker.setTxtAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        if (onCreatePickerViewListener != null)
            onCreatePickerViewListener.onCreatePickerView(prevOption, prevLevel, nextPicker, nextLevel);
        nextPicker.setData(data);
        addPickerView(nextPicker);
        return nextPicker;
    }

    /**
     * add PickerView
     *
     * @param pickerView PickerView
     */
    @Override
    public void addPickerView(PickerView pickerView) {
        //Log.d(TAG, "<<< addPickerView >>>");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pickerView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(pickerView.width, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (pickerView.weight > 0) {
            layoutParams.weight = pickerView.weight;
        }
        pickerView.setLayoutParams(layoutParams);
        pickerView.setOnItemClickedListener(this);
        pickerLayout.addView(pickerView);
    }

    /**
     * set item clicked listener
     *
     * @param listener OnPickerViewItemClickedListener
     */
    @Override
    public void setOnPickerViewItemClickedListener(OnPickerViewItemClickedListener listener) {
        this.onPickerViewItemClickedListener = listener;
    }

    @Override
    public void setLinkedMode(boolean val) {
        this.isLinkedMode = val;
    }

    @Override
    public void setOnCreatePickerViewListener(OnCreatePickerViewListener onCreatePickerViewListener) {
        this.onCreatePickerViewListener = onCreatePickerViewListener;
    }

    /**
     * 计算当前节点后有多少列
     *
     * @param nodes 集合
     * @return 列数
     */
    private int getNextLevelCount(List<? extends IPickerData> nodes) {
        if (nodes == null)
            return 0;
        return getCount(nodes, 1);
    }

    private int getCount(List<? extends IPickerData> nodes, int level) {
        if (nodes == null)
            return level;
        for (IPickerData pickerData : nodes) {
            if (pickerData.isSelected()) {
                if (pickerData.nodes() != null) {
                    return getCount(pickerData.nodes(), level + 1);
                } else {
                    return level;
                }
            }
        }
        return level;
    }

    /**
     * 是否全部选择结束
     */
    boolean isAllSelectedOver() {
        for (int i = 0; i < getPickerViewCount(); i++) {
            PickerView pickerView = getPickerView(i);
            if (pickerView.getVisibility() == VISIBLE && !pickerView.isSelectedOver()) {
                return false;
            }
        }
        return true;
    }

    private int getPickerViewPosition(PickerView pickerView) {
        for (int i = 0; i < getPickerViewCount(); i++) {
            if (getPickerView(i) == pickerView) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public void onItemClicked(PickerView pickerView, int position1, IPickerData data) {
        int level = getPickerViewPosition(pickerView);

        if (isLinkedMode) {
            //每次选择操作后计算后续的列表数量
            int maxVisibleCount = level + 1;
            if (data.nodes() != null) {
                maxVisibleCount += getNextLevelCount(data.nodes());
                //auto create next level PickerView
                int nextPosition = level + 1;
                if (getPickerViewCount() > nextPosition) {
                    getPickerView(nextPosition).setVisibility(VISIBLE);
                    getPickerView(nextPosition).setData(data.nodes());
                } else {
                    createNextPickerView(pickerView, level, nextPosition, data.nodes());
                }
            }

            //Log.d(TAG, "MaxVisibleCount:" + maxVisibleCount);
            for (int i = level + 1; i < getPickerViewCount(); i++) {
                boolean val = (i < maxVisibleCount);
                //Log.d(TAG, "level:" + i + " = " + val);
                getPickerView(i).setVisibility(val ? VISIBLE : GONE);
            }
        }


        if (getPickerView(level).isMultiSelect) {
            //如果当前列表是多选并且上一级列表需要显示数量则需要刷新上级列表数据
            if (level - 1 > 0 && getPickerView(level - 1).isShowPickCount)
                getPickerView(level - 1).refreshPickCount();
        } else {
            //如果当前列表是单选并且下一级列表是多选则需要刷新列表计数
            if (level + 1 < getPickerViewCount() && getPickerView(level + 1).isMultiSelect) {
                pickerView.refreshPickCount();
            }
        }

        if (onPickerViewItemClickedListener != null)
            onPickerViewItemClickedListener.onPickerViewItemClicked(pickerView, level, data);

        //如果确认按钮不可见并且所有选择已经选中则触发选择完成事件
        if (btnConfirm.getVisibility() != VISIBLE && isAllSelectedOver()) {
            onPicked();
        }
    }
}
