package dk.dmaa0214.guiLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import dk.dmaa0214.guiLayer.extensions.Base64HTMLEditorKit;
import dk.dmaa0214.guiLayer.extensions.KeyListener;
import dk.dmaa0214.modelLayer.SPNews;

import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;

public class NewsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public NewsDialog(SPNews spNews) {
		super((JDialog) null, spNews.getTitle(), true);
		KeyListener.addEscapeListener(this);
		String text = getText(spNews);
		String creatorText = "Added " + spNews.getDate() + " by " + spNews.getAddedBy();
		String channelsText = spNews.getChannelsAsSingleText();
		String channelToolTipText = spNews.getChannelsAsToolTipText();

		setBounds(100, 100, 450, 128);
		getContentPane().setLayout(new BorderLayout());

		JPanel topPane = new JPanel();
		topPane.setBorder(new EmptyBorder(5, 5, 0, 5));
		getContentPane().add(topPane, BorderLayout.NORTH);
		topPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("250px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("80px"),},
			new RowSpec[] {
				RowSpec.decode("14px"),}));

		JLabel lblTitle = new JLabel(spNews.getTitle());
		topPane.add(lblTitle, "1, 1, fill, top");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel lblChannels = new JLabel(channelsText);
		lblChannels.setHorizontalAlignment(SwingConstants.RIGHT);
		topPane.add(lblChannels, "3, 1, fill, top");
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "1, 1, fill, fill");

		JEditorPane	editorPanel = new JEditorPane();
		editorPanel.setContentType("text/html");
		editorPanel.setEditable(false);
		editorPanel.setEditorKit(new Base64HTMLEditorKit());
		editorPanel.setText(text);
		editorPanel.setBorder(null);
		editorPanel.setPreferredSize(new Dimension(500, 500));
		editorPanel.setCaretPosition(0);
		scrollPane.setViewportView(editorPanel);


		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("59px"),},
				new RowSpec[] {
				RowSpec.decode("23px"),}));

		JLabel lblCreatorInfo = new JLabel(creatorText);
		lblChannels.setToolTipText(channelToolTipText);
		buttonPane.add(lblCreatorInfo, "1, 1");

		buttonPane.add(cancelButton, "2, 1, left, top");
		
		pack();
	}

	private String getText(SPNews spNews) {
		String txt = spNews.getFullText();
		if(txt.isEmpty()) {
			txt = spNews.getText();
		}
		return txt;
	}

}
