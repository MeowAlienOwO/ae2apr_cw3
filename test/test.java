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
//     Update #: 99
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

    @Before
    public void setUp(){
	String file = "config.txt";
	ConfigFileReader cfr = new ConfigFileReader(file);
	
    }

    @Test
    @Ignore
    public void testFileReaderCorrect() {
	// String file = "/media/workingArea/CS_Y3sem1/ae2apr_cw3/config.txt";
	
	List<ConfigData> dataList = cfr.getConfigDataList();
	assertEquals("Stock fails: " + cfr.getStock(), cfr.getStock(), "UNNC");
	assertEquals("length not matches: " + cfr.getConfigDataList().size(),
		     cfr.getConfigDataList().size(), 3);
	assertEquals("localhost failure! ",
		     cfr.getConfigDataList().get(0).getAddress(),
		     "localhost");

	assertEquals("port at index 0 failure!",
		     cfr.getConfigDataList().get(0).getPort(),
		     12345);
	
    }

    @Test
    public void test_Connection() {
	
    }
}

class SimpleServer implements Runnable{
    ServerSocket ss;
    Socket socket;
    int choice;
    // company, volumn, price
    String[][][] testData = {
	{"AAA", "1", "3"},
	{"BBB", "1", "3"}

    };
    // constructor
    SimpleServer(int port, int choice){
	ss = new ServerSocket(port);
	this.choice = choice;

    }

    @Override
    public void run(){
	socket = ss.accept();
	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());

	if(br.readLine().split(" ")[0].equals("LOGIN")){
	    osw.write("OK\n");
	    osw.flush();
	}

	for(int i = 0; i < testData[choice].length; i++){
	    osw.write("TICK " + testData[choice][i][0] + " " + testData[choice][i][1] + " " + testData[choice][i][2] + "\n");
	}

	br.readLine().equals("LOGOUT");
	
	
	
    }
    
    
}
// 
// test.java ends here
