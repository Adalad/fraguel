package fraguel.android.states;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.State;
import fraguel.android.gallery.BigImageAdapter;
import fraguel.android.gallery.ImageAdapter;

public class ImageState extends State{
	
	private TextView title;
	private TextView text;
	private Gallery gallery;
	private ScrollView sv;
	private Gallery bigGallery;
	private int currentIndex;
	
	public ImageState() {
		super();
		id = 4;
	}


	@Override
	public void load() {
		// TODO Auto-generated method stub
		viewGroup = new LinearLayout(FRAGUEL.getInstance().getApplicationContext());
		((LinearLayout) viewGroup).setOrientation(LinearLayout.VERTICAL);
		
			title= new TextView(FRAGUEL.getInstance().getApplicationContext());
			title.setText("Facultad A");
			
			
			
			setParamsSmallGallery();
			
			
			setParamsBigGallery();
		
		
		sv= new ScrollView(FRAGUEL.getInstance().getApplicationContext());
		text= new TextView(FRAGUEL.getInstance().getApplicationContext());
		sv.addView(text);
		
		loadViews();
		
		currentIndex=0;
		
		
		FRAGUEL.getInstance().addView(viewGroup);
		gallery.setSelection(0, true);
		//gallery.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	
	private void setParamsSmallGallery(){
		gallery=new Gallery(FRAGUEL.getInstance().getApplicationContext());
		gallery.setAdapter(new ImageAdapter(FRAGUEL.getInstance().getApplicationContext()));
		gallery.setHorizontalScrollBarEnabled(true);
		
		

		gallery.setOnItemClickListener(new OnItemClickListener() {
		

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if (currentIndex==position){
				viewGroup.removeAllViews();
				viewGroup.addView(bigGallery);
				bigGallery.setSelection(position, true);
			}
			currentIndex=position;
		}
		});
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				
				text.setText("Posici�n: "+ position+"\n"+"\n"+"La posicion en la que se encuentra el elemento pulsado es la "+position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		
	}
	
	private void setParamsBigGallery(){
		bigGallery=new Gallery(FRAGUEL.getInstance().getApplicationContext());
		bigGallery.setAdapter(new BigImageAdapter(FRAGUEL.getInstance().getApplicationContext()));
		bigGallery.setHorizontalScrollBarEnabled(true);
		
		

		bigGallery.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if (currentIndex==position){
				viewGroup.removeAllViews();
				loadViews();
				gallery.setSelection(position, true);
			}
			currentIndex=position;
			
		}
		});
		
		bigGallery.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				
				Toast.makeText(FRAGUEL.getInstance().getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
			
	}
	
	private void loadViews(){
		viewGroup.addView(title);
		viewGroup.addView(gallery);
		viewGroup.addView(sv);
		
	}

}
