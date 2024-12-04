package websocket;

import com.google.gson.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.lang.reflect.Type;

public class GsonUserGameCommand {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer())
            .create();

    public static Gson getGson() {
        return GSON;
    }
}

class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement jsonElm, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElm.getAsJsonObject();
        String type = json.get("commandType").getAsString();
        if (type.equals("MAKE_MOVE")) {
            return new Gson().fromJson(json, MakeMoveCommand.class);
        } else {
            return new Gson().fromJson(json, UserGameCommand.class);
        }
    }
}