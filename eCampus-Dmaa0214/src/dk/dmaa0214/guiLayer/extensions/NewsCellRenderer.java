package dk.dmaa0214.guiLayer.extensions;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import dk.dmaa0214.modelLayer.SPNews;

public class NewsCellRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewsCellRenderer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		SPNews newsItem = (SPNews) value;
		NewsListCellModel cellModel = new NewsListCellModel(newsItem, isSelected);
		
		return cellModel;
	}

}
