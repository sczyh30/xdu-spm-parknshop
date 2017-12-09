package io.spm.parknshop.cart.config;

import io.spm.parknshop.cart.domain.CartProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PreDestroy;

@EnableRedisRepositories("io.spm.parknshop.cart")
@Configuration
public class RedisConfig {

  @Autowired
  private RedisConnectionFactory factory;

  @Bean
  public ReactiveRedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  public ReactiveRedisTemplate<String, CartProductDO> cartReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory){
    RedisSerializationContext<String, CartProductDO> serializationContext = RedisSerializationContext
      .<String, CartProductDO>newSerializationContext(new StringRedisSerializer())
      .hashKey(new StringRedisSerializer())
      .hashValue(new Jackson2JsonRedisSerializer<>(CartProductDO.class))
      .build();
    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory){
    return new StringRedisTemplate(connectionFactory);
  }

  @PreDestroy
  public void flushTestDb() {
    factory.getConnection().flushDb();
  }
}
