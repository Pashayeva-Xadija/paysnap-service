package az.devlab.paysnapservice.config;

import com.stripe.Stripe;
import com.stripe.net.RequestOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StripeConfig {

    private final StripeProperties stripeProperties;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    @Bean
    public RequestOptions stripeRequestOptions() {
        return RequestOptions.builder()
                .setApiKey(stripeProperties.getSecretKey())
                .build();
    }
}
