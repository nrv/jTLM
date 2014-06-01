package name.herve.jtlm.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	private Executor processor;

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
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Loading lists ...", false);
							lists = new ArrayList<TwitterList>();
							lists.addAll(jtlm.getLists());
							setStatusMessage("Ready !", true);
							refreshLists();
						} catch (JTLMException e1) {
							setStatusMessage("Error : " + e1.getMessage(), false);
						}
					}
				});
			} else if (b == btReloadFriends) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Loading friends ...", false);
							friends = jtlm.getFriends(jtlm.getAuthenticatedUser().getScreenName());

							for (Component c : allListOptions.getComponents()) {
								if (c instanceof GuiList) {
									GuiList l = (GuiList) c;
									if (l.isShowSelected()) {
										setStatusMessage("Loading list " + l.getTwitterList().getName() + " ...", false);
										jtlm.fillListMembers(l.getTwitterList());
									}
								}
							}
							setStatusMessage("Filtering ...", true);
							filterLists();
							setStatusMessage("Ready !", true);
							refreshLists();
						} catch (final JTLMException e1) {
							setStatusMessage("Error : " + e1.getMessage(), false);

						}
					}
				});
			}
		}
	}

	public void filterLists() {
		for (Component c : allListOptions.getComponents()) {
			if (c instanceof GuiList) {
				GuiList l = (GuiList) c;
				if (l.isShowSelected() && l.isFilterSelected()) {
					for (TwitterUser u : l.getTwitterList()) {
						System.out.println("filtering " + u.getScreenName());
						friends.removeMember(u.getScreenName());
					}
				}
			}
		}
	}

	public void hide(TwitterUserCollection l) {
		lists.remove(l);
		refreshLists();
	}

	public void refreshLists() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					allListOptions.removeAll();
					allListOptions.add(Box.createHorizontalGlue());
					for (TwitterList l : lists) {
						GuiList guiList = new GuiList(jtlm, GuiMainPanel.this, l);
						guiList.startInterface();
						allListOptions.add(guiList);
						allListOptions.add(Box.createHorizontalGlue());
					}
					allListOptions.revalidate();
					allListOptions.repaint();

					allLists.removeAll();
					GuiAccountList guiList = new GuiAccountList(jtlm, GuiMainPanel.this, friends);
					guiList.startInterface();
					allLists.add(guiList);
					allLists.revalidate();
					allLists.repaint();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setLoginText(String text) {
		lbLogin.setText(text);
	}

	private void setStatusMessage(final String msg, final boolean buttons) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					btReloadFriends.setEnabled(buttons);
					btReloadLists.setEnabled(buttons);

					jtlm.setStatusMessage(msg);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void startInterface() {
		processor = Executors.newSingleThreadExecutor();

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
