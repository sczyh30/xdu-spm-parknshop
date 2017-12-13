package io.spm.parknshop.api.config;

import io.spm.parknshop.api.filter.ApiErrorExceptionHandler;
import io.spm.parknshop.api.filter.JwtAuthenticationWebFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

  private final ResourceProperties resourceProperties;

  private final ApplicationContext applicationContext;

  private final List<ViewResolver> viewResolvers;

  private final ServerCodecConfigurer serverCodecConfigurer;

  public WebFluxConfig(ResourceProperties resourceProperties,
                       ApplicationContext applicationContext,
                       ObjectProvider<List<ViewResolver>> viewResolversProvider,
                       ServerCodecConfigurer serverCodecConfigurer) {
    this.resourceProperties = resourceProperties;
    this.applicationContext = applicationContext;
    this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
    this.serverCodecConfigurer = serverCodecConfigurer;
  }

  @Bean
  public ErrorWebExceptionHandler apiExceptionHandler(ErrorAttributes errorAttributes) {
    ApiErrorExceptionHandler exceptionHandler = new ApiErrorExceptionHandler(errorAttributes, resourceProperties, applicationContext);
    exceptionHandler.setViewResolvers(this.viewResolvers);
    exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
    exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
    return exceptionHandler;
  }

  @Bean
  public JwtAuthenticationWebFilter jwtTokenFilter() {
    return new JwtAuthenticationWebFilter(viewResolvers, serverCodecConfigurer);
  }

  @Bean
  @Order(-3)
  public CorsWebFilter corsFilter() {
    return new CorsWebFilter(exchange -> {
      CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
      configuration.addAllowedMethod("*");
      return configuration;
    });
  }
}
