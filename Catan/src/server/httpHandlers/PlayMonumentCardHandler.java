package server.httpHandlers;

import java.io.IOException;

import server.facade.FacadeSwitch;
import shared.communication.PlayMonumentParams;
import shared.utils.Serializer;
import shared.utils.ServerResponseException;
import client.model.ClientModel;

import com.sun.net.httpserver.HttpExchange;

public class PlayMonumentCardHandler implements IHttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		/*
		 * Grab the information from the request sent over.
		 */

		int gameID = HandlerUtil.getGameID(exchange);

		/*
		 * If gameID sent from the server is -1, there is no cookie sent from
		 * the server. Therefore, send back an "400" error message to the
		 * client.
		 */
		if (gameID == -1) {
			HandlerUtil.sendResponse(exchange, 400, "No Game Cookie",
					String.class);
		} else {

			try {
				String inputStreamString = HandlerUtil
						.requestBodyToString(exchange);
				PlayMonumentParams playMonumentParam = (PlayMonumentParams) Serializer
						.deserialize(inputStreamString,
								PlayMonumentParams.class);

				ClientModel model = FacadeSwitch.getSingleton().playMonument(
						playMonumentParam);
				HandlerUtil.sendResponse(exchange, 200, model,
						ClientModel.class);
			} catch (ServerResponseException e) {
				HandlerUtil.sendResponse(exchange, 400,
						"Failed to play monument card." + e.getMessage(),
						String.class);
				e.printStackTrace();
			}
		}
	}

}
