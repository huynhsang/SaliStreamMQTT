package CONTROLLERs;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import UTILs.Utility;
import MODELs.PackageData;

public class RecordAudio extends Thread{
	
	public volatile boolean runnable = false;
	private PubSub pubsub;
	private String topic;
	
	public RecordAudio(PubSub pubsub, String topic){
		this.pubsub = pubsub;
		this.topic = topic;
	}
	
	public void run(){
		try {
			final AudioFormat format = Utility.getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		    final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		    line.open(format, 4096);
		    line.start();
		    
		    byte buffer[] = new byte[4096];
	        PackageData data = new PackageData("mic", null);
	        
		    while(runnable){
		    	int count = line.read(buffer, 0, buffer.length);
				  if (count > 0) {
					  data.setDatas(buffer);
					  sendMsg(Utility.constructJSON(data).getBytes(), 0);
				  }
		    }
		    line.close();
		}catch(LineUnavailableException e){
			e.printStackTrace();
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
