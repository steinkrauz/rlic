package com.scac.RLicServer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/** class RLicDataHolder. */
public final class RLicDataHolder {
	/** Constructor. */
	private RLicDataHolder() {
	}
	
	//private static String CFG_KEY="df345Kna2D"; 
	private static String CFG_KEY="d";

	private static RLicDataHolder instance = null;

	public static RLicDataHolder getInstance() {
		if (instance == null) {
			instance = new RLicDataHolder();
		}
		return instance;
	}
	
	private boolean _EditMode;
	public boolean getEditMode(){
		return _EditMode;
	}
	
	public void switchEditMode(boolean onoff, String Key){
		if (CFG_KEY.equals(Key)){
			_EditMode = onoff;
		}
	}

	private RLicConfig cfg;

	public void loadConfig() throws FileNotFoundException {
		XMLDecoder decoder = new XMLDecoder(new FileInputStream(
				"rlicsettings.xml"));
		setCfg((RLicConfig) decoder.readObject());
		decoder.close();

	}
	
	public void saveConfig() throws FileNotFoundException{
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream("rlicsettings.xml")));
		e.writeObject(cfg);
		e.close();
		
	}

	public RLicConfig getCfg() {
		return cfg;
	}

	protected void setCfg(RLicConfig cfg) {
		this.cfg = cfg;
	}
}