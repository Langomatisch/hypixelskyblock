package de.langomatisch.skyblock.coins.entity;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Data
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "player_coins")
public class CoinsPlayer {

    @Id
    @Column(length = 36, unique = true, nullable = false)
    private String uuid;

    private double coins;

}
