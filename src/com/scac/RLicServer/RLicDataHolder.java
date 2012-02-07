package com.scac.RLicServer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/** class RLicDataHolder. 
 * A singleton that manages configuration data
 * */
public final class RLicDataHolder {
	/** Constructor. */
	private RLicDataHolder() {
	}
	
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
	
	/**sets the edit flag on and off, a user should provide a key string
	 * to change the flag. The value of the key is stored plainly in the configuration xml file
	 * under the node "cfgKey"
	 * @param onoff
	 * @param Key
	 */
	public void switchEditMode(boolean onoff, String Key){
		if (cfg.getCfgKey().equals(Key)){
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