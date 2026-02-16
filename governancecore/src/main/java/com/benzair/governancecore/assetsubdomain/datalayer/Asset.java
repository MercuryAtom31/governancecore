package com.benzair.governancecore.assetsubdomain.datalayer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
   @Id
   public String id;
   public String name;
   public String owner;
   public String classification;
   public LocalDateTime createdAt;
   public LocalDateTime updatedAt;



}