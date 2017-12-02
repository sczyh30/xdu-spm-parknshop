package io.spm.parknshop.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "io.spm.parknshop")
@EnableJpaRepositories(basePackages = "io.spm.parknshop")
@EnableTransactionManagement
public class JdbcConfig {
}
