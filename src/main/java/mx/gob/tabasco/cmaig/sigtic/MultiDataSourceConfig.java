package mx.gob.tabasco.cmaig.sigtic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiDataSourceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Value("${spring.jpa.hibernate.ddl-auto:none}")
    private String ddlAuto;

    @Bean
    public JpaConfigFactory jpaConfigFactory() {
        return new JpaConfigFactory(jpaProperties, ddlAuto);
    }
}
