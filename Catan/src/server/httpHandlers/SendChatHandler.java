package server.httpHandlers;

import java.io.IOException;

import server.facade.FacadeSwitch;
import shared.communication.ChatMessage;
import shared.utils.Serializer;
import shared.utils.ServerResponseException;
import client.model.ClientModel;

import com.sun.net.httpserver.HttpExchange;

public class SendChatHandler implements IHttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		int gameID = HandlerUtil.getGameID(exchange);
		// if gameID is -1, there is no cookie so send back an error message

		if (gameID == -1) {
			HandlerUtil.sendResponse(exchange, 400, "No Game Cookie",
					String.class);

		} else {

			try {

				String inputStreamString = HandlerUtil
						.requestBodyToString(exchange);
				// otherwise put the cookie in the chatMessage
				ChatMessage chatMessage = (ChatMessage) Serializer.deserialize(
						inputStreamString, ChatMessage.class);

				FacadeSwitch.getSingleton().setGameID(gameID);
				ClientModel model = FacadeSwitch.getSingleton().sendChat(
						chatMessage);
				HandlerUtil.sendResponse(exchange, 200, model,
						ClientModel.class);
			} catch (ServerResponseException e) {
				HandlerUtil.sendResponse(exchange, 400, "Failed to send chat."
						+ e.getMessage(), String.class);
				e.printStackTrace();
			}
		}

	}
}
