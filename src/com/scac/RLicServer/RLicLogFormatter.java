package com.scac.RLicServer;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class RLicLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		// Create a StringBuffer to contain the formatted record
		// start with the date.
		StringBuffer sb = new StringBuffer();

		// Get the date from the LogRecord and add it to the buffer
		Date date = new Date(record.getMillis());
		sb.append(String.format("%1$tT %1$td.%1$tm.%1$tY", date));
		sb.append(";");

		// Get the formatted message (includes localization
		// and substitution of parameters) and add it to the buffer
		sb.append(formatMessage(record));
		sb.append("\n");

		return sb.toString();
	}

}
