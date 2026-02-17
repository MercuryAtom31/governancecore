package com.benzair.governancecore.assetsubdomain.datalayer;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
   private UUID id;

   // JPA annotation to specify that this column cannot be null
   @Column (nullable = false)
   private String name;

   @Column (nullable = false)
   private String owner;

   @Column (nullable = false)
   private String classification;

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