package mx.gob.tabasco.cmaig.sigtic;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class JpaConfigFactory {

    private final JpaProperties jpaProperties;
    private final String ddlAuto;

    public JpaConfigFactory(JpaProperties jpaProperties, String ddlAuto) {
        this.jpaProperties = jpaProperties;
        this.ddlAuto = ddlAuto;
    }

    public DataSource buildDataSource(String prefix) {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    public LocalContainerEntityManagerFactoryBean buildEntityManager(
            DataSource dataSource,
            String packages,
            String persistenceUnit) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(packages);
        em.setPersistenceUnitName(persistenceUnit);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(jpaProperties.isShowSql());

        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());

        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.dialect", jpaProperties.getDatabasePlatform());

        em.setJpaPropertyMap(properties);

        return em;
    }

    public PlatformTransactionManager buildTransactionManager(
            LocalContainerEntityManagerFactoryBean emf) {

        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf.getObject());
        return tm;
    }

}
