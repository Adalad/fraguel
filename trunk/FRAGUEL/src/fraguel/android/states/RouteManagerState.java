package fraguel.android.states;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.R;
import fraguel.android.State;
import fraguel.android.lists.RouteManagerAdapter;


public class RouteManagerState extends State {
	
	public static final int STATE_ID = 8;
	// Variables de los botones del menu
	private static final int ROUTEMANAGERSTATE_ADDROUTE = 1;
	private static final int ROUTEMANAGERSTATE_DELETEROUTE = 2;

	LinearLayout container;
	RouteManagerAdapter adapter;
	TextView title;
	ListView list;
	//0->routes,1->points,2->pointData
	int internalState;
	int selectedRoute,selectedPoint;
	
	public RouteManagerState() {
		super();
		id = STATE_ID;
	}
	
	@Override
	public void load() {
		// TODO Auto-generated method stub
		
		container= new LinearLayout(FRAGUEL.getInstance().getApplicationContext());
		container.setOrientation(LinearLayout.VERTICAL);
		title= new TextView(FRAGUEL.getInstance().getApplicationContext());
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		container.addView(title);
		loadRoutes(0);
		viewGroup=container;
		selectedRoute=0;
		selectedPoint=0;
		
		FRAGUEL.getInstance().addView(viewGroup);
		
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private void addItemClickListenerToListView(){
		
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				switch (internalState){
				case 0:
					selectedRoute=position;
					loadPoints(selectedRoute);
					break;
				case 1:
					selectedPoint=position;
					loadInfoAboutPoint(selectedPoint);
					break;
				}
				
			}
			
		});
	}
	
	private void loadPoints(int route){
		title.setText("Puntos de la ruta: "+ route);
		container.removeView(list);
		setAdapter();
		adapter.setTitle(new String[] {"Punto 0","Punto 1","Punto 2"});
		adapter.setDescription(new String[] {"Facultad de Medicina","Trincheras Norte","Hospital Cl�nico"});
		internalState=1;
		list.setSelection(selectedPoint);
	}
	
	private void loadInfoAboutPoint(int point){
		container.removeView(list);
		title.setText("ENLAZAR CON LOS DATOS DEL PUNTO " + selectedPoint+ " DE LA RUTA "+selectedRoute);
		internalState=2;
		
	}
	
	private void loadRoutes(int routeFocus){
		container.removeView(list);
		title.setText("Rutas descargadas en su terminal");
		setAdapter();
		internalState=0;
		list.setSelection(routeFocus);
		
	}
	
	private void setAdapter(){
		list= new ListView(FRAGUEL.getInstance().getApplicationContext());
		list.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		adapter = new RouteManagerAdapter(FRAGUEL.getInstance().getApplicationContext());
		list.setAdapter(adapter);
		ColorDrawable divcolor = new ColorDrawable(Color.DKGRAY);
		list.setDivider(divcolor);
		list.setDividerHeight(2);
		addItemClickListenerToListView();
		container.addView(list);
		
		
	}
	
	
	public boolean dispatchKeyEvent(KeyEvent event){
		boolean result=false;
		if (event.getKeyCode()==event.KEYCODE_BACK & event.getAction()==event.ACTION_DOWN){
			if(internalState>0){
			
				switch (internalState){
				case 1:
					loadRoutes(selectedRoute);
					break;
				case 2:
					loadPoints(selectedRoute);
					break;
				}
			
			result=true;
			}
		}
		if (result)
			return true;
		else
			return super.dispatchKeyEvent(event);
	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//Borramos el menu de opciones anterior
		menu.clear();
		//A�adimos las opciones del menu
		menu.add(0,ROUTEMANAGERSTATE_ADDROUTE, 0, R.string.routemanagerstate_menu_addroute).setIcon(R.drawable.change_map_icon);
		menu.add(0,ROUTEMANAGERSTATE_DELETEROUTE, 0, R.string.routemanagerstate_menu_deleteroute).setIcon(R.drawable.geotaging);
		
		return menu;
	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case ROUTEMANAGERSTATE_ADDROUTE:
			loadRoutes(selectedRoute);
			return true;

		case ROUTEMANAGERSTATE_DELETEROUTE:
			loadRoutes(selectedRoute);
			return true;
		}
		return false;
	}
}