package mx.gob.tabasco.cmaig.sigtic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "seguridadEntityManagerFactory",
        transactionManagerRef = "seguridadTransactionManager",
        basePackages = "mx.gob.tabasco.cmaig.sigtic.sas.repository"
)
public class SeguridadConfig {

    @Bean(name = "seguridadDatasource")
    @ConfigurationProperties("spring.datasource.seguridad")
    public DataSource seguridadDatasource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "seguridadEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean seguridadEntityManagerFactory(
            JpaConfigFactory factory,
            @Qualifier("seguridadDatasource") DataSource ds) {

        return factory.buildEntityManager(
                ds,
                "mx.gob.tabasco.cmaig.sigtic.sas.model",
                "seguridad"
        );
    }

    @Bean(name = "seguridadTransactionManager")
    public PlatformTransactionManager seguridadTransactionManager(
            JpaConfigFactory factory,
            @Qualifier("seguridadEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {

        return factory.buildTransactionManager(emf);
    }
}