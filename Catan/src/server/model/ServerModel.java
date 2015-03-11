package server.model;

import client.data.UserPlayerInfo;
import shared.utils.Serializer;

/**
 * Contains all data relevant to the game
 * 
 * <pre>
 * <b>Domain:</b>
 * -bank:ResourceList
 * -chat:MessageList
 * -log:MessageList
 * -map:Map
 * -players:[Player]
 * -tradeOffer:TradeOffer
 * -turnTracker:TurnTracker
 * -version:int
 * -winner:int
 * </pre>
 * 
 * @author Seth White
 *
 */
public class ServerModel {
	private ResourceList bank;
	private MessageList chat;
	private MessageList log;
	private Deck deck;
	private Map map;
	private Player[] players;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private int version = -1;
	private int winner;
	public static Notifier notifier = null;

	private static ServerModel clientModel = null;// singleton instance of
													// ClientModel

	/**
	 * Default constructor
	 */
	public ServerModel() {// empty constructor defeats instantiation

	}

	public static Notifier getNotifier() {
		if (notifier == null) {
			notifier = new Notifier();
		}
		return notifier;
	}

	public static ServerModel getSingleton() {// returns the singleton
		if (clientModel == null) {
			clientModel = new ServerModel();
		}
		return clientModel;
	}

	public boolean hasFourPlayers() {
		boolean hasFourPlayers = true;
		for (Player player : this.getPlayers()) {
			if (player == null) {
				hasFourPlayers = false;
			}
		}
		return hasFourPlayers;
	}

	public ResourceList getBank() {
		return bank;
	}

	public void setBank(ResourceList bank) {
		this.bank = bank;
	}

	public MessageList getChat() {
		return chat;
	}

	public void setChat(MessageList chat) {
		this.chat = chat;
	}

	public MessageList getLog() {
		return log;
	}

	public void setLog(MessageList log) {
		this.log = log;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}

	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	private void setUserPlayerInfoIndex(ServerModel clientModel) {
		for (Player player : clientModel.getPlayers()) {
			UserPlayerInfo upi = UserPlayerInfo.getSingleton();
			if (player != null && player.getName().equals(upi.getName())) {
				UserPlayerInfo.getSingleton().setPlayerIndex(
						player.getPlayerIndex());
			}
		}
	}

	public void setClientModel(ServerModel clientModel) {
		ServerModel.clientModel = clientModel;
		setUserPlayerInfoIndex(clientModel);
		notifier.modelUpdated();
	}

	public String toString() {
		// TODO Need to add serialize method to Serializer such that it can
		// serialize a server.model.ServerModel
		
		//return Serializer.serializeClientModel(this);
		return "TODO";
	}

}
