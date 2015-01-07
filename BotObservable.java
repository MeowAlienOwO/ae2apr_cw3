//                              -*- Mode: Java -*- 
// BotObservable.java --- 
// Filename: BotObservable.java
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
//     Update #: 2
// 

// Code:
package stock;
interface BotObservable{

    public void attach(BotObserver o);
    public void detach(BotObserver o);
    public void notifyObservers();

}


// 
// BotObservable.java ends here
