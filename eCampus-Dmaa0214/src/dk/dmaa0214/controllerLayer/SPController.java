package dk.dmaa0214.controllerLayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

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


public class SPController {

	private SPFolder root;
	private SPFolderCont spFolderCont;
	private String siteURL;
	private String sitePath;
	private String localPath;
	private WebClient webClient;
	private int counter;
	private boolean checkMD5;

	public SPController() {
		siteURL = null;
	}
	
	public SPFolder getConnectedToSP(String user, String pass, String localPath, String siteURL, String sitePath, boolean checkMD5) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		spFolderCont = SPFolderCont.getInstance();
		
		this.checkMD5 = checkMD5;
		System.out.println("start Controller");
		long startTime = System.currentTimeMillis();
		
		this.localPath = localPath;
		//localPath = "D:/SkyDrive/UCN/Software konstruktion/Databaser";
		this.siteURL = siteURL;
		//sitePath = "/my-ecampus/holdsites/ec-dmaa0214/Materiale/2. Semester/Software konstruktion/Databaser";
		this.sitePath = sitePath;
		String[] rootName = sitePath.split("/");
		
		root = new SPFolder(rootName[rootName.length-1]);
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
        //credentialProvider.addNTLMCredentials(“username”, “mypasword”, null, -1, “localhost”, “mydomain”);
	    HtmlPage page = webClient.getPage(siteURL + sitePath);
	    getFilesOnPage(page, root);
	    
	    System.out.println("rootsize:" + root.getChildNodes().size());
	    if(root.getChildNodes().size() > 0){
		    for(Object s : root.getChildNodes()){
		    	System.out.println(s.getClass().getName());
		    	if(s instanceof SPFolder){
		    		getChildren((SPFolder)s);
		    	}
		    }
	    }
	    
	    long endTime   = System.currentTimeMillis();
	    System.out.println("Files: " + counter);
	    NumberFormat formatter = new DecimalFormat("#0.00000");
	    endTime = System.currentTimeMillis();
	    System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
	    System.out.println("rootsize end:" + root.getChildNodes().size());
	    
	    removeEmptyFolders();
	    
	    return root;
	}
	
	private void removeEmptyFolders() {
		ArrayList<SPFolder> folders = spFolderCont.getFiles();
	    for(SPFolder sp : folders){
	    	if(sp.isEmpty()){
	    		sp.getParent().removeChild(sp);
	    	}
	    }
	}

	private void getChildren(SPFolder s) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		spFolderCont.addFile(s);
		System.out.println(s.getPath());
		HtmlPage page = webClient.getPage(siteURL + s.getPath());
		getFilesOnPage(page, s);
		ArrayList<Object> children = s.getChildNodes();
		System.out.println("size: " + children.size());
		if(children.size() > 0){
			System.out.println("-----------");
			for(Object child : children){
				if(child instanceof SPFolder){
					getChildren((SPFolder)child);
					//if(isFoldersEmpty((SPFolder)child)) {
			    	//	((SPFolder)child).getParent().removeChild(child);
			    	//}
				}
			}
			
			System.out.println("-----------");
		}
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
	    			counter++;
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
		
	public void downloadFiles(List<Object> selectedList) throws NullPointerException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		if (selectedList.size() == 0) {
			throw new NullPointerException("Du skal vælge nogle filer først");
		}
		
		HashSet<SPFile> dlList = new HashSet<SPFile>();
		System.out.println("before while");
		while(selectedList.size() != 0) {
			System.out.println("list size: " + selectedList.size());
			Object obj = selectedList.get(0);
			if(obj instanceof SPFolder){
				for(Object objA : ((SPFolder) obj).getChildNodes()){
					System.out.println("forloop: " + objA);
					if (!selectedList.contains(objA)) {
						System.out.println("forloop if:" + objA);
						selectedList.add(objA);
					}
				}
				((SPFolder) obj).getParent().removeChild(obj);
			}else if(obj instanceof SPFile){
				System.out.println("added: " + obj);
				dlList.add((SPFile) obj);
				((SPFile) obj).getParent().removeChild(obj);
			}
			selectedList.remove(obj);
		}
		System.out.println("after while");
		
		for(SPFile f : dlList) {
			downloadFile(f);
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

	public SPFolder getRoot() {
		return root;
	}
}
