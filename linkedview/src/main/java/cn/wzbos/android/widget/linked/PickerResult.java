package cn.wzbos.android.widget.linked;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择结果集对象
 * Created by wuzongbo on 2019/4/21.
 */
public class PickerResult {

    private List<List<IPickerData>> mSelectValues;


    PickerResult(List<List<IPickerData>> mSelectValues) {
        this.mSelectValues = mSelectValues;
    }

    /**
     * 获取所有列的选择结果集
     *
     * @return result [[IPickerData],[IPickerData],[IPickerData...IPickerData]]
     */
    public List<List<IPickerData>> getSelectVaules() {
        return mSelectValues;
    }


    /**
     * 获取指定列的选择结果集
     *
     * @param index 列的索引
     * @return result [IPickerData,IPickerData,...,IPickerData]
     */
    public List<? extends IPickerData> getArray(int index) {
        if (this.mSelectValues != null && this.mSelectValues.size() > index) {
            return this.mSelectValues.get(index);
        }
        return null;
    }


    /**
     * 获取每一列的选择结果集
     *
     * @return result ["1"]["1"]["1,2,3"]
     */
    public List<List<String>> getIdArray() {
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < mSelectValues.size(); i++) {
            values.add(getIds(i));
        }
        return values;
    }


    /**
     * 获取每一列的选择结果集
     *
     * @param split 分割字符
     * @return result ["1"]["1"]["1,2,3"]
     */
    public List<String> getSplitIdArray(String split) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < mSelectValues.size(); i++) {
            values.add(getSplitIds(i, split));
        }
        return values;
    }

    /**
     * 获取每一列的选择结果集
     *
     * @param split 分割字符
     * @return result ["name"]["name"]["name,name,name"]
     */
    public List<String> getSplitNameArray(String split) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < mSelectValues.size(); i++) {
            values.add(getSplitNames(i, split));
        }
        return values;
    }


    /**
     * 获取指定列的选择ID结果集
     *
     * @param index 列的索引
     * @return result ["1","2",...,"8"]
     */
    public List<String> getIds(int index) {
        List<String> values = null;
        if (this.mSelectValues != null && this.mSelectValues.size() > index) {
            values = new ArrayList<>();
            for (IPickerData data : this.mSelectValues.get(index)) {
                values.add(data.getId());
            }
        }
        return values;
    }

    /**
     * 获取指定列的ID选择结果集，以字符串分割形式返回
     *
     * @param index index 列的索引
     * @param split 分割字符
     * @return result "1,2,...,8"
     */
    public String getSplitIds(int index, String split) {
        if (this.mSelectValues != null && this.mSelectValues.size() > index) {
            StringBuilder val = new StringBuilder();
            for (IPickerData pickerData : this.mSelectValues.get(index)) {
                if (val.length() > 0)
                    val.append(split);
                val.append(pickerData.getId());
            }
            return val.toString();
        }
        return null;
    }

    /**
     * 获取指定列的Name选择结果集
     *
     * @param index index 列的索引
     * @return result ["name","name",...,"name"]
     */
    public List<String> getNames(int index) {
        List<String> values = null;
        if (this.mSelectValues != null && this.mSelectValues.size() > index) {
            values = new ArrayList<>();
            for (IPickerData data : this.mSelectValues.get(index)) {
                values.add(data.getName());
            }
        }
        return values;
    }

    /**
     * 获取指定列的Name选择结果集，以字符串分割形式返回
     *
     * @param index index 列的索引
     * @param split 分割字符
     * @return result "name,name,...,name"
     */
    public String getSplitNames(int index, String split) {
        if (this.mSelectValues != null && this.mSelectValues.size() > index) {
            StringBuilder val = new StringBuilder();
            for (IPickerData pickerData : this.mSelectValues.get(index)) {
                if (val.length() > 0)
                    val.append(split);
                val.append(pickerData.getName());
            }
            return val.toString();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String ids : getSplitIdArray(",")) {
            if (builder.length() > 0)
                builder.append("\n");
            builder.append(ids);
        }
        return builder.toString();

    }
}
