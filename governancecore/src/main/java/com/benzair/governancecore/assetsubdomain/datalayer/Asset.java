package com.benzair.governancecore.assetsubdomain.datalayer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    private String name;
    private String type;
    private String value;

}