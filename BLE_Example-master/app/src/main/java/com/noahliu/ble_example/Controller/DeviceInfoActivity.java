package com.noahliu.ble_example.Controller;

import static android.graphics.Color.rgb;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.noahliu.ble_example.Module.Adapter.ExpandableListAdapter;
import com.noahliu.ble_example.Module.Enitiy.ScannedData;
import com.noahliu.ble_example.Module.Enitiy.ServiceInfo;
import com.noahliu.ble_example.Module.Service.BluetoothLeService;
import com.noahliu.ble_example.R;
import com.noahliu.ble_example.Result_Activity;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoActivity extends AppCompatActivity implements ExpandableListAdapter.OnChildClick {
    public static final String TAG = DeviceInfoActivity.class.getSimpleName() + "My";
    public static final String INTENT_KEY = "GET_DEVICE";
    private BluetoothLeService mBluetoothLeService;
    private ScannedData selectedDevice;
    private TextView tvAddress, tvStatus, tvRespond,Text_now,Text_max,Text_avg;
    private ExpandableListAdapter expandableListAdapter;
    private boolean isLedOn = false;
    private float MAX,MIN,AVG,SUM,COUNT;
    private Button End;

    private boolean isRunning = false;
    private LineChart chart;
    private Thread thread;
    private String AVGG,Maxx;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);
        initBLE();
        initUI();

        MAX=0;
        MIN=15;
        AVG=0;
        SUM=0;
        COUNT=1;
        End=findViewById(R.id.button_End);
        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DeviceInfoActivity.this,Result_Activity.class);
                i.putExtra("AVG",AVGG);
                i.putExtra("MAX",Maxx);
                startActivity(i);
            }
        });

        chart = findViewById(R.id.lineChart);
        /**載入圖表*/
        initChart();
        startRun("0");

    }
    /*----------------------graph-----------------*/

    /**
     * 開始跑圖表
     */

    private void startRun(String data) {
        if (isRunning) return;
        if (thread != null) thread.interrupt();
//            Runnable runnable = new Runnable() {@Override public void run() {}};
        //簡略寫法
        isRunning = true;
        Runnable runnable = () -> {
            //取亂數
            //addData(Integer.valueOf(data).intValue());

        };
//            thread = new Thread(new Runnable()
//            {@Override public void run() {runOnUiThread(runnable);}});
        //簡略寫法
        thread = new Thread(() -> {
            while (isRunning) {
                runOnUiThread(runnable);
                if (!isRunning) break;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 載入圖表
     */
    private void initChart() {
        chart.getDescription().setEnabled(false);//設置不要圖表標籤
        chart.setTouchEnabled(true);//設置不可觸碰
        chart.setDragEnabled(true);//設置不可互動
        //設置單一線數據
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        //設置左下角標籤
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        //設置Ｘ軸
        XAxis x = chart.getXAxis();
        x.setTextColor(Color.GRAY);
        x.setDrawGridLines(true);//畫X軸線
        x.setPosition(XAxis.XAxisPosition.BOTTOM);//把標籤放底部
        x.setLabelCount(21, true);//設置顯示15個標籤
        //設置X軸標籤內容物
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + Math.round(value);
            }
        });
        //
        YAxis y = chart.getAxisLeft();
        y.setTextColor(Color.GRAY);
        y.setDrawGridLines(true);
        y.setAxisMaximum(20);//最高
        y.setAxisMinimum(0);//最低0
        chart.getAxisRight().setEnabled(false);//右邊Y軸不可視
        chart.setVisibleXRange(0, 20);//設置顯示範圍
    }

    /**
     * 新增資料
     */
    private void addData(float inputData) {
        LineData data = chart.getData();//取得原數據
        ILineDataSet set = data.getDataSetByIndex(0);//取得曲線(因為只有一條，故為0，若有多條則需指定)
        if (set == null) {
            set = createSet();
            data.addDataSet(set);//如果是第一次跑則需要載入數據
        }
        data.addEntry(new Entry(set.getEntryCount(), inputData), 0);//新增數據點
                                                                                            //---entry->barentry
        data.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.setVisibleXRange(0, 20);//設置可見範圍
        chart.moveViewToX(data.getEntryCount());//將可視焦點放在最新一個數據，使圖表可移動
    }

    /**
     * 設置數據線的樣式
     */
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "次數");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(rgb(0,100,255));
        set.setLineWidth(2);
        set.setDrawCircles(false);
        set.setFillColor(rgb(20,20,20));
        set.setFillAlpha(80);  //透明度
        set.setDrawFilled(true);
        set.setValueTextColor(Color.GRAY);
        set.setDrawValues(false);
        return set;
    }
/*
    public static void initBarChart(BarChart chart, List<BarEntry> entries, String title, @ColorInt int barColor) {
        BarDataSet set1 = new BarDataSet(entries, title);
        set1.setValueTextColor(Color.WHITE);
        set1.setColor(barColor);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        // 設定bar的寬度，但是點很多少的時候好像沒作用，會拉得很寬
        data.setBarWidth(0.1f);
        // 設定value值 顏色
        data.setValueTextColor(Color.WHITE);
        //設定y軸顯示的標籤
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) (value * 100)) + "%";
            }
        });


        chart.setData(data);
        chart.invalidate();
    }
*/



    /*----------------------------------------------*/
    /**
     * 初始化藍芽
     */
    private void initBLE() {
        /**綁定Service
         * @see BluetoothLeService*/
        Intent bleService = new Intent(this, BluetoothLeService.class);
        bindService(bleService, mServiceConnection, BIND_AUTO_CREATE);
        /**設置廣播*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);//連接一個GATT服務
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);//從GATT服務中斷開連接
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);//查找GATT服務
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);//從服務中接受(收)數據

        registerReceiver(mGattUpdateReceiver, intentFilter);
        if (mBluetoothLeService != null) mBluetoothLeService.connect(selectedDevice.getAddress());  //---BT connect
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        expandableListAdapter = new ExpandableListAdapter();
        expandableListAdapter.onChildClick = this::onChildClick;
        ExpandableListView expandableListView = findViewById(R.id.gatt_services_list);
        expandableListView.setAdapter(expandableListAdapter);
        tvAddress = findViewById(R.id.device_address);
        tvStatus = findViewById(R.id.connection_state);
        //tvRespond = findViewById(R.id.data_value);
        Text_now=findViewById(R.id.now_text);
        Text_max=findViewById(R.id.max_text);
        Text_avg=findViewById(R.id.avg_text);
        tvAddress.setText(selectedDevice.getAddress());
        //tvStatus.setText("未連線");
        tvAddress.setBackgroundColor(rgb(255,0,0));
        //tvRespond.setText("---");

        //tvRespond.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * 藍芽已連接/已斷線資訊回傳
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            mBluetoothLeService.connect(selectedDevice.getAddress());  //-------------------BT connect
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService.disconnect();
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            /**如果有連接*/
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "藍芽已連線");
                //tvStatus.setText("已連線");
                tvAddress.setBackgroundColor(rgb(0,255,0));

            }
            /**如果沒有連接*/
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "藍芽已斷開");

            }
            /**找到GATT服務*/
            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, "已搜尋到GATT服務");
                List<BluetoothGattService> gattList = mBluetoothLeService.getSupportedGattServices();
                displayGattAtLogCat(gattList);
                expandableListAdapter.setServiceInfo(gattList);
            }
            /**接收來自藍芽傳回的資料*/
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "接收到藍芽資訊");
                byte[] getByteData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                StringBuilder stringBuilder = new StringBuilder(getByteData.length);
                for (byte byteChar : getByteData)
                    stringBuilder.append(String.format("%02X ", byteChar));
                String stringData = new String(getByteData);
                Log.d(TAG, "String: " + stringData + "\n"
                        + "byte[]: " + BluetoothLeService.byteArrayToHexStr(getByteData));
                /*tvRespond.append("String: "+stringData+"\n"
                        +"byte[]: "+BluetoothLeService.byteArrayToHexStr(getByteData)+"\n");*/
                //tvRespond.append("\n"+/*"String: " +*/ stringData
                        /*+ "byte[]: " + BluetoothLeService.byteArrayToHexStr(getByteData) + "\n"*///);
                isLedOn = getByteData.equals("486173206F6E");
                //isLedOn = BluetoothLeService.byteArrayToHexStr(getByteData).equals("486173206F6E");

                float avgg=0;
                int to_int;
                float test = 0;
                try{
                    test = Float.parseFloat(stringData);
                    Text_now.setText(stringData);
                }catch(NumberFormatException e){
                    System.out.println("浮點數轉換錯誤");
                }
                if(MAX<test) {
                    MAX=test;
                    Text_max.setText(stringData);
                    Maxx=stringData;
                }
                if(test<MIN){
                    MIN=test;
                }
                SUM=SUM+test;
                avgg=(SUM/COUNT)*1000;
                to_int= (int) (avgg);
                avgg=to_int;
                avgg=avgg/1000;
                AVGG= String.valueOf(avgg);
                Text_avg.setText(AVGG);
                COUNT++;
                addData(test);
            }
        }
    };//onReceive

    /**
     * 將藍芽所有資訊顯示在Logcat
     */
    private void displayGattAtLogCat(List<BluetoothGattService> gattList) {
        for (BluetoothGattService service : gattList) {
            Log.d(TAG, "Service: " + service.getUuid().toString());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                Log.d(TAG, "\tCharacteristic: " + characteristic.getUuid().toString() + " ,Properties: " +
                        mBluetoothLeService.getPropertiesTagArray(characteristic.getProperties()));
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    Log.d(TAG, "\t\tDescriptor: " + descriptor.getUuid().toString());
                }
            }
        }
    }

    /**
     * 關閉藍芽
     */
    private void closeBluetooth() {
        if (mBluetoothLeService == null) return;
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
        unregisterReceiver(mGattUpdateReceiver);

    }

    @Override
    protected void onStop() {
        super.onStop();
        closeBluetooth();
    }

    /**
     * 點擊物件，即寫資訊給藍芽(或直接讀藍芽裝置資訊)
     */
    @Override
    public void onChildClick(ServiceInfo.CharacteristicInfo info) {
        String led = "off";
        if (!isLedOn) led = "on";
        mBluetoothLeService.sendValue(led, info.getCharacteristic());
    }


}
