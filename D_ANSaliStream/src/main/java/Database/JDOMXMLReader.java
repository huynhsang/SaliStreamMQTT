package Database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import MODELs.User;

public class JDOMXMLReader {
	final static String fileName = "/db/db_users.xml";
	
	public ArrayList<User> getUsers(){
		org.jdom2.Document jdomDoc;
		ArrayList<User> userList = new ArrayList<User>();
        try {
        	jdomDoc = useSAXParser(fileName);
        	Element root = jdomDoc.getRootElement();
        	List<Element> userListElements = root.getChildren("User");
        	for(Element item : userListElements){
        		User user = new User();
        		user.setId(Integer.parseInt(item.getAttributeValue("id")));
        		user.setEmail(item.getChildText("email"));
        		user.setPassword(item.getChildText("password"));
        		user.setUserName(item.getChildText("userName"));
        		if(item.getChildText("status").equals("true")){
        			user.setStatus(true);
        		}else{
        			user.setStatus(false);
        		}
        		user.setAvatarPath(item.getChildText("avatarPath"));
        		userList.add(user);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
        return userList;
	}
	
	/*//Get JDOM document from DOM Parser
    private org.jdom2.Document useDOMParser(String fileName)
            throws ParserConfigurationException, SAXException, IOException {
        //creating DOM Document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        ClassLoader classLoader = getClass().getClassLoader();
        Document doc = dBuilder.parse(new File(classLoader.getResource(fileName).getFile()));
        DOMBuilder domBuilder = new DOMBuilder();
        return domBuilder.build(doc);
 
    }*/
    
  //Get JDOM document from SAX Parser
    private org.jdom2.Document useSAXParser(String fileName) throws JDOMException,
            IOException {
    	
    	String[] path = System.getProperty("user.dir").split("D_ANSaliStream");
    	String filePath = path[0]+fileName;
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(new File(filePath));
    }
    
    /*private String getFile(String fileName) {

    	StringBuilder result = new StringBuilder("");

    	//Get file from resources folder
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(fileName).getFile());

    	try (java.util.Scanner scanner = new java.util.Scanner(file)) {

    		while (scanner.hasNextLine()) {
    			String line = scanner.nextLine();
    			result.append(line).append("\n");
    		}

    		scanner.close();

    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	return result.toString();

      }*/
}
