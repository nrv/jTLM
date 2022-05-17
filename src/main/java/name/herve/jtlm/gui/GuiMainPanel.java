package name.herve.jtlm.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.Tweet;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.persistence.JTLMData;
import plugins.nherve.toolbox.gui.GUIUtil;

public class GuiMainPanel extends GuiJTLMPanel implements StatusListener, ActionListener {
	private final static File TMPFILE = new File("/tmp/jtlm.data");
	private static final long serialVersionUID = -2798294419337804399L;
	private static final int maxToRetrieve = 100;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private class RetrieveTweetsAction extends AbstractAction {
		@Override
		public void actionPerformed(final ActionEvent e) {
			processor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						TwitterUser u = model.getTwitterUser(Integer.valueOf(e.getActionCommand()));
						last(u);
					} catch (JTLMException e) {
						setStatusMessage("Error : " + e.getMessage(), false);
					}
				}
			});

		}
	}

	private class UnfollowAction extends AbstractAction {
		@Override
		public void actionPerformed(final ActionEvent e) {
			processor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						TwitterUser u = model.getTwitterUser(Integer.valueOf(e.getActionCommand()));
						u = jtlm.unfollow(u.getId());
						model.removeUser(u);
						model.fireTableDataChanged();
						setStatusMessage("Unfollowed @" + u.getScreenName() + " - " + u.getName(), true);
					} catch (JTLMException e) {
						setStatusMessage("Error : " + e.getMessage(), false);
					}
				}
			});

		}
	}

	private void last(TwitterUser u) throws JTLMException {
		setStatusMessage("Retrieving last tweets of @" + u.getScreenName() + " - " + u.getName(), false);
		System.out.println("@" + u.getScreenName() + " - " + u.getName());
		u.setLastTweetDone(true);
		List<Tweet> tweets = jtlm.getLastTweets(u.getId());
		tweets.sort(new Comparator<Tweet>() {

			@Override
			public int compare(Tweet o1, Tweet o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		if (tweets != null && !tweets.isEmpty()) {
			u.setLastTweet1(sdf.format(tweets.get(0).getDate()));
			u.setLastTweet20(sdf.format(tweets.get(tweets.size() - 1).getDate()));
		}
	}

	private JLabel lbStatus;
	private JLabel lbLogin;
	private JButton btConnect;
	private JButton btRetrieveLastTweets;
	private JButton btSynchronize;
	private JButton btApplyListChanges;
	private JButton btLocalSave;
	private JButton btLocalLoad;

	private GuiTableModel model;
	private GUITable table;
	private Executor processor;

	private JTLMData data;

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
			if (b == btSynchronize) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Loading lists ...", false);
							data.getLists().clear();
							data.getLists().addAll(jtlm.getLists());
						} catch (JTLMException e1) {
							setStatusMessage("Error : " + e1.getMessage(), false);
						}

						try {
							setStatusMessage("Loading friends ...", false);
							data.setFriends(jtlm.getFriends(jtlm.getAuthenticatedUser().getScreenName()));

							for (TwitterList tl : data.getLists()) {
								setStatusMessage("Loading list " + tl.getName() + " ...", false);
								jtlm.fillListMembers(tl);
							}

						} catch (final JTLMException e1) {
							setStatusMessage("Error : " + e1.getMessage(), false);

						}

						model.setData(data);
						setStatusMessage("Ready !", true);
					}
				});
			} else if (b == btLocalSave) {
				processor.execute(new Runnable() {
					@Override
					public void run() {

						try {
							setStatusMessage("Saving data to " + TMPFILE, false);
							JTLMData.save(data, TMPFILE);
							setStatusMessage("Ready !", true);
						} catch (JTLMException e1) {
							e1.printStackTrace();
						}
					}
				});
			} else if (b == btLocalLoad) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Loading data from " + TMPFILE, false);
							data = JTLMData.load(TMPFILE);
							model.setData(data);
							setStatusMessage("Ready !", true);
						} catch (JTLMException e1) {
							e1.printStackTrace();
						}
					}
				});
			} else if (b == btConnect) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Trying to connect ...", false);
							jtlm.init();
							jtlm.start();
							setLoginText("@" + jtlm.getAuthenticatedUser().getScreenName() + " - " + jtlm.getAuthenticatedUser().getName());
							setStatusMessage("Ready !", true);
						} catch (JTLMException e1) {
							setStatusMessage("Unable to connect to Twitter : " + e1.getMessage(), false);
						}
					}
				});
			} else if (b == btApplyListChanges) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							setStatusMessage("Trying to synchronize list changes ...", false);
							jtlm.syncLists();
							setStatusMessage("Ready !", true);
						} catch (JTLMException e1) {
							setStatusMessage("Unable to connect to Twitter : " + e1.getMessage(), false);
						}
					}
				});
			} else if (b == btRetrieveLastTweets) {
				processor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							int count = maxToRetrieve;
							setStatusMessage("Retrieving last tweets for " + count + " users ...", false);
							for (TwitterUser u : model.getFriends()) {
								if (u.isLastTweetDone()) {
									continue;
								}
								last(u);
								count--;
								if (count == 0) {
									break;
								}
							}
							setStatusMessage("Ready !", true);
						} catch (JTLMException e1) {
							setStatusMessage("Unable to connect to Twitter : " + e1.getMessage(), true);
						}
					}
				});
			}
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
					btSynchronize.setEnabled(buttons && jtlm.isConnected());
					btRetrieveLastTweets.setEnabled(buttons && jtlm.isConnected());
					btConnect.setEnabled(buttons && !jtlm.isConnected());
					btLocalLoad.setEnabled(buttons);
					btLocalSave.setEnabled(buttons);

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

		data = new JTLMData();

		btConnect = new JButton("Connect");
		btConnect.setToolTipText("Connect");
		btConnect.addActionListener(this);

		btSynchronize = new JButton("Synchronize");
		btSynchronize.setToolTipText("Synchronize");
		btSynchronize.addActionListener(this);

		btRetrieveLastTweets = new JButton("Last tweets");
		btRetrieveLastTweets.setToolTipText("WARNING - time consuming !");
		btRetrieveLastTweets.addActionListener(this);

		btLocalLoad = new JButton("Load");
		btLocalLoad.setToolTipText("Load");
		btLocalLoad.addActionListener(this);

		btLocalSave = new JButton("Save");
		btLocalSave.setToolTipText("Save");
		btLocalSave.addActionListener(this);

		btApplyListChanges = new JButton("Apply");
		btApplyListChanges.setToolTipText("Apply");
		btApplyListChanges.addActionListener(this);

		add(GUIUtil.createLineBoxPanel(lbLogin, Box.createHorizontalGlue(), btConnect, Box.createHorizontalStrut(10), btSynchronize, Box.createHorizontalStrut(10),
				btRetrieveLastTweets, Box.createHorizontalStrut(10), btApplyListChanges, Box.createHorizontalStrut(10), btLocalLoad, Box.createHorizontalStrut(10), btLocalSave),
				BorderLayout.PAGE_START);

		JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());

		model = new GuiTableModel(jtlm, data);

		table = new GUITable(model, new RetrieveTweetsAction(), new UnfollowAction());

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		middle.add(scrollPane, BorderLayout.CENTER);

		add(middle, BorderLayout.CENTER);

		lbStatus = new JLabel();
		add(GUIUtil.createLineBoxPanel(lbStatus, Box.createHorizontalGlue()), BorderLayout.PAGE_END);

		setStatusMessage("Ready !", true);
	}

	@Override
	public void statusChanged(String statusMessage) {
		lbStatus.setText(statusMessage);
	}
}
