package dk.dmaa0214.guiLayer;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.SwingConstants;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import dk.dmaa0214.controllerLayer.SPController;
import dk.dmaa0214.modelLayer.SPFile;
import dk.dmaa0214.modelLayer.SPFileCont;

public class SPGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUser;
	private JTextField txtPass;
	private JTextField txtSPPath;
	private JTextField txtLocalPath;
	private String siteURL;
	private String sitePath;
	private JList<SPFile> spList;
	private SPController spCtr;
	private SPFileCont spfileCont;
	private DefaultListModel<SPFile> spListModel;

	/**
	 * Create the application.
	 */
	public SPGUI() {
		spCtr = new SPController();
		siteURL = "http://ecampus.ucn.dk";
		sitePath = "/my-ecampus/holdsites/ec-dmaa0214/Materiale/";
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{550, 0};
		gridBagLayout.rowHeights = new int[]{300, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 172, 40);
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 2, 0, 0));
		
		JLabel lblUser = new JLabel("Brugernavn");
		panel_1.add(lblUser);
		
		txtUser = new JTextField();
		lblUser.setLabelFor(txtUser);
		panel_1.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblPass = new JLabel("Password");
		panel_1.add(lblPass);
		
		txtPass = new JPasswordField();
		lblPass.setLabelFor(txtPass);
		txtPass.setColumns(10);
		panel_1.add(txtPass);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 50, 353, 33);
		panel.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblLocalPath = new JLabel("Lokal sti");
		panel_2.add(lblLocalPath);
		lblLocalPath.setLabelFor(txtLocalPath);
		
		txtLocalPath = new JTextField();
		panel_2.add(txtLocalPath);
		txtLocalPath.setColumns(18);
		
		JButton btnBrowser = new JButton("Browse");
		btnBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browseForLocalPath();
			}
		});
		panel_2.add(btnBrowser);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(406, 112, 144, 71);
		panel.add(panel_3);
		
		JButton btnRun = new JButton("Sammenlign filer");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getFileList();
			}
		});
		panel_3.add(btnRun);
		
		JButton btnDownload = new JButton("Download selected");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				downloadSelected();
			}
		});
		panel_3.add(btnDownload);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 111, 386, 178);
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		spList = new JList<SPFile>();
		spfileCont = SPFileCont.getInstance();
		spListModel = new DefaultListModel<SPFile>();
		spList.setModel(spListModel);
		panel_4.add(spList);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 82, 553, 26);
		panel.add(panel_5);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblPath = new JLabel("http://ecampus.ucn.dk/my-ecampus/holdsites/ec-dmaa0214/Materiale/");
		lblPath.setHorizontalAlignment(SwingConstants.LEFT);
		lblPath.setLabelFor(txtLocalPath);
		panel_5.add(lblPath);
		
		txtSPPath = new JTextField();
		panel_5.add(txtSPPath);
		txtSPPath.setColumns(10);
		
		txtSPPath.setText("2. Semester/ITIO");
		
	}

	private void updateModel() {
		spListModel.removeAllElements();
		for(SPFile f : spfileCont.getFiles()){
			spListModel.addElement(f);
		}
	}

	private void downloadSelected() {
		List<SPFile> selectedList = spList.getSelectedValuesList();
		try {
			spCtr.downloadFiles(selectedList);
		} catch (NullPointerException e) {
			showErrorDialog(e.getMessage());
		} catch (FailingHttpStatusCodeException e) {
			showErrorDialog(e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			showErrorDialog(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			showErrorDialog(e.getMessage());
			e.printStackTrace();
		}
		updateModel();
	}

	private void browseForLocalPath() {
		JFileChooser j = new JFileChooser();
		j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int choice = j.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			txtLocalPath.setText(j.getSelectedFile().getPath());
		}
	}

	private void getFileList() {
		try {
			String user = txtUser.getText(); 
			String pass = txtPass.getText();
			String localPath = txtLocalPath.getText();
			//System.out.println("local: " + localPath);
			//System.out.println("siteURL: "+ siteURL); 
			//System.out.println("sitePath: "+ sitePath + txtSPPath.getText()); 
			if(user.isEmpty()) {
				showErrorDialog("Brugernav må ikke være tomt");
			}
			else if(pass.isEmpty()) {
				showErrorDialog("Password må ikke være tomt");
			}
			else if(localPath.isEmpty()) {
				showErrorDialog("Lokal sti må ikke være tom");			
			}
			else {
				spCtr.getConnectedToSP(user, pass, localPath, siteURL, sitePath + txtSPPath.getText());
				updateModel();
			}
					
		}
		catch (FailingHttpStatusCodeException e) {
			if (e.getStatusCode() == 401) {
				showErrorDialog("Login er forkert");
			}
			else if (e.getStatusCode() == 404) {
				showErrorDialog("Sti til eCampus er forkert");
			}
			else {
				showErrorDialog("Code: " +  e.getStatusCode() + ": " + e.getStatusMessage());
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			showErrorDialog(e.getMessage());
		}
		
	}
	
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
