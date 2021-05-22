package tutorview;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import mainview.Display;
import mainview.ListPanel;
import mainview.MouseClickListener;
import model.Bid;
import model.User;
import mainview.Observer;
import studentview.StudentAllBids;

/**
 * This is the View for Tutor to see all available (unclosed, unexpired) mathc requests
 */
public class TutorAllBids extends JPanel implements Observer {
	List<Bid> bids;
	User user;
	private JList<Bid> bidList;
	public TutorAllBids(Display display, User user) {
		super(new BorderLayout());
		this.user = user;
		placeComponents();
	}

	protected void placeComponents() {
//		super.placeComponents();
		bids = Bid.getAll();
		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		DefaultListModel<Bid> model = new DefaultListModel<Bid>();
		for (Bid b : bids) {
			String text = b.toString();
			JPanel panel = new JPanel();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			panel.add(tA);
			tA.setEditable(false);
			bidList = new JList<Bid>(model);
			bidList.setCellRenderer(new CellRenderer());
			
//			this.setSwitchPanelListener(main, tA, new TutorResponseView(display, user, b));
			panels.add(panel);
		}
		
		JPanel midPanel = new ListPanel(panels);
//        main.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
//		main.add(scrollp);
//		this.display.setVisible();
		
	}

	public void setListListener(MouseClickListener listener) {
		this.bidList.addMouseListener(listener);
	}

	private class CellRenderer extends JPanel implements ListCellRenderer<Bid> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Bid> list, Bid value, int index,
													  boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			String text = value.toString();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			tA.setEditable(false);
			this.add(tA);
			return this;
		}
	}

	public Bid getSelectedBid() {
		return this.bidList.getSelectedValue();
	}

	@Override
	public void update() {
		this.bids = Bid.getAll();
		this.placeComponents();
	}
}
