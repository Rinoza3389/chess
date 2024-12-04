package websocket;

import com.google.gson.*;
import websocket.messages.*;

import java.lang.reflect.Type;

public class GsonServerMessage {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new ServerMessageSerializer())
            .registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer())
            .create();

    public static Gson getGson() {
        return GSON;
    }
}

class ServerMessageSerializer implements JsonSerializer<ServerMessage> {
    @Override
    public JsonElement serialize(ServerMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("serverMessageType", message.getServerMessageType().toString());
        json.add("message", context.serialize(message));
        return json;
    }
}

class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("serverMessageType").getAsString();
//        JsonElement data = jsonObject.get("message");

        return switch (type) {
            case "LOAD_GAME" -> context.deserialize(jsonObject, LoadGameMessage.class);
            case "ERROR" -> context.deserialize(jsonObject, ErrorMessage.class);
            case "NOTIFICATION" -> context.deserialize(jsonObject, NotificationMessage.class);
            default -> throw new JsonParseException("Unknown type: " + type);
        };

    }
}