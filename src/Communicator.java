//                              -*- Mode: Java -*- 
// Communicator.java --- 
// Filename: Communicator.java
// Description: 
// Author: Zhang Huayan
// ID number: 6511043
// E-mail: zy11043@nottingham.edu.cn / MeowAlienOwO@gmail.com
// Version: 
// 

// Commentary: 
// 
// 

// Change Log:
// Status: 
// Table of Contents: 
// 
//     Update #: 328
// 

// Code:
package stock;

import java.io.*;
import java.net.*;
import java.util.*;

class Communicator implements Runnable{
    private ConfigData configData = null;
    private Queue<String[]> tickMsg = null;
    private Socket socket = null;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private int average = 0;
    private int volumn = 0;
    private boolean changed;


    // constructor
    public Communicator(ConfigData configData){
	this.configData = configData;
	this.tickMsg = new LinkedList<String[]>();
    }

    // getter
    public String getServerName(){
	return configData.getName();
    }

    public int getAverage(){
	return average;
    }
    public int getVolumn(){
	return volumn;
    }
    public Queue<String[]> getTickMsg(){
	return tickMsg;
    }

    // methods
    @Override
    public void run(){
	connect();
	login();
	try {
	    while(true){
		String line = br.readLine();
		if(line != null){ // case of the main thread executed logout but the
		                  // thread is reading -> read null line.
		    
		    String[] splited = line.split(" ");
		    if(splited[0].equals("ERROR"))
			throw new Exception("serverError!");
		    if(splited[0].equals("TICK")
		       && splited[1].equals(BotSystem.getBotSystem().getStock())){
			tickMsg.add(splited);
		    }
		}
	    }
	}catch(SocketException se) {
	    // exit thread
	}catch(IOException ioe){
	    System.out.println("Error " + ioe.getMessage());
	    ioe.printStackTrace();
	    System.exit(4);
	}
	catch(Exception e){
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	    System.exit(4);
	}

    }

    public void stop(){
	try{
	    osw.write("LOGOUT\n");
	    osw.flush();
	    socket.close();
	}catch(SocketException se){
	    // exit
	}catch(IOException ioe){
	    System.out.println("Error " + ioe.getMessage());
	    ioe.printStackTrace();
	}
    }

    private void connect(){
	try{
	    socket = new Socket(configData.getAddress(), configData.getPort());
	    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    osw = new OutputStreamWriter(socket.getOutputStream());
	}catch(IOException ioe){
	    System.out.println("Error: IOException");
	    System.out.println(ioe.getMessage());
	    ioe.printStackTrace();
	    System.exit(2);
	}
    }

    private void login(){
	assert br != null && osw != null;
	try{
	    String loginMsg = "LOGIN 1 " + configData.getUsrname() + " " + configData.getPasswd() + "\n";
	    osw.write(loginMsg);
	    osw.flush();
	    String feedback = br.readLine();
	    if(feedback.split(" ")[0].equals("ERROR"))
		throw new Exception("can\'t log in!");
	    
	}catch(Exception e){
	    System.out.println("Error" + e.getMessage());
	    e.printStackTrace();
	    System.exit(2);
	}
    }

    public void updateAverage(String[] tick){
	assert tick[0].equals("TICK") 
	    && tick[1].equals(BotSystem.getBotSystem().getStock())
	    && tick.length == 4;

	String company = tick[1];
	int    tickVolumn  = Integer.parseInt(tick[2]);
	int    tickPrice   = Integer.parseInt(tick[3]);
	// core algorithm to calculate new average
	if(tickVolumn >= 5){
	    average = tickPrice;
	    volumn = 5;
	}else if(tickVolumn + volumn > 5){
	    average  = (average * (10 - (tickVolumn + volumn)) + tickVolumn * tickPrice) / 5;
	    volumn = 5;
	}else{
	    average = (average * volumn + tickPrice * tickVolumn) / (tickVolumn + volumn);
	    volumn = volumn + tickVolumn;
	}

    }
}


// 
// Communicator.java ends here
