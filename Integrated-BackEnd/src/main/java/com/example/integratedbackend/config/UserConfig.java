package com.example.integratedbackend.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.integratedbackend.Users",
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "userEntityTransactionManager"
)
public class UserConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.user.configuration")
    public DataSource userDataSource() {
        return userDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "userEntityManager")
    public LocalContainerEntityManagerFactoryBean userEntityManager(
            EntityManagerFactoryBuilder builder
    ) {
        return builder
                .dataSource(userDataSource())
                .packages("com.example.integratedbackend.Users")
                .build();
    }

    @Bean(name = "userEntityTransactionManager")
    public PlatformTransactionManager userTransactionManager(
            final @Qualifier("userEntityManager")
            LocalContainerEntityManagerFactoryBean userEntityManager
    ) {
        return new JpaTransactionManager(
                Objects.requireNonNull(userEntityManager.getObject())
        );
    }

    @Bean
    public EntityManagerFactoryBuilder userEntityManagerFactoryBuilder() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        return new EntityManagerFactoryBuilder(vendorAdapter, new HashMap<>(), null);
    }
}
