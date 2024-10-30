package dataaccess;

import org.junit.jupiter.api.*;
import dataaccess.SqlDataAccess;

public class SqlAccessTests {

    private static SqlDataAccess currAccess;

    @BeforeEach
    public void setup() throws DataAccessException {
        currAccess = new SqlDataAccess();
    }
}
