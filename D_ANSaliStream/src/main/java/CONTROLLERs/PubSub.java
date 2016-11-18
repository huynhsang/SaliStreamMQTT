package CONTROLLERs;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


public class PubSub {
	
	private MqttClient mqttClient;
	private MqttConnectOptions connOpts;
	private String broker;
	private String clientId;
	
	public PubSub(String broker, String clientId){
		this.broker = broker;
		this.clientId = clientId;
		initConnect();
	}
	
	private void initConnect(){
		try {
            // Create an Mqtt client
            mqttClient = new MqttClient(broker, clientId);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            // Connect the client
            System.out.println("Connecting to Solace broker: " + broker);
            mqttClient.connect(connOpts);
            
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
	}
	
	public void subscribe(String topic, int qos){
		try {
			mqttClient.subscribe(topic, qos);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void publish(String topic, MqttMessage message) throws MqttPersistenceException, MqttException{
		mqttClient.publish(topic, message);
	}
	
	public void on(MqttCallback callback){
		mqttClient.setCallback(callback);
	}
	
	public boolean unSubscribe(String topic){
		boolean check = false;
		try {
			mqttClient.unsubscribe(topic);
			check = true;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}
	
	public void disconnect(){
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
