package studentview;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import mainview.MouseClickListener;
import model.Contract;

/**
 * View that displays all contracts where this Student is first party
 */
public class StudentAllContracts extends JPanel {
	public static final int CONTRACT_QUOTA = 5; 
	private JList<Contract> contractList;
	List<Contract> contracts;
	public StudentAllContracts(List<Contract> contracts) {
		super(new BorderLayout());
		this.setBackground(Color.CYAN);
		this.contracts = contracts;
		placeComponents();
	}
	
	private void placeComponents() {
		DefaultListModel<Contract> model = new DefaultListModel<Contract>();
		for (Contract c : contracts)
			model.addElement(c);
		contractList = new JList<>(model);

		contractList.setCellRenderer(new ContractCellRenderer());

		JScrollPane scrollp = new JScrollPane(contractList);
		this.add(scrollp);
	}

	public void setSignContractListener(MouseClickListener listener) {
		contractList.addMouseListener(listener);
	}

	public Contract getSelectedContract() {
		return contractList.getSelectedValue();
	}

	public int getSignedContracts() {
		int cnt = 0;
		for (Contract c : contracts) {
			if (c.isSigned())
				cnt++;
		}
		return cnt;
	}

	private class ContractCellRenderer extends JPanel implements ListCellRenderer<Contract> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Contract> list, Contract c, int index,
				boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			JPanel panel = new JPanel();
			if (c.firstPartySigned()) {
				JTextArea tA = new JTextArea();
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA);
			} else if (c.firstPartySigned() == false) {
				JTextArea tA = new JTextArea();
				JButton bT = new JButton("Sign");
				
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA, BorderLayout.CENTER);
				panel.add(bT, BorderLayout.EAST);
			}
			this.add(panel);
			return this;
		}
		
	}
}
