package name.herve.jtlm.gui;

import javax.swing.JPanel;

import name.herve.jtlm.JTLMApplication;

public abstract class GuiJTLMPanel extends JPanel {
	private static final long serialVersionUID = -317789488737323147L;

	protected JTLMApplication jtlm;

	public GuiJTLMPanel(JTLMApplication jtlm) {
		super();
		this.jtlm = jtlm;
	}

}
