package CONTROLLERs;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public interface ControlLiveStreamUI {
	public JLabel getLblImageCam();
	public JTextArea getTxtAreaChatbox();
	public void initLiveStreamDevice(String topic, String type);
	public void showWaitDialog();
	public void closeWaitDialog();
	public void closeAll();
	public void closeLiveStream();
}
