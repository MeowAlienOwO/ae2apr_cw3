//                              -*- Mode: Java -*- 
// WatcherBot.java --- 
// Filename: WatcherBot.java
// Description: Entrance of the program
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
//     Update #: 25
// 

// Code:
package stock;

public class WatcherBot{
    public static void main(String[] args) {
	try {
	    if(args.length != 1) throw new Exception("illegal argument!");

	    
	    BotSystem system = BotSystem.getBotSystem();

	    system.initialize(args[0]);
	    
	    system.work();

	}catch(Exception e){
	    System.out.println("Error: " + e.getMessage());
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}


// 
// WatcherBot.java ends here
