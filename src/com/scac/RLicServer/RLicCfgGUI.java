package com.scac.RLicServer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class RLicCfgGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton AddBtn;
	private JButton RemBtn;
	private JButton SaveBtn;
	private JTree tree;
	private CfgTreeModel model;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RLicCfgGUI();

	}

	private static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
	}

	public RLicCfgGUI() {
		super("Access list editor");
		setNativeLookAndFeel();
		RLicDataHolder dh = RLicDataHolder.getInstance();
		try {
			dh.loadConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		model = new CfgTreeModel();
		tree = new JTree(model);
		tree.setEditable(true);
		JScrollPane TreeView = new JScrollPane(tree);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(TreeView, c);

		AddBtn = new JButton("Add");
		RemBtn = new JButton("Delete");
		SaveBtn = new JButton("Save");
		addActionListeners();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(AddBtn, c);
		c.gridx = 1;
		add(RemBtn, c);
		c.gridx = 2;
		add(SaveBtn, c);

		setSize(300, 600);
		setVisible(true);
	}

	private void addActionListeners() {
		AddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tree.getSelectionCount() != 1)
					return;
				Object node = tree.getSelectionPath().getLastPathComponent();
				if (node instanceof RLicConfig) {
					model.addNewNetwork();
				}else{
					if (node instanceof String) {
						node = tree.getSelectionPath().getParentPath().getLastPathComponent();
					}
					model.addNewUser((RLicToken)node);
				}
				tree.treeDidChange();
				tree.updateUI();
			}
		});
		
		RemBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tree.getSelectionCount() != 1)
					return;
				Object node = tree.getSelectionPath().getLastPathComponent();
				if (node instanceof RLicToken) {
					model.removeNetwork((RLicToken)node);
				}else{
					if (node instanceof String){
						RLicToken Network = (RLicToken) tree.getSelectionPath().getParentPath().getLastPathComponent();
						Network.getUsers().remove(node);
					}
				}
				tree.treeDidChange();
				tree.updateUI();
			}
		});
		SaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RLicDataHolder.getInstance().saveConfig();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});

	}

	private class CfgTreeModel implements TreeModel {
		private RLicConfig cfg;

		public CfgTreeModel() {
			cfg = RLicDataHolder.getInstance().getCfg();
		}

		public void removeNetwork(RLicToken node) {
			cfg.getTokens().remove(node);
			
		}

		public void addNewUser(RLicToken node) {
			int Cnt = node.getUsers().size() + 1; 
			node.getUsers().add("an_user"+Integer.toString(Cnt));
			
		}

		public void addNewNetwork() {
			RLicToken tkn = new RLicToken();
			tkn.setNetMask("X.X.X.X");
			cfg.getTokens().add(tkn);
		}

		public void addTreeModelListener(TreeModelListener arg0) {
			// TODO Auto-generated method stub

		}

		public Object getChild(Object arg0, int arg1) {
			ArrayList children = getObjChildren(arg0);
			return children.get(arg1);

		}

		private ArrayList getObjChildren(Object arg0) {
			ArrayList children;
			if (arg0 instanceof RLicConfig) {
				children = cfg.getTokens();
			} else {
				if (arg0 instanceof RLicToken) {
					RLicToken tkn = (RLicToken) arg0;
					children = tkn.getUsers();
				} else
					return null;
			}
			return children;
		}

		public int getChildCount(Object arg0) {
			if (arg0 instanceof RLicConfig) {
				return cfg.getTokens().size();
			} else {
				if (arg0 instanceof RLicToken) {
					RLicToken tkn = (RLicToken) arg0;
					return tkn.getUsers().size();
				}
			}
			return 0;
		}

		public int getIndexOfChild(Object arg0, Object arg1) {
			ArrayList children = getObjChildren(arg0);
			for (int i = 0; i < children.size(); i++) {
				if (arg1.equals(children.get(i)))
					return i;
			}
			return 0;
		}

		public Object getRoot() {
			return cfg;
		}

		public boolean isLeaf(Object arg0) {
			if (arg0 instanceof String)
				return true;
			return false;
		}

		public void removeTreeModelListener(TreeModelListener arg0) {
			// TODO Auto-generated method stub

		}

		public void valueForPathChanged(TreePath arg0, Object arg1) {
			if (arg0.getLastPathComponent() instanceof RLicConfig) {
				return;
			}
			if (arg0.getLastPathComponent() instanceof RLicToken) {
				RLicToken tkn = (RLicToken) arg0.getLastPathComponent();
				tkn.setNetMask((String) arg1);
				return;
			}
			if (arg0.getLastPathComponent() instanceof String) {
				RLicToken tkn = (RLicToken) arg0.getParentPath().getLastPathComponent();
				String OldUser = (String) arg0.getLastPathComponent();
				for (int i=0;i<tkn.getUsers().size();i++){
					if (OldUser.equals(tkn.getUsers().get(i))){
						tkn.getUsers().set(i, arg1);
						tree.updateUI();
						break;
					}
				
				}
			}

		}
	}

}
