package ui.ReqRes;

import model.GameData;

import java.util.ArrayList;

public record ListResponse(ArrayList<GameData> games) {
}
