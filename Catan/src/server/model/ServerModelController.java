package server.model;

import java.util.ArrayList;

import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.*;
import client.data.UserPlayerInfo;

/**
 * Handles all "CanDo" methods and provides access to the client model
 * 
 * <pre>
 * <b>Domain:</b>
 * -clientModel:ClientModel
 * </pre>
 * 
 * @author Seth White
 *
 */
public class ServerModelController {

	ServerModel model;

	public ServerModelController(ServerModel model) {
		this.model = model;
	}

	public CatanColor getPlayerColor(int playerIndex) {
		switch (model.getPlayers()[playerIndex].getColor()) {
		case "red":
			return CatanColor.RED;
		case "green":
			return CatanColor.GREEN;
		case "blue":
			return CatanColor.BLUE;
		case "yellow":
			return CatanColor.YELLOW;
		case "puce":
			return CatanColor.PUCE;
		case "brown":
			return CatanColor.BROWN;
		case "white":
			return CatanColor.WHITE;
		case "purple":
			return CatanColor.PURPLE;
		case "orange":
			return CatanColor.ORANGE;
		}
		return null;
	}

	public HexType stringToHexType(String name) {
		if (name != null) {
			switch (name.toLowerCase()) {
			case "ore":
				return HexType.ORE;
			case "lumber":
			case "wood":
				return HexType.WOOD;
			case "sheep":
				return HexType.SHEEP;
			case "wheat":
			case "grain":
				return HexType.WHEAT;
			case "brick":
				return HexType.BRICK;
			case "water":
				return HexType.WATER;
			case "desert":
				return HexType.DESERT;
			}
		}
		return null;
	}

	/**
	 * Check if it is the given player's turn. If it is the player's turn,
	 * return true.
	 * 
	 * @pre none
	 * @post Boolean if it is the player's turn or not
	 * @param playerIndex
	 *            Player being queried.
	 * @return
	 */
	public boolean isPlayerTurn(int playerIndex) {
		if (model.getTurnTracker().getCurrentTurn() == playerIndex) {
			return true;
		}
		return false;
	}

	/**
	 * Check to see if the road is able to be placed at the location inside the
	 * given road
	 * 
	 * @param road
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean legitRoadPlacement(Road road) {
		EdgeLocation edgeLocation = road.getLocation();
		HexLocation hexLocation = edgeLocation.getHexLoc();
		hexLocation.getNeighborLoc(edgeLocation.getDir());
		return false;
	}

	/**
	 * tests if the player can roll
	 * 
	 * @Pre it is the current turn of the player attempting to roll
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canRollNumber(int playerIndex) {
		if (isPlayerTurn(playerIndex)
				&& model.getTurnTracker().getStatus().equals("Rolling")) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the player can build a road
	 * 
	 * @Pre it is the current turn of the player attempting to build a road
	 * @Pre player has the required resources to buy the road
	 * @Pre the road is attached to another road or building
	 * @Pre the road is not over another road
	 * @Pre player has available road piece
	 * @Post result: a boolean reporting success/fail
	 */
	// our implementation forces the player to build a settlement first
	public boolean canBuildRoad(int playerIndex, Road road, boolean isFree,
			boolean setupPhase) {
		ResourceList requiredResourceList = new ResourceList(1, 0, 0, 0, 1);
		/*
		 * Check Pre-conditions. I.e. check if it is the current player's turn,
		 * if the player has the required resources, the road is not covering
		 * another road, the road is attached to a road or a building, and if
		 * the player has an available road piece.
		 */

		if (isPlayerTurn(playerIndex)
				&& (playerHasResources(playerIndex, requiredResourceList) || isFree)
				&& !roadExists(road)
				&& (hasConnectingBuilding(road) || hasConnectingRoad(road))
				&& (playerHasAvailableRoadPiece(playerIndex) || setupPhase)
				&& (model.getTurnTracker().getStatus().equals("Playing") || setupPhase)
				&& !roadOnWater(road)) {
			return true;
		}
		return false;
	}

	private boolean roadOnWater(Road road) {
		if (road.getLocation().getHexLoc().isWater()
				&& road.getLocation().getHexLoc()
						.getNeighborLoc(road.getLocation().getDir()).isWater()) {
			return true;
		}
		return false;
	}

	public boolean canBuildSecondRoad(int playerIndex, Road road,
			boolean setupPhase) {
		/*
		 * Check Pre-conditions. I.e. check if it is the current player's turn,
		 * if the player has the required resources, the road is not covering
		 * another road, the road is attached to a road or a building, and if
		 * the player has an available road piece.
		 */

		if (isPlayerTurn(playerIndex)
				&& !roadExists(road)
				&& (connectedToSecondSettlement(road))
				&& (playerHasAvailableRoadPiece(playerIndex) || setupPhase)
				&& (model.getTurnTracker().getStatus().equals("Playing") || setupPhase)
				&& !roadOnWater(road)) {
			return true;
		}
		return false;
	}

	public boolean canBuyRoad() {
		int playerIndex = UserPlayerInfo.getSingleton().getPlayerIndex();
		ResourceList requiredResourceList = new ResourceList(1, 0, 0, 0, 1);
		if (isPlayerTurn(playerIndex)
				&& playerHasResources(playerIndex, requiredResourceList)
				&& model.getPlayers()[playerIndex].getRoads() > 0)
			return true;
		return false;
	}

	private boolean playerHasResources(int playerIndex,
			ResourceList resourceList) {
		if (model.getPlayers()[playerIndex].getResources().contains(
				resourceList)) {
			return true;
		}
		return false;
	}

	private boolean roadExists(Road newRoad) {
		for (Road existingRoad : model.getMap().getRoads()) {
			if (!existingRoad.isNotEquivalent(newRoad)) {
				return true;
			}
		}
		return false;
	}

	private int roadOwner(Road road) {
		/*
		 * Find owner of road given inputed road. If none, return -1 index.
		 * Else, return index of owner.
		 */
		for (Road existingRoad : model.getMap().getRoads()) {
			if (!existingRoad.isNotEquivalent(road)) {
				return existingRoad.getOwner();
			}
		}
		return -1;
	}

	private boolean hasConnectingBuilding(Road road) {
		HexLocation platformHex = road.getLocation().getHexLoc();

		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.getOwner() == road.getOwner()) {
				if (buildingExistsForRoad(settlement, road, platformHex)) {
					return true;
				}
			}
		}

		for (VertexObject city : model.getMap().getCities()) {
			if (city.getOwner() == road.getOwner()) {
				if (buildingExistsForRoad(city, road, platformHex)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean connectedToSecondSettlement(Road road) {
		HexLocation platformHex = road.getLocation().getHexLoc();

		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.getOwner() == road.getOwner()) {
				if (buildingExistsForRoad(settlement, road, platformHex)
						&& !roadTouchingNewSettlement(settlement)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean buildingExistsForRoad(VertexObject existingBuilding,
			Road road, HexLocation platformHex) {
		VertexObject testObject = null;
		VertexLocation testLocation = null;
		switch (road.getLocation().getDir()) {
		case NorthWest:
			testLocation = new VertexLocation(platformHex,
					VertexDirection.NorthWest);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex, VertexDirection.West);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		case North:
			testLocation = new VertexLocation(platformHex,
					VertexDirection.NorthEast);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex,
					VertexDirection.NorthWest);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		case NorthEast:
			testLocation = new VertexLocation(platformHex,
					VertexDirection.NorthEast);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex, VertexDirection.East);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		case SouthEast:
			testLocation = new VertexLocation(platformHex,
					VertexDirection.SouthEast);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex, VertexDirection.East);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		case South:
			testLocation = new VertexLocation(platformHex,
					VertexDirection.SouthEast);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex,
					VertexDirection.SouthWest);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		case SouthWest:
			testLocation = new VertexLocation(platformHex, VertexDirection.West);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			testLocation = new VertexLocation(platformHex,
					VertexDirection.SouthWest);
			testObject = new VertexObject(road.getOwner(), testLocation);
			if (existingBuilding.isEquivalent(testObject)) {
				return true;
			}
			break;
		}
		return false;
	}

	private boolean hasConnectingRoad(Road road) {
		HexLocation roadHexLoc = road.getLocation().getHexLoc();
		EdgeDirection roadEdgeDir = road.getLocation().getDir();
		HexLocation roadNeighbor = roadHexLoc.getNeighborLoc(roadEdgeDir);

		EdgeLocation testLocation = null;
		Road testRoad = null;

		switch (roadEdgeDir) {
		case NorthWest:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.North);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.SouthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.NorthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor, EdgeDirection.South);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		case North:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.NorthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.NorthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.SouthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.SouthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		case NorthEast:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.North);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.SouthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor, EdgeDirection.South);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.NorthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		case SouthWest:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.South);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.NorthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor, EdgeDirection.North);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.SouthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		case South:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.SouthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.SouthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.NorthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.NorthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		case SouthEast:
			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.South);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadHexLoc, EdgeDirection.NorthEast);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor, EdgeDirection.North);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}

			testLocation = new EdgeLocation(roadNeighbor,
					EdgeDirection.SouthWest);
			testRoad = new Road(road.getOwner(), testLocation);
			if (road.getOwner() == roadOwner(testRoad)) {
				return true;
			}
			break;
		}

		return false;
	}

	private boolean playerHasAvailableRoadPiece(int playerIndex) {
		if (model.getPlayers()[playerIndex].getRoads() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * tests if the player can build a city
	 * 
	 * @Pre it is the current turn of the player attempting to build a city
	 * @Pre player has the required resources to buy the city
	 * @Pre the city is replacing an existing settlement
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canBuildCity(VertexObject city) {
		ResourceList resourceList = new ResourceList(0, 3, 0, 2, 0);
		if (isPlayerTurn(city.getOwner())
				&& playerHasResources(city.getOwner(), resourceList)
				&& preexistingSettlement(city, false)
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;
	}

	public boolean canBuyCity() {
		ResourceList resourceList = new ResourceList(0, 3, 0, 2, 0);
		int playerIndex = UserPlayerInfo.getSingleton().getPlayerIndex();
		if (isPlayerTurn(playerIndex)
				&& playerHasResources(playerIndex, resourceList)
				&& model.getPlayers()[playerIndex].getCities() > 0)
			return true;
		return false;
	}

	/**
	 * Returns false if adjacent buildings exist, name can be changed, wasn't
	 * feeling very creative
	 * 
	 * @param existingBuilding
	 * @param newBuilding
	 * @param platformHex
	 * @return
	 */
	private boolean hasAdjacentVertexObject(VertexObject existingBuilding,
			VertexObject newBuilding, HexLocation platformHex) {
		VertexObject testBuilding = null;
		HexLocation neighborHex = null;
		VertexLocation testLocation = null;
		switch (newBuilding.getLocation().getDir()) {
		case NorthWest:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.NorthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.West);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthWest);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.NorthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		case NorthEast:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.NorthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.East);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthEast);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.NorthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		case East:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.NorthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.SouthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthEast);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.SouthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		case SouthEast:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.East);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.SouthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthEast);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.SouthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		case SouthWest:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.West);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.SouthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthWest);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.SouthEast);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		case West:
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.NorthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			testLocation = new VertexLocation(newBuilding.getLocation()
					.getHexLoc(), VertexDirection.SouthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			neighborHex = newBuilding.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthWest);
			testLocation = new VertexLocation(neighborHex,
					VertexDirection.SouthWest);
			testBuilding = new VertexObject(newBuilding.getOwner(),
					testLocation);
			if (existingBuilding.isEquivalent(testBuilding)) {
				return true;
			}
			break;
		}

		return false;
	}

	/**
	 * Returns true if there are no adjacent buildings to the proposed new
	 * settlement
	 * 
	 * @param newSettlement
	 * @return
	 */
	private boolean noAdjacentBuildings(VertexObject newSettlement) {
		HexLocation platformHex = newSettlement.getLocation().getHexLoc();
		for (VertexObject existingSettlement : model.getMap().getSettlements()) {
			if (hasAdjacentVertexObject(existingSettlement, newSettlement,
					platformHex)) {
				return false;
			}
		}
		for (VertexObject existingCity : model.getMap().getCities()) {
			if (hasAdjacentVertexObject(existingCity, newSettlement,
					platformHex)) {
				return false;
			}
		}
		return true;
	}

	private boolean roadTouchingNewSettlement(VertexObject newSettlement) {
		VertexDirection newSettlementDirection = newSettlement.getLocation()
				.getDir();

		for (Road existingRoad : model.getMap().getRoads()) {
			if (existingRoad.getOwner() == newSettlement.getOwner()) {
				HexLocation newSettlementLocation = newSettlement.getLocation()
						.getHexLoc();
				HexLocation roadNeighbor = null;
				EdgeLocation testLocation = null;
				Road testRoad = null;

				switch (newSettlementDirection) {
				case NorthWest:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.NorthWest);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.NorthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.North);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.NorthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				case East:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.NorthEast);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.NorthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.SouthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.South);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				case NorthEast:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.NorthEast);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.NorthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.North);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.NorthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				case SouthEast:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.SouthEast);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.South);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.SouthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.SouthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				case West:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.NorthWest);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.NorthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.SouthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.South);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				case SouthWest:
					roadNeighbor = newSettlementLocation
							.getNeighborLoc(EdgeDirection.SouthWest);
					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.SouthWest);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(newSettlementLocation,
							EdgeDirection.South);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}

					testLocation = new EdgeLocation(roadNeighbor,
							EdgeDirection.SouthEast);
					testRoad = new Road(existingRoad.getOwner(), testLocation);
					if (!existingRoad.isNotEquivalent(testRoad)) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * tests if the player can build a settlement
	 * 
	 * @Pre it is the current turn of the player attempting to build the
	 *      settlement
	 * @Pre player has the required resources to buy the settlement
	 * @Pre Settlement is two edges away from all other settlements
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canBuildSettlement(VertexObject settlement, boolean isFree,
			boolean setupPhase) {
		int playerIndex = settlement.getOwner();
		ResourceList resourceList = new ResourceList(1, 0, 1, 1, 1);
		if (isPlayerTurn(playerIndex)
				&& (playerHasResources(playerIndex, resourceList) || isFree)
				&& !preexistingBuilding(settlement, true)
				&& noAdjacentBuildings(settlement)
				&& (roadTouchingNewSettlement(settlement) || setupPhase)
				&& (model.getTurnTracker().getStatus().equals("Playing") || setupPhase)
				&& !settlementOnWater(settlement)) {
			return true;
		}
		return false;
	}

	public ArrayList<ResourceType> getAdjacentResources(VertexObject settlement) {
		HexLocation location1 = null;
		HexLocation location2 = null;
		HexLocation location3 = null;

		switch (settlement.getLocation().getDir()) {
		case NorthWest:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthWest);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.North);
			break;
		case NorthEast:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthEast);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.North);
			break;
		case East:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthEast);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthEast);
			break;
		case SouthEast:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.South);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthEast);
			break;
		case SouthWest:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.South);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthWest);
			break;
		case West:
			location1 = settlement.getLocation().getHexLoc();
			location2 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.NorthWest);
			location3 = settlement.getLocation().getHexLoc()
					.getNeighborLoc(EdgeDirection.SouthWest);
		}
		ArrayList<ResourceType> resourceList = new ArrayList<ResourceType>();
		for (Hex hex : model.getMap().getHexes()) {
			if (hex.getLocation().equals(location1)
					|| hex.getLocation().equals(location2)
					|| hex.getLocation().equals(location3)) {
				if (!hex.getResource().toLowerCase().equals("desert")) {
					resourceList.add(ResourceType.valueOf(hex.getResource()
							.toUpperCase()));
				}
			}
		}
		return resourceList;
	}

	private boolean settlementOnWater(VertexObject settlement) {
		switch (settlement.getLocation().getDir()) {
		case NorthWest:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.NorthWest).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.North).isWater())
				return true;
			break;
		case NorthEast:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.NorthEast).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.North).isWater())
				return true;
			break;
		case East:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.NorthEast).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.SouthEast).isWater())
				return true;
			break;
		case SouthEast:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.South).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.SouthEast).isWater())
				return true;
			break;
		case SouthWest:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.South).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.SouthWest).isWater())
				return true;
			break;
		case West:
			if (settlement.getLocation().getHexLoc().isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.NorthWest).isWater()
					&& settlement.getLocation().getHexLoc()
							.getNeighborLoc(EdgeDirection.SouthWest).isWater())
				return true;
		}
		return false;
	}

	public boolean canBuySettlement() {
		ResourceList resourceList = new ResourceList(1, 0, 1, 1, 1);
		int playerIndex = UserPlayerInfo.getSingleton().getPlayerIndex();
		if (isPlayerTurn(playerIndex)
				&& playerHasResources(playerIndex, resourceList)
				&& model.getPlayers()[playerIndex].getSettlements() > 0)
			return true;
		return false;
	}

	private boolean preexistingSettlement(VertexObject building,
			boolean dontCheckOwner) {
		boolean result = false;
		int buildingOwner = building.getOwner();
		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.isEquivalent(building)) {
				if (!dontCheckOwner)
					result = (settlement.getOwner() == buildingOwner);
				else
					result = true;
			}
		}
		for (VertexObject city : model.getMap().getCities()) {
			if (city.isEquivalent(building)) {
				result = false;
			}
		}
		return result;

	}

	/**
	 * checks that a settlement exists on the proposed city spot
	 * 
	 * @param building
	 * @param dontCheckOwner
	 * @return
	 */
	private boolean preexistingBuilding(VertexObject building,
			boolean dontCheckOwner) {

		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.isEquivalent(building)) {
				return true;
			}
		}

		for (VertexObject city : model.getMap().getCities()) {
			if (city.isEquivalent(building)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * checks the owner of a settlement at a location, returns -1 if no one
	 * 
	 * @param location
	 * @return
	 */
	public int settlementOwner(VertexObject location) {

		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.isEquivalent(location)) {
				return settlement.getOwner();
			}
		}

		return -1;
	}

	/**
	 * checks the owner of a city at a location, returns -1 if no one
	 * 
	 * @param location
	 * @return
	 */
	public int cityOwner(VertexObject location) {

		for (VertexObject city : model.getMap().getCities()) {
			if (city.isEquivalent(location)) {
				return city.getOwner();
			}
		}

		return -1;
	}

	/**
	 * tests if the player can discard cards
	 * 
	 * @Pre the player has more than 7 cards after a 7 is rolled
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canDiscardCards(int playerIndex) {
		if (model.getPlayers()[playerIndex].getResources().totalResourceCount() > 7
				&& !model.getPlayers()[playerIndex].alreadyDiscarded()
				&& model.getTurnTracker().getStatus().equals("Discarding")) {
			return true;
		}
		return false;
	}

	/**
	 * tests if the player can offer a player trade
	 * 
	 * @Pre It is the offering player's turn, or the player is counter-offering
	 *      after the current player has offered a trade
	 * @Pre The player has the resources to offer
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canOfferTrade(int playerIndex, ResourceList resourceList) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getResources().contains(
						resourceList)
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;

	}

	/**
	 * /** tests if the player can accept a player trade
	 * 
	 * @Pre it is the current turn of the player attempting to accept the trade
	 * @Pre the player has the offered resources
	 * @Pre the player has the asked for resources
	 * @Post result: a boolean reporting success/fail
	 *
	 * @param playerIndex
	 *            Player being offered a trade.
	 * @param resourceList
	 *            Current trade proposal.
	 * @return
	 */
	public boolean canAcceptTrade(int playerIndex, ResourceList resourceList) {
		if (model.getPlayers()[playerIndex].getResources().contains(
				resourceList)
				&& model.getTradeOffer().getReceiver() == playerIndex) {
			return true;
		}
		return false;
	}

	/**
	 * tests if the player can maritime trade
	 * 
	 * @Pre it is the current turn of the player attempting to maritime trade
	 * @Pre the player has a settlement near a port
	 * @Pre the player has the required ratio of resources
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canMaritimeTrade(int playerIndex, ResourceType resource,
			int ratioNumber) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getResources().ofAKind(
						resource, ratioNumber)
				&& model.getTurnTracker().getStatus().equals("Playing")) {

			if (ratioNumber == 2 && playerOnResourcePort(playerIndex, resource)) {
				return true;
			} else if (ratioNumber == 3 && playerOnNormalPort(playerIndex)) {
				return true;
			} else if (ratioNumber == 4)
				return true;
		}
		return false;
	}

	// Special
	private boolean playerOnNormalPort(int playerIndex) {
		/*
		 * Check if any settlement is connected to a normal port. If so, then
		 * return true.
		 */
		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (playerIndex == settlement.getOwner()
					&& buildingOnNormalPort(settlement)) {
				return true;
			}
		}

		/*
		 * Check if any city is connected to a normal port. If so, then return
		 * true.
		 */
		for (VertexObject city : model.getMap().getCities()) {
			if (playerIndex == city.getOwner() && buildingOnNormalPort(city)) {
				return true;
			}
		}

		return false;
	}

	// Special
	private boolean buildingOnNormalPort(VertexObject building) {
		/*
		 * Loop through the list of ports to find any ports without resources
		 * connected to the building.
		 */
		for (Port port : model.getMap().getPorts()) {
			if (port.getResource() == null) {

				/*
				 * Create a road that matches the edgeLocation and direction of
				 * the port. This road should have a connecting building that
				 * matches the owner, if this is the case, then you can access
				 * the port.
				 */
				EdgeLocation edgeLocation = new EdgeLocation(
						port.getLocation(), port.getDir());
				Road road = new Road(building.getOwner(), edgeLocation);
				boolean connect = hasConnectingBuilding(road);

				if (connect) {
					return true;
				}
			}
		}
		return false;
	}

	// Special
	private boolean playerOnResourcePort(int playerIndex, ResourceType resource) {
		/*
		 * Check if any settlement is connected to a normal port. If so, then
		 * return true.
		 */
		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (playerIndex == settlement.getOwner()
					&& buildingOnResourcePort(playerIndex, settlement, resource)) {
				return true;
			}
		}

		/*
		 * Check if any city is connected to a normal port. If so, then return
		 * true.
		 */
		for (VertexObject city : model.getMap().getCities()) {
			if (playerIndex == city.getOwner()
					&& buildingOnResourcePort(playerIndex, city, resource)) {
				return true;
			}
		}

		return false;
	}

	// Special
	private boolean buildingOnResourcePort(int playerIndex,
			VertexObject building, ResourceType resource) {
		/*
		 * Loop through the list of ports to find any ports with a matching
		 * resource connected to the building.
		 */
		for (Port port : model.getMap().getPorts()) {
			if (port.getResource() != null
					&& port.getResource().equals(resource.toString())) {

				/*
				 * Create a road that matches the edgeLocation and direction of
				 * the port. This road should have a connecting building that
				 * matches the owner, if this is the case, then you can access
				 * the port.
				 */
				EdgeLocation edgeLocation = new EdgeLocation(
						port.getLocation(), port.getDir());
				Road road = new Road(building.getOwner(), edgeLocation);
				boolean connect = hasConnectingBuilding(road);

				if (connect) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canMoveRobber(HexLocation hexLocation) {
		if (!model.getMap().getRobber().equals(hexLocation)
				&& !hexLocation.isWater()) {
			return true;
		}
		return false;
	}

	public boolean playerTouchingRobber(int robbedPlayer,
			HexLocation robberLocation) {
		VertexObject testObject = null;
		VertexLocation testLocation = null;

		// check settlements
		for (VertexObject settlement : model.getMap().getSettlements()) {
			if (settlement.getOwner() == robbedPlayer) {
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.NorthEast);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.NorthWest);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.West);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.SouthEast);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.SouthWest);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.East);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (settlement.isEquivalent(testObject)) {
					return true;
				}
			}
		}

		// check cities
		for (VertexObject city : model.getMap().getCities()) {
			if (city.getOwner() == robbedPlayer) {
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.NorthEast);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.NorthWest);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.West);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.SouthEast);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.SouthWest);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
				testLocation = new VertexLocation(robberLocation,
						VertexDirection.East);
				testObject = new VertexObject(robbedPlayer, testLocation);
				if (city.isEquivalent(testObject)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * tests if the player can rob a player
	 * 
	 * @Pre it is the current turn of the player attempting to rob
	 * @Pre the player has just rolled a 7 or the player has just played a
	 *      soldier card
	 * @Pre the victim player is adjacent to the hex the robber is on
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canRobPlayer(HexLocation robberLocation, int robbingPlayer,
			int robbedPlayer) {
		if (isPlayerTurn(robbingPlayer)
				&& model.getTurnTracker().getStatus().equals("Robbing")
				&& playerTouchingRobber(robbedPlayer, robberLocation)
				&& model.getPlayers()[robbedPlayer].getResources()
						.totalResourceCount() > 0) {
			return true;
		}
		return false;

	}

	/**
	 * tests if the player can buy a dev card
	 * 
	 * @Pre it is the current turn of the player attempting to buy the devCard
	 * @Pre player has the required resources to buy the card
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canBuyDevCard(int playerIndex) {
		ResourceList resourceList = new ResourceList(0, 1, 1, 1, 0);
		if (isPlayerTurn(playerIndex)
				&& playerHasResources(playerIndex, resourceList)
				&& model.getDeck().hasDevCard()
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;

	}

	/**
	 * tests if the player can play a soldier card
	 * 
	 * @Pre it is the current turn of the player attempting to play the card
	 * @Pre Current player has the card
	 * @Pre Current player has not already played a devCard this turn
	 * @Pre this dev card was not purchased this turn
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canPlaySoldierCard(HexLocation hexLocation,
			int robbingPlayer, int robbedPlayer) {
		if (isPlayerTurn(robbingPlayer)
				&& model.getPlayers()[robbingPlayer].getOldDevCards()
						.getSoldier() > 0
				&& !model.getPlayers()[robbingPlayer].hasPlayedDevCard()
				&& canRobPlayer(hexLocation, robbingPlayer, robbedPlayer)
				&& !model.getMap().getRobber().equals(hexLocation)) {

			return true;
		}
		return false;
	}

	/**
	 * tests if the player can play a year of plenty card
	 * 
	 * @Pre it is the current turn of the player attempting to play the card
	 * @Pre Current player has the card
	 * @Pre Current player has not already played a devCard this turn
	 * @Pre this dev card was not purchased this turn
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canPlayYearOfPlentyCard(int playerIndex,
			ResourceList requestedResources) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getOldDevCards()
						.getYearOfPlenty() > 0
				&& !model.getPlayers()[playerIndex].hasPlayedDevCard()
				&& model.getBank().contains(requestedResources)
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;

	}

	/**
	 * tests if the player can play a road building card
	 * 
	 * @Pre it is the current turn of the player attempting to play the card
	 * @Pre Current player has the card
	 * @Pre Current player has not already played a devCard this turn
	 * @Pre this dev card was not purchased this turn
	 * @Post result: a boolean reporting success/fail
	 */
	public boolean canPlayRoadBuildingCard(int playerIndex) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getOldDevCards()
						.getRoadBuilding() > 0
				&& !model.getPlayers()[playerIndex].hasPlayedDevCard()
				&& model.getTurnTracker().getStatus().equals("Playing")
				&& model.getPlayers()[playerIndex].getRoads() >= 2) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the player can play a monument card.
	 * 
	 * @param playerIndex
	 * @return
	 */
	public boolean canPlayMonumentCard(int playerIndex) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getOldDevCards()
						.getMonument() > 0
				&& (model.getPlayers()[playerIndex].getVictoryPoints() + model
						.getPlayers()[playerIndex].getOldDevCards()
						.getMonument()) >= 10
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the player can play a monopoly card.
	 * 
	 * @param playerIndex
	 * @return
	 */
	public boolean canPlayMonopolyCard(int playerIndex) {
		if (isPlayerTurn(playerIndex)
				&& model.getPlayers()[playerIndex].getOldDevCards()
						.getMonopoly() > 0
				&& !model.getPlayers()[playerIndex].hasPlayedDevCard()
				&& model.getTurnTracker().getStatus().equals("Playing")) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the player can finish their turn.
	 * 
	 * @param playerIndex
	 *            Player ending turn.
	 * @return True if player can end turn.
	 */
	public boolean canFinishTurn(int playerIndex) {
		if (isPlayerTurn(playerIndex)) {
			return true;
		}
		return false;
	}

	public ServerModel getServerModel() {
		return model;
	}

	public PortType stringToPortType(String resource) {
		if (resource == null) {
			return PortType.THREE;
		}
		switch (resource.toLowerCase()) {
		case "ore":
			return PortType.ORE;
		case "lumber":
		case "wood":
			return PortType.WOOD;
		case "sheep":
			return PortType.SHEEP;
		case "wheat":
		case "grain":
			return PortType.WHEAT;
		case "brick":
			return PortType.BRICK;
		case "three":
			return PortType.THREE;
		}
		return null;
	}

	/**
	 * checks to see if the player has the largest army
	 * 
	 * @param playerIndex
	 */
	public boolean hasLargestArmy(int playerIndex) {

		int largestArmyPlayerIndex = model.getTurnTracker().getLargestArmy();
		return (largestArmyPlayerIndex == playerIndex);

	}

	/**
	 * checks to see if player has the longest road
	 * 
	 * @param playerIndex
	 * @return
	 */
	public boolean hasLongestRoad(int playerIndex) {

		int longestRoadPlayerIndex = model.getTurnTracker().getLongestRoad();
		return (longestRoadPlayerIndex == playerIndex);

	}
}
