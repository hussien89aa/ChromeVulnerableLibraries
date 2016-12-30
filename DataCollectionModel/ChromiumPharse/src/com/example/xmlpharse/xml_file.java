package com.example.xmlpharse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
public class xml_file {

	 public static void main(String argv[]) {
		  //open connection to database
		   
		   Connection c = null;
		    Statement stmt = null;
		 try {
			 
			      Class.forName("org.sqlite.JDBC");
			      c = DriverManager.getConnection("jdbc:sqlite:VDataset.db");
			      c.setAutoCommit(false);
			      System.out.println("Opened database successfully");
			    
			      stmt = c.createStatement();
			      //remove old data
			      String sqld = "delete from summary;delete from VulnerabilitiesDetails;"; 
		      stmt.executeUpdate(sqld);
			      
			    // list of version to check every version in one line   
			  Scanner reader=new Scanner(System.in);
			  reader = new Scanner(new FileReader("all.txt"));
			 String VersionFile;
			 
		        while (( reader.hasNext()) )
		        {// start read 
		        	VersionFile = reader.nextLine();

		    //	String reading the XML files hosted in this diretcion
			File fXmlFile = new File(VersionFile+ ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		//  System.out.println("\n-----------------\nTest Summary For Vulnerabilities in Chromium Version:"+ VersionFile +"\n-----------------");
			 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         // System.out.println("Vulnerabilities in Chromium Version:"+ VersionFile  );
			NodeList nList = doc.getElementsByTagName("dependency");
			 
            int VulnerableDependencies=0;
            int VulnerabilitiesFound=0;
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				Node nNode = nList.item(temp);
				
		
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					
                 try{
                	 NodeList nvulnerabilities =(NodeList)  eElement.getElementsByTagName("vulnerabilities") ;
                  // NodeList nListevidenceCollected =(NodeList)  eElementnvulnerabilities.getElementsByTagName("vulnerabilities") ;
                	 VulnerableDependencies=VulnerableDependencies+ nvulnerabilities.getLength();
     				
                	 //only print vulnerable arr
                  if(nvulnerabilities.getLength()>0)
                			 {
                				//System.out.println("\nThird Party :" + nNode.getNodeName());
        					//System.out.println("Staff id : " + eElement.getAttribute("id"));
        	           //   System.out.print("\n\nThird Party : " + eElement.getElementsByTagName("fileName").item(0).getTextContent());
        	            // System.out.print("\nDescription : " + eElement.getElementsByTagName("description").item(0).getTextContent());
                   		
              
                	 //System.out.println("nListevidenceCollected:"+nvulnerabilities.getLength());
                  //vulnerability files
                  Node nNodenvulnerabilities = nvulnerabilities.item(0);
              	if (nNodenvulnerabilities.getNodeType() == Node.ELEMENT_NODE) {
              		Element eElementnvulnerabilities = (Element) nNodenvulnerabilities;
                 NodeList nListvulnerability =(NodeList) eElementnvulnerabilities.getElementsByTagName("vulnerability");
                 VulnerabilitiesFound=VulnerabilitiesFound+nListvulnerability.getLength();
                 //// System.out.println("Vulnerabilities Found:"+nListvulnerability.getLength());
                 for (int index = 0; index < nListvulnerability.getLength(); index++) {
                		Node nNodeListvulnerability = nListvulnerability.item(index);
        				
        				if (nNodeListvulnerability.getNodeType() == Node.ELEMENT_NODE) {

        					Element eElementListvulnerability = (Element) nNodeListvulnerability;
        				 	 // System.out.println("\tvulnerability name : " + eElementListvulnerability.getElementsByTagName("name").item(0).getTextContent());
        					//   System.out.println("\tCPE Confidence : " + eElementListvulnerability.getElementsByTagName("cvssAccessComplexity").item(0).getTextContent());
        					 //	  System.out.println("\tcvssScore : " + eElementListvulnerability.getElementsByTagName("cvssScore").item(0).getTextContent());
        				 	  //System.out.println("\tseverity : " + eElementListvulnerability.getElementsByTagName("severity").item(0).getTextContent());
        				 	 //System.out.println("\tdescription : " + eElementListvulnerability.getElementsByTagName("description").item(0).getTextContent());
                        		
        				 	//VulnerabilitiesFound++;
        				   	  String sql2 = "INSERT INTO VulnerabilitiesDetails (version,ThirdPartyName,ThirdPartyPath,ThirdPartyDesc,IDName,Severity,CVSSScore) " +
        	       	                   "VALUES ('"+VersionFile+"','"+eElement.getElementsByTagName("fileName").item(0).getTextContent()+"','"
        				   			  +eElement.getElementsByTagName("filePath").item(0).getTextContent()
        				   			  +"','"+eElementListvulnerability.getElementsByTagName("description").item(0).getTextContent()
        				   			+"','"+eElementListvulnerability.getElementsByTagName("name").item(0).getTextContent()
        				   			+"','"+eElementListvulnerability.getElementsByTagName("severity").item(0).getTextContent()
        				   			 +"',"+eElementListvulnerability.getElementsByTagName("cvssScore").item(0).getTextContent()+");"; 
        	       	      stmt.executeUpdate(sql2);	  
        					  
        				}
                	 
                 }
              	}
                			 }
              	}catch(Exception ex){}
 
			
                  }
				
		 
                }
				
			
				 
			// System.out.println("\n-----------------\nTest Summary\n-----------------");
		   System.out.print("\n\tFinal results for Chromium Version:"+ VersionFile  );	
			 System.out.print("\tVulnerable Dependencies: "+VulnerableDependencies);
	          System.out.print("\tDependencies Scanned: "+nList.getLength());
	          System.out.print("\tVulnerabilities Found:"+ VulnerabilitiesFound);
	          String sql = "INSERT INTO summary (version,VulnerableDependencies,DependenciesScanned,VulnerabilitiesFound) " +
	                   "VALUES ('"+VersionFile+"',"+VulnerableDependencies+","+ nList.getLength()+","+VulnerabilitiesFound +");"; 
	      stmt.executeUpdate(sql);
	      
		    	}// start read
		        
		     
		   } catch (Exception e) {

				e.printStackTrace();
			    }
		 try{
			 //end the connection to the database
			   stmt.close();
			      c.commit();
			      c.close();
		}catch(Exception e) {}
		 
		   System.out.print("\n\n-----------Done--------------");
		   
		   
		  }
}
