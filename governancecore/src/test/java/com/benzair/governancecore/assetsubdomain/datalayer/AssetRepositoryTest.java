package com.benzair.governancecore.assetsubdomain.datalayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.EmbeddedDatabaseConnection;

@DataJpaTest // Spring Boot annotation to configure JPA tests, which sets up an in-memory database and scans for JPA repositories
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // Use H2 in-memory database for testing
public class AssetRepositoryTest {
    
    @Autowired
    private AssetRepository assetRepository;

    @Test
    public void AssetRepository_getAllAssets_ReturnsListOfAssets() {
        // Arrange


        // Act


        // Assert
        
    }
}
