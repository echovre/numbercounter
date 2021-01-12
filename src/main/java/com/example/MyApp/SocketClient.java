package com.example.MyApp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

public class SocketClient {

	private Socket socket            = null; 
	private BufferedWriter out     = null; 

	public SocketClient(String address, int port){
		try{ 
			socket = new Socket(address, port); 
			System.out.println("Connected"); 

			out    = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
		} catch(Exception e) { 
			System.out.println(e); 
		}
	}

	public boolean writeToSocket(String line) {
		try{ 
			out.write(line+"\r\n"); 
		} catch(IOException e){   
			System.out.println("Socket closed, returning false.");
			return false;
		} 
		return true;
	}
	
	public void close() {
		try{ 
			out.close(); 
			socket.close(); 
		} catch(Exception e) { 
			System.out.println(e); 
		}
	}

	public static void main(String args[]) {
		
		int numThreads=3;
		
		for(int i=0; i<numThreads; i++) {
			Thread t = new Thread() {
				public void run() {
					SocketClient client = new SocketClient("127.0.0.1", 4000);
					int numbers=2000000;
					for (int i=0;i<100;i++) {
						client.test(numbers);
						try {
							Thread.sleep(3000);
							System.out.println("sleeping 3 sec");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			        client.close();
				}
			};
			t.start();
		}
	}
	
	public void test(int n) {
	    Random random = new Random();
    	System.out.println("sending "+n+" numbers...");
        for(int i=0; i<n; i++){
        	String randomNum = String.format("%09d", random.nextInt(999999999));
        	//System.out.println(randomNum);
        	if(!writeToSocket(randomNum)) {
        		System.out.println("Socket closed, exiting.");
        		System.exit(0);
        	}
        }
        System.out.println("closing socket...");
	} 
} 
