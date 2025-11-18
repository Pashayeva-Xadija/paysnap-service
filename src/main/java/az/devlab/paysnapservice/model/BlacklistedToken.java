package az.devlab.paysnapservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("blacklisted_tokens")
public class BlacklistedToken {

    @Id
    private String id;

    private String token;

    private Instant expiresAt;
}
