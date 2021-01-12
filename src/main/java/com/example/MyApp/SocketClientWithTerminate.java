package com.example.MyApp;

import java.util.Random;

public class SocketClientWithTerminate extends SocketClient{

	public SocketClientWithTerminate(String address, int port)	{
		super(address,port);
	}

	public static void main(String args[]) {
		SocketClientWithTerminate client = new SocketClientWithTerminate("127.0.0.1", 4000);
		client.test(5);
	}
	
	@Override
	public void test(int n) {
	    Random random = new Random();
        for(int i=0; i<n; i++){
        	String randomNum = String.format("%09d", random.nextInt(1000000000));
        	System.out.println(randomNum);
        	writeToSocket(randomNum);
        }
        System.out.println("writing terminate");
        writeToSocket("terminate");
        System.out.println("closing socket...");
        close();
	} 
} 
