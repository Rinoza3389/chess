package server;
import model.GameData;
import java.util.ArrayList;

public record ListResponse(ArrayList<GameData> games) {
}
