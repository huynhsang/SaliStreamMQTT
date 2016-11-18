package CONTROLLERs;

import java.util.ArrayList;

import Database.JDOMXMLReader;
import MODELs.User;

public class VerifyLogin {

	ArrayList<User> users;
	
	public VerifyLogin(){
		initListUser();
	}
	
	public User checkUser(String email, String password){
		for(User user : users){
			if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
				return user;
			}
		}
		return null;
	}
	
	private void initListUser(){
		JDOMXMLReader jdomxmlReader = new JDOMXMLReader();
		users = jdomxmlReader.getUsers();
	}
	
	public ArrayList<User> getUsers(){
		return users;
	}
}
