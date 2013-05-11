package com.hazuu.android.gpsdemo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hazuu.android.gpsdemo.interfaces.Constants;
import com.hazuu.android.gpsdemo.interfaces.GPSCallback;
import com.hazuu.android.gpsdemo.managers.GPSManager;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements GPSCallback {
	private GPSManager gpsManager = null;
	private double speed = 0.0;
	private AbsoluteSizeSpan sizeSpanLarge = null;
	private AbsoluteSizeSpan sizeSpanSmall = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gpsManager = new GPSManager();

		gpsManager.startListening(getApplicationContext());
		gpsManager.setGPSCallback(this);

		((TextView) findViewById(R.id.gps_info))
				.setText(getString(R.string.gps_info));
	}

	@Override
	public void onGPSUpdate(Location location) {
		location.getLatitude();
		location.getLongitude();
		speed = location.getSpeed();

		String speedString = String.valueOf(roundDecimal(convertSpeed(speed), 2));
		String unitString = "km/h";

		setSpeedText(R.id.gps_info, speedString + " " + unitString);
	}

	@Override
	protected void onDestroy() {
		gpsManager.stopListening();
		gpsManager.setGPSCallback(null);

		gpsManager = null;

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private double convertSpeed(double speed) {
		return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS);
	}

	private double roundDecimal(double value, final int decimalPlace) {
		BigDecimal bd = new BigDecimal(value);

		bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
		value = bd.doubleValue();

		return value;
	}

	private void setSpeedText(int textid, String text) {
		Spannable span = new SpannableString(text);
		int firstPos = text.indexOf(32);

		span.setSpan(sizeSpanLarge, 0, firstPos,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(sizeSpanSmall, firstPos + 1, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		TextView tv = ((TextView) findViewById(textid));

		tv.setText(span);
	}

}
