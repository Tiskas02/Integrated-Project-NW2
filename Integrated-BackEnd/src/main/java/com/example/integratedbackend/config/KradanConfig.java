package com.example.integratedbackend.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.integratedbackend.Kradankanban",
        entityManagerFactoryRef = "kradankanbanEntityManager",
        transactionManagerRef = "kradankanbanEntityTransactionManager"
)
public class KradanConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.kradankanban")
    public DataSourceProperties kradanDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.kradankanban.configuration")
    public DataSource kradankanBanDataSource() {
        return kradanDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "kradankanbanEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean kradankanbanEntityManager(
            EntityManagerFactoryBuilder builder
    ) {
        return builder
                .dataSource(kradankanBanDataSource())
                .packages("com.example.integratedbackend.Kradankanban")
                .build();
    }

    @Bean(name = "kradankanbanEntityTransactionManager")
    public PlatformTransactionManager kradankanbanTransactionManager(
            final @Qualifier("kradankanbanEntityManager")
            LocalContainerEntityManagerFactoryBean kradankanbanEntityManager
    ) {
        return new JpaTransactionManager(
                Objects.requireNonNull(kradankanbanEntityManager.getObject())
        );
    }
}
