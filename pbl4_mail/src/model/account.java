package model;

public class account {
	public String id ;
	public String user_name;
	public String password;
	public String phone;

	public account() {}

	public account(String id, String user_name, String password, String phone) {
		super();
		this.id = id;
		this.user_name = user_name;
		this.password = password;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
