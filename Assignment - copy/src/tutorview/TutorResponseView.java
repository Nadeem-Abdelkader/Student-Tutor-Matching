package tutorview;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import mainview.Display;
import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Utils;
import mainview.View;
import model.Bid;
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.User;

import java.awt.FlowLayout;

/**
 * This is the View where Tutor view the response to a specific match request.
 * If the match request is type open, the Tutor sees all competing bids from other Tutors.
 * If the match request is type close, the Tutor is redirected to TutorMessageView.
 */
class TutorResponseView extends TutorView {
	private Bid bid;

	public TutorResponseView(Display display, User user, Bid bid) {
		super(display, user);
		this.bid = bid;
	}

	@Override
	protected void placeComponents() {
		super.placeComponents();
		if (bid.getType() == Bid.BidType.open) {
			JButton createBid = new JButton("Create Bid");
			JButton buyOut = new JButton("Buy Out Bid");
			
			ArrayList<JComponent> panels = new ArrayList<JComponent> ();
			
			List<BidResponse> responses = bid.getResponse();
			for (BidResponse r : responses) {
				JPanel panel = new JPanel();
				JEditorPane eP = new JEditorPane();
				
				eP.setText(r.toString());
				panel.add(eP);
			}
			JPanel midPanel = new ListPanel(panels);
	        main.add(midPanel);

			JPanel bottomP = new JPanel(new FlowLayout() );
			bottomP.add(createBid);
			bottomP.add(buyOut);
	        main.add(bottomP, BorderLayout.SOUTH);
	        
	        createBid.addMouseListener(new MouseClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (bid.checkEligibility(user)) {
						display.removePanel(main);
						View cBV = new CreateBidView(display, user, bid);
						cBV.display();
					} else {
						Utils.INSUFFICIENT_COMPETENCY.show();
					}
				}});
	        
	        buyOut.addMouseListener(new MouseClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (bid.checkEligibility(user)) {

						Contract.postContract(bid.getInitiatorId(), user.getId(), 
								bid.getSubject().getId(),
								new ContractAddInfo(true, true));
						Bid.closeDownBid(bid.getId());
						Utils.SUCCESS_CONTRACT_CREATION.show();
					} else {
						Utils.INSUFFICIENT_COMPETENCY.show();
					}
				}
	        });
			this.display.setVisible();
		} else if (bid.getType() == Bid.BidType.close) {
			display.removePanel(this.main);
			(new TutorMessageView(display, user, bid)).display();
		}
	}

}
