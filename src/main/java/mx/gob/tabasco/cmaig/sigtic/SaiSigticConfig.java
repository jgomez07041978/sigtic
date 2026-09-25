package mx.gob.tabasco.cmaig.sigtic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "saiSigticEntityManagerFactory",
        transactionManagerRef = "saiSigticTransactionManager",
        basePackages = "mx.gob.tabasco.cmaig.sigtic.sai.repository"
)
public class SaiSigticConfig {

    @Bean(name = "saiDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.sai")
    public DataSource saiDataSource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "saiSigticEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean saiSigticEntityManagerFactory(
            JpaConfigFactory factory,
            @Qualifier("saiDataSource") DataSource ds) {

        return factory.buildEntityManager(
                ds,
                "mx.gob.tabasco.cmaig.sigtic.sai.model",
                "sai"
        );
    }

    @Bean(name = "saiSigticTransactionManager")
    public PlatformTransactionManager saiSigticTransactionManager(
            JpaConfigFactory factory,
            @Qualifier("saiSigticEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {

        return factory.buildTransactionManager(emf);
    }
}
