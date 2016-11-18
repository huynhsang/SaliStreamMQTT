package CONTROLLERs;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;

import MODELs.User;

public interface ControlMainUI {
	public JLabel getBtnLiveStream();
	public JLabel getBtnCallClient();
	public JList<User> getListNotifications();
	public JTextArea getTxtAreaChatbox();
	public void showDialogCall(User client);
	public void changeClient(User guest);
	
}
