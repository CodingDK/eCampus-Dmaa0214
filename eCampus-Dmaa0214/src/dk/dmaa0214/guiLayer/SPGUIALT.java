package dk.dmaa0214.guiLayer;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JScrollPane;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import dk.dmaa0214.controllerLayer.SPController;
import dk.dmaa0214.modelLayer.SPFile;
import dk.dmaa0214.modelLayer.SPFileCont;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JButton;

public class SPGUIALT extends JPanel {
	private JTextField txtUser;
	private JTextField txtPass;
	private JTextField txtLocalPath;
	private JTextField txtSPPath;
	private String siteURL;
	private String sitePath;
	private SPController spCtr;
	private SPFileCont spfileCont;
	private JTree tree;
	private DefaultTreeModel model;
	protected ArrayList<Object> selected;
	protected ArrayList<TreePath[]> paths;
	/**
	 * Create the panel.
	 */
	public SPGUIALT() {
		paths = new ArrayList<TreePath[]>();
		selected = new ArrayList<Object>();
		spCtr = new SPController();
		siteURL = "http://ecampus.ucn.dk";
		sitePath = "/my-ecampus/holdsites/ec-dmaa0214/Materiale/";
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("265px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("175px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("52px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("26px:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("26px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("212px:grow"),
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
		add(panel, "4, 1, fill, fill");
		
		JPanel panel_6 = new JPanel();
		add(panel_6, "2, 3, 3, 1, fill, fill");
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
		
		txtLocalPath = new JTextField();
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
		add(panel_2, "2, 5, 3, 1, fill, fill");
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
		panel_2.add(txtSPPath, "3, 1, fill, default");
		txtSPPath.setColumns(10);
		
		JComboBox cmbFirstPath = new JComboBox();
		panel_2.add(cmbFirstPath, "5, 1, fill, default");
		
		JComboBox cmnSecondPath = new JComboBox();
		panel_2.add(cmnSecondPath, "7, 1, fill, default");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 7, fill, fill");
		
		DefaultMutableTreeNode root  = new DefaultMutableTreeNode("ROOT");
		
		model = new DefaultTreeModel(root);
		
		tree = new JTree(model);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				makeSelections(arg0);
			}
		});
		tree.setRootVisible(false);
		scrollPane.setViewportView(tree);
		
		JPanel panel_3 = new JPanel();
		add(panel_3, "4, 7, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(39dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.add(panel_5, "2, 2, fill, fill");
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JButton btnRun = new JButton("Synchronize");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getFileList();
			}
		});
		panel_5.add(btnRun, "2, 2");
		
		JButton btnDownload = new JButton("Download Changes");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				downloadSelected();
			}
		});
		panel_5.add(btnDownload, "2, 4");
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, "2, 4, fill, fill");

	}
	
	protected void makeSelections(TreeSelectionEvent arg0) {
		TreePath[] pth = arg0.getPaths();
		for(TreePath ps : pth){
			System.out.println(ps + "/");
		}
		
		for(TreePath p : pth){
			if(arg0.isAddedPath(p)){
				selected.add(p.getLastPathComponent());
			}else{
				selected.remove(p.getLastPathComponent());
			}
		}
	}
	

	private void downloadSelected() {
		for(Object o : selected){
			System.out.println(o);
		}
		/*
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
		*/
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
				buildTreeFromString(model, SPFileCont.getInstance().getFiles());
				model.reload();
				expandAllNodes();
				//updateModel();
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
	
	private void buildTreeFromString(final DefaultTreeModel model, final ArrayList<SPFile> SPFiles) {
		
		String rmPath = sitePath + txtSPPath.getText();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        
        for(SPFile sp : SPFiles){
        String [] strings = sp.getPath().substring(rmPath.length()+1).split("/");

        DefaultMutableTreeNode node = root;

	        for (String s: strings) {
	            int index = childIndex(node, s);
	            
	            if (index < 0) {
	                // Add the new node
	                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
	                node.insert(newChild, node.getChildCount());
	                node = newChild;
	            } else {
	                node = (DefaultMutableTreeNode) node.getChildAt(index);
	            }
	        }
        }
    }

    private int childIndex(final DefaultMutableTreeNode node, final String childValue) {
        Enumeration<DefaultMutableTreeNode> children = node.children();
        DefaultMutableTreeNode child = null;
        int index = -1;

        while (children.hasMoreElements() && index < 0) {
            child = children.nextElement();

            if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
                index = node.getIndex(child);
            }
        }

        return index;
    }
    
    private void expandAllNodes(){
        for(int i=0;i<tree.getRowCount();++i){
            tree.expandRow(i);
        }
    }

}
