//                              -*- Mode: Java -*- 
// BotTray.java --- 

// Filename: BotTray.java
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
//     Update #: 81
// 

// Code:

package stock;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
class BotTray implements Runnable{
    private TrayIcon icon;
    private SystemTray tray;
    private volatile boolean running;
    // constructor
    BotTray(){

	this.running = true;	
	try{

	    tray = SystemTray.getSystemTray();
	    int[] pixels = new int[32 * 32];
	    int value = Color.GREEN.getRGB();
	    // System.out.println("RGB = " + value);
	    for(int i = 0; i < pixels.length; i++){
		pixels[i] = value;
	    }
 
	    Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(32, 32, pixels, 0, 32));


	    PopupMenu popup = new PopupMenu("Watcher Bot Menu");

	    MenuItem exitItem = new MenuItem("Quit");
	    exitItem.addActionListener(new ActionListener(){
		    @Override
		    public void actionPerformed(ActionEvent e){
			System.exit(0);
		    }
		});

	    
	    popup.add(exitItem);
	    this.icon = new TrayIcon(image, "Watcher Bot", popup);
	    tray.add(icon);
	}catch(UnsupportedOperationException unoe){
	    System.out.println("Error " + unoe.getMessage());
	    unoe.printStackTrace();
	    System.exit(3);
	}catch(AWTException awte){
	    System.out.println("Error " + awte.getMessage());
	    awte.printStackTrace();
	    System.exit(3);
	}
    }

    @Override
    public void run(){
	int i = 0;
	while(running){
	    LinkedList<String> messageQueue = (LinkedList<String>)BotSystem.getBotSystem().getMessageQueue();
	    if(!messageQueue.isEmpty()){
		String infor = messageQueue.poll();
		System.out.println(infor + "tray infor");
		icon.displayMessage("Chance", infor, TrayIcon.MessageType.INFO);
	    }
	}
    }

    public void stop(){
	this.running = false;

	
    }
}


// 
// BotTray.java ends here





