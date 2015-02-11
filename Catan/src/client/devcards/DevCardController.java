package client.devcards;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.ResourceType;
import client.base.*;
import client.model.ClientModel;

/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController,Observer {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;

	/**
	 * DevCardController constructor
	 * 
	 * @param view
	 *            "Play dev card" view
	 * @param buyCardView
	 *            "Buy dev card" view
	 * @param soldierAction
	 *            Action to be executed when the user plays a soldier card. It
	 *            calls "mapController.playSoldierCard()".
	 * @param roadAction
	 *            Action to be executed when the user plays a road building
	 *            card. It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view,
			IBuyDevCardView buyCardView, IAction soldierAction,
			IAction roadAction) {

		super(view);

		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
		ClientModel.getSingleton().addObserver(this);
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView) super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {

		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {

		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {

		getBuyCardView().closeModal();
	}

	@Override
	public void startPlayCard() {

		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {

	}

	@Override
	public void playMonumentCard() {

	}

	@Override
	public void playRoadBuildCard() {

		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {

		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1,
			ResourceType resource2) {

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
