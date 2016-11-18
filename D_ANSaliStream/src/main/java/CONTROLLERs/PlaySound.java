package CONTROLLERs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import UTILs.Utility;

public class PlaySound extends Thread{
	
	private byte[] sound;
	private static SourceDataLine line;
	private static int bufferSize = 4096;
	private static AudioFormat format;
	byte buffer[];
	
	public static void soundConfig(){
		format = Utility.getAudioFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	      try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize);
		    line.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeSound(){
		line.stop();
		line.close();
	}
	
	public PlaySound(byte[] sound){
		this.sound = sound;
		buffer = new byte[bufferSize];
	}

	@Override
	public void run() {
		synchronized(this){
			InputStream input = new ByteArrayInputStream(sound);
			final AudioInputStream ais = new AudioInputStream(input, format, 
				    sound.length / format.getFrameSize());
			try {
				  ais.read(buffer, 0, bufferSize);
				  line.write(buffer, 0, bufferSize);
				  ais.close();
				
			  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
		}
	}
	
	
}
