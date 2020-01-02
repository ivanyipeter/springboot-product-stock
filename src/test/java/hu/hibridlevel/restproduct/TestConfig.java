package hu.hibridlevel.restproduct;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@PropertySource("classpath:application-test.properties")
@EnableAutoConfiguration
@EnableJpaRepositories
public class TestConfig {
}
