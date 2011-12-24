package org.peacockteam.gu;

import org.wiigee.control.AndroidWiigee;
import org.wiigee.control.Wiigee;
import org.wiigee.device.Device;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.ButtonListener;
import org.wiigee.event.ButtonPressedEvent;
import org.wiigee.event.ButtonReleasedEvent;
import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;

public class GesturesUnlockActivity extends Activity implements GestureListener,
	AccelerationListener {
	
	private AndroidWiigee wiigee;
	private String TAG = "GesturesUnlockActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Init wegee
        
        this.wiigee = new AndroidWiigee();
    }

	@Override
	public void accelerationReceived(AccelerationEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "accelerationReceived:" + event.toString());
	}

	@Override
	public void motionStartReceived(MotionStartEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "motionStartReceived:" + event.toString());
	}

	@Override
	public void motionStopReceived(MotionStopEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "motionStopReceived:" + event.toString());
	}

	@Override
	public void gestureReceived(GestureEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "gestureReceived:" + event.toString());
	}
}