package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.model.ClientModel;
import client.model.ClientModelFacade;
import shared.model.Deck;
import shared.model.ResourceList;
import shared.utils.Serializer;

public class DevCardTest {
	private String clientModelJson = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"location\":{\"x\":0,\"y\":-2}},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-2},\"number\":3},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"brick\",\"location\":{\"x\":0,\"y\":-1},\"number\":8},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-1},\"number\":11},{\"resource\":\"sheep\",\"location\":{\"x\":-2,\"y\":0},\"number\":10},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":12},{\"resource\":\"sheep\",\"location\":{\"x\":0,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":1,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":2,\"y\":0},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":1},\"number\":6},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":0,\"y\":1},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":2},\"number\":9},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":2},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":2}],\"roads\":[{\"owner\":0,\"location\":{\"direction\":\"S\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"direction\":\"SE\",\"x\":0,\"y\":1}},{\"owner\":0,\"location\":{\"direction\":\"SW\",\"x\":2,\"y\":0}}],\"cities\":[],\"settlements\":[{\"owner\":0,\"location\":{\"direction\":\"SW\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"direction\":\"NW\",\"x\":1,\"y\":0}}],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":3,\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":12,\"playerIndex\":0,\"name\":\"string\",\"color\":\"purple\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":13,\"playerIndex\":1,\"name\":\"test1\",\"color\":\"puce\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":14,\"playerIndex\":2,\"name\":\"test2\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":12,\"cities\":4,\"settlements\":4,\"soldiers\":0,\"victoryPoints\":1,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":15,\"playerIndex\":3,\"name\":\"test3\",\"color\":\"orange\"}],\"log\":{\"lines\":[{\"source\":\"string\",\"message\":\"string built a settlement\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"}]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":4}";
	private ClientModel clientModel = null;

	@Before
	public void setUp() throws Exception {
		clientModel = Serializer.deserializeClientModel(clientModelJson);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void canBuyDevCardPass() {
		System.out.println("Testing CanBuyDevCard 1");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Playing");
		clientModel.getPlayers()[0]
				.setResources(new ResourceList(0, 1, 1, 1, 0));
		clientModel.setDeck(new Deck(1, 0, 0, 0, 0));
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canBuyDevCard(0);
		assertTrue(pass);
	}

	@Test
	public void canBuyDevCardFail() { // player doesn't have cards
		System.out.println("Testing CanBuyDevCard 2");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Playing");
		clientModel.getPlayers()[0]
				.setResources(new ResourceList(0, 1, 0, 1, 0));
		clientModel.setDeck(new Deck(1, 0, 0, 0, 0));
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canBuyDevCard(0);
		assertFalse(pass);
	}

	@Test
	public void canBuyDevCardFailTwo() { // bank doesn't have cards
		System.out.println("Testing CanBuyDevCard 3");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Playing");
		clientModel.getPlayers()[0]
				.setResources(new ResourceList(0, 1, 1, 1, 0));
		clientModel.setDeck(new Deck(0, 0, 0, 0, 0));
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canBuyDevCard(0);
		assertFalse(pass);
	}

	@Test
	public void canBuyDevCardFailThree() { // not turn
		System.out.println("Testing CanBuyDevCard 4");
		clientModel.getTurnTracker().setCurrentTurn(1);
		clientModel.getTurnTracker().setStatus("Playing");
		clientModel.getPlayers()[0]
				.setResources(new ResourceList(0, 1, 1, 1, 0));
		clientModel.setDeck(new Deck(1, 0, 0, 0, 0));
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canBuyDevCard(0);
		assertFalse(pass);
	}

	@Test
	public void canBuyDevCardFailFour() { // not "Playing"
		System.out.println("Testing CanBuyDevCard 5");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Rolling");
		clientModel.getPlayers()[0]
				.setResources(new ResourceList(0, 1, 1, 1, 0));
		clientModel.setDeck(new Deck(1, 0, 0, 0, 0));
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canBuyDevCard(0);
		assertFalse(pass);
	}

	@Test
	public void canFinishTurnPass() {
		System.out.println("Testing CanFinishTurn 1");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Playing");
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canFinishTurn(0);
		assertTrue(pass);
	}

	@Test
	public void canFinishTurnFail() { // not his turn
		System.out.println("Testing CanFinishTurn 2");
		clientModel.getTurnTracker().setCurrentTurn(1);
		clientModel.getTurnTracker().setStatus("Playing");
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canFinishTurn(0);
		assertFalse(pass);
	}

	@Test
	public void canFinishTurnFailTwo() { // not his turn
		System.out.println("Testing CanFinishTurn 3");
		clientModel.getTurnTracker().setCurrentTurn(0);
		clientModel.getTurnTracker().setStatus("Rolling");
		ClientModelFacade clientModelController = new ClientModelFacade();
		ClientModel.getSingleton().setClientModel(clientModel);
		boolean pass = clientModelController.canFinishTurn(0);
		assertFalse(pass);
	}

}
