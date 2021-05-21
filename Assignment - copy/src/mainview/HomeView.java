package mainview;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.HomeButtonListener;
import controller.TutorController;
import controller.ViewAllBidsListener;
import controller.ViewContractsListener;
import tutorview.CreateBidView;
import tutorview.TutorAllBidsView;
import tutorview.TutorView;
import model.User;
import studentview.StudentView;

import java.awt.FlowLayout;
public class HomeView extends View {

	private User user;
	/**
		 * Home view which directs user to different site
		 * @param display the shared display
		 * @param user the loged-in user
		 */
	public HomeView(Display display, User user) {
		
		super(display);
		this.user = user;
	}

	protected void placeComponents() {

		JPanel panel = createPanel(new FlowLayout());
		JButton studentButton = new JButton("Student site");
		
        JButton tutorButton = new JButton("Tutor site");
        
        if (user.isStudent()) { 
        	panel.add(studentButton);
        	addSwitchPanelListener(panel, studentButton, new StudentView(display, user));
        }
        if (user.isTutor()) {
        	panel.add(tutorButton);
//        	addSwitchPanelListener(panel, tutorButton, new TutorView(display, user));
			TutorController tutorController = new TutorController(display);
			View tutorView = new TutorView(display, user);
			tutorController.addView(tutorView);
			tutorController.addListener(tutorView, new HomeButtonListener());
			tutorController.addListener(tutorView, new ViewAllBidsListener());
			tutorController.addListener(tutorView, new ViewContractsListener());

			tutorController.initView();

			TutorView tutorAllBidsView = new TutorAllBidsView(display, user);
			TutorView createBidView  = new CreateBidView(display, user);

        }
        
        JButton logOut = new JButton("Log out");
        panel.add(logOut);
        addSwitchPanelListener(panel, logOut, new AuthenticationView(display));
        this.display.setVisible();
	}
}
