package CONTROLLERs;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import MODELs.PackageData;
import UTILs.Utility;

public class RecordCam extends Thread{
	
	private PubSub pubsub;
	private String topic;
	private JLabel camView = null;
	public volatile boolean runnable = false;
	
	public RecordCam(PubSub pubsub, String topic, JLabel camView){
		this.pubsub = pubsub;
		this.topic = topic; 
		this.camView = camView;
	}
	
	public RecordCam(PubSub pubsub, String topic){
		this.pubsub = pubsub;
		this.topic = topic; 
	}
	
	public void run(){
		Mat webcamMatImage = new Mat();
		BufferedImage tempImage;
		VideoCapture capture = new VideoCapture(0);
		PackageData data = new PackageData("webcam", null);
		
		synchronized(this){	
			if(capture.isOpened()){
				while(runnable){
					capture.read(webcamMatImage);
					if(!webcamMatImage.empty()){
						tempImage = Utility.toBufferedImage(webcamMatImage);
						data.setDatas(Utility.imgToBytes(tempImage));
						sendMsg(Utility.constructJSON(data).getBytes(), 0);
						if(camView != null){
							ImageIcon imageIcon = new ImageIcon(Utility.scaleImage(tempImage, 800, 606), "Captured Video");
							camView.setIcon(imageIcon);
						}
					}else{
						System.out.println("--Frame not captured--Break!");
						break;
					}
				}
			}else{
				System.out.println("Couldn't open capture.");
			}
			capture.release();
		}
	}
	
	public void setTopic(String topic){
		this.topic = topic;
	}
	
	private void sendMsg(byte[] datas, int QoS){
		try {
			pubsub.publish(topic, Utility.getMqttMessage(datas, QoS));
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
