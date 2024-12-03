package websocket;

import com.google.gson.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.lang.reflect.Type;

public class GsonUserGameCommand {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}

class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement jsonElm, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElm.getAsJsonObject();
        String type = json.get("CommandType").getAsString();
        if (type.equals("MAKE_MOVE")) {
            return context.deserialize(json, MakeMoveCommand.class);
        } else {
            return context.deserialize(json, UserGameCommand.class);
        }
    }
}