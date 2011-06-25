package fraguel.android.gallery;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.R;
import fraguel.android.resources.ResourceManager;
import fraguel.android.states.RouteManagerState;
import fraguel.android.threads.ImageDownloadingThread;

public class BigImageAdapter extends BaseAdapter{
    int mGalleryItemBackground;
    private Context mContext;
    Bitmap bmp;

    /*private Integer[] mImageIds = {
            R.drawable.guerracivil_1,
            R.drawable.guerracivil_2,
            R.drawable.guerracivil_3,
            R.drawable.guerracivil_4,
          
    };*/
    private ArrayList<Pair<String,String>> images= new ArrayList<Pair<String,String>>();

    public BigImageAdapter(Context c) {
        mContext = c;
        TypedArray a = FRAGUEL.getInstance().obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public void setData(ArrayList<Pair<String,String>> data){
    	this.images=data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView i;
    	if (convertView==null){
    		i = new ImageView(mContext);
    	}else {
            i = (ImageView) convertView;
        }
    	
        
       //i.setImageResource(mImageIds[position]);
        
        String path="";
        path=ResourceManager.getInstance().getRootPath()+"/tmp/"+"route"+Integer.toString(FRAGUEL.getInstance().getCurrentState().getRoute().id)+"/"+"point"+Integer.toString(FRAGUEL.getInstance().getCurrentState().getPointOI().id)+"images"+position+".png";
    	File f= new File(path);
    	bmp=null;
        
    	
		if (f.exists()){
			bmp = BitmapFactory.decodeFile(path);
			i.setImageBitmap(bmp);
			
		}else{
			i.setImageDrawable(FRAGUEL.getInstance().getResources().getDrawable(R.drawable.loading));				
		}
               
        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth(); 
        int height= display.getHeight();
        
        int currentImageWidth=0;
    	int currentImageHeight=0;
        
    	
        currentImageWidth=i.getDrawable().getIntrinsicWidth();
        currentImageHeight=i.getDrawable().getIntrinsicHeight();
        
        int imageWidth = 0;
        int imageHeight = 0;

        if (currentImageHeight < currentImageWidth) {
            imageWidth = width;
            imageHeight =currentImageHeight * imageWidth / currentImageWidth;
        }
        else {
            imageHeight = height;
            imageWidth = currentImageWidth * imageHeight/ currentImageHeight;
        }
        
        i.setLayoutParams(new Gallery.LayoutParams(width,height));
        //i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setBackgroundResource(mGalleryItemBackground);
        
        
        
        return i;
    }
    public void recycleBitmap(){
    	if (bmp!=null)
    		bmp.recycle();
    }
    
}
