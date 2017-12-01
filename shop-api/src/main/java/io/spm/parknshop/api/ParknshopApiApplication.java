package io.spm.parknshop.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry for ParkNShop API server.
 *
 * @author Eric Zhao 14130140389
 * @date 2017/12/1
 */
@ComponentScan
@SpringBootApplication
public class ParknshopApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParknshopApiApplication.class, args);
  }
}
