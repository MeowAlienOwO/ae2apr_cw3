//                              -*- Mode: Java -*- 
// ConfigFileReader.java --- 
// Filename: ConfigFileReader.java
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

import java.io.*;
import java.util.*;

class ConfigFileReader{
    
    private List<String> configList;
    private List<ConfigData> dataList;
    private String stock;
    // constructor
    public ConfigFileReader(String filepath){
	try{
	    this.configList = new ArrayList<String>();
	    this.dataList   = new ArrayList<ConfigData>();
	    setConfigList(filepath);
	    setDataList(configList);
	}catch(FileNotFoundException fnfe){
	    System.out.println("Error: file \'" + fnfe.getMessage() + "\' can\'t found.");
	    fnfe.printStackTrace();
	    System.exit(1);
	}catch(Exception e){
	    System.out.println("Error: " + e.getMessage());
	    e.printStackTrace();
	    System.exit(1);
	}
    }


    // setter
    private void setConfigList(String filepath) throws FileNotFoundException, IOException{
	File file = new File(filepath);

	BufferedReader br = new BufferedReader(new FileReader(file));
	    
	String line;
	while((line = br.readLine()) != null){
	    configList.add(line);
	}
    }

    private void setDataList(List<String> configList) throws Exception{
	
	String line;
	
	for(int i = 0; i < configList.size(); i++){
	    line = configList.get(i);
	    if(line.equals("")) continue;
	    if(line.split("=").length != 2)
		throw new Exception("Illegal Configuration File!");
	    String key = line.split("=")[0];
	    String value = line.split("=")[1];
	    if(key.equals("stock")){
		if(stock == null){
		    stock = value;
		}else{
		    throw new Exception("Illegal Configuration File!");
		}
	    }else if(key.equals("server")){
		String[] values = value.split(":");
		if(values.length != 5)
		    throw new Exception("Illegal Configuration File!");
		dataList.add(new ConfigData(values));
	    }

	}

    }
    // getter
    public List<String> getConfigString(){
	return configList;
    }
    
    public List<ConfigData> getConfigDataList(){
	return dataList;
    }

    public String getStock(){
	return stock;
    }
}

// 
// ConfigFileReader.java ends here
