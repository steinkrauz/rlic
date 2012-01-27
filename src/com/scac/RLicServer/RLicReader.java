package com.scac.RLicServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class RLicReader extends BufferedReader {

	public RLicReader(Reader arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public String readLine() throws IOException {
		String in = super.readLine();
		if (in != null)
			return RLicUtils.strDecode(in);
		else
			return "";
	}

}
