//                              -*- Mode: Java -*- 
// ConfigData.java --- 
// Filename: ConfigData.java
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
//     Update #: 15
// 

// Code:

package stock;

class ConfigData{
    private String address;
    private String name;
    private String usrname;
    private String passwd;
    private int port;
    // constructor

    ConfigData(String address, String port, String name, String usrname, String passwd) throws NumberFormatException{
	this.address = address;
	this.port    = Integer.parseInt(port);
	this.name    = name;
	this.usrname = usrname;
	this.passwd  = passwd;
    }
    
    ConfigData(String[] values){

	assert values.length == 5;
	
	this.address = values[0];
	this.port    = Integer.parseInt(values[1]);
	this.name    = values[2];
	this.usrname = values[3];
	this.passwd  = values[4];
    }

    // getter
    public String getAddress(){
	return address;
    }
    
    public int getPort(){
	return port;
    }

    public String getName(){
	return name;
    }

    public String getUsrname(){
	return usrname;
    }

    public String getPasswd(){
	return passwd;
    }


}

// 
// ConfigData.java ends here
