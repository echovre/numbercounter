package com.example.MyApp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log{

	//Assuming that requirement 5:
	//		Only numbers may be written to the log file.
	// 		Each number must be followed by a server-native newline sequence.
	//means that log file header and the "INFO:" part of the log message is acceptable.
	//If not see log2.java 
	
    static Handler fileHandler = null;
    
    private static final Logger LOGGER = Logger.getLogger(Log.class.getClass().getName());
    
    public Log(String logFile) {
    	try {
            fileHandler = new FileHandler(logFile);
            SimpleFormatter simple = new SimpleFormatter();
            fileHandler.setFormatter(simple);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
    
    public void close() {
    	LOGGER.info("done.");
    }
    
    public void log(String message) {
    	synchronized(LOGGER){
    		LOGGER.info(message);
    	}
    }
    
    //test
    public static void main(String[] args) {
    	Log myLogger=new Log("number.log");
    	myLogger.log("test message");
    }

}
