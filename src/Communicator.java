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
//     Update #: 227
// 

// Code:
package stock;

import java.io.*;
import java.net.*;
import java.util.*;

class Communicator implements Runnable{
    
    private static final int VOLUMN = 0;
    private static final int PRICE  = 1;

    private ConfigData configData = null;
    private Socket socket = null;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private Map<String, int[]> companyMap; // store <company, [VOLUMN, PRICE]>
    private boolean changed;		   // flag to show whether the 
                                           // company map changed
    // constructor
    public Communicator(ConfigData configData){
	this.configData = configData;
	this.companyMap = new HashMap<String, int[]>();
	setChanged(false);

    }
    // setter
    public void setChanged(boolean changed){
	this.changed = changed;
    }
    // getter
    public String getServerName(){
	return configData.getName();
    }

    public boolean hasCompany(String company){
	return companyMap.containsKey(company);
    }
    
    public Set<String> getCompanySet(){
	return companyMap.keySet();
    }
    
    public boolean isChanged(){
	return changed;
    }
    public int getAveragePrice(String company){
	return companyMap.get(company)[PRICE];
    }
    

    // methods
    @Override
    public void run(){
	connect();

	login();
	try {
	    while(true){
		String line = br.readLine();
		String[] splited = line.split(" ");

		if(splited[0].equals("ERROR"))
		    throw new Exception("serverError!");
		if(splited[0].equals("TICK")){
		    synchronized(companyMap){
			updateAverage(splited);
		    }
		}
	    }
	}
	catch (SocketException se) {
	    // exit the loop
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

    private void updateAverage(String[] tick){
	assert tick[0].equals("TICK") && tick.length == 4;


	String company = tick[1];
	int    volumn  = Integer.parseInt(tick[2]);
	int    price   = Integer.parseInt(tick[3]);
	int[]  pair    = new int[2];
	if(!hasCompany(company)){
	    pair[VOLUMN] = volumn;
	    pair[PRICE] = price;
	    companyMap.put(company, pair);
	}else{
	    pair = companyMap.get(company);
	    assert volumn > 0 && pair[VOLUMN] > 0;
	    if(volumn >= 5){
		pair[VOLUMN] = 5;
		pair[PRICE]  = price;
	    }else if(pair[VOLUMN] + volumn > 5){
		pair[VOLUMN] = 5;
		pair[PRICE]  = (pair[PRICE] * (10 - (pair[VOLUMN] + volumn)) + volumn * price) / 5;		
	    }else{
		pair[PRICE] = (pair[PRICE] * pair[VOLUMN] + price * volumn) / (volumn + pair[VOLUMN]);
	    }
	    companyMap.replace(company, pair);
	}

	setChanged(true);

    }

    
}


// 
// Communicator.java ends here
