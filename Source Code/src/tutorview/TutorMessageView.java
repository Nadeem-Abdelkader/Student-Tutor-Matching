package tutorview;

import java.awt.BorderLayout;

import javax.swing.*;
import model.EventType;
import mainview.MessageView;
import mainview.MouseClickListener;
import mainview.Observer;
import model.Bid;
import model.Message;
import model.User;

/**
 * This is the View where Tutor messages Student in close bidding
 */
public class TutorMessageView extends JPanel implements MessageView, Observer {

	private Message message;
	private Bid bid;
	private JButton send = new JButton("Send");
	private JButton selectBid = new JButton("Select bid");
	private User user;
	private JTextField chatBox;

	public TutorMessageView(User user, Message message, Bid bid) {
		this.bid = bid;
		this.message = message;
		placeComponents();
	}

//	public TutorMessageView(User user, Bid bid) {
//		this.bid = bid;
//	}

	protected void placeComponents() {
		Message mS = this.message;

		JTextArea log = (mS == null? new JTextArea() : this.getLogArea(mS.getMessageLog()));
//		JTextArea log = this.getLogArea(mS.getMessageLog());

		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		chatBox = this.getChatBox();
		chatArea.add(chatBox);

		JPanel bTs = new JPanel();
		bTs.setLayout(new BoxLayout(bTs, BoxLayout.Y_AXIS));
		bTs.add(send);
		bTs.add(selectBid);

		chatArea.add(bTs, BorderLayout.EAST);

		this.add(log, BorderLayout.CENTER);
		this.add(chatArea, BorderLayout.SOUTH);
	}

	public String getChatContent() {
		return this.chatBox.getText();
	}

	public void setSendMessageListener(MouseClickListener listener) {
		this.send.addMouseListener(listener);
	}

	public void setSelectBidListener(MouseClickListener listener) {
		this.selectBid.addMouseListener(listener);
	}


	@Override
	public void update(EventType e) {

	}
}