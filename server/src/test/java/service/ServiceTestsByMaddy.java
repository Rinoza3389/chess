package service;

import org.junit.jupiter.api.*;
import server.RegisterRequest;
import service.Services.*;

public class ServiceTestsByMaddy {

    private static Services mainService;

    @BeforeEach
    public void setup() {
        mainService = new Services();
    }

    @Test
    @DisplayName("Test Clear")
    public void testClear() {
        RegisterRequest regReq = new RegisterRequest("mlorrig", "1234", "mlorriga@byu.edu");

    }

}
