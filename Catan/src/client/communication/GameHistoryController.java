package client.communication;

import java.util.*;

import client.base.*;
import client.model.ClientModel;
import shared.definitions.*;

/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements
		IGameHistoryController, Observer {

	public GameHistoryController(IGameHistoryView view) {

		super(view);
		initFromModel();
		ClientModel.getSingleton().addObserver(this);		
	}

	@Override
	public IGameHistoryView getView() {

		return (IGameHistoryView) super.getView();
	}

	private void initFromModel() {

		// <temp>

		List<LogEntry> entries = new ArrayList<LogEntry>();
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(
				CatanColor.ORANGE,
				"This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(
				CatanColor.ORANGE,
				"This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(
				CatanColor.ORANGE,
				"This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));
		entries.add(new LogEntry(CatanColor.BROWN, "This is a brown message"));
		entries.add(new LogEntry(
				CatanColor.ORANGE,
				"This is an orange message ss x y z w.  This is an orange message.  This is an orange message.  This is an orange message."));

		getView().setEntries(entries);

		// </temp>
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Change the view, yay!
		
	}

}
