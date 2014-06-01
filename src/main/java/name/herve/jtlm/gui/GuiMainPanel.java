package name.herve.jtlm.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.model.TwitterUserCollection;
import plugins.nherve.toolbox.gui.GUIUtil;

public class GuiMainPanel extends GuiJTLMPanel implements StatusListener, ActionListener {
	private static final long serialVersionUID = -2798294419337804399L;

	private JLabel lbStatus;
	private JLabel lbLogin;
	private JButton btReloadLists;
	private JButton btReloadFriends;
	private JPanel allLists;
	private JPanel allListOptions;

	private List<TwitterList> lists;
	private TwitterFriends friends;

	public GuiMainPanel(JTLMApplication jtlm) {
		super(jtlm);

		jtlm.addStatusListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == null) {
			return;
		}

		if (o instanceof JButton) {
			JButton b = (JButton) e.getSource();
			if (b == btReloadLists) {
				try {
					lists = new ArrayList<TwitterList>();
					lists.addAll(jtlm.getLists());

					refreshLists();
				} catch (JTLMException e1) {
					jtlm.setStatusMessage("Error : " + e1.getMessage());
				}
			} else if (b == btReloadFriends) {
				try {
					friends = jtlm.getFriends(jtlm.getAuthenticatedUser().getScreenName());
					
					for (TwitterUser u : friends) {
					}
					
					filterLists();
					refreshLists();
				} catch (JTLMException e1) {
					jtlm.setStatusMessage("Error : " + e1.getMessage());
				}
			}
		}
	}

	public void filterLists() {
	}

	public void hide(TwitterUserCollection l) {
		lists.remove(l);
		refreshLists();
	}

	public void refreshLists() {
		allListOptions.removeAll();
		allListOptions.add(Box.createHorizontalGlue());
		for (TwitterList l : lists) {
			GuiList guiList = new GuiList(jtlm, this, l);
			guiList.startInterface();
			allListOptions.add(guiList);
			allListOptions.add(Box.createHorizontalGlue());
		}
		allListOptions.revalidate();
		allListOptions.repaint();

		allLists.removeAll();
		GuiAccountList guiList = new GuiAccountList(jtlm, this, friends);
		guiList.startInterface();
		allLists.add(guiList);
		allLists.revalidate();
		allLists.repaint();
	}

	public void setLoginText(String text) {
		lbLogin.setText(text);
	}

	public void startInterface() {
		setLayout(new BorderLayout());

		lbLogin = new JLabel("- - - - - -");

		lists = new ArrayList<TwitterList>();
		friends = new TwitterFriends();

		btReloadLists = new JButton("Reload lists");
		btReloadLists.setToolTipText("Reload lists");
		btReloadLists.addActionListener(this);

		btReloadFriends = new JButton("Reload friends");
		btReloadFriends.setToolTipText("Reload friends");
		btReloadFriends.addActionListener(this);
		add(GUIUtil.createLineBoxPanel(lbLogin, Box.createHorizontalGlue(), btReloadLists, Box.createHorizontalStrut(10), btReloadFriends), BorderLayout.PAGE_START);

		JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());

		allListOptions = new JPanel();
		allListOptions.setOpaque(false);
		allListOptions.setLayout(new BoxLayout(allListOptions, BoxLayout.LINE_AXIS));
		middle.add(allListOptions, BorderLayout.PAGE_START);

		allLists = new JPanel();
		allLists.setOpaque(false);
		allLists.setLayout(new BoxLayout(allLists, BoxLayout.LINE_AXIS));
		middle.add(allLists, BorderLayout.CENTER);

		add(middle, BorderLayout.CENTER);

		lbStatus = new JLabel();
		add(GUIUtil.createLineBoxPanel(lbStatus, Box.createHorizontalGlue()), BorderLayout.PAGE_END);
	}

	@Override
	public void statusChanged(String statusMessage) {
		lbStatus.setText(statusMessage);
	}
}
