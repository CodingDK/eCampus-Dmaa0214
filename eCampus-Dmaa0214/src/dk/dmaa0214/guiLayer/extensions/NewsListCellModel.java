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
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.SwingConstants;

public class NewsListCellModel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewsListCellModel(SPNews spNews, boolean isSelected) {
		setBackground(Color.WHITE);
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("320px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		if(isSelected){
			panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		add(panel, "2, 2, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel.add(panel_2, "1, 1, fill, fill");
		panel_2.setLayout(new BorderLayout(0, 0));
		
		String title = makeLineBreak(spNews.getTitle(),75);
		
		JLabel lblTitle = new JLabel("<html>" + title + "</html>");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblTitle, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, "1, 3, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel_2 = new JLabel(spNews.getChannels().get(0) + ", " + spNews.getAddedBy());
		panel_3.add(lblNewLabel_2, "1, 1");
		
		JLabel lblNewLabel_1 = new JLabel(spNews.getDate());
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_3.add(lblNewLabel_1, "3, 1");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, "1, 5, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		String text = makeLineBreak(spNews.getText(),75);
		
		JLabel lblText = new JLabel("<html>" + text + "</html>");
		lblText.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(lblText, BorderLayout.CENTER);
		
		
		
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
