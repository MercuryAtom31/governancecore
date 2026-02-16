package com.benzair.governancecore.assetsubdomain.datalayer;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assets") // Table name in the database
@Getter // Lombok annotation to generate getters
@Setter // Lombok annotation to generate setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
@Builder // Lombok annotation to implement the builder pattern
public class Asset {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

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