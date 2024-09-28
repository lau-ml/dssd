package dssd.server.config;

import dssd.server.interceptors.BonitaRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final BonitaRequestInterceptor bonitaRequestInterceptor;

    public RestTemplateConfig(BonitaRequestInterceptor bonitaRequestInterceptor) {
        this.bonitaRequestInterceptor = bonitaRequestInterceptor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(bonitaRequestInterceptor);
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
