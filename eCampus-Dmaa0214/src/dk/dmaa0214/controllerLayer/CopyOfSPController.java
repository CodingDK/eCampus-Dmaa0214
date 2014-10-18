package dk.dmaa0214.controllerLayer;

import java.io.File;
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
import java.util.List;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import dk.dmaa0214.modelLayer.SPFile;
import dk.dmaa0214.modelLayer.SPFileCont;


public class CopyOfSPController {

	private ArrayList<String> folders;
	private SPFileCont spFileCon;
	private String siteURL;
	private String sitePath;
	private String localPath;
	private WebClient webClient;

	public CopyOfSPController() {
		siteURL = null;
	}
	
	public void getConnectedToSP(String user, String pass, String localPath, String siteURL, String sitePath) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		System.out.println("start Controller");
		long startTime = System.currentTimeMillis();
		folders = new ArrayList<String>();
		spFileCon = SPFileCont.getInstance();
		spFileCon.getFiles().clear();
		this.localPath = localPath;
		//localPath = "D:/SkyDrive/UCN/Software konstruktion/Databaser";
		this.siteURL = siteURL;
		//sitePath = "/my-ecampus/holdsites/ec-dmaa0214/Materiale/2. Semester/Software konstruktion/Databaser";
		this.sitePath = sitePath;
				
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
        //credentialProvider.addNTLMCredentials(“username”, “mypasword”, null, -1, “localhost”, “mydomain”);
	    HtmlPage page = webClient.getPage(siteURL + sitePath);
	    getFilesOnPage(page);
	    while(!folders.isEmpty()) {
	    	String path = folders.get(0);
	    	if(path != null) {
	    		page = webClient.getPage(siteURL+ path);
	    		getFilesOnPage(page);
	    	}
	    	folders.remove(0);
	    }
	    
	    long endTime   = System.currentTimeMillis();
	    System.out.println("Files: " + spFileCon.getFiles().size());
	    NumberFormat formatter = new DecimalFormat("#0.00000");
	   // System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
	    //for(SPFile f : fileCon.getFiles()) {
	    //	System.out.println(f.getShortPath());
	    	//downloadFile(f);
	   // }
	    endTime = System.currentTimeMillis();
	    System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
	
	}

	private void getFilesOnPage(HtmlPage page) throws UnsupportedEncodingException {
		HtmlTable matTable = page.getFirstByXPath("//table[@id='onetidDoclibViewTbl0']");
	    for (HtmlTableRow matRow : matTable.getRows()) {
	    	if(!matRow.getAttribute("class").equals("ms-viewheadertr ms-vhltr")) {
	    		//System.out.println(matRow);
	    		SPFile spFile = new SPFile(sitePath);
	    		for (HtmlTableCell matCell : matRow.getCells()) {
	    			String cellClassAtt = matCell.getAttribute("class");
					
	    			if (cellClassAtt.equals("ms-vb-title")) {
	    				spFile.setName(matCell.asText().trim());
	    				HtmlAnchor link = (HtmlAnchor) matCell.getFirstChild().getFirstChild();
	    				if(link == null) {
	    					throw new NullPointerException("Error in reading from site. errorcode: 1");
	    				}
	    				String href = link.getHrefAttribute();
	    				if(href.equals("")) {
	    					throw new NullPointerException("Error in reading from site. errorcode: 2");
	    				}
	    				else if(href.contains("Forms/AllItems.aspx?")) {
	    					href = href.substring(href.indexOf("RootFolder=")+11,href.indexOf("&FolderCTID"));
	    					href = URLDecoder.decode(href, "UTF-8");
	    					if(href != null) {
	    						folders.add(href);
	    					}
	    					
	    					//System.out.println("Containes: " + href);
	    				}
	    				else {
	    					//System.out.println("else: " + href);
		    				//System.out.println("----");
	    					spFile.setPathAndType(href);		    	    		
	    				}
	    			}
	    			else if (cellClassAtt.equals("ms-vb2")) {
	    				spFile.setChangedTime(matCell.asText());
	    			}
	    			else if (cellClassAtt.equals("ms-vb-user")) {
	    				spFile.setAddedBy(matCell.asText());
	    			}
				}
	    		if(spFile.getPath() != null && !hasLocalFile(spFile)) {
	    			spFileCon.addFile(spFile);
				}
	    //System.out.println("BREAK");		
	    //break;
	    	}
	    }
	}

	private boolean hasLocalFile(SPFile spFile) {
		boolean retVal = true;
		File file = new File(localPath + spFile.getShortPath());
		if(!file.exists()) {
			retVal = false;
			System.out.println("test: " + file.getParentFile().exists());
			//downloadFile(spFile);
			//System.out.println(mixPath + ", " + spFile.getChangedTime() + " ## " + file.lastModified());
		}
		return retVal;
	}
	
	public void downloadFiles(List<SPFile> selectedList) throws NullPointerException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		if (selectedList.size() == 0) {
			throw new NullPointerException("Du skal vælge nogle filer først");
		}
		for (SPFile f : selectedList) {
			downloadFile(f);
			spFileCon.remove(f);
		}
	}
	
	public void downloadFile(SPFile spFile) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		if(siteURL == null) {
			throw new NullPointerException("Du skal sammenligne filer først");
		}
		
		File localFile = new File(localPath + spFile.getShortPath());
	    InputStream ins = webClient.getPage(siteURL + spFile.getPath()).getWebResponse().getContentAsStream();
	    
    	if(!localFile.getParentFile().exists()) {
    		System.out.println("create dir: " + localFile.getParentFile().getName());
    		localFile.getParentFile().mkdirs();
    	}
    	OutputStream ous = new FileOutputStream(localFile);
        int length = -1;
        System.out.println("downloading " + spFile.getName());
        byte[] buffer = new byte[1024];
        while ((length = ins.read(buffer)) > -1) {
        	ous.write(buffer, 0, length);
        }
        System.out.println("downloading done");
        ous.close();
        ins.close();
	    
	}
}
