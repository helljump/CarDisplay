package ru.zipta.cardisplay2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class AndroidLauncher extends AndroidApplication {

    public static final String TAG = AndroidLauncher.class.getSimpleName();
	public static final String ACTION_USB_PERMISSION = "ru.zipta.USB_PERMISSION";
	private UsbDeviceConnection connection;
	private UsbDevice device;
	private UsbSerialDevice serialPort;
	private UsbManager usbManager;
    //eventBus.register(carDisplay.getScreen());
    //eventBus.unregister(carDisplay.getScreen());
	private EventBus eventBus = EventBus.getDefault();
    private CarDisplay carDisplay;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        carDisplay = new CarDisplay(getContext());
        initialize(carDisplay, config);
	}

    public class MessageEvent {
        public MessageEvent(byte[] data) {
            this.data = data;
        }

        final byte[] data;
    }

    private UsbSerialInterface.UsbReadCallback readCallback = new UsbSerialInterface.UsbReadCallback()
    {
        @Override
        public void onReceivedData(byte[] data)
        {
            eventBus.post(new MessageEvent(data));
        }
    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted =
                        intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                Gdx.app.debug(TAG, "permission " + granted + "\n");
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    Gdx.app.debug(TAG, "serial " + serialPort + "\n");
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(readCallback); //
                            Gdx.app.debug(TAG, "serial opened\n");

                        } else {
                            Gdx.app.debug(TAG, "port not opened\n");
                        }
                    } else {
                        Gdx.app.debug(TAG, "port is null\n");
                    }
                } else {
                    Gdx.app.debug(TAG, "perm not granted\n");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                //onClickStart(startButton);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                //onClickStop(stopButton);
            }
        };
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if(!usbDevices.isEmpty())
        {
            boolean keep = true;
            for(Map.Entry<String, UsbDevice> entry : usbDevices.entrySet())
            {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();
                Gdx.app.debug(TAG, "VID:" + deviceVID + " PID:" + devicePID + "\n");
                if (deviceVID == 6790 || deviceVID == 9025)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                            new Intent(ACTION_USB_PERMISSION), 0);
                    Gdx.app.debug(TAG, "request perm\n");
                    usbManager.requestPermission(device, pi);
                    keep = false;
                }else{
                    connection = null;
                    device = null;
                }
                if(!keep){
                    break;
                }
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        if(serialPort!=null) {
            serialPort.close();
            connection.close();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
