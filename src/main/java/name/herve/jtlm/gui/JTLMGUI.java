package name.herve.jtlm.gui;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import name.herve.jtlm.JTLMApplication;

public class JTLMGUI {
	public static void main(String[] args) {
		new JTLMGUI().start();
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
	}

}
