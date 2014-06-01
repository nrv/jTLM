package name.herve.jtlm.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.model.TwitterUser;

public class GuiAccount extends GuiJTLMPanel {
	private static final long serialVersionUID = 3677953799573253804L;

	private TwitterUser user;
	private JLabel lbInfo;

	public GuiAccount(JTLMApplication jtlm, TwitterUser user) {
		super(jtlm);
		this.user = user;
	}

	public void startInterface() {
		BoxLayout bl = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(bl);
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		setToolTipText(user.getDescription());
		lbInfo = new JLabel("@" + user.getScreenName() + " - " + user.getName());
		add(lbInfo);
		add(Box.createHorizontalGlue());
	}

}
