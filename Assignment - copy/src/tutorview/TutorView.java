package tutorview;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import mainview.Display;
import mainview.HomeView;
import mainview.NavigationPane;
import model.User;

import mainview.View;

/**
 * This is the View responsible for redirecting the Tutor to other Views they need.
 */
public class TutorView extends View implements NavigationPane{
	protected User user;
//	public JButton createBid = new JButton("Create Match Request");
	public JButton viewAllBids = new JButton("View All Bids");
	public JButton viewContracts = new JButton("View Contracts");
	public JPanel main = new JPanel(new BorderLayout());

//
//	private JButton viewAllBids = new JButton("View All Bids");
//	private JButton viewContracts = new JButton("View Contracts");
//	protected JPanel main;

	public TutorView(Display display, User user) {
		super(display);
		this.user = user;
	}

	protected void placeComponents() {
		showNavigationPane(display, main, new JButton[]{homeButton, viewAllBids, viewContracts});
		display.createPanel(main);
		this.display.setVisible();
//		main = createPanel(new BorderLayout());
//		this.display.setVisible();
//		setSwitchPanelListener(main, homeButton, new HomeView(display, user));
//		setSwitchPanelListener(main, viewAllBids, new TutorAllBidsView(display, user));
//		setSwitchPanelListener(main, viewContracts, new TutorAllContractsView(display, user));
//		showNavigationPane(display, main, new JButton[]{homeButton, viewAllBids, viewContracts});
//		this.display.setVisible();
	}

}
