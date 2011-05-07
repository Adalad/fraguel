package fraguel.android.threads;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import fraguel.android.FRAGUEL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

public class ImageDownloadingThread extends Thread{

	private String url;
	private int index;
	private URLConnection conn;
	private InputStream is;
	private BufferedInputStream bis;
	public ImageDownloadingThread(String path,int imageIndex){
		url=path;
		index=imageIndex;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		FRAGUEL.getInstance().bmp=getImageBitmap(url,index);
	}
	private Bitmap getImageBitmap(String url,int imageIndex) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            conn = aURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            
            Message m = new Message();
    		m.arg2 = imageIndex;
    		FRAGUEL.getInstance().imageHandler.sendMessage(m);
       } catch (IOException e) {
           Log.e("Image", "Error getting bitmap", e);
       }
       return bm;
    } 
	public void stopThread(){
		try {
			bis.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}