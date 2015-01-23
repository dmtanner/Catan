package shared.utils;

import shared.communication.CommandList;
import shared.communication.CreateGameParams;
import shared.communication.GameSummary;
import shared.communication.GamesList;
import shared.communication.JoinGameParams;
import shared.communication.LoginResponse;
import shared.communication.SaveParams;
import shared.communication.UserCredentials;
import shared.communication.LogLevels;
import shared.locations.*;
import client.model.ResourceList;
import client.model.ClientModel;

/**
 * Interface to be implemented by servers, i.e. MockServer, ServerProxy, Server.
 * All methods from API to be implemented with differing functionality
 * 
 * @author Seth White
 *
 */
public interface IServer {
	// ----NON-MOVE APIs--------
	/**
	 * This method takes in UserCredentials, which contain a username and
	 * password, and determine if the inputed username and password is a valid
	 * combination. A LoginResponse will be returned back.
	 * 
	 * @Pre Username is not null.
	 * @Pre Password is not null.
	 * @PreIf the passedÂ­in (username, password) pair is valid, 1. The server
	 *        returns an HTTP 200 success response with "Success" in the body.
	 *        2. The HTTP response headers set the catan.user cookie to contain
	 *        the identity of the loggedÂ­in player.
	 * @Post If the passed-in credentials is valid, the server returns a success
	 *       response with a cookie.
	 * @param credentials
	 *            Information containing username and password.
	 * @return
	 */
	public LoginResponse Login(UserCredentials credentials);

	/**
	 * This method will take UserCredentials and determine if the inputed
	 * username already exists. If not, a new user will be created using inputed
	 * data. A LoginResponse will be returned back.
	 * 
	 * @Pre username is not null.
	 * @Pre password is not null.
	 * @Pre The specified username is not already in use
	 * @Post If there is no existing user with the specified username, 1. A new
	 *       user account has been created with the specified username and
	 *       password. 2. The server returns an HTTP 200 success response with
	 *       "Success" in the body 3. The HTTP response headers set the
	 *       catan.user cookie to contain the identity of the logged­in player.
	 * @Post If there is already an existing user with the specified name, or
	 *       the operation fails for any other reason, 1. The server returns an
	 *       HTTP 400 error response, and the body contains an error message.
	 * @param credentials
	 *            Information containing username and password.
	 * @return
	 */
	public LoginResponse Register(UserCredentials credentials);

	/**
	 * This method will get a list containing the current list of games stored
	 * within the server.
	 * 
	 * @Pre none
	 * @Post If the operation succeeds, 1. The server returns an HTTP 200
	 *       success response. 2. The body contains a JSON array containing a
	 *       list of objects that contain information about the server's games.
	 * @Post If the operation fails, 1. The server returns an HTTP 400 error
	 *       response, and the body contains an error message.
	 * @return
	 */
	public GamesList getGameList();

	/**
	 * This method will create a game using the inputed game parameters within
	 * the server. Following this creation, a game summary will be returned to
	 * the client.
	 * 
	 * @Pre name is not null
	 * @Pre randomTiles, randomNumbers, and randomPorts contain valid boolean
	 *      values.
	 * @Post If the operation succeeds, 1. A new game with the specified
	 *       properties has been created 2. The server returns an HTTP 200
	 *       success response. 3. The body contains a JSON object describing the
	 *       newly created game.
	 * @param params
	 * @return
	 */
	public GameSummary createGame(CreateGameParams params);

	/**
	 * This method will add a user to a game that requires a player. The client
	 * will be returned a boolean stating whether they have been successfully
	 * added or rejected from the game.
	 * 
	 * @Pre 1. The user has previously logged in to the server (i.e., they have
	 *      a valid catan.user HTTP cookie). 2. The player may join the game
	 *      because 2.a They are already in the game, OR 2.b There is space in
	 *      the game to add a new player 3. The specified game ID is valid 4.
	 *      The specified color is valid (red, green, blue, yellow, puce, brown,
	 *      white, purple, orange).
	 * @Post If the operation succeeds, 1. The server returns an HTTP 200
	 *       success response with â€œSuccessâ€� in the body. 2. The player is
	 *       in the game with the specified color (i.e. calls to /games/list
	 *       method will show the player in the game with the chosen color). 3.
	 *       The server response includes the Set cookie response header setting
	 *       the catan.game HTTP cookie.
	 * @Post If the operation fails,1. The server returns an HTTP 400 error
	 *       response, and the body contains an error message.
	 * 
	 * @param params
	 * @return
	 */
	public boolean joinGame(JoinGameParams params);

	/**
	 * This method will save the game parameters to the server. The client will
	 * be returned a boolean if the game was successfully saved. Prepares params
	 * to be sent over network, then sends them to server to save a game
	 * 
	 * @pre Game id is valid.
	 * @pre Filename is not null or empty.
	 * @post A valid boolean returned.
	 */
	public boolean saveGame(SaveParams params);

	/**
	 * This method will take a filename and try and load the game matching the
	 * filename input. A boolean will be returned to the client stating whether
	 * the game was loaded.
	 * 
	 * @pre A saved game with the specified filename exists on the server.
	 * @post A valid boolean returned.
	 */
	public boolean loadGame(String fileName);

	/**
	 * Prepares the version number to be sent to the server, then retrieves
	 * current game from the server if it's different than the current version.
	 * 
	 * @pre User has logged on and joined a game, and therefore has cookies.
	 * @pre Version is a valid integer.
	 * @post A valid ClientModel returned.
	 */
	public ClientModel getCurrentGame(int version);

	/**
	 * This method resets the game within the server.
	 * 
	 * @pre none
	 * @post A valid ClientModel returned.
	 */
	public ClientModel resetGame();

	/**
	 * Retrieves all the past commands in the current game from the server.
	 * 
	 * @pre none
	 * @post A valid set of commands returned.
	 */
	public CommandList getCommands();

	/**
	 * Prepares commands to be sent to the server, then sends them to server to
	 * apply to current game.
	 * 
	 * @pre User has logged on and joined a game, and therefore has cookies.
	 * @post A valid ClientModel returned.
	 */
	public ClientModel setCommands(CommandList commands);

	/**
	 * Retrieves a list from the server of the different types of AI players
	 * available available.
	 * 
	 * @pre none
	 * @post A valid list of AI types returned.
	 */
	public String[] getAITypes();

	/**
	 * Prepares the AIType to the server, then sends it to server to create a
	 * new AI player.
	 * 
	 * @pre User has logged on and joined a game, and therefore has cookies.
	 * @pre There is space in the game for another player.
	 * @pre The AIType is a valid type returned by the getAITypes method.
	 * @post A valid boolean returned.
	 */
	public boolean addAI(String AIType);

	/**
	 * Prepares the log level to the server, then sends it to server to change
	 * the granularity of the log it keeps.
	 * 
	 * @pre Level is a valid LogLevel (SEVERE, WARNING, INFO, CONFIG, FINE,
	 *      FINER, FINEST).
	 * @post A valid boolean returned.
	 */
	public boolean changeLogLevel(LogLevels level);

	/**
	 * This method will send a signal to the server checking to see if the
	 * current version of the model has been changed.
	 * 
	 * @Pre none
	 * @Post Gets an updated version of the model from the server.
	 */
	public ClientModel poll();

	// ----MOVE APIs--------
	/**
	 * This method sends a chat message to the server. A new ClientModel is
	 * returned to the client.
	 * 
	 * @Pre none
	 * @Post chat contains the player's message at the end
	 * @param content
	 * @return
	 */
	public ClientModel sendChat(String content);

	/**
	 * This method will send a response to the server for a trade offer given to
	 * the client. A new ClientModel will be returned.
	 * 
	 * @Pre Current player has been offered a domestic trade
	 * @Pre To accept the offered trade, current player has the required
	 *      resources
	 * @Post If the current player accepted, the current player and the player
	 *       who offered swap the specified resources
	 * @Post If the current player declined no resources are exchanged
	 * @Post The trade offer is removed
	 * @param willAccept
	 */
	public ClientModel acceptTrade(boolean willAccept);

	/**
	 * This method will discard a set of cards from the client's hand.
	 * 
	 * @Pre The status of the client model is 'Discarding'
	 * @Pre The current player has over 7 cards
	 * @Pre The current player has the cards the current player is choosing to
	 *      discard.
	 * @Post Postconditions
	 * @Post The current player gives up the specified resources
	 * @Post If the current player is the last one to discard, the client model
	 *       status changes to 'Robbing'
	 * @param discardedCards
	 * @return
	 */
	public ClientModel discardCards(ResourceList discardedCards);

	/**
	 * This method will roll the dice for the client if current turn. Following
	 * the rolling of the dice, the cient's model will be updated.
	 * 
	 * @Pre It is the current player's turn
	 * @Pre The client model's status is "Rolling"
	 * @Post The client model's status is now in "Discarding" or "Robbing" or
	 *       "Playing"
	 * @param number
	 * @return
	 */
	public ClientModel rollNumber(int number);

	/**
	 * This method will build a road for the client. A new ClientModel will be
	 * returned to the client.
	 * 
	 * @Pre The road location is open
	 * @Pre The road location is connected to another road owned by the player
	 * @Pre The road location is not on water
	 * @Pre The current player has the required resources (1 wood, 1 brick or 1
	 *      road)
	 * @Pre Setup round: Must be placed by settlement owned by the player with
	 *      no adjacent road
	 * @Post The current player lost the resources required to build a road (1
	 *       wood, 1 brickÍ¾ 1 road)
	 * @Post The road is on the map at the specified location
	 * @Post If applicable, 'longest road' has been awarded to the player with
	 *       the longest road
	 * @param free
	 * @param roadLocation
	 * @return
	 */
	public ClientModel buildRoad(boolean free, EdgeLocation roadLocation);

	/**
	 * This method will build a settlement for the client. A new ClientModel
	 * will be returned to the client.
	 * 
	 * @Pre The settlement location is open The settlement location is not on
	 *      water
	 * @Pre The settlement location is connected to one of the current player's
	 *      roads except during setup
	 * @Pre The current player has the required resources (1 wood, 1 brick, 1
	 *      wheat, 1 sheepÍ¾ 1 settlement)
	 * @Pre The settlement cannot be placed adjacent to another settlement
	 * 
	 * @param free
	 * @param vertexLocation
	 * @return
	 */
	public ClientModel buildSettlement(boolean free,
			VertexLocation vertexLocation);

	/**
	 * This method will build a city for the client. A new ClientModel will be
	 * returned to the client.
	 * 
	 * updates model, builds a city in specified location
	 * 
	 * @pre the city location is where you currently have a settlement
	 * @pre you have the required resources (2 wheat, 3 ore; 1 city)
	 * @post you lost the resources required to build a city (2 wheat, 3 ore; 1
	 *       city)
	 * @post the city is on the map at the specified location
	 * @post you got a settlement back
	 */
	public ClientModel buildCity(VertexLocation vertexLocation);

	/**
	 * This method will offer a trade with another player. A new ClientModel
	 * will be returned to the client.
	 * 
	 * displays to other player a trade offer
	 * 
	 * @pre you have the resources you are offering
	 * @post the trade is offered to the other player (stored in the server
	 *       model)
	 */
	public ClientModel offerTrade(ResourceList offer, int receiver);

	/**
	 * This method will complete trade with a maritime port. A new ClientModel
	 * will be returned to the client.
	 * 
	 * trades resource cards according to a certain ratio
	 * 
	 * @pre you have the resources you are giving
	 * @pre for ratios less than 4, you have the correct port for the trade
	 * @post trade has been executed (offered resources are in the bank, and the
	 *       requested resource has been received)
	 */
	public ClientModel maritimeTrade(int ratio, String inputResource,
			String outputResource);

	/**
	 * This method will move the robber and steal from a specific victim. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @pre robber is not being kept in the same location
	 * @pre if a player is being robbed, the player being robbed has resource
	 *      cards
	 * @post robber is in the new location
	 * @post player being robbed (if any) gave you one of his resource cards
	 *       (randomly selected)
	 */
	public ClientModel robPlayer(HexLocation location, int victimIndex);

	/**
	 * This method will end the client's turn and start a new players turn. A
	 * new ClientModel will be returned to the client.
	 * 
	 * @pre none
	 * @post cards in new dev card hand have been transferred to old dev card
	 * @post it is the next player's turn
	 */
	public ClientModel finishTurn();

	/**
	 * This method removes resource cards from player's in return for a
	 * development card. A new ClientModel will be returned to the client.
	 * 
	 * @pre player has the required resources
	 * @pre there is a development card left in the bank
	 * @post player has a new development card
	 * @post if it is monument card, add it to old devcard hand else, add it to
	 *       new devcard hand
	 */
	public ClientModel buyDevCard();

	/**
	 * This method will play a soldier card from a player's hand. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @Pre player has the specific card they want to play in their
	 *      "old dev card hand"
	 * @Pre player hasn't played a dev card this turn
	 * @Pre It's the player's turn
	 * @Pre The current model status is "playing"
	 * @Pre The robber isn't being kept in the same place
	 * @Pre The player to rob has cards (-1 if you can't rob anyone)
	 * @Post The robber is in the new location
	 * @Post The player to rob gives one random resource card to the player
	 *       playing the soldier
	 */
	public ClientModel playSoldierCard(HexLocation location, int victimIndex);

	/**
	 * This method will play a year of plenty card from a player's hand. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @Pre player has the specific card they want to play in their
	 *      "old dev card hand"
	 * @Pre player hasn't played a dev card this turn
	 * @Pre It's the player's turn
	 * @Pre The current model status is "playing"
	 * @Pre The two resources you specify are in the bank
	 * @Post Player gains the two resources specified
	 */
	public ClientModel playYearOfPlentyCard(String resource1, String resource2);

	/**
	 * This method will play a road building card from a player's hand. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @Pre player has the specific card they want to play in their
	 *      "old dev card hand"
	 * @Pre player hasn't played a dev card this turn
	 * @Pre It's the player's turn
	 * @Pre The current model status is "playing"
	 * @Pre The first road location is connected to one of your roads
	 * @Pre The second road location is connected to one of your roads on the
	 *      previous location
	 * @Pre Neither location is on water
	 * @Pre Player has two roads
	 * @Post Player uses two roads
	 * @Post The map lists the roads correctly
	 */
	public ClientModel playRoadBuildingCard(EdgeLocation spot1,
			EdgeLocation spot2);

	/**
	 * This method plays a monopoly card from the player's hand. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @Pre player has the specific card they want to play in their
	 *      "old dev card hand"
	 * @Pre player hasn't played a dev card this turn
	 * @Pre It's the player's turn
	 * @Pre The current model status is "playing"
	 * @Post All other players lose the resource card type chosen
	 * @Post The player of the card gets an equal number of that resource type
	 */
	public ClientModel playMonopolyCard(String resource);

	/**
	 * This method plays a monument card from the player's hand. A new
	 * ClientModel will be returned to the client.
	 * 
	 * @Pre none
	 * @Post current player gains a victory point
	 *
	 */
	public ClientModel playMonument();
}
