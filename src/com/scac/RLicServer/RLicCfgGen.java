package com.scac.RLicServer;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RLicCfgGen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLEncoder e;
		RLicConfig cfg = new RLicConfig();
		cfg.setServerPort(4444);
		cfg.setLogPath("connect.log");
		RLicToken tkn = new RLicToken();
		tkn.setNetMask("10.1.1.50");
		ArrayList usr = new ArrayList();
		usr.add("duce");usr.add("qqqq");usr.add("www");
		tkn.setUsers(usr);
		ArrayList tkns = new ArrayList();
		tkns.add(tkn);
		cfg.setTokens(tkns);
		try {
			
			 e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream("default.xml")));
			 e.writeObject(cfg);
			 e.close();
		} catch (FileNotFoundException fnf) {
			System.out.println(fnf.getMessage());
		}
		
		
	}

}
