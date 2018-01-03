package io.spm.parknshop.configcenter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration("globalConfRedisConfig")
public class RedisConfig {

  @Bean
  public ReactiveRedisTemplate<String, String> adminReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory){
    RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
      .<String, String>newSerializationContext(new StringRedisSerializer())
      .hashKey(new StringRedisSerializer())
      .hashValue(new StringRedisSerializer())
      .build();
    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory){
    return new StringRedisTemplate(connectionFactory);
  }
}
