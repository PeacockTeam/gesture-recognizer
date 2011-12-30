package org.peacockteam.gu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openintents.sensorsimulator.db.SensorSimulator;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;
import org.wiigee.control.AndroidWiigee;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class GesturesUnlockActivity extends Activity {
	
	private SensorManager mSensorManager;
	private SensorManagerSimulator mSensorManagerSimulator;
	
	final private AndroidWiigee wiigee = new AndroidWiigee();
	
	private static int TRAIN_BUTTON = 1;
	private static int CLOSE_GESTURE_BUTTON = 2;
	private static int RECOGNITION_BUTTON = 3;
	
	// TODO: let user change ulr from app 
	private static String ACCLAB_URL = "http://192.168.0.189:3000/1.0/acc/put";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (isEmulator()) {
        	mSensorManagerSimulator = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        	mSensorManagerSimulator.connectSimulator();
        } else {
        	mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        }

        // We use IMEI code for identity this device (really?) in acclab
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String  deviceId = telephonyManager.getDeviceId();
        
        if (deviceId.equals("000000000000000")) {
        	// XXX: do not pull this data
        	// or trying generate unqique id for emulator (wifi, mac, or?)
        }
        
//		wiigee.getDevice().resetAccelerationFilters();
//		wiigee.getDevice().addAccelerationFilter(new LowPassFilter());
//		wiigee.getDevice().addAccelerationFilter(new IdleStateFilter());

//		wiigee.getDevice().addAccelerationFilter(new IdleStateFilter());
//		wiigee.getDevice().addAccelerationFilter(new MotionDetectFilter(this));
//		wiigee.getDevice().addAccelerationFilter(new DirectionalEquivalenceFilter());
		
        wiigee.getDevice().addAccelerationListener(new AccelerationListener() {
			
			@Override
			public void motionStopReceived(MotionStopEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void motionStartReceived(MotionStartEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void accelerationReceived(AccelerationEvent event) {
				JSONObject data = new JSONObject();
				
				try {
					data.put("timestamp", new Date().getTime())
						.put("device_id", deviceId)
						.put("device_name", "Emulator")
						.put("data", new JSONObject()
										.put("x", event.getX())
										.put("y", event.getY())
										.put("z", event.getY()));

					postData(data.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.prefs:
        	startActivity(new Intent(getApplicationContext(), PreferencesView.class));
            return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		if (isEmulator()) {
			mSensorManagerSimulator.registerListener((SensorEventListener) wiigee.getDevice(), 
					mSensorManagerSimulator.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
					SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			mSensorManager.registerListener((android.hardware.SensorEventListener) wiigee.getDevice(), 
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		try {
			wiigee.getDevice().setAccelerationEnabled(true);
		} catch (IOException e) {
			Log.e(getClass().toString(), e.getMessage(), e);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		if (isEmulator()) {
			mSensorManagerSimulator.unregisterListener((SensorEventListener) wiigee.getDevice());
		} else {
			mSensorManager.unregisterListener((android.hardware.SensorEventListener) wiigee.getDevice());
		}
		
		try {
			wiigee.getDevice().setAccelerationEnabled(false);
		} catch (Exception e) {
			Log.e(getClass().toString(), e.getMessage(), e);
		}
	}

	public static boolean isEmulator() {
	    return Build.MANUFACTURER.equals("unknown");
	}
	
	// XXX: rewrite this!
	public void postData(String data) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(ACCLAB_URL);

	    try {        
	        httppost.setEntity(new StringEntity(data.toString()));
	        httppost.setHeader("Content-type", "application/json");
	        
	        HttpResponse response = httpclient.execute(httppost);
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}
}
