package name.herve.jtlm.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.model.TwitterList;

public class GuiList extends GuiJTLMPanel {
	private static final long serialVersionUID = 1556141126397162553L;

	private TwitterList list;
	private GuiMainPanel parent;
	private JCheckBox cbFilter;
	private JCheckBox cbShow;

	public GuiList(JTLMApplication jtlm, GuiMainPanel parent, TwitterList list) {
		super(jtlm);
		this.parent = parent;
		this.list = list;
	}

	public TwitterList getTwitterList() {
		return list;
	}

	public boolean isFilterSelected() {
		return cbFilter.isSelected();
	}

	public boolean isShowSelected() {
		return cbShow.isSelected();
	}

	public void startInterface() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		cbFilter = new JCheckBox("filter");
		cbFilter.setSelected(true);

		cbShow = new JCheckBox("show");
		cbShow.setSelected(true);

		add(new JLabel(list.getName()));
		add(cbFilter);
		add(cbShow);
	}
}
