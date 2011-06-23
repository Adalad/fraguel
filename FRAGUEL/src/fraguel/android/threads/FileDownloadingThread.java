package fraguel.android.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Message;
import fraguel.android.FRAGUEL;
import fraguel.android.resources.ResourceManager;

public class FileDownloadingThread extends Thread{

	private String[] urls,names;
	private URL aURL;
	private File f ;
	private String savedData;
	
	public FileDownloadingThread(String[] paths,String[] filenames,String arSavingPath){
		super();
		urls=paths;
		names=filenames;
		f=null;
		savedData=arSavingPath;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i=0;
		String absolutePath;
		boolean isMainFile=false;
		for (String url: urls){
				if (!url.endsWith(".xml"))
					absolutePath=savedData+names[i];
				else if (url.equals("http://www.blackmesa.es/fraguel/xml/prueba.xml")){
					absolutePath=ResourceManager.getInstance().getRootPath()+"/"+names[i];
					isMainFile=true;
				}else
					absolutePath=ResourceManager.getInstance().getRootPath()+"/routes/"+names[i];
				
				f = new File(absolutePath);
				
				
				if (!f.exists()&& url!=null){
					
					try{
						aURL= new URL(url);
						
						HttpURLConnection urlConnection = (HttpURLConnection) aURL.openConnection();
						
						urlConnection.setRequestMethod("GET");
				        urlConnection.setDoOutput(true);
				        
				        urlConnection.connect();
						
						FileOutputStream fileOutputStream = new FileOutputStream(absolutePath);
						
						InputStream inputStream = urlConnection.getInputStream();
						
						//this is the total size of the file
				        int totalSize = urlConnection.getContentLength();
				        //variable to store total downloaded bytes
				        int downloadedSize = 0;
				        
				        byte[] buffer = new byte[1024];
				        int bufferLength = 0;
				        
				        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
			                //add the data in the buffer to the file in the file output stream (the file on the sd card
			                fileOutputStream.write(buffer, 0, bufferLength);
			                //add up the size so we know how much is downloaded
			                downloadedSize += bufferLength;

			        }
			        //close the output stream when done
			        fileOutputStream.close();
			        
			        Message m = new Message();
			        if (isMainFile && urls.length==1){
			        	m.arg1=1;
			        	FRAGUEL.getInstance().fileHandler.sendMessage(m);
			        }
			        else if (i == urls.length-1){
			        	m.arg1 = 2;
			        	FRAGUEL.getInstance().fileHandler.sendMessage(m);
			        }
			        	
					
						
					}catch(Exception e){
						f.delete();
					}
		
				}else if (f.exists()){
		
					 Message m = new Message();
				        if (isMainFile && urls.length==1){
				        	m.arg1=1;
				        	FRAGUEL.getInstance().fileHandler.sendMessage(m);
				        }
				        else if (i == urls.length-1){
				        	m.arg1 = 2;
				        	FRAGUEL.getInstance().fileHandler.sendMessage(m);
				        }
					
				}
				i++;
					
		
		}
		
		
	}

}
