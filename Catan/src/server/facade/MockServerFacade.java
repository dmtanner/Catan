package server.facade;

import java.util.ArrayList;
import java.util.HashMap;

import server.model.ServerModel;
import shared.communication.AcceptTradeParams;
import shared.communication.AddAIParams;
import shared.communication.AddAIResponse;
import shared.communication.BuildCityParams;
import shared.communication.BuildRoadCardParams;
import shared.communication.BuildRoadParams;
import shared.communication.BuildSettlementParams;
import shared.communication.ChangeLogLevelParams;
import shared.communication.ChangeLogLevelResponse;
import shared.communication.ChatMessage;
import shared.communication.CommandList;
import shared.communication.CreateGameParams;
import shared.communication.DiscardCardsParams;
import shared.communication.GameSummary;
import shared.communication.InvalidInputException;
import shared.communication.JoinGameParams;
import shared.communication.LoadGameParams;
import shared.communication.MaritimeTradeParams;
import shared.communication.MoveRobberParams;
import shared.communication.MoveSoldierParams;
import shared.communication.Password;
import shared.communication.PlayMonopolyParams;
import shared.communication.PlayMonumentParams;
import shared.communication.SaveParams;
import shared.communication.TradeOfferParams;
import shared.communication.UserActionParams;
import shared.communication.UserCredentials;
import shared.communication.Username;
import shared.communication.YearOfPlentyParams;
import shared.data.GameInfo;
import shared.data.PlayerInfo;
import shared.model.MessageLine;
import shared.model.MessageList;
import shared.model.Player;
import shared.model.ResourceList;
import shared.model.TradeOffer;
import shared.utils.Serializer;
import shared.utils.ServerResponseException;
import client.model.ClientModel;

public class MockServerFacade implements IServerFacade {

	// ClientModel clientMockModel = null; //just hard code this server model
	private String clientModelJson = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"location\":{\"x\":0,\"y\":-2}},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-2},\"number\":3},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"brick\",\"location\":{\"x\":0,\"y\":-1},\"number\":8},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-1},\"number\":11},{\"resource\":\"sheep\",\"location\":{\"x\":-2,\"y\":0},\"number\":10},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":12},{\"resource\":\"sheep\",\"location\":{\"x\":0,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":1,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":2,\"y\":0},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":1},\"number\":6},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":0,\"y\":1},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":2},\"number\":9},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":2},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":2}],\"roads\":[{\"owner\":3,\"location\":{\"direction\":\"S\",\"x\":1,\"y\":0}},{\"owner\":3,\"location\":{\"direction\":\"SE\",\"x\":0,\"y\":1}},{\"owner\":3,\"location\":{\"direction\":\"SW\",\"x\":2,\"y\":0}}],\"cities\":[],\"settlements\":[{\"owner\":3,\"location\":{\"direction\":\"SW\",\"x\":1,\"y\":0}}],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":3,\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":12,\"playerIndex\":0,\"name\":\"string\",\"color\":\"purple\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":13,\"playerIndex\":1,\"name\":\"test1\",\"color\":\"puce\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":14,\"playerIndex\":2,\"name\":\"test2\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":12,\"cities\":4,\"settlements\":4,\"soldiers\":0,\"victoryPoints\":1,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":15,\"playerIndex\":3,\"name\":\"test3\",\"color\":\"orange\"}],\"log\":{\"lines\":[{\"source\":\"string\",\"message\":\"string built a settlement\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"}]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":4}";

	// add some data?
	ServerModel serverMockModel = (ServerModel) Serializer.deserialize(
			clientModelJson, ServerModel.class);

	public MockServerFacade(ServerModel model) {
		this.serverMockModel = model;
	}

	/**
	 * Prepares credentials to be sent over network, then sends them to server
	 * login. The inputed credentials must matched the expected canned response.
	 * 
	 * @Pre Username not null.
	 * @Pre Password not null.
	 * @Post A valid LoginResponse returned.
	 */
	@Override
	public boolean login(UserCredentials credentials)
			throws ServerResponseException {
		boolean wasSuccess = true;
		if (credentials.getUsername() == "test"
				&& credentials.getPassword() == "pass") {
			wasSuccess = true;
		}
		return wasSuccess;
	}

	@Override
	public boolean Register(UserCredentials credentials)
			throws ServerResponseException {
		boolean wasSuccess = false;
		try {
			new Username(credentials.getUsername());
			new Password(credentials.getPassword());
		} catch (InvalidInputException e) {
			return false;
		}
		if (credentials.getUsername() != "test") {
			wasSuccess = true;
		}
		return wasSuccess;
	}

	/**
	 * Get a list of games from the mock server. A canned list of games should
	 * be returned.
	 * 
	 * @Pre None
	 * @Post Returns a hard coded GamesList object.
	 */
	@Override
	public GameSummary[] getGameList() throws ServerResponseException {
		GameSummary[] games = new GameSummary[2];
		return games;
	}

	/**
	 * Creates a game in the mock server. A canned game summary should be
	 * generated given the input parameters.
	 * 
	 * @Pre Valid CreateGameParams
	 * @Post Returns a canned GameSummary object.
	 */
	@Override
	public GameInfo createGame(CreateGameParams params)
			throws ServerResponseException {
		if (params.getname() == null)
			return null;

		GameInfo test = new GameInfo();
		test.setTitle("game1");
		test.setId(1);
		for (int i = 0; i < 5; i++) {
			test.addPlayer(new PlayerInfo());
		}
		return test;
	}

	/**
	 * The Client that calls this method to be added to a game. This should
	 * return true given that the client is not already part of a game.
	 * 
	 * @Pre The user has previously logged in to the server.
	 * @Pre The player may join the game because they are already in the game,
	 *      or there is space in the game to add a new player.
	 * @Pre Valid JoinGameParams
	 * @Post Returns true if the player was added.
	 */
	@Override
	public String joinGame(JoinGameParams params)
			throws ServerResponseException {
		String validColor = "red,orange,blue,green,yellow,purple,puce,white,brown";
		// check if it is a valid Catan color given.
		// Also, we will say that the color green is already taken.
		if (!(validColor.contains(params.getColor().toLowerCase()))
				|| params.getColor().toLowerCase() == "green")
			return "Invalid Request";
		return "Success";
	}

	/**
	 * Saves the current game state. Should return true if the save parameters
	 * are valid.
	 * 
	 * @Pre The specified game ID is valid.
	 * @Pre The specified file name is valid (not null or empty).
	 * @Post If good input, returns a true and writes to a local file.
	 * @Post If bad input, will return false and doesn't write to file.
	 */
	@Override
	public String saveGame(SaveParams params) throws ServerResponseException {
		if ((params.getId() == 1 || params.getId() == 0)
				&& params.getname() != null) {
			if (params.getname().equals("game0.txt")
					|| params.getname().equals("game1.txt")) {
				return "Success";
			}
		}
		return "Invalid Request";
	}

	/**
	 * Loads a saved game. Should return true if the input string is valid.
	 * 
	 * @Pre A previously saved game file with the specified name exists in the
	 *      server's saves/directory.
	 * @Post If the operation succeeds, the game in the specified file has been
	 *       loaded into the server and its state restored (including its ID).
	 * @Post If bad parameters, throw an error.
	 */
	@Override
	public String loadGame(LoadGameParams params)
			throws ServerResponseException {
		if (params != null) {
			if (params.getName().equals("game0.txt"))
				return "Success";
		}
		return "Invalid Request";
	}

	/**
	 * Gets the current Client Model associated with this game. Should be a
	 * canned ClientModel that is returned to the the client.
	 * 
	 * @Pre Player is logged in and part of the game
	 * @Pre If specified, the version number is included as the version query
	 *      parameter in the request URL, and its value is a valid integer.
	 * @Post If the operation fails, an exception is thrown
	 */
	@Override
	public ClientModel getCurrentGame(int version)
			throws ServerResponseException {
		if (version < 0)
			return null;
		return serverMockModel.toClientModel();
	}

	/**
	 * Sets the game back to starting state, which is a canned response (a
	 * different ClientModel that that given by getCurrentGame method).
	 * 
	 * @Pre Correct parameters are sent.
	 * @Post If the operation succeeds, the game's command history has been
	 *       cleared out.
	 * @Post If the operation succeeds, the game's players have NOT been cleared
	 *       out.
	 * @Post If the operation succeeds, the body contains the game's updated
	 *       client model JSON.
	 */
	@Override
	public ClientModel resetGame() throws ServerResponseException {
		serverMockModel.setLog(new MessageList(new MessageLine[1]));
		return serverMockModel.toClientModel();
	}

	/**
	 * Gets a list of canned commands.
	 * 
	 * @Pre The caller has previously logged in to the server and joined a game.
	 * @Post If the operation succeeds, returns a valid CommandList.
	 */
	@Override
	public CommandList getCommands() throws ServerResponseException {
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add("games/create");
		commandList.add("games/join");
		commandList.add("games/join");
		commandList.add("games/join");
		commandList.add("games/join");
		commandList.add("moves/buildSettlement");
		commandList.add("move/buildRoad");
		commandList.add("moves/buildRoad");
		commandList.add("moves/buildSettlement");

		return new CommandList(commandList);
	}

	/**
	 * Executes commands inputed by parameter, should return a ClientModel that
	 * matches the inputed commands.
	 * 
	 * @Pre Valid CommandList object.
	 * @Post If the operation succeeds, the passed­in command list has been
	 *       applied to the game.
	 * @Post If the operation succeeds, the body contains an updated client
	 *       model.
	 */
	@Override
	public ClientModel setCommands(CommandList commands)
			throws ServerResponseException {
		// COORDINATE THIS WITH WHO EVER IS RUNNING THE TEST SO WE CAN DECIDE
		// WHAT COMMAND TO MANUALLY EXECUTE HERE"
		int current = (serverMockModel.getTurnTracker().getCurrentTurn() + 1) % 4;
		serverMockModel.getTurnTracker().setCurrentTurn(current);
		return serverMockModel.toClientModel();
	}

	/**
	 * Returns a list of AI types, should be a canned response.
	 * 
	 * @Pre None
	 * @Post If the operation succeeds, returns a list of AI types.
	 */
	@Override
	public String[] getAITypes() throws ServerResponseException {
		String[] types = new String[1];
		types[0] = ("LARGEST ARMY");
		return types;
	}

	/**
	 * Creates and adds an AI to the game and should return true given that the
	 * AI is a valid input parameter.
	 * 
	 * @Pre There is space in the game for another player (i.e., the game is not
	 *      full).
	 * @Pre The specified "AIType" is valid (i.e., one of the values returned by
	 *      the /game/listAI method).
	 * @Post If the operation succeeds, a new AI player of the specified type
	 *       has been added to the current game.
	 */
	@Override
	public AddAIResponse addAI(AddAIParams params)
			throws ServerResponseException {
		AddAIResponse response = new AddAIResponse();
		if (params.getAIType() != "LARGEST ARMY")
			response.setResponse("Invalid Request");
		else
			response.setResponse("Success");
		return response;

	}

	/**
	 * Updates the log level and returns true given that the input parameter is
	 * not null.
	 * 
	 * @Pre The caller specifies a valid logging level. Valid values include:
	 *      SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST @ boolean
	 *      returned
	 */
	@Override
	public ChangeLogLevelResponse changeLogLevel(ChangeLogLevelParams level)
			throws ServerResponseException {
		String levels = "severe,warning,info,config,fine,finer,finest";
		ChangeLogLevelResponse response = new ChangeLogLevelResponse();
		response.setResponse("Invalid Request");
		if (level.getLogLevel() != null) {
			if (levels.contains(level.getLogLevel().toLowerCase()))
				response.setResponse("Success");
		}
		return response;
	}

	/**
	 * sends a chat message with player name added to the end, should return a
	 * ClientModel with the updated chat string.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Post The chat contains your message at the end.
	 */
	@Override
	public ClientModel sendChat(ChatMessage chatMessage)
			throws ServerResponseException {
		MessageLine newMessage = new MessageLine(chatMessage.getContent(),
				"billy");
		MessageLine[] oldMessages = serverMockModel.getChat().getLines();
		int size = oldMessages.length + 1;
		MessageLine[] newMessages = new MessageLine[size];
		System.arraycopy(oldMessages, 0, newMessages, 0, size - 1);
		newMessages[size - 1] = newMessage;
		MessageList newList = new MessageList(newMessages);
		serverMockModel.setChat(newList);

		return serverMockModel.toClientModel();
	}

	/**
	 * Used to accept trades between player, should return a ClientModel that
	 * takes into account the newly accepted trade.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre You have been offered a domestic trade.
	 * @Pre To accept the offered trade, you have the required resources.
	 * @Post If you accepted, you and the player who offered swap the specified
	 *       resources.
	 * @Post If you declined no resources are exchanged.
	 * @Post The trade offer is removed.
	 */
	@Override
	public ClientModel acceptTrade(AcceptTradeParams param)
			throws ServerResponseException {
		if (param.isWillAccept() == false)
			return serverMockModel.toClientModel();
		Player player0 = serverMockModel.getPlayers()[0];
		Player player1 = serverMockModel.getPlayers()[1];
		// Our canned trade will be 1 sheep for 2 grain
		ResourceList p0Res = player0.getResources();
		ResourceList p1Res = player1.getResources();
		int newWheat0 = p0Res.getWheat() + 2;
		p0Res.setWheat(newWheat0);
		int newSheep0 = p0Res.getSheep() - 1;
		p0Res.setSheep(newSheep0);
		int newWheat1 = p1Res.getWheat() - 2;
		p1Res.setWheat(newWheat1);
		int newSheep1 = p1Res.getSheep() + 1;
		p1Res.setSheep(newSheep1);
		serverMockModel.setTradeOffer(new TradeOffer(-1, -1, null));// what does
																	// an empty
																	// Trade
																	// offer
																	// look like
		return serverMockModel.toClientModel();
	}

	/**
	 * Discards a player's cards. Should return an correctly updated
	 * ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre The status of the client model is 'Discarding'.
	 * @Pre You have over 7 cards.
	 * @Pre You have the cards you're choosing to discard.
	 * @Post You gave up the specified resources.
	 * @Post If you're the last one to discard, the client model status changes
	 *       to 'Robbing'.
	 */
	@Override
	public ClientModel discardCards(DiscardCardsParams params)
			throws ServerResponseException {
		return serverMockModel.toClientModel();
	}

	/**
	 * Apply actions given a specific rollNumber of number. Should return an
	 * correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game
	 * @Pre It is the player's turn
	 * @Pre The client model's status is "Rolling"
	 * @Post The client model's status is now in "Discarding" or "Robbing" or
	 *       "Playing"
	 */
	@Override
	public ClientModel rollNumber(int number) throws ServerResponseException {
		return serverMockModel.toClientModel();
	}

	/**
	 * Builds a new road. Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre The road location is open.
	 * @Pre The road location is connected to another road owned by the player.
	 * @Pre The road location is not on water.
	 * @Pre You have the required resources (1 wood, 1 brick & 1 road).
	 * @Pre Setup round: Must be placed by settlement owned by the player with
	 *      no adjacent road.
	 */
	@Override
	public ClientModel buildRoad(BuildRoadParams params)
			throws ServerResponseException {
		if (!params.isFree())
			return serverMockModel.toClientModel();
		return null;
	}

	/**
	 * Builds a settlement. Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre The settlement location is open–  The settlement location is not
	 *      on water.
	 * @Pre The settlement location is connected to one of your roads except
	 *      during setup.
	 * @Pre You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep &
	 *      1 settlement).
	 * @Pre The settlement cannot be placed adjacent to another settlement
	 * @Post You lost the resources required to build a settlement (1 wood, 1
	 *       brick, 1 wheat, 1 sheep & 1 settlement).
	 * @Post The settlement is on the map at the specified location.
	 */
	@Override
	public ClientModel buildSettlement(BuildSettlementParams param)
			throws ServerResponseException {
		if (param.isFree())
			return serverMockModel.toClientModel();

		return null;
	}

	/**
	 * Builds a city. Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre The city location is where you currently have a settlement.
	 * @Pre You have the required resources (2 wheat, 3 ore & 1 city).
	 * @Post You lost the resources required to build a city (2 wheat, 3 ore & 1
	 *       city).
	 * @Post The city is on the map at the specified location.
	 * @Post You got a settlement back.
	 */
	@Override
	public ClientModel buildCity(BuildCityParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Player uses this function to offer a trade to another player. Should
	 * return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre You have the resources you are offering.
	 * @Post The trade is offered to the other player (stored in the server
	 *       model).
	 */
	@Override
	public ClientModel offerTrade(TradeOfferParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Player makes a trade with bank using specified resources. Should return
	 * an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre You have the resources you are giving.
	 * @Pre For ratios less than 4, you have the correct port for the trade.
	 * @Post The trade has been executed (the offered resources are in the bank,
	 *       and the requested resource has been received).
	 */
	@Override
	public ClientModel maritimeTrade(MaritimeTradeParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Player uses this to move robber and steal a card. Should return an
	 * correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre The robber is not being kept in the same location.
	 * @Pre If a player is being robbed (i.e., victimIndex != ­1), the player
	 *      being robbed has resource cards.
	 * @Post The robber is in the new location.
	 * @Post The player being robbed (if any) gave you one of his resource cards
	 *       (randomly selected).
	 */
	@Override
	public ClientModel robPlayer(MoveRobberParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * This effectively ends a player's turn and makes it the next player's
	 * turn. Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Post The cards in your new development card hand have been transferred
	 *       to your old development card hand.
	 * @Post It is the next player's turn.
	 */
	@Override
	public ClientModel finishTurn(UserActionParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Player uses this to buy a development card. Should return an correctly
	 * updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre You have the required resources (1 ore, 1 wheat, 1 sheep).
	 * @Pre There are development cards left in the deck.
	 * @Post You have a new card.
	 * @Post If it is a monument card, it has been added to your old development
	 *       card hand.
	 * @Post If it is a non­monument card, it has been added to your new
	 *       development card hand (unplayable this turn).
	 */
	@Override
	public ClientModel buyDevCard(UserActionParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Moves the robber to a new p;ace and possibly steals someone's resource
	 * card. Should return an correctly updated ClientModel.
	 * 
	 * @Pre The robber is not being kept in the same location.
	 * @Pre If a player is being robbed (i.e., victimIndex != ­1), the player
	 *      being robbed has resource cards.
	 * @Post The robber is in the new location.
	 * @Post The player being robbed (if any) gave you one of his resource cards
	 *       (randomly selected).
	 * @Post If applicable, "largest army" has been awarded to the player who
	 *       has played the most soldier cards.
	 * @Post You are not allowed to play other development cards during this
	 *       turn (except for monument cards, which may still be played).
	 */
	@Override
	public ClientModel playSoldierCard(MoveSoldierParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Player plays a year of plenty card, gains two of a certain kind of
	 * resource. Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre The two specified resources are in the bank.
	 * @Post You gained the two specified resources.
	 */
	@Override
	public ClientModel playYearOfPlentyCard(YearOfPlentyParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * A player will place two roads on the map. Should return an correctly
	 * updated ClientModel.
	 * 
	 * @Pre The first road location (spot1) is connected to one of your roads.
	 * @Pre The second road location (spot2) is connected to one of your roads
	 *      or to the first road location (spot1).
	 * @Pre Neither road location is on water.
	 * @Pre You have at least two unused roads.
	 * @Post You have two fewer unused roads.
	 * @Post Two new roads appear on the map at the specified locations.
	 * @Post If applicable, "longest road" has been awarded to the player with
	 *       the longest road.
	 */
	@Override
	public ClientModel playRoadBuildingCard(BuildRoadCardParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Plays a monopoly card, steals a resources from other players. Should
	 * return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Post All of the other players have given you all of their resource cards
	 *       of the specified type.
	 */
	@Override
	public ClientModel playMonopolyCard(PlayMonopolyParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Plays a monument card, which gives the player who played a victory point.
	 * Should return an correctly updated ClientModel.
	 * 
	 * @Pre Player is logged in and part of the game.
	 * @Pre It is the player's turn.
	 * @Pre Player has enough monument cards to win the game (i.e., reach 10
	 *      victory points).
	 * @Post Player gains a victory point.
	 */
	@Override
	public ClientModel playMonument(PlayMonumentParams params)
			throws ServerResponseException {
		return null;
	}

	/**
	 * Polls the server for an updated version of the model. Should return an
	 * correctly updated ClientModel.
	 * 
	 * @Pre None.
	 * @Post Gets an updated version of the model from the server. We will just
	 *       change the turn tracker
	 */

	@Override
	public ClientModel updateModel(int versionNumber)
			throws ServerResponseException {
		int currentVersion = serverMockModel.getVersion();
		if (currentVersion != versionNumber)
			return serverMockModel.toClientModel();
		return null;
	}

	@Override
	public int getGameID() {
		return 0;
	}

	@Override
	public void setGameID(int gameID) {

	}

	@Override
	public ServerModel getServerModel() {
		return this.serverMockModel;
	}

	@Override
	public int getPlayerID() {
		return 0;
	}

	@Override
	public void setPlayerID(int playerId) {

	}

	@Override
	public HashMap<Integer, ServerModel> getModelMap() {
		// TODO Auto-generated method stub
		return new HashMap<Integer, ServerModel>();
	}

	@Override
	public void setFirstGame() {

	}

}
