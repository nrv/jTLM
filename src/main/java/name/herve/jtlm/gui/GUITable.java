package name.herve.jtlm.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class GUITable extends JTable {
	private static final long serialVersionUID = 6020421272135752671L;

	private Action retAc;
	private Action unfAc;
	private GuiTableModel tm;

	public GUITable(GuiTableModel dm, Action retAc, Action unfAc) {
		super(dm);

		this.retAc = retAc;
		this.unfAc = unfAc;
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

	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		
		new ButtonColumn(this, retAc, 0);
		new ButtonColumn(this, unfAc, 1);
	}
}
