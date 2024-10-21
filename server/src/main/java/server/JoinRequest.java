package server;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}
