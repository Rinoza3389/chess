package ui.reqres;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}