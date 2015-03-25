package test;

import shared.communication.LoginResponse;
import shared.utils.Serializer;
import client.devcards.Resource;
import client.model.ClientModel;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializerTest {
	private String clientModelJson = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"location\":{\"x\":0,\"y\":-2}},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-2},\"number\":3},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"brick\",\"location\":{\"x\":0,\"y\":-1},\"number\":8},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-1},\"number\":11},{\"resource\":\"sheep\",\"location\":{\"x\":-2,\"y\":0},\"number\":10},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":12},{\"resource\":\"sheep\",\"location\":{\"x\":0,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":1,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":2,\"y\":0},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":1},\"number\":6},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":0,\"y\":1},\"number\":5},{\"resource\":\"wood\",\"location\":{\"x\":1,\"y\":1},\"number\":4},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":2},\"number\":9},{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":2},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":2}],\"roads\":[{\"owner\":0,\"location\":{\"direction\":\"S\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"direction\":\"SE\",\"x\":0,\"y\":1}},{\"owner\":0,\"location\":{\"direction\":\"SW\",\"x\":2,\"y\":0}}],\"cities\":[],\"settlements\":[{\"owner\":0,\"location\":{\"direction\":\"SW\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"direction\":\"NW\",\"x\":1,\"y\":0}}],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":3,\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":12,\"playerIndex\":0,\"name\":\"string\",\"color\":\"purple\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":13,\"playerIndex\":1,\"name\":\"test1\",\"color\":\"puce\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":14,\"playerIndex\":2,\"name\":\"test2\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":12,\"cities\":4,\"settlements\":4,\"soldiers\":0,\"victoryPoints\":1,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":15,\"playerIndex\":3,\"name\":\"test3\",\"color\":\"orange\"}],\"log\":{\"lines\":[{\"source\":\"string\",\"message\":\"string built a settlement\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"},{\"source\":\"test3\",\"message\":\"test3 built a road\"}]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":4}";
	private String clientModelExpectedString = "{\"bank\":{\"brick\":24,\"ore\":24,\"sheep\":24,\"wheat\":24,\"wood\":24},\"chat\":{\"lines\":[]},\"log\":{\"lines\":[{\"message\":\"string built a settlement\",\"source\":\"string\"},{\"message\":\"test3 built a road\",\"source\":\"test3\"},{\"message\":\"test3 built a road\",\"source\":\"test3\"},{\"message\":\"test3 built a road\",\"source\":\"test3\"}]},\"deck\":{\"yearOfPlenty\":2,\"monument\":5,\"soldier\":14,\"roadBuilding\":2,\"monopoly\":2},\"map\":{\"hexes\":[{\"location\":{\"x\":0,\"y\":-2},\"number\":0},{\"location\":{\"x\":1,\"y\":-2},\"resource\":\"ore\",\"number\":3},{\"location\":{\"x\":2,\"y\":-2},\"resource\":\"wood\",\"number\":3},{\"location\":{\"x\":-1,\"y\":-1},\"resource\":\"wheat\",\"number\":8},{\"location\":{\"x\":0,\"y\":-1},\"resource\":\"brick\",\"number\":8},{\"location\":{\"x\":1,\"y\":-1},\"resource\":\"sheep\",\"number\":9},{\"location\":{\"x\":2,\"y\":-1},\"resource\":\"wood\",\"number\":11},{\"location\":{\"x\":-2,\"y\":0},\"resource\":\"sheep\",\"number\":10},{\"location\":{\"x\":-1,\"y\":0},\"resource\":\"sheep\",\"number\":12},{\"location\":{\"x\":0,\"y\":0},\"resource\":\"sheep\",\"number\":10},{\"location\":{\"x\":1,\"y\":0},\"resource\":\"wheat\",\"number\":11},{\"location\":{\"x\":2,\"y\":0},\"resource\":\"brick\",\"number\":5},{\"location\":{\"x\":-2,\"y\":1},\"resource\":\"wood\",\"number\":6},{\"location\":{\"x\":-1,\"y\":1},\"resource\":\"brick\",\"number\":4},{\"location\":{\"x\":0,\"y\":1},\"resource\":\"ore\",\"number\":5},{\"location\":{\"x\":1,\"y\":1},\"resource\":\"wood\",\"number\":4},{\"location\":{\"x\":-2,\"y\":2},\"resource\":\"ore\",\"number\":9},{\"location\":{\"x\":-1,\"y\":2},\"resource\":\"wheat\",\"number\":6},{\"location\":{\"x\":0,\"y\":2},\"resource\":\"wheat\",\"number\":2}],\"ports\":[{\"location\":{\"x\":1,\"y\":-3},\"dir\":\"South\",\"direction\":\"S\",\"ratio\":3},{\"location\":{\"x\":-2,\"y\":3},\"dir\":\"NorthEast\",\"direction\":\"NE\",\"ratio\":3},{\"resource\":\"sheep\",\"location\":{\"x\":3,\"y\":-3},\"dir\":\"SouthWest\",\"direction\":\"SW\",\"ratio\":2},{\"location\":{\"x\":2,\"y\":1},\"dir\":\"NorthWest\",\"direction\":\"NW\",\"ratio\":3},{\"location\":{\"x\":-3,\"y\":0},\"dir\":\"SouthEast\",\"direction\":\"SE\",\"ratio\":3},{\"resource\":\"wood\",\"location\":{\"x\":-1,\"y\":-2},\"dir\":\"South\",\"direction\":\"S\",\"ratio\":2},{\"resource\":\"ore\",\"location\":{\"x\":0,\"y\":3},\"dir\":\"North\",\"direction\":\"N\",\"ratio\":2},{\"resource\":\"wheat\",\"location\":{\"x\":3,\"y\":-1},\"dir\":\"NorthWest\",\"direction\":\"NW\",\"ratio\":2},{\"resource\":\"brick\",\"location\":{\"x\":-3,\"y\":2},\"dir\":\"NorthEast\",\"direction\":\"NE\",\"ratio\":2}],\"roads\":[{\"owner\":0,\"location\":{\"hexLoc\":{\"x\":1,\"y\":0},\"dir\":\"South\",\"direction\":\"S\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"hexLoc\":{\"x\":0,\"y\":1},\"dir\":\"SouthEast\",\"direction\":\"SE\",\"x\":0,\"y\":1}},{\"owner\":0,\"location\":{\"hexLoc\":{\"x\":2,\"y\":0},\"dir\":\"SouthWest\",\"direction\":\"SW\",\"x\":2,\"y\":0}}],\"settlements\":[{\"owner\":0,\"location\":{\"hexLoc\":{\"x\":1,\"y\":0},\"dir\":\"SouthWest\",\"direction\":\"SW\",\"x\":1,\"y\":0}},{\"owner\":0,\"location\":{\"hexLoc\":{\"x\":1,\"y\":0},\"dir\":\"NorthWest\",\"direction\":\"NW\",\"x\":1,\"y\":0}}],\"cities\":[],\"radius\":3,\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"playerIndex\":0,\"playerid\":0,\"cities\":4,\"settlements\":5,\"name\":\"string\",\"color\":\"purple\",\"discarded\":false,\"monuments\":0,\"newDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"oldDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"playedDevCard\":false,\"resources\":{\"brick\":0,\"ore\":0,\"sheep\":0,\"wheat\":0,\"wood\":0},\"roads\":15,\"soldiers\":0,\"victoryPoints\":0},{\"playerIndex\":1,\"playerid\":0,\"cities\":4,\"settlements\":5,\"name\":\"test1\",\"color\":\"puce\",\"discarded\":false,\"monuments\":0,\"newDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"oldDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"playedDevCard\":false,\"resources\":{\"brick\":0,\"ore\":0,\"sheep\":0,\"wheat\":0,\"wood\":0},\"roads\":15,\"soldiers\":0,\"victoryPoints\":0},{\"playerIndex\":2,\"playerid\":0,\"cities\":4,\"settlements\":5,\"name\":\"test2\",\"color\":\"blue\",\"discarded\":false,\"monuments\":0,\"newDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"oldDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"playedDevCard\":false,\"resources\":{\"brick\":0,\"ore\":0,\"sheep\":0,\"wheat\":0,\"wood\":0},\"roads\":15,\"soldiers\":0,\"victoryPoints\":0},{\"playerIndex\":3,\"playerid\":0,\"cities\":4,\"settlements\":4,\"name\":\"test3\",\"color\":\"orange\",\"discarded\":false,\"monuments\":0,\"newDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"oldDevCards\":{\"monopoly\":0,\"monument\":0,\"roadBuilding\":0,\"soldier\":0,\"yearOfPlenty\":0},\"playedDevCard\":false,\"resources\":{\"brick\":0,\"ore\":0,\"sheep\":0,\"wheat\":0,\"wood\":0},\"roads\":12,\"soldiers\":0,\"victoryPoints\":1}],\"turnTracker\":{\"currentTurn\":0,\"status\":\"FirstRound\",\"longestRoad\":-1,\"largestArmy\":-1},\"version\":4,\"winner\":-1}";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSerialize() {
		Resource resource = new Resource("ORE");
		String jsonString = Serializer.serialize(resource);
		assertEquals("{\"name\":\"ORE\"}", jsonString);
	}

	@Test
	public void testDeserializeClientModel() {
		// String jsonString =
		// "{\"bank\":{\"brick\":\"10\",\"ore\":\"10\",\"sheep\":\"10\",\"wheat\":\"10\",\"wood\":\"10\"},\"chat\":{\"lines\":[{\"message\":\"test\",\"source\":\"test\"}]},\"log\":{\"lines\":[{\"message\":\"test\",\"source\":\"test\"}]},\"map\":{\"hexes\":[{\"location\":{\"x\":\"10\",\"y\":\"10\"},\"resource\":\"test\",\"15\":\"10\"}],\"ports\":[{\"resource\":\"test\",\"location\":{\"x\":\"10\",\"y\":\"10\"},\"direction\":\"test\",\"ratio\":\"10\"}],\"roads\":[{\"owner\":\"2\",\"location\":{\"x\":\"10\",\"y\":\"10\",\"direction\":\"test\"}}],\"settlements\":[{\"owner\":\"2\",\"location\":{\"x\":\"10\",\"y\":\"10\",\"direction\":\"test\"}}],\"cities\":[{\"owner\":\"2\",\"location\":{\"x\":\"10\",\"y\":\"10\",\"direction\":\"test\"}}],\"radius\":\"10\",\"robber\":{\"x\":\"10\",\"y\":\"10\"}},\"players\":[{\"cities\":\"15\",\"color\":\"test\",\"discarded\":\"true\",\"monuments\":\"15\",\"name\":\"test\",\"newDevCards\":{\"monopoly\":\"15\",\"monument\":\"15\",\"roadBuilding\":\"15\",\"soldier\":\"15\",\"yearOfPlenty\":\"15\"},\"oldDevCards\":{\"monopoly\":\"15\",\"monument\":\"15\",\"roadBuilding\":\"15\",\"soldier\":\"15\",\"yearOfPlenty\":\"15\"},\"playerIndex\":\"2\",\"playedDevCard\":\"true\",\"playerID\":\"10\",\"resources\":{\"brick\":\"10\",\"ore\":\"10\",\"sheep\":\"10\",\"wheat\":\"10\",\"wood\":\"10\"},\"roads\":\"15\",\"settlements\":\"10\",\"soldiers\":\"10\",\"victoryPo10s\":\"10\"}],\"tradeOffer\":{\"sender\":\"10\",\"receiver\":\"10\",\"offer\":{\"brick\":\"10\",\"ore\":\"10\",\"sheep\":\"10\",\"wheat\":\"10\",\"wood\":\"10\"}},\"turnTracker\":{\"currentTurn\":\"2\",\"status\":\"test\",\"longestRoad\":\"2\",\"largestArmy\":\"2\"},\"version\":\"2\",\"winner\":\"2\"}";
		ClientModel clientModel = Serializer
				.deserializeClientModel(clientModelJson);
		String tempString = clientModel.toString();
		assertEquals(clientModelExpectedString, tempString);
	}

	@Test
	public void testGenericDeserializer() {
		LoginResponse loginResponse = new LoginResponse("test", "testpass");
		String json = Serializer.serialize(loginResponse);
		LoginResponse deserializedLoginResponse = (LoginResponse) Serializer
				.deserialize(json, LoginResponse.class);
		assertEquals("testpass", deserializedLoginResponse.getPassword());
	}
}
