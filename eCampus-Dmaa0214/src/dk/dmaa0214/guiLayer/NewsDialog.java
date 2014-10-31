package dk.dmaa0214.guiLayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import dk.dmaa0214.modelLayer.SPNews;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class NewsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public NewsDialog(SPNews spNews) {
		setBounds(100, 100, 450, 128);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("fill:default"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "1, 1, fill, fill");
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblTitle = new JLabel("<html>" + spNews.getTitle() + "</html>");
				lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
				panel.add(lblTitle, BorderLayout.CENTER);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "1, 3, fill, fill");
			panel.setLayout(new BorderLayout(0, 0));
			{
				String text = makeLineBreak(spNews.getText(), 50);
				JLabel lblText = new JLabel("<html>" + text + "</html>");
				lblText.setVerticalAlignment(SwingConstants.TOP);
				lblText.setHorizontalAlignment(SwingConstants.LEFT);
				lblText.setFont(new Font("Tahoma", Font.PLAIN, 9));
				panel.add(lblText, BorderLayout.CENTER);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						NewsDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
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
