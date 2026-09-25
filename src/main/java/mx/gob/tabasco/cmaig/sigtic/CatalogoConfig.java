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
        entityManagerFactoryRef = "catalogosEntityManagerFactory",
        transactionManagerRef = "catalogosTransactionManager",
        basePackages = "mx.gob.tabasco.cmaig.sigtic.sac.repository"
)
public class CatalogoConfig {

    @Bean(name = "catalogoDatasource")
    @ConfigurationProperties("spring.datasource.catalogos")
    public DataSource catalogoDatasource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "catalogosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean catalogosEntityManagerFactory(
            JpaConfigFactory factory,
            @Qualifier("catalogoDatasource") DataSource ds) {

        return factory.buildEntityManager(
                ds,
                "mx.gob.tabasco.cmaig.sigtic.sac.model",
                "catalogo"
        );
    }

    @Bean(name = "catalogosTransactionManager")
    public PlatformTransactionManager catalogosTransactionManager(
            JpaConfigFactory factory,
            @Qualifier("catalogosEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {

        return factory.buildTransactionManager(emf);
    }
}