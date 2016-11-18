package ADAPTER;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import MODELs.User;

@SuppressWarnings("serial")
public class RendererListCellSaliStream extends DefaultListCellRenderer implements ListCellRenderer<Object>{
	
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		User item = (User) value;
		setText(item.getUserName() + " is live");
		//setIcon(new ImageIcon(Utility.getImageFile(item.getAvatarPath())));
		setIcon(new ImageIcon(getClass().getResource(item.getAvatarPath())));
		if(isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}else{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		setEnabled(true);
		setFont(list.getFont());
		return this;
	}
}
