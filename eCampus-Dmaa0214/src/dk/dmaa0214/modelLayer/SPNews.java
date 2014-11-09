package dk.dmaa0214.modelLayer;

import java.util.ArrayList;

public class SPNews {

	private int id;
	private String title;
	private String date;
	private ArrayList<String> channels;
	private String text;
	private String fullText;
	private String addedBy;
	private boolean read;
	
	/**
	 * Instantiates a new SPNews.
	 *
	 * @param id the id
	 * @param title the title
	 * @param date the date
	 * @param channels the channels
	 * @param text the text
	 * @param addedBy the added by
	 * @param read the read
	 */
	public SPNews(int id, String title, String date,
			ArrayList<String> channels, String text, String addedBy, boolean read) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		if (channels != null) {
			this.channels = channels;
		} else {
			this.channels = new ArrayList<String>();
		}
		this.text = text;
		this.addedBy = addedBy;
		this.read = read;
		this.fullText = "";
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the channels
	 */
	public ArrayList<String> getChannels() {
		return channels;
	}
	
	public String getChannelsAsSingleText() {
		String text = "None";
		if (channels.size() > 1) {
			text = "More channels";
		} else if (channels.size() == 1) {
			text = channels.get(0);
		}
		return text;
	}
	
	public String getChannelsAsToolTipText() {
		String text = "";
		if (channels.size() > 1) {
			text = channels.toString();
			text = text.substring(1, text.length() - 1).replace(", ", ",");
			text = text.replace(",", "<br>");
			text = "<html>" + text + "</html>";
		} else if (channels.size() == 1) {
			text = channels.get(0);
		}
		return text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the addedBy
	 */
	public String getAddedBy() {
		return addedBy;
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @return the fullText
	 */
	public String getFullText() {
		return fullText;
	}

	/**
	 * @param fullText the fullText to set
	 */
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
}
