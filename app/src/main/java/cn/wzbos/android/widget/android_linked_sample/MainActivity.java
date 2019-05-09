package cn.wzbos.android.widget.android_linked_sample;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.wzbos.android.widget.linked.ILinked;
import cn.wzbos.android.widget.linked.IPickerData;
import cn.wzbos.android.widget.linked.LinkedPopupWindow;
import cn.wzbos.android.widget.linked.LinkedView;
import cn.wzbos.android.widget.linked.PickerResult;
import cn.wzbos.android.widget.linked.PickerView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    LinkedView linkedView;
    LinkedView simpleLinkedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkedView = findViewById(R.id.simpleLinkedView);
        simpleLinkedView = findViewById(R.id.linkedView);
        initLinkedView();
        initPriceView();
    }

    public List<MyPickerBean> getData() {
        List<MyPickerBean> list = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("area.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }

            list = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<MyPickerBean>>() {
            }.getType());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_simple:
                linkedView.setVisibility(View.GONE);
                if (simpleLinkedView.getVisibility() != View.VISIBLE) {
                    simpleLinkedView.restore();
                    simpleLinkedView.setVisibility(View.VISIBLE);
                } else {
                    simpleLinkedView.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_linked:
                simpleLinkedView.setVisibility(View.GONE);
                if (linkedView.getVisibility() != View.VISIBLE) {
                    linkedView.restore();
                    linkedView.setVisibility(View.VISIBLE);
                } else {
                    linkedView.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_single: {
                LinkedPopupWindow popupWindow = new LinkedPopupWindow(this);
                popupWindow.setCancelButtonVisible(false);
                popupWindow.setConfirmButtonVisible(false);
                popupWindow.addPickerView(new PickerView(this)
                        .setTxtAlignment(View.TEXT_ALIGNMENT_CENTER)
                        .setStateBackground(R.drawable.bg_item_pop)
                        .setShowDivider(true)
                        .setData(getPickerData()));
                popupWindow.setOnPickedListener((linkView, result) -> Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show());
                popupWindow.showAtBottom(view);
            }
            break;
            case R.id.btn_double: {
                LinkedPopupWindow popupWindow = new LinkedPopupWindow(this);
                popupWindow.setDivider(true);
                popupWindow.addPickerView(new PickerView(this)
                        .setWidth(200)
                        .setShowDivider(true)
                        .setStateBackground(R.drawable.bg_item_pop)
                        .setTxtAlignment(View.TEXT_ALIGNMENT_CENTER)
                        .setData(getPickerData()));
                popupWindow.addPickerView(new PickerView(this)
                        .setData(getPickerData())
                        .setShowDivider(true)
                        .setTxtAlignment(View.TEXT_ALIGNMENT_CENTER)
                        .setStateBackground(R.drawable.bg_item_pop));
                popupWindow.setOnPickedListener((linkView, result) -> Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show());
                popupWindow.showAtBottom(view);
            }
            break;
        }
    }

    void initPriceView() {
        simpleLinkedView.setBackgroundColor(0xFFFFFFFF);
        simpleLinkedView.setResetButtonVisible(false);
        simpleLinkedView.setDivider(true);
        simpleLinkedView.setConfirmButtonVisible(false);
        simpleLinkedView.addPickerView(new PickerView(this)
                .setShowIcon(true)
                .setMultiSelect(false)
                .setWeight(1)
                .setShowDivider(true)
                .setData(getPickerData()));
        simpleLinkedView.setOnCreatePickerViewListener((prevView, prevPosition, nextView, nextPosition) -> Log.d(TAG, "onCreatePickerView"));
        simpleLinkedView.setOnPickedListener((linkView, result) -> Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show());
    }


    private void initLinkedView() {
        linkedView.setLinkedMode(true);
        linkedView.setDivider(true);
        linkedView.setOnCreatePickerViewListener((prevView, prevPosition, nextView, nextPosition) -> {
            switch (nextPosition) {
                case 0:
                    nextView.setWidth(300);
                    nextView.setWeight(0);
                    break;
                case 1: {
                    List<IPickerData> values = nextView.getSelectedValues();
                    boolean isd = (values.size() > 0 && "附近".equals(values.get(0).getName()));
                    nextView.setShowIcon(isd);
                    nextView.setShowPickCount(!isd);
                }
                break;
                case 2:
                    nextView.setMultiSelect(true);
                    nextView.setShowIcon(true);
                    break;
            }
            nextView.setBackgroundColor(((nextPosition + 1) % 2) == 0 ? 0xFFF5F8FC : 0xFFFFFFFF);
        });
        linkedView.setData(getData());
        linkedView.setOnPickerViewItemClickedListener((pickerView, position, data) -> {
            if (position == 0) {
                PickerView option = linkedView.getPickerView(1);
                if (option != null) {
                    boolean isd = "附近".equals(data.getName());
                    option.setShowIcon(isd);
                    option.setShowPickCount(!isd);
                }
            } else if (position == 1) {
                PickerView option = linkedView.getPickerView(2);
                if (option != null) {
                    option.setShowIcon(true);
                    option.setMultiSelect(true);
                }
            }
        });
        linkedView.setOnPickedListener(new ILinked.OnPickedListener() {
            @Override
            public void onPicked(ILinked linkView, PickerResult result) {
                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ArrayList<MyPickerBean> getPickerData() {
        ArrayList<MyPickerBean> list1 = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list1.add(new MyPickerBean(i, String.valueOf((char) ('A' + i))));
        }
        return list1;
    }

}
