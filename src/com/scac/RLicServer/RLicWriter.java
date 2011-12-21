package com.scac.RLicServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class RLicWriter extends PrintWriter {
	
	public void println(String arg0){
		super.println(RLicUtils.strEncode(arg0));
	}

	public RLicWriter(Writer arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(OutputStream arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(String arg0) throws FileNotFoundException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(File arg0) throws FileNotFoundException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(Writer arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(OutputStream arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(String arg0, String arg1) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RLicWriter(File arg0, String arg1) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
