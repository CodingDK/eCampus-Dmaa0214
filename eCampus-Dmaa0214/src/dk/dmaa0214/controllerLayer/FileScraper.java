package dk.dmaa0214.controllerLayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import dk.dmaa0214.modelLayer.SPFile;
import dk.dmaa0214.modelLayer.SPFolder;
import dk.dmaa0214.modelLayer.SPFolderCont;

public class FileScraper extends SwingWorker<SPFolder, String> {
	private String user;
	private String pass;
	private String localPath;
	private String siteURL;
	private String sitePath;
	private boolean checkMD5;
	private JLabel statusLabel;
	private SPFolderCont spFolderCont;
	private WebClient webClient;
	private SPFolder root;
	
	public FileScraper(String user, String pass, String localPath, String siteURL, String sitePath, boolean checkMD5, JLabel statusLabel){
		this.user = user;
		this.pass = pass;
		this.localPath = localPath;
		this.siteURL = siteURL;
		this.sitePath = sitePath;
		this.checkMD5 = checkMD5;
		this.statusLabel = statusLabel;
		this.spFolderCont = SPFolderCont.getInstance();
	}

	@Override
	protected SPFolder doInBackground() throws Exception {
		getConnectedToSP();
		return root;
	}
	
	protected void process(final List<String> chks){
		for(final String s : chks){
			statusLabel.setText("Status: " + s);
		}
	}
	
	private SPFolder getConnectedToSP() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		System.out.println("Connecting to eCampus");
		publish("Connecting to eCampus");
		String[] rootName = sitePath.split("/");
		
		root = new SPFolder(rootName[rootName.length-1]);
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
	    HtmlPage page = webClient.getPage(siteURL + sitePath);
	    getFilesOnPage(page, root);
	    
	    if(root.getChildNodes().size() > 0){
		    for(Object s : root.getChildNodes()){
		    	if(s instanceof SPFolder){
		    		System.out.println("Fetching child nodes - " + ((SPFolder) s).getName());
		    		getChildren((SPFolder)s);
		    	}
		    }
	    }

	    removeEmptyFolders();
			    
	    return root;
	}
	
	private int getFilesOnPage(HtmlPage page, SPFolder parent) throws UnsupportedEncodingException {
		int retInt = 0;
		if(parent == null){
			parent = root;
		}
		HtmlTable matTable = page.getFirstByXPath("//table[@id='onetidDoclibViewTbl0']");
		String changedTime = "";
		String addedBy = "";
		String href = "";
		String name = "";
	    for (HtmlTableRow matRow : matTable.getRows()) {
	    	boolean isFolder = false;
	    	if(!matRow.getAttribute("class").equals("ms-viewheadertr ms-vhltr")) {

	    		for (HtmlTableCell matCell : matRow.getCells()) {
	    			String cellClassAtt = matCell.getAttribute("class");
					
	    			if (cellClassAtt.equals("ms-vb-title")) {
	    				name = matCell.asText().trim();
	    				HtmlAnchor link = (HtmlAnchor) matCell.getFirstChild().getFirstChild();
	    				if(link == null) {
	    					throw new NullPointerException("Error in reading from site. errorcode: 1");
	    				}
	    				href = link.getHrefAttribute();
	    				if(href.equals("")) {
	    					throw new NullPointerException("Error in reading from site. errorcode: 2");
	    				}
	    				else if(href.contains("Forms/AllItems.aspx?")) {

	    					href = href.substring(href.indexOf("RootFolder=")+11,href.indexOf("&FolderCTID"));
	    					href = URLDecoder.decode(href, "UTF-8");

	    					isFolder = true;
	    				}
	    			}
	    			else if (cellClassAtt.equals("ms-vb2")) {
	    				changedTime = matCell.asText();
	    			}
	    			else if (cellClassAtt.equals("ms-vb-user")) {
	    				addedBy = matCell.asText();
	    			}
				}
	    		if(href != null) {
	    			if(isFolder){
	    				SPFolder spFolder = new SPFolder(sitePath, name, href, addedBy, changedTime, parent);
		    			parent.addChild(spFolder);
	    			}else if(!hasLocalFile(href.substring(sitePath.length()))){
	    				SPFile spFile = new SPFile(sitePath, name, href, addedBy, changedTime, parent);
	    				parent.addChild(spFile);
		    			retInt++;
	    			}
				}
	    	}
	    }
	    return retInt;
	}

	private boolean hasLocalFile(String path) {
		boolean retVal = true;
		File file = new File(localPath + path);
		if(!file.exists()) {
			retVal = false;
			System.out.println("test: " + file.getParentFile().exists());
		} else if(checkMD5) {
			if(!file.isDirectory()){
				System.out.println("Comparing MD5");
				publish("Comparing MD5");
				System.out.println(file);
				System.out.println(siteURL + sitePath + path);
				String md5Local = "";
				String md5External = "";
				try {
					FileInputStream localFile = new FileInputStream(file);
					md5Local = org.apache.commons.codec.digest.DigestUtils.md5Hex(localFile);
					localFile.close();
					
					InputStream externalFile = webClient.getPage(siteURL + sitePath + path).getWebResponse().getContentAsStream();
					md5External = org.apache.commons.codec.digest.DigestUtils.md5Hex(externalFile);
					externalFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(!md5Local.equals(md5External)){
					retVal = false;
				}
			}
		}
		
		
		return retVal;
	}

	private void removeEmptyFolders() {
		publish("Removing the empty folders....");
		ArrayList<SPFolder> folders = spFolderCont.getFiles();
	    for(SPFolder sp : folders){
	    	if(sp.isEmpty()){
	    		sp.getParent().removeChild(sp);
	    	}
	    }
	}

	private void getChildren(SPFolder s) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		publish("Fetching child nodes - " + ((SPFolder) s).getName());
		spFolderCont.addFile(s);
		System.out.println(s.getPath());
		HtmlPage page = webClient.getPage(siteURL + s.getPath());
		getFilesOnPage(page, s);
		ArrayList<Object> children = s.getChildNodes();
		System.out.println("size: " + children.size());
		if(children.size() > 0){
			for(Object child : children){
				if(child instanceof SPFolder){
					getChildren((SPFolder)child);
				}
			}
		}
	}
	
	protected void done(){
		try{
			get();
		} catch(ExecutionException ex) {
			Throwable e = ex.getCause();
			if(e instanceof FailingHttpStatusCodeException){
				if (((FailingHttpStatusCodeException) e).getStatusCode() == 401) {
					statusLabel.setText("Error");
					showErrorDialog("Login er forkert");
				} else if (((FailingHttpStatusCodeException) e).getStatusCode() == 404) {
					statusLabel.setText("Error");
					showErrorDialog("Sti til eCampus er forkert");
				} else {
					statusLabel.setText("Error");
					showErrorDialog("Code: " +  ((FailingHttpStatusCodeException) e).getStatusCode() + ": " + ((FailingHttpStatusCodeException) e).getStatusMessage());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
