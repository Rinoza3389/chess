package server;

import java.util.Objects;

public record RegisterRequest(String username, String password, String email) {
}
