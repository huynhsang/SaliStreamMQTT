package UTILs;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;

import CONTROLLERs.PubSub;
import MODELs.PackageData;

public class Utility {
	
	public static final int IMAGE_UNKNOWN = -1;
	public static final int IMAGE_JPEG = 0;
	public static final int IMAGE_PNG = 1;
	public static final int IMAGE_GIF = 2;
	
	/**
     * Method to construct JSON
     * 
     * @param PackageData
     * @param topic
     * @return JSONString
     */
	public static String constructJSON(PackageData object){
		JSONObject result = new JSONObject();
		JSONObject obj = new JSONObject();
		
		try{
			obj.put("type", object.getType());
			obj.put("msg", object.getMsg());
			obj.put("date", object.getDate());
			obj.put("to", object.getTo());
			String encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(object.getDatas());
			obj.put("datas", encodedString);
			obj.put("owner", object.getOwner());
			
			//result.put("topic", topic);
			result.put("content", obj);	
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		
		return result.toString();
	}
	
	/**
     * Method to convert Image to Bytes
     * 
     * @param BufferedImage
     * @return byte[]
     */
	public static byte[] imgToBytes(BufferedImage img){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write( img, "jpg", baos );
			baos.flush();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return baos.toByteArray();
		
	}
	
	/**
     * Method to convert Mat to Image
     * 
     * @param Matrix
     * @return Image
     */
	public static BufferedImage toBufferedImage(Mat matrix){
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( matrix.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = matrix.channels()*matrix.cols()*matrix.rows();
		byte [] buffer = new byte[bufferSize];
		matrix.get(0,0,buffer); // get all the pixels
		BufferedImage image = new BufferedImage(matrix.cols(),matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);  

		return image;
	}
	
	
	/**
     *@method get image from resources
     *@param filePath
     */
	public static Image getImageFile(String filePath){
		File pathToFile = new File(filePath);
		Image image = null;
		try {
			image = ImageIO.read(pathToFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	
	/**
	 *@method scale image
	 *@param image, width, height
	 */
	public static Image scaleImage(Image img, int w, int h){
		 
		BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.drawImage(img, 0, 0, w, h, null);
		graphics2D.dispose();
		
		return scaledImage;
	 }
	
	
	/**
	 *@method set format Audio
	 *
	 */
	public static AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
	 
	/**
	 * @method getMqttMessage
	 * @param bytes, qos
	 */
	public static MqttMessage getMqttMessage(byte[] datas, int Qos){
		MqttMessage message = new MqttMessage(datas);
        message.setQos(Qos);
        return message;
	}
	
	
	public static void sendMsg(PubSub pubsub, byte[] datas, String topic, int QoS){
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
