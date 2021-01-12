package com.example.MyApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketServer{ 

	private final static String LOG_FILE="./number.log";
	
	private static final int MAX_CONNECTIONS=5;
	
	private Log2 myLogger=null;
	
	private int socket_connections=0;

	private boolean terminateFlag=false;

	private Socket          socket   = null; 
	private ServerSocket    server   = null; 
	private List<Thread> threadsList  = new ArrayList<Thread>();
	
	private HashSet<Integer> valuesWritten = new HashSet<>(12500000);
	//TODO ideas:
	//use circular buffer? LBQ?
	//store timestamp of when number was received alongside the number itself
	//use Exchange<cache> instead
	private HashSet<Integer> cache10sec=new HashSet<Integer>();
	private HashSet<Integer> cache20sec=new HashSet<Integer>();
	
	private MetricsThread metricsThread=new MetricsThread();
	private MonitorThread monitorThread=new MonitorThread();
	
	private int uniqueTotal=0;

	public SocketServer(int port) {

		monitorThread.start();

		metricsThread.start();

		try {
			server = new ServerSocket(port);
			System.out.println("starting server...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean printFlag=true;
		while (true) {
			try {
				if(socket_connections<=MAX_CONNECTIONS) {
					socket = server.accept();
					socket_connections++;
					System.out.println(socket_connections+" clients connected...");
					NumberCounterThread myThread=new NumberCounterThread(socket,LOG_FILE);
					myThread.start();
					threadsList.add(myThread);
				}else {
					if(printFlag) {
						System.out.println("max connections reached, rejecting all additional clients.");
					}
					printFlag=false;
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	class MetricsThread extends Thread{
		public void run() {
			while(true) {
				//sleep 10 seconds
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					System.out.println(e);
				}

				int unique=0;
				int duplicates=0;
				synchronized(cache10sec) {
					for(Integer each : cache10sec) {
						//TODO: dont repeat comparisons in loop, hash?
						if(cache20sec.contains(each)) {
							duplicates++;
						}else {
							unique++;
						}
					}
					System.out.format("Received %d unique numbers, %d duplicates. Unique total: %d \n",
							unique,duplicates,uniqueTotal);
					cache20sec.clear();
					cache20sec.addAll(cache10sec);
					cache10sec.clear();
				}
			}
		}
	}
	class MonitorThread extends Thread{
		public void run() {
			while(true) {
				if(terminateFlag) {
					for (Thread thread : threadsList) {
						System.out.println("Stopping thread:"+thread.getName());
						thread.interrupt();
						if (myLogger!=null) {
							myLogger.close();
						}
						metricsThread.interrupt();
					}
					System.out.println("Stopping app.");
					System.exit(0);
				}
			}
		}
	}

	class NumberCounterThread extends Thread {
		protected Socket socket;

		public NumberCounterThread(Socket clientSocket, String logFile) {
			this.socket = clientSocket;
			myLogger=new Log2(logFile);
		}

		public void run() {
			InputStream inputStream = null;
			BufferedReader reader = null;
			try {
				inputStream = socket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while (true) {
					if(socket.isClosed()) {
						socket_connections--;
						return;
					}
					line = reader.readLine();
					//System.out.println("got line:"+line);
					if(line!=null && line.equalsIgnoreCase("terminate")) {
						System.out.println("received terminate, stopping app.");
						terminateFlag=true;
						socket.close();
						socket_connections--;
						return;
					}else if(!validate(line)) {
						System.out.println("invalid input:"+line+", terminating socket.");
						socket.close();
						socket_connections--;
						return;
					} else {
						int input=Integer.parseInt(line);
						synchronized(cache10sec) {
							cache10sec.add(input);
						}
						//System.out.println("received input to log:"+line);
						if (recordValue(input) ){
							myLogger.log(line);
							uniqueTotal++;
						}
					}
				}
			} catch (Exception e) {
				myLogger.close();
				return;
			}
		}

		public boolean recordValue(Integer value) {
			synchronized(valuesWritten){
				if (!valuesWritten.contains(value)){
					valuesWritten.add(value);
					return true;
				}
				return false;
			}
		}

		public boolean validate(String inputLine) {
			if (inputLine==null){
				return false;
			}
			Pattern p = Pattern.compile("^[0-9]{9}$");
			Matcher m = p.matcher(inputLine);
			return m.matches();
		}

	}

	public static void main(String args[]){ 
		SocketServer server = new SocketServer(4000); 
	} 
}
