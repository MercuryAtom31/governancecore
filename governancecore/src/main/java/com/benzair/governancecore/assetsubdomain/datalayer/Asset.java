package com.benzair.governancecore.assetsubdomain.datalayer;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

   @Column (nullable = false)
   private LocalDateTime createdAt;

   @Column (nullable = false)
   private LocalDateTime updatedAt;

}