package name.herve.jtlm.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GUITable extends JTable {
	private static final long serialVersionUID = 6020421272135752671L;

	private GuiTableModel tm;

	public GUITable(GuiTableModel dm) {
		super(dm);

		this.tm = dm;
		setAutoCreateRowSorter(true);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		int ridx = convertRowIndexToModel(row);
		
		if (!tm.hasRowAList(ridx)) {
			c.setBackground(Color.RED);
		} else {
			c.setBackground(getBackground());
		}
		if (c instanceof JLabel) {
			((JLabel) c).setToolTipText(tm.getTwitterUser(ridx).getDescription());
		}
		return c;
	}
}
