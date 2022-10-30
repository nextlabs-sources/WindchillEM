package com.nextlabs;

public enum LogLevel {UNDEFINED, DEBUG, INFO, WARN, ERROR;

	public static LogLevel parse(String loglevel)
	{
		if(loglevel.equalsIgnoreCase("debug"))
			return DEBUG;
		else if(loglevel.equalsIgnoreCase("info"))
			return INFO;
		else if(loglevel.equalsIgnoreCase("warn"))
			return WARN;
		else if(loglevel.equalsIgnoreCase("error"))
			return ERROR;
		else
			return UNDEFINED;
	}
	public static String format(LogLevel llvl)
	{
		switch(llvl)
		{
		case DEBUG:
			return "DEBUG";
		case INFO:
			return "INFO ";
		case WARN:
			return "WARN ";
		case ERROR:
			return "ERROR";
		case UNDEFINED:
			return "UNDEF";
		default:
			return "UNKWN";
		}
	}

}
