package MODELs;

public class PackageData {
	private String type;
	private String msg;
	private String date;
	private int to;
	private byte [] datas;
	private String owner;
	
	public PackageData(String type, byte[] datas){
		this.type = type;
		this.datas= datas;
		this.msg = "";
		this.date = "";
		this.owner = "";
	}
	
	public PackageData(String type, String msg, String date, int to, String owner){
		this.type = type;
		this.msg = msg;
		this.date = date;
		this.to = to;
		this.datas = "".getBytes();
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
}
