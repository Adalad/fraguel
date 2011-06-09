package fraguel.android.states;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.R;
import fraguel.android.Route;
import fraguel.android.State;
import fraguel.android.gps.GPSProximityListener;
import fraguel.android.gps.GPSProximityRouteListener;
import fraguel.android.maps.MapItemizedOverlays;
import fraguel.android.maps.NextPointOverlay;
import fraguel.android.maps.PointOverlay;
import fraguel.android.maps.RouteOverlay;
import fraguel.android.notifications.BackKeyNotification;
import fraguel.android.notifications.RouteSelectionNotification;
import fraguel.android.notifications.StopRouteNotification;
import fraguel.android.resources.ResourceManager;
import fraguel.android.utils.RouteInfoDialog;

public class MapState extends State implements OnTouchListener{

	// Singleton
	private static MapState mapInstance;
	private boolean isMyPosition;
	private MyPositionOverlay me=null;

	// Variables de los botones del men
	private static final int MAPSTATE_MENU_CHANGEMAP = 1;
	private static final int MAPSTATE_MENU_MY_POSITION = 2;
	private static final int MAPSTATE_MENU_EXPLORE_MAP = 3;
	private static final int MAPSTATE_MENU_COMPASS=4;
	private static final int MAPSTATE_MENU_STARTROUTE = 5;
	private static final int MAPSTATE_MENU_DRAWPATH=6;
	private static final int MAPSTATE_MENU_STOPTALKING=7;
	private static final int MAPSTATE_MENU_STOPROUTE = 8;
	


	public static final int STATE_ID = 2;

	private MapController mapControl;
	private MapView mapView;
	private View popupPI;
	private View popupPIonroute;
	private View popupOnRoute;
	private List<Overlay> mapOverlays;
	private boolean isPopupPI;
	private boolean isPopupOnRoute;
	private boolean isPopupPIOnRoute;
	private boolean isContextMenuDisplayed,showWay=true;
	private Route routeContext;
	public final CharSequence[] options = {"Desde el principio", "","Elegir otra ruta"};
	private final CharSequence[] end = {"Abandonar ruta"};
	public final CharSequence[] rutas=new CharSequence[FRAGUEL.getInstance().routes.size()];
	private RouteInfoDialog dialog;


	public MapState() {
		super();
		id = STATE_ID;
		// Singleton
		mapInstance = this;
		
	}

	public static MapState getInstance() {
		if (mapInstance == null)
			mapInstance = new MapState();
		return mapInstance;
	}


	@Override
	public void load() {
		//Creamos e importamos el layout del xml
		LayoutInflater li=  FRAGUEL.getInstance().getLayoutInflater();
		if(viewGroup==null)
			viewGroup= (ViewGroup) li.inflate(R.layout.maingooglemaps,  null);
		FRAGUEL.getInstance().addView(viewGroup);

		//Creamos e importamos el popup del xml
		isPopupPI=false;
		popupPI= li.inflate(R.layout.popup,  null);
		//LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//popupView.setLayoutParams(params);
        popupPI.setOnTouchListener((OnTouchListener) FRAGUEL.getInstance());
        
        //Creamos e importamos el popup onroute del xml
        isPopupPIOnRoute=false;
		popupPIonroute= li.inflate(R.layout.popup2,  null);
		popupPIonroute.setOnTouchListener((OnTouchListener) FRAGUEL.getInstance());
		
		//Creamos e importamos el popup onroute del xml
		isPopupOnRoute=false;
		popupOnRoute= li.inflate(R.layout.popup3,  null);
		popupOnRoute.setOnTouchListener((OnTouchListener) FRAGUEL.getInstance());
		

		//Creamos, importamos y configuramos la mapview del xml
		mapView = (MapView) FRAGUEL.getInstance().findViewById(R.id.mapview);
		//mapView.setOnClickListener((OnClickListener) FRAGUEL.getInstance());
		//mapView.setOnTouchListener((OnTouchListener) FRAGUEL.getInstance());
		mapView.setTraffic(false);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.setEnabled(true);
		
		mapView.setOnTouchListener((OnTouchListener) FRAGUEL.getInstance());

		mapControl = mapView.getController();
		GeoPoint pointInit = new GeoPoint((int) (40.4435602 * 1000000), (int) (-3.7267881 * 1000000));
		mapControl.setZoom(15);
		mapControl.setCenter(pointInit);

		//Creamos los Overlays
		mapOverlays = mapView.getOverlays();
		
		if (me==null)
			me = new MyPositionOverlay(FRAGUEL.getInstance().getApplicationContext(),mapView);
		

		isMyPosition=true;
		
		//Cargamos todo
		if (!me.isRouteMode())
			this.reStartMap();
		else{
			mapControl.setCenter(me.getMyLocation());
			//refreshMapRouteMode();
		}
		
		
		isContextMenuDisplayed=false;
		routeContext=null;
		mapView.setKeepScreenOn(true);

		
	}
	
	@Override
	public void unload(){
		super.unload();
		mapView.setKeepScreenOn(false);
	}

	public boolean isPopupPI() {
		return isPopupPI;
	}
	public boolean isPopUpPIOnRoute(){
		return isPopupPIOnRoute;
	}
	public boolean isPopupOnRoute() {
		return isPopupOnRoute;
	}
	public boolean isAnyPopUp(){
		return isPopupPI || isPopupPIOnRoute || isPopupOnRoute;
	}

	public void setPopupPI() {
		FRAGUEL.getInstance().addView(popupPI);
		this.isPopupPI = true;
	}
	public void setPopupPIOnRoute() {
		FRAGUEL.getInstance().addView(popupPIonroute);
		this.isPopupPIOnRoute = true;
	}
	public void setPopupOnRoute() {
		FRAGUEL.getInstance().addView(popupOnRoute);
		this.isPopupOnRoute = true;
	}
	
	public void removePopUpPI(){
		FRAGUEL.getInstance().getView().removeView(popupPI);
		this.isPopupPI = false;
		 
	}
	public void removePopUpOnRoute(){
		FRAGUEL.getInstance().getView().removeView(popupOnRoute);
		this.isPopupOnRoute = false;
		 
	}
	public void removePopUpPIOnRoute(){
		FRAGUEL.getInstance().getView().removeView(popupPIonroute);
		this.isPopupPIOnRoute = false;
		 
	}
	
	@Override
	public void onClick(View v) {

		this.removePopUpPI();
		this.removePopUpOnRoute();
		this.removePopUpPIOnRoute();
		
		
		switch (v.getId()) {
		case R.id.btn_popupPI_info:
			FRAGUEL.getInstance().changeState(InfoState.STATE_ID);
			FRAGUEL.getInstance().getCurrentState().loadData(route, point);
			break;
		case R.id.btn_popupPI_photo:
			FRAGUEL.getInstance().changeState(ImageState.STATE_ID);
			FRAGUEL.getInstance().getCurrentState().loadData(route, point);
			break;
		case R.id.btn_popupPI_video:
			FRAGUEL.getInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(point.video)));
			break;
		case R.id.btn_popupPI_ar:
			FRAGUEL.getInstance().changeState(ARState.STATE_ID);
			break;
		case R.id.btn_popupPIonroute_moreinfo:
			FRAGUEL.getInstance().changeState(PointInfoState.STATE_ID);
			FRAGUEL.getInstance().getCurrentState().loadData(route, point);
			break;
		default:
			

		}


	}

	@Override
	public void imageLoaded(int index){
		if (index==0){
			String path=ResourceManager.getInstance().getRootPath()+"/tmp/route"+Integer.toString(route.id)+"/point"+point.id+"icon.png";
			Bitmap bmp = BitmapFactory.decodeFile(path);
			((ImageView) popupPI.findViewById(R.id.popupPI_imagen2)).setImageBitmap(bmp);
			popupPI.invalidate();
		}else{
			setImageToDialog();
		}
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent mev) {
		// TODO Auto-generated method stub
		if (view==popupPI && isPopupPI()){
		  removePopUpPI();

		}
          
		if (view==popupPIonroute && isPopUpPIOnRoute()){
			  removePopUpPIOnRoute();

			}
		if (view==popupOnRoute && isPopupOnRoute()){
			removePopUpOnRoute();

		}

		return true;
	}


	public MapView getMapView() {
		return mapView;
	}

	public View getPopupPI() {
		return popupPI;
	}
	
	public View getPopupPIonroute() {
		return popupPIonroute;
	}
	
	public View getPopupOnRoute() {
		return popupOnRoute;
	}

	public void animateTo(GeoPoint g){
		mapControl.animateTo(g);		
	}
	
	public void refreshMapRouteMode(){
		mapOverlays.clear();
		mapOverlays.add(me);
		addRouteOverlays();
	}
	
	
	private void addRouteOverlays(){
		//pintamos las l�neas
		mapOverlays.add(new RouteOverlay());
		//pintamos los ya visitados
		MapItemizedOverlays visited = new MapItemizedOverlays(FRAGUEL.getInstance().getResources().getDrawable(R.drawable.map_marker_visited),FRAGUEL.getInstance());
		
		for (PointOI point : me.getRoutePointsVisited()){
			visited.addOverlay(new PointOverlay(new GeoPoint((int)(point.coords[0]*1000000),(int)(point.coords[1]*1000000)), point.title, point.title,point,me.getCurrentRoute()));
		}

			
		if (visited.size()!=0)	
			mapOverlays.add(visited);
		
		//pintamos los no visitados
		visited= new MapItemizedOverlays(FRAGUEL.getInstance().getResources().getDrawable(R.drawable.map_marker_notvisited),FRAGUEL.getInstance());
		for (PointOI point : me.getRoutePointsNotVisited()){
			visited.addOverlay(new PointOverlay(new GeoPoint((int)(point.coords[0]*1000000),(int)(point.coords[1]*1000000)),point.title, point.title,point,me.getCurrentRoute()));
		}
		if (visited.size()!=0)	
			mapOverlays.add(visited);
		
		
		
	}
	public void startRoute(){
		this.removeAllPopUps();
		this.refreshMapRouteMode();
	}
	
	public void removeAllPopUps(){
		removePopUpPI();
		this.removePopUpOnRoute();
		this.removePopUpPIOnRoute();
	}
	
	public void reStartMap(){
		mapOverlays.clear();
		loadAllPoints();
	}
	
	public void loadAllPoints(){
		//PUNTOS LEIDOS DE FICHERO
		Drawable image;
		MapItemizedOverlays capa;
		GeoPoint point;
		PointOverlay item;
		for (Route r : FRAGUEL.getInstance().routes) {
			image=FRAGUEL.getInstance().getResources().getDrawable(R.drawable.map_marker_notvisited);
			
			capa=new MapItemizedOverlays(image,FRAGUEL.getInstance());
			for (PointOI p : r.pointsOI) { 
				point=new GeoPoint((int)(p.coords[0]*1000000),(int)(p.coords[1]*1000000));
				item= new PointOverlay(point,p.title,p.title,p,r);
				capa.addOverlay(item);
			}
			mapOverlays.add(capa);
		}
		mapOverlays.add(me);
	}
	
	public GeoPoint getMyLocation(){
		return me.getMyLocation();
	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		//Borramos el menu de opciones anterior
		menu.clear();
		//A�adimos las opciones del menu
		if (me.isRouteMode())
			if (showWay)
				menu.add(0, MAPSTATE_MENU_DRAWPATH, 0, "�Gu�ame!").setIcon(R.drawable.ic_menu_routeadd);
			else
				menu.add(0, MAPSTATE_MENU_DRAWPATH, 0, "No guiar").setIcon(R.drawable.ic_menu_routerem);
		if (FRAGUEL.getInstance().isTalking())
			menu.add(0,MAPSTATE_MENU_STOPTALKING, 0, "Detener audio").setIcon(R.drawable.ic_menu_talkstop);
		menu.add(0, MAPSTATE_MENU_CHANGEMAP, 0, R.string.mapstate_menu_changemap).setIcon(R.drawable.ic_menu_mapmode);		
		menu.add(0, MAPSTATE_MENU_MY_POSITION, 0,R.string.mapstate_menu_my_position).setIcon(R.drawable.ic_menu_mylocation);
		menu.add(0, MAPSTATE_MENU_EXPLORE_MAP, 0,R.string.mapstate_menu_explore_map).setIcon(R.drawable.ic_menu_search);
		menu.add(0, MAPSTATE_MENU_COMPASS, 0,R.string.mapstate_menu_compass).setIcon(R.drawable.ic_menu_compass);
		if (!me.isRouteMode())
			menu.add(0, MAPSTATE_MENU_STARTROUTE, 0, "Comenzar ruta").setIcon(R.drawable.ic_menu_route);
		else
			menu.add(0, MAPSTATE_MENU_STOPROUTE, 0, "Abandonar ruta").setIcon(R.drawable.ic_menu_route);

		return menu;
	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		//A�adimos los eventos del menu de opciones
		switch (item.getItemId()) {

		case MAPSTATE_MENU_CHANGEMAP:
			if (mapView.isSatellite())
				mapView.setSatellite(false);
			else
				mapView.setSatellite(true);
			return true;
		
		case MAPSTATE_MENU_DRAWPATH:
			if(showWay){
				mapOverlays.add(new NextPointOverlay());
			}
			else{
				this.refreshMapRouteMode();			
			}
			showWay=!showWay;
			return true;
		case MAPSTATE_MENU_EXPLORE_MAP:
			isMyPosition=false;
			return true;
		case MAPSTATE_MENU_MY_POSITION:
			mapControl.animateTo(getMyLocation());
			isMyPosition=true;
			return true;
			
		case MAPSTATE_MENU_COMPASS:
			if (me.isCompassEnabled())
				me.disableCompass();
			else
				me.enableCompass();
			return true;

		case MAPSTATE_MENU_STARTROUTE:
			int i=0;
			for (Route r: FRAGUEL.getInstance().routes){
				rutas[i]=r.name;
				i++;
			}
			
			FRAGUEL.getInstance().createDialog("Elegir ruta", rutas, new RouteSelectionNotification(), new BackKeyNotification());
						
			return true;
		case MAPSTATE_MENU_STOPTALKING:
			FRAGUEL.getInstance().stopTalking();
			return true;
		
		case MAPSTATE_MENU_STOPROUTE:
			FRAGUEL.getInstance().createDialog("�Desea abandonar la ruta?", end, new StopRouteNotification(), new BackKeyNotification());
			return true;
		}
		return false;
	}

	public void setContextMenuDisplayed(boolean isContextMenuDisplayed) {
		this.isContextMenuDisplayed = isContextMenuDisplayed;
	}
	
	public boolean isContextMenuDisplayed() {
		return isContextMenuDisplayed;
	}
	
	public Route getContextRoute(){
		return routeContext;
	}
	public void setContextRoute (Route r){
		routeContext=r;
	}

	private void setImageToDialog() {
		if (dialog!=null)
			dialog.imageLoaded();
	}

	public RouteInfoDialog getDialog() {
		return dialog;
	}
	public void setRouteInfoDialog(RouteInfoDialog d){
		dialog=d;
	}
	public MyPositionOverlay getGPS(){
		return me;
	}
	

	//****************************************************************************************
	//****************************************************************************************
	public class MyPositionOverlay extends MyLocationOverlay{

		private GPSProximityRouteListener routeListener;
		private GPSProximityListener pointListener;
		private boolean isDialogDisplayed = false,routeMode=false;
		
		private float[] position = { 0, 0, 0 };
		
		public MyPositionOverlay(Context context, MapView mapView) {
			super(context, mapView);
			routeListener=new GPSProximityRouteListener();
			pointListener=new GPSProximityListener();
			this.disableCompass();
			this.enableMyLocation();
			// TODO Auto-generated constructor stub
		}

		@Override
		public synchronized void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			super.onLocationChanged(location);
			
			//notify changes to current state
			position[0]=(float) location.getLatitude();
			position[1]=(float) location.getLongitude();
			position[2]=(float) location.getAltitude();
			FRAGUEL.getInstance().getCurrentState().onLocationChanged(position);
			
			if (!routeMode)
				pointListener.onLocationChanged(location);
			else
				routeListener.onLocationChanged(location);
			
			if (isMyPosition)
				mapControl.animateTo(getMyLocation());
			
			//A�adir informaci�n de la posici�n a la matriz de rotaci�n general
			// rotMatrix: matriz 4X4 de rotaci�n para pasarla a OpenGL
			float[] rotMatrix=FRAGUEL.getInstance().getRotMatrix();
			rotMatrix[3] = position[0];
			rotMatrix[7] = position[1];
			rotMatrix[11] = position[2];
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			super.onProviderDisabled(provider);
		}
		public double getLatitude() {
			return position[0];
		}

		public double getLongitude() {
			return position[1];
		}

		public double getAltitude() {
			return position[2];
		}

		public void setDialogDisplayed(boolean isDialogDisplayed) {
			this.isDialogDisplayed = isDialogDisplayed;
		}

		public boolean isDialogDisplayed() {
			return isDialogDisplayed;
		}
		
		public void startRoute(Route r, PointOI p){
			routeMode=true;
			if (p==null)
				p=r.pointsOI.get(0);
			routeListener.startRoute(r, p);
			
		}
		
		public ArrayList<PointOI> getRoutePointsVisited(){
			if (routeMode==true)
				return routeListener.pointsVisited();
			else
				return null;
			
		}
		
		public ArrayList<PointOI> getRoutePointsNotVisited(){
			if (routeMode==true)
				return routeListener.pointsToVisit();
			else
				return null;
		}
		
		public void stopRoute(){
			removeAllPopUps();
			routeMode=false;
			MapState.getInstance().reStartMap();
		}
		
		public boolean isRouteMode(){
			return routeMode;
		}
		
		public Route getCurrentRoute(){
			return routeListener.getCurrentRoute();
		}

	}
	

}
