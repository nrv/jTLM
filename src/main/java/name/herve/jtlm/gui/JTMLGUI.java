package name.herve.jtlm.gui;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;

public class JTMLGUI {
	public static void main(String[] args) {
		new JTMLGUI().start();
	}

	private JTLMApplication app;

	public void start() {
		app = new JTLMApplication();

		GuiMainPanel gui = new GuiMainPanel(app);
		gui.startInterface();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame f = new JFrame();
		Container mainPanel = f.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(gui);
		f.setTitle("jTLM");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 500);
		f.setVisible(true);

		try {
			app.setStatusMessage("Trying to connect ...");
			app.init();
			app.start();
			app.setStatusMessage("Ready !");
			gui.setLoginText("@" + app.getAuthenticatedUser().getScreenName() + " - " + app.getAuthenticatedUser().getName());
		} catch (JTLMException e) {
			app.setStatusMessage("Unable to connect to Twitter : " + e.getMessage());
		}
	}

}
