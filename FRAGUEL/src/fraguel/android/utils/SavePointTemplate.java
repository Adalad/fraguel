package fraguel.android.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class SavePointTemplate  extends LinearLayout{
	
	private TextView latitudeGPS,longitudeGPS;

	public SavePointTemplate(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		
		TextView label= new TextView (context);
		label.setText("Coordenadas GPS");
		label.setGravity(Gravity.CENTER_HORIZONTAL);
		this.addView(label);
		
		/*LinearLayout wholeGPSContent = new LinearLayout(context);
		wholeGPSContent.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		wholeGPSContent.setOrientation(LinearLayout.VERTICAL);*/
		
		LinearLayout layoutLatitude = new LinearLayout(context);
		layoutLatitude.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layoutLatitude.setOrientation(LinearLayout.HORIZONTAL);
		layoutLatitude.setPadding(10, 10, 0, 0);
		
		TextView latitude= new TextView (context);
		latitude.setText("Latitud: ");
		latitudeGPS = new TextView(context);
		latitudeGPS.setText("waiting for data...");
		
		layoutLatitude.addView(latitude);
		layoutLatitude.addView(latitudeGPS);
		this.addView(layoutLatitude);
		
		
		LinearLayout layoutLongitude = new LinearLayout(context);
		layoutLongitude.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layoutLongitude.setOrientation(LinearLayout.HORIZONTAL);
		layoutLongitude.setPadding(10, 10, 0, 0);
		
		
		TextView longitude= new TextView (context);
		longitude.setText("Longitud: ");
		longitudeGPS = new TextView(context);
		longitudeGPS.setText("waiting for data...");
		
		layoutLongitude.addView(longitude);
		layoutLongitude.addView(longitudeGPS);
		this.addView(layoutLongitude);
	}
	
	public void setLatitude(float latitude){
		latitudeGPS.setText(Float.toString(latitude));
	}
	public void setLongitude(float longitude){
		longitudeGPS.setText(Float.toString(longitude));
	}
	
}
