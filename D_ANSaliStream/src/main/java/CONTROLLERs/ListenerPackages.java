package CONTROLLERs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import ADAPTER.RendererListCellSaliStream;
import CONSTANTs.ConfigConstant;
import MODELs.PackageData;
import MODELs.User;
import UTILs.Utility;

public class ListenerPackages implements MqttCallback{

	
	private PubSub pubsub;
	
	private ControlLiveStreamUI controlCallStream = null, controlSaliStream = null;
	private ControlMainUI controlMainUI;
	private JList<User> listNotifications;
	private DefaultListModel<User> dm = new DefaultListModel<User>();
	private ArrayList<User> userList, noticeList = new ArrayList<User>();
	private ArrayList<StringBuilder> txtAreaList;
	private StringBuilder txtAreaSaliStream = new StringBuilder();;
	private User owner;
	
	public ListenerPackages(PubSub pubsub, ArrayList<User> userList, User owner, ControlMainUI controlMainUI){
		this.pubsub = pubsub;
		this.userList = userList;
		this.controlMainUI = controlMainUI;
		this.owner = owner;
		initControlUI();
		initTxtAreaList();
		PlaySound.soundConfig();
	}
	
	private void initControlUI(){
		listNotifications = controlMainUI.getListNotifications();
	}
	
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		System.out.println("Connection to Solace broker lost!" + arg0.getMessage());
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		byte [] bytes = message.getPayload();
    	String msg = new String(bytes);
    	JSONObject obj = new JSONObject(msg);
    	JSONObject content = obj.getJSONObject("content");
    	bytes = org.apache.commons.codec.binary.Base64.decodeBase64(content.getString("datas"));
    	String type = content.getString("type");
    	switch(type){
    		case "webcam":
    			showCam(bytes, topic);
    			break;
    		case "mic":
    			new PlaySound(bytes).start();
    			break;
    		case "chat":
    			handleMsg(new PackageData(type, content.getString("msg"), content.getString("date"), content.getInt("to"), content.getString("owner")), topic);
    			break;
    		case "notice":
    			
    			handleNotifications(new PackageData(type, content.getString("msg"), content.getString("date"), content.getInt("to"), content.getString("owner")), topic);
    			break;
    		case "off":
    			
    			break;
    	}
	}
	
	private void handleMsg(PackageData packageData, String topic){
		int id = Integer.parseInt(packageData.getOwner());
		if(topic.contains(ConfigConstant.prefixLiveStream)){
			txtAreaSaliStream.append(packageData.getMsg());
			controlSaliStream.getTxtAreaChatbox().setText(txtAreaSaliStream.toString());
			
		}else{
			int guestId = packageData.getTo();
			if(guestId == owner.getId() || guestId == 0){
				addMsgToChatBox(packageData.getMsg(), id);
				if(guestId != 0){
					controlCallStream.getTxtAreaChatbox().setText(getMsgFromChatBox(id).toString());
				}
				controlMainUI.getTxtAreaChatbox().setText(getMsgFromChatBox(id).toString());
				controlMainUI.changeClient(getUser(id));
			}else{
				addMsgToChatBox(packageData.getMsg(), guestId);
				controlCallStream.getTxtAreaChatbox().setText(getMsgFromChatBox(guestId).toString());
				controlMainUI.getTxtAreaChatbox().setText(getMsgFromChatBox(guestId).toString());
				controlMainUI.changeClient(getUser(guestId));
			}	
		}
		
	}
	
	private void handleNotifications(PackageData packageData, String topic) {
		// TODO Auto-generated method stub
		int i = Integer.parseInt(packageData.getOwner());
		if(topic.equals(ConfigConstant.liveStreamNotice)){
			if(packageData.getMsg().equals("on")){
				noticeList.add(userList.get(i-1));
			}else{
				for(User user : noticeList){
					if(user.getId() == i){
						noticeList.remove(user);
						break;
					}
				}
			}
			resetNoticeList();
		}else{
			if(packageData.getMsg().equals("request call")){
				controlMainUI.showDialogCall(getUser(i));
			}else if(packageData.getMsg().equals("response: accept")){
				controlCallStream.closeWaitDialog();
				final String str = ConfigConstant.prefixTopic +"call" + i;
				
				Runnable runnable = new Runnable(){
					@Override
					public void run() {
						pubsub.subscribe(str, 0);
					}
				};
				new Thread(runnable).start();
				controlCallStream.initLiveStreamDevice(ConfigConstant.prefixTopic +"call" + owner.getId(), "call");
			}else if(packageData.getMsg().equals("response: decline")){
				controlCallStream.closeAll();
				setControlCallStream(null);
			}
		}
	}

	private void showCam(byte[] imageInByte, String topic){
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage tempImage = null;
		try {
			tempImage = ImageIO.read(in);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(topic.contains(ConfigConstant.prefixLiveStream)){
			controlSaliStream.getLblImageCam().setIcon(new ImageIcon(Utility.scaleImage(tempImage, 800, 606)));
		}else{
			controlCallStream.getLblImageCam().setIcon(new ImageIcon(Utility.scaleImage(tempImage, 800, 606)));
		}
	}
	
	private void resetNoticeList(){
		dm.clear();
		for(User user : noticeList){
			dm.addElement(user);
		};
		listNotifications.setCellRenderer(new RendererListCellSaliStream());
		listNotifications.setModel(dm);
	}
	
	private void initTxtAreaList(){
		txtAreaList = new ArrayList<>();
		int count = userList.size();
		while(count>0){
			txtAreaList.add(new StringBuilder());
			count--;
		}
		
	}
	
	private User getUser(int id){
		for(User user : userList){
			if(user.getId() == id) return user;
		}
		return null;
	}

	public void setControlCallStream(ControlLiveStreamUI controlCallStream) {
		this.controlCallStream = controlCallStream;
	}

	public void setControlSaliStream(ControlLiveStreamUI controlSaliStream) {
		this.controlSaliStream = controlSaliStream;
	}
	
	public void addMsgToChatBox(String msg, int id){
		txtAreaList.get(id-1).append(msg);
	}
	
	public StringBuilder getMsgFromChatBox(int id){
		return txtAreaList.get(id-1);
	}
	
	public void initTxtAreaSaliStream(){
		txtAreaSaliStream.setLength(0);
	}
	
	public ControlMainUI getControlMainUI(){
		return this.controlMainUI;
	}
	
}
