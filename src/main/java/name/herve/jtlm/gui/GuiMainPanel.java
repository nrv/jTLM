package name.herve.jtlm.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.persistence.JTLMData;
import plugins.nherve.toolbox.gui.GUIUtil;

public class GuiMainPanel extends GuiJTLMPanel implements StatusListener, ActionListener {
	private final static File TMPFILE = new File("/tmp/jtlm.data");
	private static final long serialVersionUID = -2798294419337804399L;

	private JLabel lbStatus;
	private JLabel lbLogin;
	private JButton btConnect;
	private JButton btSynchronize;
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

		btLocalLoad = new JButton("Load");
		btLocalLoad.setToolTipText("Load");
		btLocalLoad.addActionListener(this);

		btLocalSave = new JButton("Save");
		btLocalSave.setToolTipText("Save");
		btLocalSave.addActionListener(this);

		add(GUIUtil.createLineBoxPanel(lbLogin, Box.createHorizontalGlue(), btConnect, Box.createHorizontalStrut(10), btSynchronize, Box.createHorizontalStrut(10), btLocalLoad, Box.createHorizontalStrut(10), btLocalSave), BorderLayout.PAGE_START);

		JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());

		model = new GuiTableModel(jtlm, data);
		table = new GUITable(model);
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
