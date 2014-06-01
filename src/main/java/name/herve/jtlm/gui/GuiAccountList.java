package name.herve.jtlm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.model.TwitterUserCollection;
import plugins.nherve.toolbox.gui.GUIUtil;

public class GuiAccountList extends GuiJTLMPanel implements ActionListener {
	private static final long serialVersionUID = -3153445707869649150L;

	private TwitterUserCollection list;
	
	private GuiMainPanel parent;
	
	private JButton btLoad;
	private JButton btHide;
	private JPanel internal;
	private JScrollPane scroll;

	public GuiAccountList(JTLMApplication jtlm, GuiMainPanel parent, TwitterUserCollection list) {
		super(jtlm);
		this.parent = parent;
		this.list = list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == null) {
			return;
		}

		if (o instanceof JButton) {
			JButton b = (JButton) e.getSource();
			if (b == btHide) {
				parent.hide(list);
			} else if (b == btLoad) {
				try {
					if (list instanceof TwitterList) {
						jtlm.fillListMembers((TwitterList)list);
					} else {
						list.clearMembers();
						TwitterUserCollection col = jtlm.getFriends(jtlm.getAuthenticatedUser().getScreenName());
						for (TwitterUser u : col) {
							list.addMember(u);
						}
					}
					refreshList();
				} catch (JTLMException e1) {
					jtlm.setStatusMessage("Error : " + e1.getMessage());
				}
			} 
		}
	}
	
	public void refreshList() {
		internal.removeAll();
		internal.add(Box.createVerticalGlue());
		
		list.sort();
		
		for (TwitterUser u : list) {
			GuiAccount gui = new GuiAccount(jtlm, u);
			gui.startInterface();
			internal.add(gui);
			internal.add(Box.createVerticalStrut(10));
		}
		internal.revalidate();
		internal.repaint();
	}

	public void startInterface() {
		BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(bl);
		setBorder(new TitledBorder(list.getName()));

		btLoad = new JButton("Load");
		btLoad.setToolTipText("Load");
		btLoad.addActionListener(this);
		
		btHide = new JButton("Hide");
		btHide.setToolTipText("Hide");
		btHide.addActionListener(this);
		
//		add(GUIUtil.createLineBoxPanel(btLoad, Box.createHorizontalGlue(), btHide));

		internal = new JPanel();
		internal.setLayout(new BoxLayout(internal, BoxLayout.PAGE_AXIS));
		
		refreshList();
		
		scroll = new JScrollPane(internal);
		add(scroll);
	}
}
