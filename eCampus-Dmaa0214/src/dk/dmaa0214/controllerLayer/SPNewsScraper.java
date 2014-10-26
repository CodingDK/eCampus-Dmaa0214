package dk.dmaa0214.controllerLayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import dk.dmaa0214.modelLayer.SPNews;
import dk.dmaa0214.modelLayer.SPNewsCont;

public class SPNewsScraper {
	
	//public static void main(String [] args) {
		//new SPNewsScraper();
	//}

	private SPNewsCont spNewsCont;
	private String user;
	private String pass;
	private String siteURL;
	
	public SPNewsScraper(String user, String pass) {
		this.user = user;
		this.pass = pass;
		this.spNewsCont = SPNewsCont.getInstance();
		this.siteURL = "http://ecampus.ucn.dk/Noticeboard/_Layouts/NoticeBoard/Ajax.aspx?Action="
				+ "GetNewsList&ShowBodyContent=SHORT200&WebId=87441127-db6f-4499-8c99-3dea925e04a8"
				+ "&ChannelList=11776,4096,3811,3817,4311,4312,4313,4768,4314,4315,4316,4317,4310,"
				+ "&DateFormat=dd/MM/yyyy HH:mm&List=Current,Archived&IncludeRead=true&MaxToShow=10"
				+ "&Page=1&frontpageonly=false";
		try {
			ScrapeNewsList(getNewsList());
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String getNewsList() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		DefaultCredentialsProvider credentialProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
		credentialProvider.addNTLMCredentials(user, pass, null, -1, "localhost", "UCN");
	    HtmlPage page = webClient.getPage(siteURL);
	    return page.asText();
	}

	private void ScrapeNewsList(String input) { //throws NullPointerException
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
			spNewsCont.addNews(newsObj);
			ArrayList<SPNews> objs = spNewsCont.getNewsList();
			for (SPNews spNews : objs) {
				System.out.println("id: " + spNews.getId());
				System.out.println("text: " + spNews.getTitle());
			}
		}
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
