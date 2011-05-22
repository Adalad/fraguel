package fraguel.android.notifications;

import fraguel.android.states.MapState;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class BackKeyNotification implements DialogInterface.OnKeyListener{

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if (arg2.getKeyCode()==KeyEvent.KEYCODE_BACK){
			MapState.getInstance().setContextMenuDisplayed(false);
			MapState.getInstance().setChooseAnotherRoute(false);
			MapState.getInstance().setChooseAnotherPoint(false);
			MapState.getInstance().setContextRoute(null);
			arg0.dismiss();
			return true;
		}
		return false;
	}

}
