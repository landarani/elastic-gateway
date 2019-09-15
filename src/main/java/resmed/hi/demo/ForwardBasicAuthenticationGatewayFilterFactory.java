package resmed.hi.demo;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class ForwardBasicAuthenticationGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange, chain) -> {
            String token = "Basic " + Base64.getEncoder().encodeToString((config.getName() + ":" + config.getValue()).getBytes());
            ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.set(AUTHORIZATION, token))
                .build();

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

}
