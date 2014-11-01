package dk.dmaa0214.controllerLayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

import dk.dmaa0214.modelLayer.SPNews;

public class SPNewsScraper {
	
	//public static void main(String [] args) {
	//	new SPNewsScraper(); 
	//}
	
	private WebClient webClient;
	
	public SPNewsScraper(String user, String pass) {		
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
	}
	
	public void getSingleNews(SPNews spNews) throws FailingHttpStatusCodeException, MalformedURLException, IOException, NullPointerException {
		int id = spNews.getId();
		String siteDialogURL = "http://ecampus.ucn.dk/Noticeboard/Lists/NoticeBoard/DispForm.aspx?"
				+ "NoticeBoardItem=" + id + "&WebID=87441127-db6f-4499-8c99-3dea925e04a8&IsDlg=1";
		HtmlPage page = webClient.getPage(siteDialogURL);
		HtmlTableCell td = page.getFirstByXPath("//td[@class='wt-2column-t1-td1']");
		if(td == null) {
			throw new NullPointerException("Nyhedstekst kunne ikke hentes. Internkode: #3");
		}
		DomNode div = td.getChildNodes().get(0);
		if(div == null) {
			throw new NullPointerException("Nyhedstekst kunne ikke hentes. Internkode: #4");
		}
		System.out.println(div.getChildNodes());
		spNews.setFullText(div.asXml());
		//System.out.println(div.asXml());
	}

	public ArrayList<SPNews> getNewsList() throws NullPointerException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		String siteURL = "http://ecampus.ucn.dk/Noticeboard/_Layouts/NoticeBoard/Ajax.aspx?Action="
				+ "GetNewsList&ShowBodyContent=SHORT100&WebId=87441127-db6f-4499-8c99-3dea925e04a8"
				+ "&ChannelList=11776,4096,3811,3817,4311,4312,4313,4768,4314,4315,4316,4317,4310,"
				+ "&DateFormat=dd/MM/yyyy HH:mm&List=Current,Archived&IncludeRead=true&MaxToShow=10"
				+ "&Page=1&frontpageonly=false";
		HtmlPage page = webClient.getPage(siteURL);
	    return ScrapeNewsList(page.asText());
	}

	private ArrayList<SPNews> ScrapeNewsList(String input) throws NullPointerException {
		ArrayList<SPNews> newslist = new ArrayList<SPNews>();
		int iStart = getNextIndex(input, 0);
		if(!input.substring(0, iStart).equals("OK")) {
			throw new NullPointerException("Nyhederne kan ikke læses. Internkode: #1. Status: " + input.substring(0, iStart));
		}
		String[] allNews = input.split("\\|\\$\\$\\|");
		
		System.out.println("count: " + (allNews.length-1));
		for (int i = 1; i < allNews.length; i++) {
			String[] singleNews = allNews[i].split("\\|\\$\\|");
			if(singleNews.length != 11) {
				throw new NullPointerException("Nyhederne kan ikke læses. Internkode: #2. Rapport: " + singleNews.length);
			}
			int id = getIntFromString(singleNews[0]);
			String title = singleNews[1].trim();
			String date = singleNews[2].trim();
			boolean read = (getIntFromString(singleNews[3]) == 1);
			String[] channelArray = singleNews[4].trim().split("\\|");
			ArrayList<String> channels = new ArrayList<String>(Arrays.asList(channelArray));
			String addedBy = singleNews[6].trim();
			String text = singleNews[7].trim(); //7 and 8 is equal.
			
			SPNews newsObj = new SPNews(id, title, date, channels, text, addedBy, read);
			newslist.add(newsObj);
		}
		return newslist;
	}
	
	private int getIntFromString(String str) {
		int ret = -1;
		try {
			ret = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret = -1;
		}
		return ret;
	}	
	
	private int getNextIndex(String text, int fromIndex){
		int i = text.indexOf("|$|", fromIndex);
		if (i == -1) {
			throw new NullPointerException("Nyhederne kan ikke læses");
		}
		return i;
	}
}
