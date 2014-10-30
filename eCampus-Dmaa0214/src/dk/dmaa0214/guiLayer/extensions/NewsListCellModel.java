package dk.dmaa0214.guiLayer.extensions;

import javax.swing.JPanel;

import dk.dmaa0214.modelLayer.SPNews;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;

public class NewsListCellModel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewsListCellModel(SPNews spNews) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:default:grow"),},
			new RowSpec[] {
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("max(17dlu;default):grow"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("max(18dlu;default):grow"),}));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, "1, 2, fill, fill");
		String title = makeLineBreak(spNews.getTitle(),30);
		JLabel lblTitle = new JLabel("<html>" + title + "</html>");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblTitle);
		
		JPanel panel = new JPanel();
		add(panel, "1, 4, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));
		
		String text = makeLineBreak(spNews.getText(),35);
		
		JLabel lblText = new JLabel("<html>" + text + "</html>");
		lblText.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel.add(lblText);
	}
	
	private String makeLineBreak(String str, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
		    if (i > 0 && (i % length == 0)) {
		        sb.append("<br>");
		    }

		    sb.append(str.charAt(i));
		}

		str = sb.toString();
		return str;
	}

}
