package com.example.xmlpharse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class myfile {

	static Scanner reader=new Scanner(System.in);
	static Scanner reader1=new Scanner(System.in);
	public static void main(String[] args){
	    Scanner scanner2;
		try {
			scanner2 = new Scanner(new FileReader("version.txt"));
			TreeSet<String> Sortedversions=new TreeSet<String>();
			ArrayList<String> arr=new ArrayList<String>();
			 String str;
			 String Reached="000000000";
		     try{
		    	   while ((str = scanner2.nextLine()) != null)
			        { 
			       // String[] line= str.split(",");
			      //  if(line[4].equals("stable-channel"))
			      //  	FileWriterOn(line[2]);
			   // 	System.out.println(line[2] );
			        	Sortedversions.add(str);
			         //	System.out.println(str);
			        }
		     }catch(Exception ex){}
		        Iterator<String> itr=Sortedversions.iterator();
		        String old="-1";
		        while (itr.hasNext()){
		        	String t=itr.next();
		        	arr.add(t);
		       // 	System.out.println(t);
		        }
	     		System.out.println("------------------");
		        for(int i=arr.size()-1;i>=0;i--){
		          	 String s=arr.get(i);
			            String line= s.substring(0,2);
			           	if(!line.equals(old)){
			           		System.out.println(s);
			           		old=line;
			           	}
		        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
        
		System.out.println("1- to write\n 2- to read");
		int op=reader.nextInt();
		switch(op){
		case 1: // write
			System.out.println("enter to save to file");
			String text= reader1.nextLine();
			FileWriterOn(text);
			break;
			
		case 2: // read
			FileReaderOn();
			break;
		}
		
		//
		
		
	}
	static void FileReaderOn(){
		try{
		FileReader fin=new FileReader( "test.txt");
		int c;
		while((c=fin.read( ))!=-1 ){
			System.out.print((char) c);
		}
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	static void FileWriterOn(String str){
		try
		{
		FileWriter fo=new FileWriter("Recent_stable_dev_version.txt",true);
		fo.write(str +"\n");
		fo.close();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
