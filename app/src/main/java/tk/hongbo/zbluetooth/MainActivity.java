package tk.hongbo.zbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE = 10010;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            //提示用户打开蓝牙
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, REQUEST_ENABLE);
        }
        startScmeraListener();
        bluetoothAdapter.startDiscovery(); //开始搜索设备
        getBluetoothInfo();
    }

    public void getBluetoothInfo() {
        String name = bluetoothAdapter.getName(); //获取本机蓝牙名称
        String address = bluetoothAdapter.getAddress(); //获取本地蓝牙地址
        Log.d(TAG, "本机蓝牙名称：" + name + "，地址为：" + address);

        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        Log.d(TAG, "本机蓝牙数量为：" + bluetoothDevices.size());

        for (BluetoothDevice device : bluetoothDevices) {
            Log.d(TAG, "连接蓝牙设备名称：" + device.getName() + "，地址为：" + device.getAddress());
        }
    }

    public void startScmeraListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND); //发现设备
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //设备连接状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //蓝牙设备状态改变
        registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //扫描到新设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (scanDevice == null || scanDevice.getName() == null) return;
                Log.d(TAG, "搜索到设备，名称为" + scanDevice.getName() + "，地址为" + scanDevice.getAddress());

                String name = scanDevice.getName();
                if(name != null && name.equals("SAM")){
                    bluetoothAdapter.cancelDiscovery(); //取消扫描
//                    mBlthChatUtil.connect(scanDevice);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
        }
    };
}
