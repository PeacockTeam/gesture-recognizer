package org.peacockteam.gu;

import java.io.IOException;

import org.wiigee.control.AndroidWiigee;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.ButtonListener;
import org.wiigee.event.ButtonPressedEvent;
import org.wiigee.event.ButtonReleasedEvent;
import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;
import org.wiigee.filter.DirectionalEquivalenceFilter;
import org.wiigee.filter.HighPassFilter;
import org.wiigee.filter.IdleStateFilter;
import org.wiigee.filter.LowPassFilter;
import org.wiigee.filter.MotionDetectFilter;
import org.wiigee.logic.TriggeredProcessingUnit;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class GesturesUnlockActivity extends Activity implements GestureListener,
	AccelerationListener {
	
	private SensorManager mSensorManager;
	
	final private AndroidWiigee wiigee = new AndroidWiigee();
	
	private static int TRAIN_BUTTON = 1;
	private static int CLOSE_GESTURE_BUTTON = 2;
	private static int RECOGNITION_BUTTON = 3;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

//		wiigee.getDevice().resetAccelerationFilters();
//		wiigee.getDevice().addAccelerationFilter(new LowPassFilter());
//		wiigee.getDevice().addAccelerationFilter(new IdleStateFilter());

//		wiigee.getDevice().addAccelerationFilter(new IdleStateFilter());
//		wiigee.getDevice().addAccelerationFilter(new MotionDetectFilter(this));
//		wiigee.getDevice().addAccelerationFilter(new DirectionalEquivalenceFilter());
		
        wiigee.setCloseGestureButton(CLOSE_GESTURE_BUTTON);
        wiigee.setRecognitionButton(RECOGNITION_BUTTON);
        wiigee.setTrainButton(TRAIN_BUTTON);

        // Some controls for wiigee

        final Button button_train = (Button) findViewById(R.id.button_train);
        final Button button_recognition = (Button) findViewById(R.id.button_recognition);
        final Button button_close_gesture = (Button) findViewById(R.id.button_close_gesture);
        final TextView textview_status = (TextView) findViewById(R.id.textView_status);

        wiigee.addGestureListener(new GestureListener() {

			@Override
			public void gestureReceived(GestureEvent event) {
				Log.i(getClass().toString(), "Geastures " + event);
				
				textview_status.setText("Rec: " + event.getId() + " Prob:" + event.getProbability());
			}
		});
        
        button_train.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        
        button_recognition.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					textview_status.setText("Recognition...");
					wiigee.getDevice().fireButtonPressedEvent(RECOGNITION_BUTTON);
				}
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					textview_status.setText("Recognition done");
					wiigee.getDevice().fireButtonReleasedEvent(RECOGNITION_BUTTON);
				}
				
				return false;
			}
		});
        
        button_train.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					textview_status.setText("Training...");
					wiigee.getDevice().fireButtonPressedEvent(TRAIN_BUTTON);
				}
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					textview_status.setText("Train done");
					wiigee.getDevice().fireButtonReleasedEvent(TRAIN_BUTTON);
				}
				
				return false;
			}
		});
        
        button_close_gesture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textview_status.setText("Gesture closed");
				wiigee.getDevice().fireButtonPressedEvent(CLOSE_GESTURE_BUTTON);
			}
		});
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		mSensorManager.registerListener(wiigee.getDevice(), 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
		try {
			wiigee.getDevice().setAccelerationEnabled(true);
		} catch (IOException e) {
			Log.e(getClass().toString(), e.getMessage(), e);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(wiigee.getDevice());
		try {
			wiigee.getDevice().setAccelerationEnabled(false);
		} catch (Exception e) {
			Log.e(getClass().toString(), e.getMessage(), e);
		}
	}

	@Override
	public void accelerationReceived(AccelerationEvent event) {
		// TODO Auto-generated method stub
		Log.i(getClass().toString(), "accelerationReceived:" + event.toString());
	}

	@Override
	public void motionStartReceived(MotionStartEvent event) {
		// TODO Auto-generated method stub
		Log.i(getClass().toString(), "motionStartReceived:" + event.toString());
	}

	@Override
	public void motionStopReceived(MotionStopEvent event) {
		// TODO Auto-generated method stub
		Log.i(getClass().toString(), "motionStopReceived:" + event.toString());
	}

	@Override
	public void gestureReceived(GestureEvent event) {
		// TODO Auto-generated method stub
		Log.i(getClass().toString(), "gestureReceived:" + event.toString());
	}
}