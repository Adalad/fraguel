package fraguel.android.notifications;

import java.util.ArrayList;

import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.states.MainMenuState;
import android.content.DialogInterface;
import android.widget.Toast;

public class CaptureCoordinatesButton implements DialogInterface.OnClickListener{

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		MainMenuState state = (MainMenuState)FRAGUEL.getInstance().getCurrentState();
		ArrayList<PointOI> points= state.getGeoTaggingPoints();
		points.add(new PointOI());
		Toast.makeText(FRAGUEL.getInstance().getApplicationContext(),"Hay "+state.getGeoTaggingPoints().size() +" puntos guardados", Toast.LENGTH_LONG).show();
		
	}

}
