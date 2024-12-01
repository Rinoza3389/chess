package websocket;

import com.google.gson.*;
import websocket.messages.*;

import java.lang.reflect.Type;

public class GsonServerMessage {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new ServerMessageSerializer())
            .registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}

class ServerMessageSerializer implements JsonSerializer<ServerMessage> {
    @Override
    public JsonElement serialize(ServerMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", message.getServerMessageType().toString());
        json.add("data", context.serialize(message));
        return json;
    }
}

class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        return switch (type) {
            case "LOAD_GAME" -> context.deserialize(data, LoadGameMessage.class);
            case "ERROR" -> context.deserialize(data, ErrorMessage.class);
            case "NOTIFICATION" -> context.deserialize(data, NotificationMessage.class);
            default -> throw new JsonParseException("Unknown type: " + type);
        };

    }
}