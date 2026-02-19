package com.benzair.governancecore.assetsubdomain.datalayer;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
Entity means:
a Java class that represents a table in a database. 
*/
@Entity // JPA (Java Persistence API) annotation to mark this class as a database entity
@Table(name = "assets") // Table name in the database
@Getter // Lombok annotation to generate getters
@Setter // Lombok annotation to generate setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
@Builder // Lombok annotation to implement the builder pattern

public class Asset {

   @Id // JPA (Java Persistence API) annotation to mark this field as the primary key
   @GeneratedValue(strategy = GenerationType.UUID) // JPA annotation to specify that the ID should be generated as a UUID
   private UUID id; // This is the database primary key, which is auto-generated and not exposed to the service layer.

   @Embedded // JPA annotation to specify that this is an embedded object, meaning its fields will be stored in the same table as the Asset entity.
   private AssetIdentifier assetIdentifier; // This is the business ID (external uuid) that we use in the service layer, separate from the database primary key.

   // JPA annotation to specify that this column cannot be null
   @Column (nullable = false)
   private String name;

   @Column (nullable = false)
   private String owner;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AssetType assetType;

   @Enumerated(EnumType.STRING) // JPA annotation to specify that the enum should be stored as a string in the database
   @Column (nullable = false)
   private DataClassification classification;

   @Column
   private String description;

   @Column (nullable = false, updatable = false) // This column cannot be updated after creation
   private LocalDateTime createdAt;

   @Column (nullable = false)
   private LocalDateTime updatedAt;

   // @PrePersist runs only when that row is inserted the first time.
   @PrePersist // JPA annotation to specify that this method should be called before the entity is persisted (saved for the first time)
   private void prePersist() {
      LocalDateTime now = LocalDateTime.now(); // Get the current time
      createdAt = now; // Set createdAt to the current time when the entity is first persisted
      updatedAt = now; // Set updatedAt to the current time when the entity is first persisted
   }

   @PreUpdate // JPA annotation to specify that this method should be called before the entity is updated
   private void preUpdate() {
      updatedAt = LocalDateTime.now(); // Update updatedAt to the current time whenever the entity is updated
   }

}

/**
 This class represents an information asset record in the Information Security Management System (ISMS).

Practically, one `Asset` row is one thing your organization must protect, such as:

- an application (`Customer API`)
- a database (`User PII DB`)
- a server/cloud workload
- a critical business system

In ISMS terms, this is the inventory item to which we later attach:
- risks
- controls
- incidents
- evidence
- owners/accountability

So `Asset` = the core “what are we protecting?” object.
*/