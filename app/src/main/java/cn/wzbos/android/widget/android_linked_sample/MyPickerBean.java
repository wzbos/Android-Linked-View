package cn.wzbos.android.widget.android_linked_sample;

import java.util.List;

import cn.wzbos.android.widget.linked.IPickerData;

public class MyPickerBean implements IPickerData {
    private int id;
    private String name;
    private boolean selected;
    private List<MyPickerBean> items;

    MyPickerBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public List<MyPickerBean> nodes() {
        return items;
    }

}
