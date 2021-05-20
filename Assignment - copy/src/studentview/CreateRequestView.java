package studentview;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mainview.Display;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Bid;
import model.BidAddInfo;
import model.User;
import model.Subject;

/**
 * View where the Student creates a match request
 */
public class CreateRequestView extends StudentView {
	JRadioButton openBid = new JRadioButton("Open");
    JRadioButton closeBid = new JRadioButton("Close");
    ButtonGroup bidType = new ButtonGroup();
    {
    openBid.setActionCommand(Bid.BidType.open.toString());
    closeBid.setActionCommand(Bid.BidType.close.toString());
    bidType.add(openBid);
    bidType.add(closeBid);
    openBid.setSelected(true);
    }
    
    JRadioButton perSession = new JRadioButton("per session");
    JRadioButton perHour = new JRadioButton("per hour");
    ButtonGroup rateType = new ButtonGroup();
    {
    perSession.setActionCommand("per session");
    perHour.setActionCommand("per hour");
    rateType.add(perSession);
    rateType.add(perHour);
    perSession.setSelected(true);
    }
    private JComboBox<String> subject = new JComboBox<String>(Subject.getAllSubjectsNames());
	private JComboBox<String> competency = new JComboBox<String>(new String[] {"0","1","2","3","4","5","6","7","8","9","10"});
	private JTextField hourPerLesson = new JTextField();
	private JTextField sessionsPerWeek = new JTextField();
	private JTextField rate = new JTextField();
	
	private JLabel typeLb = new JLabel("Type");
	private JLabel subjectLb = new JLabel("Subject");
	private JLabel competencyLb = new JLabel("Tutor's Level of competency");
	private JLabel hourPerLessonLb = new JLabel("Preferred Hour/Lesson");
	private JLabel sessionsPerWeekLb = new JLabel("Preferred Sesions/Week");
	private JLabel rateLb = new JLabel("Preferred Rate");
	
	public CreateRequestView(Display display, User user) {
		super(display, user);
	}
	
	protected void placeComponents() {
		super.placeComponents();
		JPanel midPanel = createPanel();
		GroupLayout groupLayout = new GroupLayout(midPanel);
		midPanel.setLayout(groupLayout);
		midPanel.setBackground(Color.green);
		
		main.add(midPanel);
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
				.addComponent(typeLb)
				.addComponent(subjectLb)
				.addComponent(competencyLb)
				.addComponent(hourPerLessonLb)
				.addComponent(sessionsPerWeekLb)
				.addComponent(rateLb))
				.addGroup(groupLayout.createParallelGroup()
						.addGroup(groupLayout.createSequentialGroup().addComponent(openBid)
								.addComponent(closeBid))
						.addComponent(subject)
						.addComponent(competency)
						.addComponent(hourPerLesson)
						.addComponent(sessionsPerWeek)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(rate)
								.addComponent(perHour)
								.addComponent(perSession))
						)
				);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(typeLb)
						.addComponent(openBid)
						.addComponent(closeBid))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(subjectLb)
						.addComponent(subject))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(competencyLb)
						.addComponent(competency))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(hourPerLessonLb)
						.addComponent(hourPerLesson))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(sessionsPerWeekLb)
						.addComponent(sessionsPerWeek))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(rateLb)
						.addComponent(rate)
						.addComponent(perHour)
						.addComponent(perSession))
				);
	
		JPanel bottomPanel = createPanel();
        bottomPanel.setBackground(Color.red);
        main.add(bottomPanel, BorderLayout.SOUTH);
        JButton createRequest = new JButton("Create Request"); 
		bottomPanel.add(createRequest);
		
		createRequest.addMouseListener(new MouseClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String c = (String) competency.getSelectedItem();
				String h = hourPerLesson.getText();
				String ss = sessionsPerWeek.getText();
				String r = rate.getText(); 
				String rT = rateType.getSelection().getActionCommand();
				String sj = (String) subject.getSelectedItem();
				String t = bidType.getSelection().getActionCommand();
				try {
					BidAddInfo addInfo = new BidAddInfo(c,h,ss,r,rT);
					Bid.postBid(t, user.getId(), Subject.getSubjectId(sj), addInfo);
					Utils.SUCCESS_MATCH_REQUEST.show();
				} catch (NumberFormatException nfe) {
					Utils.INVALID_FIELDS.show();
				} catch (NullPointerException npe) {
					Utils.PLEASE_FILL_IN.show();
				}
			}});
		this.display.setVisible();
	}

}
