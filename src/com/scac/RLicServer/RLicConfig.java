package com.scac.RLicServer;

import java.util.ArrayList;

/**
 * class RLicConfig. Contains Server configuration as well as some licensing
 * data
 */
public final class RLicConfig {
	/** Constructor. */
	public RLicConfig() {
		_Tokens = new ArrayList();
	}

	private int _ServerPort;

	public int getServerPort() {
		return _ServerPort;
	}

	public void setServerPort(int port) {
		_ServerPort = port;
	}

	private String _LogPath;

	public String getLogPath() {
		return _LogPath;
	}

	public void setLogPath(String path) {
		_LogPath = path;
	}

	private ArrayList _Tokens;

	public ArrayList getTokens() {
		return _Tokens;
	}

	public void setTokens(ArrayList data) {
		_Tokens = data;
	}

	public String toString() {
		String Host;
		try {
			Host = java.net.InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			Host = "localhost";
		}
		return Integer.toString(_ServerPort) + "@" + Host;
	}
}