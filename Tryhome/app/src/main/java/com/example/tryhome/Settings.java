package com.example.tryhome;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Settings extends AppCompatActivity implements SensorEventListener {
    private static int valueMaxBrg = 4096; // For the dev's device set it to 4096 for emulation set it to 255
    /**
     * Variables
     */
    public BluetoothConnectionService bluetoothConnection;

    public ArrayList<BluetoothDevice> devices = new ArrayList<>();
    public ArrayList<BluetoothDevice> devicesAppaired = new ArrayList<>();
    private TextView textViewBT;
    private TextView textViewListBTAppaired;
    private BluetoothAdapter bluetoothAdapter;
    private Button buttonsearch;

    private BroadcastReceiver broadcastReceiver;
    private Boolean isBroadcastRegsitered = false;
    private Button back2;

    public BluetoothDevice device;

    public ListView deviceList;
    public ListView deviceAppairedList;
    public BluetoothArrayAdapter adapter, adapter2;

    public CheckBox boxAuto;
    public SensorManager mySensorManager;
    public Sensor sensorLight;

    private Boolean success = false, bool;
    private int brightness = 0;

    private final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private SeekBar seekBar;

    private Button send;
    private Activity activity;
    private EditText edit2text;

    /**
     * This is the elemens call when the activity is create, the listenners and initialisations will be there or called from here
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            checkBTPermissions();
            getPermissionBrightness();
            checkSensor();
        }
        setContentView(R.layout.activity_settings);
        /**
         * Pop up part
         */
        this.activity = this;

        /**
         SeekBar Part
         */
        seekBar = findViewById(R.id.brightnessBar);
        seekBar.setMax(valueMaxBrg);
        seekBar.setProgress(getBrightness());
        seekBar.setKeyProgressIncrement(1);

        /**
         Sensors part
         */
        boxAuto = findViewById(R.id.autoLight);
        boxAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boxAuto.isChecked()&& success) {
                    Toast.makeText(Settings.this, "The brightness will be set automatically", Toast.LENGTH_LONG).show();
                    mySensorManager.registerListener(/*(SensorEventListener)*/ Settings.this, sensorLight, mySensorManager.SENSOR_DELAY_NORMAL);

                } else {
                    if (success) {
                        mySensorManager.unregisterListener(Settings.this);
                    }else{
                        Toast.makeText(Settings.this, "Permissions aren't granted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /**
         User Interface Part
         */
        deviceList = findViewById(R.id.listViewBTSearch);
        deviceAppairedList = findViewById(R.id.listViewBTAppaired);
        textViewBT = findViewById(R.id.textViewBluetooth);
        buttonsearch = findViewById(R.id.buttonSearch);
        textViewListBTAppaired = findViewById(R.id.textViewListBluetoothAppaired);
        back2 = findViewById(R.id.buttonBack2);
        adapter = new BluetoothArrayAdapter(this, R.layout.bt_devices, devices);
        deviceList.setAdapter(adapter);
        adapter2 = new BluetoothArrayAdapter(this, R.layout.bt_devices, devicesAppaired);
        deviceAppairedList.setAdapter(adapter2);
        /**
         Bluetooth Part
         */
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (bluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                    if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                        buttonsearch.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "The broadcast on BT changing works", Toast.LENGTH_SHORT).show();
                    } else {
                        buttonsearch.setEnabled(true);
                    }
                    Toast.makeText(getApplicationContext(), "State change", Toast.LENGTH_SHORT).show();//bluetooth not supported
                    Log.i("settings", "BT change");
                }
                if (bluetoothAdapter.ACTION_REQUEST_ENABLE.equals(intent.getAction())) {
                    Log.d("settings", "BT enable");
                }
                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction()) && !devices.contains((intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)))) {
                    isBroadcastRegsitered = true;
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getName() != null) {
                        Log.i("Settings", "a device has been found :" + device.getName());
                        Toast.makeText(getApplicationContext(), "New device: " + device.getName(), Toast.LENGTH_SHORT).show();
                        devices.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                    Log.i("Settings", "Search has finished");
                    textViewBT.setText("Search finished");
                    //updateListViewMain();
                }
            }
        };
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Error Bluetooth, bluetooth not supported", Toast.LENGTH_SHORT).show();//bluetooth not supported
            buttonsearch.setEnabled(false);
            textViewBT.setText("No bluetooth module available");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show(); //bluetooth activated
                textViewBT.setText("Bluetooth activated");

            } else {
                textViewBT.setText("Bluetooth module available, please turn it on then go back to the main menu and reload the settings");
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
                if (!bluetoothAdapter.isEnabled()) {
                    buttonsearch.setEnabled(false);
                } else {
                    buttonsearch.setEnabled(true);
                }
            }
        }

        buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bluetoothAdapter.isDiscovering() == Boolean.TRUE) {
                    bluetoothAdapter.cancelDiscovery();
                    textViewBT.setText("Search canceled");
                    updateListViewMain();

                } else {
                    devices.clear();
                    bluetoothAdapter.startDiscovery();
                    textViewBT.setText("Search in progress...");
                    registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
                    registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

                }
            }
        });

        if ((bluetoothAdapter != null) && (bluetoothAdapter.isEnabled())) {
            Set<BluetoothDevice> periphAppaires = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice bluetoothDevice : periphAppaires) {
                devicesAppaired.add(bluetoothDevice);
                adapter2.notifyDataSetChanged();
            }
        }
        back2.setOnClickListener(new View.OnClickListener() {       //button to go back to the main Menu
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });// end of the listener of the button back

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * This function take three parameters in entry to react if the user change the luminosity via the bar
             * @param seekBar b
             * @param progress b
             * @param fromUser b
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && success) {
                    setBrightness(progress);
                }
            }

            /**
             * This function deals what has to be done when the user start to touch the seekbar
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * This function deals what has to be done when the user stop to touch the seek bar
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!success) {
                    Toast.makeText(Settings.this, "Permission Not granted", Toast.LENGTH_SHORT).show();
                    ;
                }
            }
        });
        if(success) {
            setBrightness(200);
        }
        deviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = VERSION_CODES.KITKAT)
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                bluetoothAdapter.cancelDiscovery();

                Log.d("Settings_Pairing", "onItemClick: You Clicked on a device.");
                String deviceName = devices.get(position).getName();
                String deviceAddress = devices.get(position).getAddress();

                Log.d("Settings_Pairing", "onItemClick: deviceName = " + deviceName);
                Log.d("Settings_Pairing", "onItemClick: deviceAddress = " + deviceAddress);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Log.d("Settings_Pairing", "Trying to pair with " + devices.get(position).getName());
                    devices.get(position).createBond();
                    device = devices.get(position);
                    if (device.getBondState() == 12) {
                        Toast.makeText(getApplicationContext(), "Your device :" + device.getName() + " has been paired successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        if (device.getBondState() == 11) {
                            Toast.makeText(getApplicationContext(), "Your device :" + device.getName() + " is pairing", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Your device :" + device.getName() + " has not been paired", Toast.LENGTH_SHORT).show();

                        }
                    }


                    //Toast.makeText(getApplicationContext(), "Your device :"+device.getName() + " has been paired unless the user said no", Toast.LENGTH_SHORT).show();
                    if ((device.getName().equals("RNBT-B100") /*the following par is ONLY for testing */ || (device.getName().equals("olivier")))) {    //RNBT-1100 is the name of the bluetooth module on the robot
                        AlertDialog.Builder myPopup = new AlertDialog.Builder(activity);
                        myPopup.setTitle("You selected : " + device.getName());
                        myPopup.setMessage("Would you like to open the control menu of the reobot?");
                        myPopup.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Clicked 'Yes'", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Settings.this, Steering.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        myPopup.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Clicked 'No'", Toast.LENGTH_SHORT).show();
                            }
                        });
                        myPopup.show();
                    }

                }
                return true;
            }
        });
    }

    /**
     * This function interact with the system to adjust the brightness of the screen
     *
     * @param brightnessLocal the value we want to assing to the brightness
     */
    public void setBrightness(int brightnessLocal) {
        if (brightnessLocal < 0) {
            brightnessLocal = 0;
        } else {
            if (brightnessLocal > valueMaxBrg) {
                brightnessLocal = valueMaxBrg;
            }
        }
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightnessLocal);
        //seekBar.setProgress(getBrightness());
    }

    /**
     * This function allows to know the value of the brightness of the screen
     *
     * @return the value of the brightness
     */
    public int getBrightness() {
        try {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            brightness = android.provider.Settings.System.getInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }

    /**
     * Check the permission to deals with the brightness of the screen
     */
    private void getPermissionBrightness() {

        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            bool = android.provider.Settings.System.canWrite(getApplicationContext());
            if (bool) {
                success = true;
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, 1000);
            }
        }
    }

    /**
     * This functions deals with the
     *
     * @param requestCode the code request
     * @param resultCode  the resultCode
     * @param data        an intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                bool = android.provider.Settings.System.canWrite(getApplicationContext());
                if (bool) {
                    success = true;
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    ;
                }
            }
        }
    }

    /**
     * Check if the permission for bluetooth are corrects
     */
    @RequiresApi(api = VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manisfest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            } else {
                Log.d("Settings", "Check permission for BT");
            }
        }
    }

    /**
     * Check if there is a bluetooth module on the device
     *
     * @return true if there is a bluetooth module, false if there is no bluetooth module
     */
    private boolean checkForBTModule() {
        Boolean module;
        if (bluetoothAdapter == null) {
            module = false;
        } else {
            module = true;
        }
        return module;
    }

    /**
     * This function refresh the adapter with the news items in the lists
     * Should bettre call .notifyDataSetChanged on the lists
     */
    public void updateListViewMain() {

        adapter = new BluetoothArrayAdapter(this, R.layout.bt_devices, devices);
        deviceList.setAdapter(adapter);
        adapter2 = new BluetoothArrayAdapter(this, R.layout.bt_devices, devicesAppaired);
        deviceAppairedList.setAdapter(adapter2);
    }

    /**
     * What has to be done when the application is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener((SensorEventListener) this);
    }

    /**
     * What has to be done when the application is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(/*(SensorEventListener)*/ Settings.this, sensorLight, mySensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * check if the sensor (Light sensor) is avaible on the device and block the Automod if there is no light sensor
     */
    public void checkSensor() {
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensorLight == null) {
            Toast.makeText(this, "No light sensor found", Toast.LENGTH_SHORT).show();
            boxAuto.setChecked(false);
            boxAuto.setActivated(false);
        } else {
            Toast.makeText(this, "Light sensor found", Toast.LENGTH_SHORT).show();
            mySensorManager.unregisterListener(Settings.this);

        }
    }

    /**
     * Implements what we do when the activity is destroyed (finished)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Settings", "On destroy called");
        if (!checkForBTModule() || !isBroadcastRegsitered) {

        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            unregisterReceiver(broadcastReceiver);
            isBroadcastRegsitered = false;
        }

    }

    /**
     * Implements the changes when the Light sensor change
     *
     * @param event is the event when the sensors change
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (boxAuto.isChecked()) {
                brightness = (int) (event.values[0]); // convert value, Max is 40 000 and max brightness is 255 for the emulated device so 40 000/ 255 equals to 156.8627 40 000/4096 equals 9.765625
                setBrightness(brightness);
                seekBar.setProgress(getBrightness());
            }
        }
    }

    /**
     * Function not used, to do something when the accuracy or precision of a sensor change
     *
     * @param sensor   the sensor
     * @param accuracy its accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
<<<<<<< Updated upstream
=======
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu:
                Toast.makeText(getApplicationContext(), "OLIVIER", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(Settings.this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
>>>>>>> Stashed changes
}
