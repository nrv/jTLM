package name.herve.jtlm.gui;

import javax.swing.table.AbstractTableModel;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.persistence.JTLMData;

public class GuiTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6303739942299513029L;

	private JTLMData data;
	private JTLMApplication jtlm;

	public GuiTableModel(JTLMApplication jtlm, JTLMData data) {
		super();
		this.jtlm = jtlm;
		this.data = data;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < 2) {
			return String.class;
		} else {
			return Boolean.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 2 + data.getLists().size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "account";
		} else if (columnIndex == 1) {
			return "name";
		} else {
			return data.getLists().get(columnIndex - 2).getName();
		}
	}

	public JTLMData getData() {
		return data;
	}

	@Override
	public int getRowCount() {
		return data.getFriends().getMembersSize();
	}
	
	public TwitterUser getTwitterUser(int rowIndex) {
		return data.getFriends().get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return getTwitterUser(rowIndex).getScreenName();
		} else if (columnIndex == 1) {
			return getTwitterUser(rowIndex).getName();
		} else {
			TwitterList tl = data.getLists().get(columnIndex - 2);
			return tl.contains(getTwitterUser(rowIndex).getScreenName());
		}
	}

	public boolean hasRowAList(int rowIndex) {
		for (TwitterList tl : data.getLists()) {
			if (tl.contains(getTwitterUser(rowIndex).getScreenName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex < 2) {
			return false;
		} else {
			return true;
		}
	}

	public void setData(JTLMData data) {
		this.data = data;
		fireTableStructureChanged();
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex >= 2) {
			TwitterList tl = data.getLists().get(columnIndex - 2);
			try {
				if (tl.contains(getTwitterUser(rowIndex).getScreenName())) {
					jtlm.removeFromList(tl, getTwitterUser(rowIndex));
				} else {
					jtlm.addToList(tl, getTwitterUser(rowIndex));
				}
				fireTableRowsUpdated(rowIndex, rowIndex);
			} catch (JTLMException e) {
				e.printStackTrace();
			}
		}
	}

}
