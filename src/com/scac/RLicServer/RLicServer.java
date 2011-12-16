/**
 * Server class for remote licensing
 */
package com.scac.RLicServer;

/**
 * @author duce
 *
 */
import java.net.*;
import java.io.*;

public class RLicServer {
	private static RLicConfig cfg;
	public static void main(String[] args) throws IOException {

		getConfig();
		
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(cfg.getServerPort());
		} catch (IOException e) {
			System.err.printf("Could not listen on port: %1$d\n",cfg.getServerPort());
			System.exit(-1);
		}

		while (listening)
			new RLicServerThread(serverSocket.accept()).start();

		serverSocket.close();
	}

	private static void getConfig() {
		RLicDataHolder dh = RLicDataHolder.getInstance();
		try {
			dh.loadConfig();
		} catch (FileNotFoundException fnf) {
			System.out.println(fnf.getMessage());
			System.exit(1);
		}
		cfg = dh.getCfg();
	}
	
}