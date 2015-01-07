//                              -*- Mode: Java -*- 
// DataBase.java --- 
// Filename: DataBase.java
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
//     Update #: 19
// 

// Code:

package stock;

interface DataBase{

    public void initialize(String[] head);

    public void add(String[] row);

    public void delete(String[] columns, String[] keys);

    public void alter(String column, String row, String value);

    public List<List<String>> select(String[] columns, String[] keys);

}

// 
// DataBase.java ends here
