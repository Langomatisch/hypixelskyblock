package de.langomatisch.skyblock.coins.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Cacheable
@Table( name = "player_coins" )
@NoArgsConstructor
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
public class CoinUser {

    @Id
    @Column(length = 36, unique = true, nullable = false)
    @Type( type = "uuid-char" )
    private UUID uuid;

    private double coins;

}
