package ui.reqRes;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}
