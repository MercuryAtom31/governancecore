package com.benzair.governancecore.assetsubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface AssetRepository extends  JpaRepository<Asset, UUID> {

    /*
    This is a custom query method.
    You are saying:
    "Spring, please find an Asset using its external business ID."
    */
    Optional<Asset> findByAssetIdentifier_AssetId(String assetId);
    
}

/*
What Is AssetRepository?

Think of AssetRepository as:
The bridge between the Java code and the database for Assets.
It is the data access boundary.

It allows the service layer to:
-Save assets
-Fetch assets
-Delete assets
-Search assets

Without ever writing SQL manually.

What do you get automatically?

Spring gives you:

-save()
-findById()
-findAll()
-delete()
-Pagination
-Sorting

You did not write any SQL. Spring generates it.

--Repository.save()

Now Spring Data JPA takes that entity and:

-Generates SQL
-Inserts it into PostgreSQL
-Triggers @PrePersist or @PreUpdate
-Stores it in the database

At this point, the asset officially exists in the ISMS system.
*/