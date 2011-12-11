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
import java.util.logging.*;

public class RLicServer {
	private static RLicConfig cfg;
	public static void main(String[] args) throws IOException {

		getConfig();
		
		configLog();

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
	
	private static void configLog() throws IOException {
		FileHandler fh = new FileHandler(cfg.getLogPath());
		// TODO: make more appropriate formatter here
		SimpleFormatter sf = new SimpleFormatter();
		fh.setFormatter(sf);
		Logger log = Logger.getLogger("com.scac.rlic");
		log.setLevel(Level.ALL);
		log.addHandler(fh);
	}
}