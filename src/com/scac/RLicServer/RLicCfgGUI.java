package com.scac.RLicServer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import sun.rmi.transport.proxy.CGIHandler;

public class RLicCfgGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RLicCfgGUI();

	}
	
	private static void setNativeLookAndFeel() {
	    try {
	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {
	      System.out.println("Error setting native LAF: " + e);
	    }
	  }
	
	public RLicCfgGUI() {
		// TODO Auto-generated constructor stub
		super("Fuck you");
		setNativeLookAndFeel();
		RLicDataHolder dh = RLicDataHolder.getInstance();
		try {
			dh.loadConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		CfgTreeModel model = new CfgTreeModel();
		JTree tree = new JTree(model);
		JScrollPane TreeView = new JScrollPane(tree);
		TreeView.setSize(300, 400);
		add(TreeView);
		setSize(300, 600);
		setVisible(true);
	}
	
		
	private class CfgTreeModel implements TreeModel{
		private RLicConfig cfg;
		
		public CfgTreeModel(){
			cfg = RLicDataHolder.getInstance().getCfg();
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
			if (arg0 instanceof RLicConfig){
				children = cfg.getTokens();
			}else{
				if (arg0 instanceof RLicToken){
					RLicToken tkn = (RLicToken)arg0;
					children = tkn.getUsers();
				}else
					return null;
			}
			return children;
		}

		public int getChildCount(Object arg0) {
			if (arg0 instanceof RLicConfig){
				return cfg.getTokens().size();
			}else{
				if (arg0 instanceof RLicToken){
					RLicToken tkn = (RLicToken)arg0;
					return tkn.getUsers().size();
				}
			}
			return 0;
		}

		public int getIndexOfChild(Object arg0, Object arg1) {
			ArrayList children = getObjChildren(arg0);
			for (int i=0;i<children.size();i++){
				if (arg1.equals(children.get(i)))
					return i;
			}
			return 0;
		}

		public Object getRoot() {
			return cfg;
		}

		public boolean isLeaf(Object arg0) {
			if (arg0 instanceof String) return true;
			return false;
		}

		public void removeTreeModelListener(TreeModelListener arg0) {
			// TODO Auto-generated method stub
			
		}

		public void valueForPathChanged(TreePath arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
	}
	

}
