//                              -*- Mode: Java -*- 
// BotSystem.java --- 
// Filename: BotSystem.java
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
//     Update #: 339
// 

// Code:

package stock;

import java.util.*;

class BotSystem{
    
    class CompanyInfor{
	String maxserver;
	String minserver;
	int maxprice;
	int minprice;

	CompanyInfor(String maxserver, 
		     int maxprice, 
		     String minserver, 
		     int minprice){
	    this.maxserver = maxserver;
	    this.minserver = minserver;
	    this.maxprice  = maxprice;
	    this.minprice  = minprice;
	}
    }

    private static BotSystem system;

    private String path;
    private List<Communicator> communicators;
    private LinkedList<String>  messageQueue;
    private String stock;
    private BotTray tray;
    private boolean running;

    private BotSystem(){
	this.communicators = new ArrayList<Communicator>();
	this.messageQueue = new LinkedList<String>();
	this.running = true;
    }


    // getter
    public static BotSystem getBotSystem(){
	if(BotSystem.system == null){
	    BotSystem.system = new BotSystem();
	}
	return BotSystem.system;
    }
    
    public LinkedList<String> getMessageQueue(){
	return messageQueue;
    }

    public String getStock(){
	return stock;
    }

    // methods
    public void initialize(String path){
	
	Runtime.getRuntime().addShutdownHook(new Thread(){
		@Override
		public void run(){
		    System.out.println("Exiting...");
		    BotSystem.getBotSystem().end();
		    System.out.println("Bye.");
		    
		}
	    });
	this.path = path;
	setCommunicators(path);
	createTray();
	System.out.println("Number of users:" + communicators.size());

    }
    public void work(){
	Communicator communicator;
	while(running){
	    for(int i = 0; i < communicators.size(); i++){
		communicator = communicators.get(i);
		Queue<String[]> tickMsg = communicator.getTickMsg();
		while(!tickMsg.isEmpty()){
		    communicator.updateAverage(tickMsg.poll());
		    updateMsgQueue();
		}
	    }
	    
	}
    }

    private void updateMsgQueue(){
	assert communicators.size() > 1;

	String maxserver = "";
	String minserver = "";
	int maxprice = Integer.MIN_VALUE;
	int minprice = Integer.MAX_VALUE;
	for(int i = 0; i < communicators.size(); i++){
	    Communicator communicator = communicators.get(i);
	    int price = communicator.getAverage();
	    String server = communicator.getServerName(); 
	    if(price > maxprice){
		maxprice = price;
		maxserver = server;
	    }else if(price < minprice){
		minprice = price;
		minserver = server;
	    }
	}

	if(maxprice - minprice > 2){
	    String message = "Opportunity between "
		+ maxserver + " (" 
		+ maxprice + ") and " 
		+ minserver + " (" 
		+ minprice + ")\n";
	    messageQueue.add(message);

	}
    }
    // private void updateDatabase(Communicator communicator){
    // 	Iterator<String> companyItr = communicator.getCompanySet().iterator();
    // 	String company;
    // 	String server;
    // 	int price;
	
    // 	while(companyItr.hasNext()){
    // 	    company = companyItr.next();
    // 	    price   = communicator.getAveragePrice(company);
    // 	    server  = communicator.getServerName();
    // 	    if(!database.containsKey(company)){
    // 		database.put(company, 
    // 			     new CompanyInfor(
    // 					      server,
    // 					      price,
    // 					      server,
    // 					      price));
    // 	    }else{
    // 		CompanyInfor infor = database.get(company);
    // 		// update information while it is the max/min in history.
    // 		// the max and min shouldn't happen in same server
    // 		if(price > infor.maxprice && server != infor.minserver){
    // 		    infor.maxprice = price;
    // 		    infor.maxserver = server;
    // 		}else if(price < infor.minprice && server != infor.maxserver){
    // 		    infor.minprice = price;
    // 		    infor.minserver = server;
    // 		}

    // 		if((server.equals(infor.maxserver)
    // 		    || server.equals(infor.minserver))
    // 		   && infor.maxprice - infor.minprice > 2){
    // 		    synchronized(messageQueue){

    // 			String message = "Opportunity between "
    // 					 + infor.maxserver + " (" 
    // 					 + infor.maxprice + ") and " 
    // 					 + infor.minserver + " (" 
    // 			    + infor.minprice + ")\n";
    // 			// System.out.println(message);
    // 			messageQueue.add(message);
    // 		    }
    // 		}
    // 	    }
    // 	}
	    
    // 	communicator.setChanged(false);
    // }

    public void end(){
    	closeConnection();
	this.running = false;
    	closeTray();

    }

    private void setCommunicators(String path){
	ConfigFileReader config;
	config = new ConfigFileReader(path);
	this.stock = config.getStock();
	communicators = new ArrayList<Communicator>();
	for(int i = 0; i < config.getConfigDataList().size(); i++){
	    communicators.add(new Communicator(config.getConfigDataList().get(i)));
	}

	for(int i = 0; i < communicators.size(); i++){
	    Thread thread = new Thread(communicators.get(i), 
				       ("Communicator " + i));
	    thread.start();
	}
	
    }

    private void createTray(){
	this.tray = new BotTray();
	
	Thread thread = new Thread(tray, "System Tray");
	thread.start();
    }

    private void closeConnection(){
	if(!communicators.isEmpty()){
	    for(int i = 0; i < communicators.size(); i++){
		communicators.get(i).stop();
	    }
	}
    }

    private void closeTray(){
	if(tray != null){
	    tray.stop();
	}
    }
}


// 
// BotSystem.java ends here
