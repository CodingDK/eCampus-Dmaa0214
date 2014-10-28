package dk.dmaa0214.guiLayer;

import javax.swing.JPanel;
import javax.swing.JTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import dk.dmaa0214.controllerLayer.FileDownloader;
import dk.dmaa0214.controllerLayer.FileScraper;
import dk.dmaa0214.guiLayer.extensions.FileTreeCellRenderer;
import dk.dmaa0214.guiLayer.extensions.FileTreeModel;
import dk.dmaa0214.guiLayer.extensions.JFilePath;
import dk.dmaa0214.modelLayer.SPFolder;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

public class SPGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUser;
	private JTextField txtPass;
	private JTextField txtLocalPath;
	private JTextField txtSPPath;
	private String siteURL;
	private String sitePath;
	private JTree tree;
	private FileTreeModel model;
	private SPFolder root;
	private JCheckBox chkMD5;
	private JButton btnDownload;
	private JProgressBar progressBar;
	private JLabel lblStatus;
	private JPanel statusPan;
	private JButton btnRun;
	/**
	 * Create the panel.
	 */
	public SPGUI() {
		siteURL = "http://ecampus.ucn.dk";
		sitePath = "/my-ecampus/holdsites/ec-dmaa0214/Materiale/";
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("265px:grow"),
				ColumnSpec.decode("175px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("52px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("26px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("26px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("212px:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("26px"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, "2, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(88dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Username:");
		panel_1.add(lblNewLabel, "1, 2, left, default");
		
		txtUser = new JTextField();
		panel_1.add(txtUser, "3, 2, fill, default");
		txtUser.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		panel_1.add(lblNewLabel_1, "1, 4, left, default");
		
		txtPass = new JPasswordField();
		panel_1.add(txtPass, "3, 4, fill, default");
		txtPass.setColumns(10);
		
		JPanel panel = new JPanel();
		add(panel, "3, 1, fill, fill");
		
		JPanel panel_6 = new JPanel();
		add(panel_6, "2, 3, 2, 1, fill, fill");
		panel_6.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("26px:grow"),}));
		
		JLabel lblLocalPath = new JLabel("Local Path:");
		panel_6.add(lblLocalPath, "1, 1, right, default");
		
		txtLocalPath = new JFilePath();
		txtLocalPath.setText("D:\\ITIO");
		panel_6.add(txtLocalPath, "3, 1, fill, default");
		txtLocalPath.setColumns(10);
		
		
		JButton btnBrowser = new JButton("Browse");
		btnBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browseForLocalPath();
			}
		});
		panel_6.add(btnBrowser, "5, 1");
		
		JPanel panel_2 = new JPanel();
		add(panel_2, "2, 5, 2, 1, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("26px"),}));
		
		JLabel lblNewLabel_2 = new JLabel("http://ecampus.ucn.dk/.../Materiale/");
		panel_2.add(lblNewLabel_2, "1, 1, right, default");
		
		txtSPPath = new JTextField();
		txtSPPath.setText("2. Semester/ITIO");
		panel_2.add(txtSPPath, "3, 1, 5, 1, fill, default");
		txtSPPath.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 7, fill, fill");
		
		model = new FileTreeModel(null);
		
		tree = new JTree(model);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				if(tree.getSelectionCount() != 0){
					btnDownload.setEnabled(true);
				} else {
					btnDownload.setEnabled(false);
				}
			}
		});
		tree.setRootVisible(false);
		tree.setCellRenderer(new FileTreeCellRenderer());  
		scrollPane.setViewportView(tree);
		
		JPanel panel_3 = new JPanel();
		add(panel_3, "3, 7, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("max(39dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.add(panel_5, "2, 1, fill, fill");
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px:grow"),}));
		
		btnRun = new JButton("Synchronize");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) statusPan.getLayout();
				cl.show(statusPan, "StatusLabel");
				getFileList();
			}
		});
		panel_5.add(btnRun, "2, 2");
		
		btnDownload = new JButton("Download Changes");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) statusPan.getLayout();
				cl.show(statusPan, "StatusProgress");
				downloadSelected();
			}
		});
		btnDownload.setEnabled(false);
		panel_5.add(btnDownload, "2, 4");
		
		JPanel panel_7 = new JPanel();
		panel_5.add(panel_7, "2, 6, fill, fill");
		panel_7.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("4dlu:grow"),
				FormFactory.BUTTON_COLSPEC,
				ColumnSpec.decode("3dlu:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		chkMD5 = new JCheckBox("Check MD5");
		panel_7.add(chkMD5, "2, 1");
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, "2, 3, fill, fill");
		
		statusPan = new JPanel();
		statusPan.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(statusPan, "2, 9, 2, 1, fill, fill");
		statusPan.setLayout(new CardLayout(0, 0));
		
		//statusPan.
		
		JPanel panel_8 = new JPanel();
		statusPan.add(panel_8, "StatusLabel");
		panel_8.setLayout(new BorderLayout(0, 0));
		
		lblStatus = new JLabel("Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel_8.add(lblStatus, BorderLayout.CENTER);
		
		JPanel panel_9 = new JPanel();
		statusPan.add(panel_9, "StatusProgress");
		panel_9.setLayout(new BorderLayout(0, 0));
		
		progressBar = new JProgressBar();
		panel_9.add(progressBar, BorderLayout.CENTER);
		progressBar.setStringPainted(true);

	}

	private void downloadSelected() {
		btnDownload.setEnabled(false);
		btnRun.setEnabled(false);
		
		final String user = txtUser.getText(); 
		final String pass = txtPass.getText();
		final String localPath = txtLocalPath.getText();
		ArrayList<Object> objs = new ArrayList<Object>();
		
		TreePath[] paths = tree.getSelectionPaths();
		for(TreePath ps : paths){
			objs.add(ps.getLastPathComponent());
		}
		
		final FileDownloader fs = new FileDownloader(user, pass, localPath, siteURL, objs, this, progressBar);
		fs.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("state")){
					switch ((StateValue) evt.getNewValue()){
					case DONE:
						try {
							fs.get();
							reloadTree();
							lblStatus.setText("Status: Done");
							btnRun.setEnabled(true);
						} catch (Exception e) {
							btnRun.setEnabled(true);
							lblStatus.setText("Status: Error");
						}
						break;
					default:
						System.out.println(evt.getNewValue());
						break;
					}
				}
			}
		});
		
		fs.execute();
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
		final String user = txtUser.getText(); 
		final String pass = txtPass.getText();
		final String localPath = txtLocalPath.getText();
		String sitePathTxt = txtSPPath.getText();
		if(user.isEmpty()) {
			showErrorDialog("Brugernavn må ikke være tomt");
		} else if(pass.isEmpty()) {
			showErrorDialog("Password må ikke være tomt");
		} else if(localPath.isEmpty()) {
			showErrorDialog("Lokal sti må ikke være tom");
		} else {
			if(sitePathTxt.trim().substring(0, 1).equals("/")) {
				sitePathTxt = sitePathTxt.trim().substring(1);
			}
			clearTree();
			final FileScraper fs = new FileScraper(user, pass, localPath, siteURL, sitePath + sitePathTxt, chkMD5.isSelected(), lblStatus);
			fs.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println(evt.getPropertyName());
					if(evt.getPropertyName().equals("state")){
						System.out.println(evt.getNewValue());
						switch ((StateValue) evt.getNewValue()){
						case DONE:
							try {
								root = fs.get();
								reloadTree();
								lblStatus.setText("Status: Done");
							} catch (Exception e) {
								lblStatus.setText("Status: Error");
							}
							break;
						}
					}
				}
			});
			fs.execute();
		}	
	}
	
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
    private void expandAllNodes(){
        for(int i=0;i<tree.getRowCount();++i){
            tree.expandRow(i);
        }
    }
    
    private void clearTree() {
    	model = new FileTreeModel(null);
    	tree.setModel(model);
    }

	public void reloadTree(){
    	model = new FileTreeModel(root);
    	tree.setModel(model);
		expandAllNodes();
    }

}
