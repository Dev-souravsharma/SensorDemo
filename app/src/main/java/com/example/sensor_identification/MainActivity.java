package com.example.sensor_identification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView sensors;
    private List<Sensor> list;
    // Individual light and proximity sensors.
    private Sensor mSensorProximity;
    private Sensor mSensorLight;

    // TextViews to display current sensor values
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensors = findViewById(R.id.edtSensors);
        mTextSensorLight = (TextView) findViewById(R.id.edtLightSensor);
        mTextSensorProximity = (TextView) findViewById(R.id.edtProximity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Getting Default sensor
        mSensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        listOfSensor();
        defaultSensor();

    }

    private void defaultSensor() {
        if (mSensorLight == null)
            mTextSensorLight.setText(R.string.error_sensor);
        if (mSensorProximity == null)
            mTextSensorProximity.setText(R.string.error_sensor);
    }

    private void listOfSensor() {
        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : list) {
            sensorText.append(currentSensor.getName()).append(
                    "\n\n");
        }
        sensors.setText(sensorText);
        sensors.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorProximity != null) {
            sensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        if (mSensorLight != null) {
            sensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                // Handle light sensor
                mTextSensorLight.setText(getResources().getString(
                        R.string.light_label, currentValue));
                break;
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(R.string.proximity_label, currentValue));
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}