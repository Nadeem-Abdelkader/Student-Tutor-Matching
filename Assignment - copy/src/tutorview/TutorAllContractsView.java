package tutorview;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mainview.Display;
import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Contract;
import model.User;

/**
 * This is the View for the Tutor to see all the contracts that involves them as the second party
 */
public class TutorAllContractsView extends TutorView {
	public TutorAllContractsView(Display display, User user) {
		super(display, user);
	}

	@Override
	protected void placeComponents() {
		super.placeComponents();
		
		List<Contract> contracts = new ArrayList<Contract>();
		contracts = Contract.getAllContractsAsSecondParty(user.getId());
		
		List<JComponent> comp = new ArrayList<JComponent>();
		
		for (Contract c : contracts) {
			JPanel panel = new JPanel();
			if (c.secondPartySigned()) {
				JTextArea tA = new JTextArea();
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA);
			} else if (c.secondPartySigned() == false) {
				JTextArea tA = new JTextArea();
				JButton bT = new JButton("Sign");
				
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA, BorderLayout.WEST);
				panel.add(bT, BorderLayout.EAST);

				bT.addMouseListener(new MouseClickListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						c.secondPartySign(true);
						if (c.isSigned()) {
							c.signContract();
							Utils.CONTRACT_SIGNED.show();
						} else
							Utils.OTHER_PARTY_PENDING.show();
					}
				});
			}
			comp.add(panel);
		}
		
		JPanel midPanel = new ListPanel(comp);
        main.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
		main.add(scrollp);
		this.display.setVisible();

	}

}
