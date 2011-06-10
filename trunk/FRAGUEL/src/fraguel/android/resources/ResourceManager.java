package fraguel.android.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import fraguel.android.FRAGUEL;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class ResourceManager {

	private static ResourceManager _instance;
	private boolean _initialized;

	private String _rootPath;

	private DownloadManager downloadManager;
	private XMLManager xmlManager;

	private ResourceManager() {
		_initialized = false;

		downloadManager=new DownloadManager("http://www.blackmesa.es/fraguel");
		xmlManager =new XMLManager();
	}

	public static ResourceManager getInstance() {
		if (_instance == null)
			_instance = new ResourceManager();

		return _instance;
	}
	
	private void createDirs(File rootSD) {
		rootSD.mkdir();
		new File(rootSD.getAbsolutePath() + "/ar").mkdir();
		new File(rootSD.getAbsolutePath() + "/config").mkdir();
		new File(rootSD.getAbsolutePath() + "/user").mkdir();
		new File(rootSD.getAbsolutePath() + "/routes").mkdir();
		new File(rootSD.getAbsolutePath() + "/tmp").mkdir();
		// TODO Create all the directories
	}

	public void initialize(final String root) {
		try {
			String state = Environment.getExternalStorageState();
			if (!Environment.MEDIA_MOUNTED.equals(state))
				throw new Exception("SD Card not avaliable");

			File sd = Environment.getExternalStorageDirectory();
			_rootPath = sd.getAbsolutePath() + "/" + root;

			if ((!sd.canRead()) || (!sd.canWrite()))
				throw new Exception("SD Card not avaliable");

			Log.d("FRAGUEL", "SD Card ready");
			
			File rootSD = new File(_rootPath);
			if (!rootSD.exists())
				createDirs(rootSD);
				
			xmlManager.setRoot(root);
			_initialized = true;
		} catch (Exception e) {
			// TODO Message asking for SD Card
			Log.d("FRAGUEL", "Error: " + e);
		}
	}

	public boolean isInitialized() {
		return _initialized;
	}

	public String getRootPath() {
		return _rootPath;
	}


	public DownloadManager getDownloadManager() {
		return downloadManager;
	}


	public XMLManager getXmlManager() {
		return xmlManager;
	}
	
public void createXMLTemplate(String fileName,String routeName,int routeId,int numPoints){
		
		File file = new File(ResourceManager.getInstance().getRootPath()+"/user/"+fileName+".xml");
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileOutputStream fileos = null;
		
		try {
			fileos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XmlSerializer serializer = Xml.newSerializer();
		
		try {
			
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, null);
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			
			serializer.startTag(null, "route");
			serializer.attribute(null, "id", Integer.toString(routeId));
					
					serializer.startTag(null, "name");
					serializer.text(routeName);
		            serializer.endTag(null, "name");
					serializer.startTag(null, "description");
		            serializer.endTag(null, "description");
					serializer.startTag(null, "icon");
		            serializer.endTag(null, "icon");
		            
					serializer.startTag(null, "points");
							
						for (int i=0;i<numPoints;i++){
							serializer.startTag(null, "point");
							serializer.attribute(null, "id", Integer.toString(i));
							
									serializer.startTag(null, "coords");
									serializer.attribute(null, "x", "0");
									serializer.attribute(null, "y", "0");
						            serializer.endTag(null, "coords");
						            
									serializer.startTag(null, "title");
						            serializer.endTag(null, "title");
						            
									serializer.startTag(null, "pointdescription");
						            serializer.endTag(null, "pointdescription");
						            
									serializer.startTag(null, "pointicon");
						            serializer.endTag(null, "pointicon");
						            
									serializer.startTag(null, "image");
						            serializer.endTag(null, "image");
						            
									serializer.startTag(null, "video");
						            serializer.endTag(null, "video");
						            
									serializer.startTag(null, "ar");
						            serializer.endTag(null, "ar");
							
				            serializer.endTag(null, "point");
					
						}
					
		            serializer.endTag(null, "points");
		            
		
			serializer.endTag(null, "route");
			
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			Toast.makeText(FRAGUEL.getInstance().getApplicationContext(), "Plantilla Creada", Toast.LENGTH_SHORT).show();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
