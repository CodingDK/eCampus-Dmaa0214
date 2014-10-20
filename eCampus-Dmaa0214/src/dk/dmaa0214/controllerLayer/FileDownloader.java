package dk.dmaa0214.controllerLayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

import dk.dmaa0214.guiLayer.SPGUI;
import dk.dmaa0214.modelLayer.SPFile;
import dk.dmaa0214.modelLayer.SPFolder;

public class FileDownloader extends SwingWorker<Void, SPFile> {

	private String localPath;
	private String siteURL;
	private WebClient webClient;
	private List<Object> dList;
	private JProgressBar progressBar;
	private SPGUI spgui;
	private int counter;
	
	public FileDownloader(String user, String pass, String localPath, String siteURL, List<Object> selectedList, SPGUI spgui, JProgressBar progressBar){
		this.localPath = localPath;
		this.siteURL = siteURL;
		this.dList = selectedList;
		this.progressBar = progressBar;
		this.spgui = spgui;
		this.counter = 0;
		
		progressBar.setMaximum(selectedList.size());
		progressBar.setMinimum(counter);
		
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
	}

	@Override
	protected Void doInBackground() throws Exception {
		downloadFiles();
		return null;
	}
	
	public void downloadFiles() throws NullPointerException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		if (dList.size() == 0) {
			throw new NullPointerException("Du skal vælge nogle filer først");
		}
		
		HashSet<SPFile> dlList = new HashSet<SPFile>();
		System.out.println("before while");
		while(dList.size() != 0) {
			System.out.println("list size: " + dList.size());
			Object obj = dList.get(0);
			if(obj instanceof SPFolder){
				for(Object objA : ((SPFolder) obj).getChildNodes()){
					System.out.println("forloop: " + objA);
					if (!dList.contains(objA)) {
						System.out.println("forloop if:" + objA);
						dList.add(objA);
					}
				}
				//((SPFolder) obj).getParent().removeChild(obj);
			}else if(obj instanceof SPFile){
				System.out.println("added: " + obj);
				dlList.add((SPFile) obj);
				//((SPFile) obj).getParent().removeChild(obj);
			}
			dList.remove(obj);
		}
		System.out.println("after while");
		
		for(SPFile f : dlList) {
			downloadFile(f);
			publish(f);
		}
	}
	
	public void downloadFile(SPFile spFile) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String shortPath = spFile.getShortPath();
		String path = spFile.getPath();	
		
		
		if(siteURL == null) {
			throw new NullPointerException("Du skal sammenligne filer først");
		}
		
		File localFile = new File(localPath + shortPath);
	    InputStream ins = webClient.getPage(siteURL + path).getWebResponse().getContentAsStream();
	    
    	if(!localFile.getParentFile().exists()) {
    		System.out.println("create dir: " + localFile.getParentFile().getName());
    		localFile.getParentFile().mkdirs();
    	}
    	OutputStream ous = new FileOutputStream(localFile);
        int length = -1;
        System.out.println("downloading " + spFile);
        
        byte[] buffer = new byte[1024];
        while ((length = ins.read(buffer)) > -1) {
        	ous.write(buffer, 0, length);
        }
        System.out.println("downloading done");
        ous.close();
        ins.close();
	    
	}
	
	protected void done(){
		try{
			get();
		} catch(ExecutionException ex) {
			Throwable e = ex.getCause();
			if(e instanceof NullPointerException){
				showErrorDialog(e.getMessage());
			}else if(e instanceof FailingHttpStatusCodeException){
				showErrorDialog(e.getMessage());
			}else if(e instanceof MalformedURLException){
				showErrorDialog(e.getMessage());
			}else if(e instanceof IOException){
				showErrorDialog(e.getMessage());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	protected void process(final List<SPFile> chks){
		for(SPFile sp : chks){
			counter++;
			sp.getParent().removeChild(sp);
			spgui.reloadTree();
			progressBar.setValue(counter);
			progressBar.setString("Downloading: " + sp);
		}
	}

}
