package server.httpHandlers;

import java.io.IOException;

import server.facade.ServerFacade;
import shared.communication.GameSummary;
import shared.utils.Serializer;
import shared.utils.ServerResponseException;

import com.sun.net.httpserver.HttpExchange;

public class ListGameHandler implements IHttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			GameSummary[] list = ServerFacade.getSingleton().getGameList();
			String jsonString = Serializer.serialize(list);
			HandlerUtil.sendResponse(exchange, 200, jsonString, String.class);
		} catch (ServerResponseException e) {
			HandlerUtil.sendResponse(exchange, 400, "Failed to get game", String.class);
			e.printStackTrace();
		}	
	}

}
