package dk.dmaa0214.modelLayer;

import java.util.ArrayList;

public class SPNewsCont {
	
	private static SPNewsCont instance;
	private ArrayList<SPNews> newsList;

	private SPNewsCont() {
		newsList = new ArrayList<SPNews>();
	}
	
	public static SPNewsCont getInstance() {
		if (instance == null) {
			instance = new SPNewsCont();
		}
		return instance;
	}

	/**
	 * @return the newsList
	 */
	public ArrayList<SPNews> getNewsList() {
		return newsList;
	}

	public void addNews(SPNews news) {
		newsList.add(news);
	}

}
