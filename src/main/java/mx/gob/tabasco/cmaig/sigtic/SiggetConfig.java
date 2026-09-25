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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "siggetEntityManagerFactory",
        transactionManagerRef = "siggetTransactionManager",
        basePackages = "mx.gob.tabasco.cmaig.sigtic.sigget.repository"
)
public class SiggetConfig {

    @Bean(name = "siggetDatasource")
    @ConfigurationProperties("spring.datasource.sigget")
    public DataSource siggetDatasource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "siggetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean siggetEntityManagerFactory(
            JpaConfigFactory factory,
            @Qualifier("siggetDatasource") DataSource ds) {

        return factory.buildEntityManager(
                ds,
                "mx.gob.tabasco.cmaig.sigtic.sigget.model",
                "sigget"
        );
    }

    @Bean(name = "siggetTransactionManager")
    public PlatformTransactionManager siggetTransactionManager(
            JpaConfigFactory factory,
            @Qualifier("siggetEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {

        return factory.buildTransactionManager(emf);
    }
}