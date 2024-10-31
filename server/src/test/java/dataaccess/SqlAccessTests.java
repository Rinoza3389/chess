package dataaccess;

import org.junit.jupiter.api.*;
import dataaccess.SqlDataAccess;

public class SqlAccessTests {

    private static SqlDataAccess currAccess;

    @BeforeAll
    public static void setup() throws DataAccessException {
        currAccess = new SqlDataAccess();
    }

    @Test
    @DisplayName("Test Add")
}
