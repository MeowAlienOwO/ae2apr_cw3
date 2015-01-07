//                              -*- Mode: Java -*- 
// test.java --- 
// Filename: test.java
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
//     Update #: 146
// 

// Code:
package stock;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import java.io.*;
import java.net.*;
public class test extends TestCase{

    // @Test
    // @Ignore
    // public void testFileReaderCorrect() {
    // 	// String file = "/media/workingArea/CS_Y3sem1/ae2apr_cw3/config.txt";
    // 	String file = "config.txt";
    // 	ConfigFileReader cfr = new ConfigFileReader(file);
    // 	List<ConfigData> dataList = cfr.getConfigDataList();
    // 	assertEquals("Stock fails: " + cfr.getStock(), cfr.getStock(), "UNNC");
    // 	assertEquals("length not matches: " + cfr.getConfigDataList().size(),
    // 		     cfr.getConfigDataList().size(), 3);
    // 	assertEquals("localhost failure! ",
    // 		     cfr.getConfigDataList().get(0).getAddress(),
    // 		     "localhost");

    // 	assertEquals("port at index 0 failure!",
    // 		     cfr.getConfigDataList().get(0).getPort(),
    // 		     12345);
	
    // }

    @Test
    public void test_algorithm() {
	String file = "config.txt";
	ConfigFileReader cfr = new ConfigFileReader(file);
	Communicator communicator = new Communicator(cfr.getConfigDataList().get(0));
	assertEquals("test mathematics", (5*2+3*7)/5, 6);
	String[][] testData ={
	     // test volumn + tick volumn< 5
	    {"TICK", "UNNC", "1", "3"}, // v = 1; p = 3
	    {"TICK", "UNNC", "1", "5"}, // v = 2; p = 4
	    {"TICK", "UNNC", "2", "6"}, // v = 4; p = 5
	    {"TICK", "UNNC", "3", "7"}, // v = 5; p = 6
	    {"TICK", "UNNC", "5", "1"}  // v = 5; p = 1
	};
	int[][] expect ={
	    {1, 3},
	    {2, 4},
	    {4, 5},
	    {5, 6},
	    {5, 1}
	};
	for(int i = 0; i < 5; i++){
	    communicator.updateAverage(testData[i]);
	    assertEquals(("test " + i + " volumn"),
			 communicator.getVolumn(),
			 expect[i][0]);
	    assertEquals(("test " + i + " average"),
			 communicator.getAverage(),
			 expect[i][1]);
	}
    }
    
}

class SimpleServer implements Runnable{
    ServerSocket ss;
    Socket socket;
    int choice;
    // volumn, price
    String[][][] testData = {
	// test average
	{{ "1", "3"},
	 { "1", "3"}}
	

    };
    // constructor
    SimpleServer(int port, int choice){
	try {

	    ss = new ServerSocket(port);
	    this.choice = choice;	    
	}
	catch (Throwable e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}


    }

    @Override
    public void run(){
	try {
	    socket = ss.accept();	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());

	    if(br.readLine().split(" ")[0].equals("LOGIN")){
		osw.write("OK\n");
		osw.flush();
	    }
	    for(int i = 0; i < testData[choice].length; i++){
		osw.write("TICK " + "UNNC" + " " + testData[choice][i][0] + " " + testData[choice][i][1] + "\n");
	    }

	    br.readLine().equals("LOGOUT");
	
	
	}
	catch (Throwable e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}


	

	
	
    }
    
    
}
// 
// test.java ends here
