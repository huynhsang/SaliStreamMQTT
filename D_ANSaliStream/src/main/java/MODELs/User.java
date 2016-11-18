package MODELs;

public class User {
	
	private int id;
	private String email;
	private String password;
	private String userName;
	private boolean status;
	private String avatarPath;
	
	public User(){};
	
	public User(int id, String email, String password, String userName, boolean status, String path){
		this.id = id;
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.status = status;
		this.avatarPath = path;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	
	
}
