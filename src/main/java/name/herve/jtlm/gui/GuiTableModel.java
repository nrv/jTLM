package name.herve.jtlm.gui;

import javax.swing.table.AbstractTableModel;

import name.herve.jtlm.JTLMApplication;
import name.herve.jtlm.JTLMException;
import name.herve.jtlm.model.TwitterFriends;
import name.herve.jtlm.model.TwitterList;
import name.herve.jtlm.model.TwitterUser;
import name.herve.jtlm.persistence.JTLMData;

public class GuiTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6303739942299513029L;
	private static final int fixedCol = 7;
	private JTLMData data;
	private JTLMApplication jtlm;

	public GuiTableModel(JTLMApplication jtlm, JTLMData data) {
		super();
		this.jtlm = jtlm;
		this.data = data;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < (fixedCol - 1)) {
			return String.class;
		} else {
			return Boolean.class;
		}
	}

	@Override
	public int getColumnCount() {
		return fixedCol + data.getLists().size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 2) {
			return "account";
		} else if (columnIndex == 3) {
			return "name";
		} else if (columnIndex == 4) {
			return "t1";
		} else if (columnIndex == 5) {
			return "t20";
		} else if (columnIndex == 0) {
			return "ret";
		} else if (columnIndex == 1) {
			return "ret";
		} else if (columnIndex == 6) {
			return "list";
		} else {
			return data.getLists().get(columnIndex - fixedCol).getName();
		}
	}

	public JTLMData getData() {
		return data;
	}

	public TwitterFriends getFriends() {
		return data.getFriends();
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
		if (columnIndex == 2) {
			return getTwitterUser(rowIndex).getScreenName();
		} else if (columnIndex == 3) {
			return getTwitterUser(rowIndex).getName();
		} else if (columnIndex == 4) {
			return getTwitterUser(rowIndex).getLastTweet1();
		} else if (columnIndex == 5) {
			return getTwitterUser(rowIndex).getLastTweet20();
		} else if (columnIndex == 0) {
			return "ret";
		} else if (columnIndex == 1) {
			return "unf";
		} else if (columnIndex == 6) {
			return hasRowAList(rowIndex);
		} else {
			TwitterList tl = data.getLists().get(columnIndex - fixedCol);
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
		if (columnIndex <= 1) {
			return true;
		}

		if (columnIndex < fixedCol) {
			return false;
		} else {
			return true;
		}
	}

	public void removeUser(TwitterUser u) {
		data.removeUser(u);
	}

	public void setData(JTLMData data) {
		this.data = data;
		fireTableStructureChanged();
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex >= fixedCol) {
			TwitterList tl = data.getLists().get(columnIndex - fixedCol);
			try {
				if (tl.contains(getTwitterUser(rowIndex).getScreenName())) {
					jtlm.removeFromList(tl, getTwitterUser(rowIndex));
				} else {
					jtlm.addToListAsync(tl, getTwitterUser(rowIndex));
				}
				fireTableRowsUpdated(rowIndex, rowIndex);
			} catch (JTLMException e) {
				e.printStackTrace();
			}
		}
	}

}
