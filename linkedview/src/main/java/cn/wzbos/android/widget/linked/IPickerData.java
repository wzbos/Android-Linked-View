package cn.wzbos.android.widget.linked;


import java.util.List;

/**
 * Created by wuzongbo on 2018/1/2.
 */

public interface IPickerData {

    String getId();

    String getName();

    boolean isSelected();

    void setSelected(boolean selected);

    List<? extends IPickerData> nodes();
}
