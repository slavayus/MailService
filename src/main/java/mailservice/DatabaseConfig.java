package mailservice;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/hibernate.properties")
public class DatabaseConfig {
    private final Properties hibernateProperties;

    @Autowired
    public DatabaseConfig(Properties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    @Bean
    public DataSource createH2DataSource() {
        val builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/sql/schema.sql")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        val bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(createH2DataSource());
        bean.setJpaVendorAdapter(jpaVendor());
        bean.setPackagesToScan("mailservice");
        bean.setJpaProperties(hibernateProperties);
        return bean;
    }

    @Bean
    public JpaVendorAdapter jpaVendor() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        val manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory().getObject());
        return manager;
    }

    @Bean
    public Properties hibernateProperties(
            @Value("${hibernate.dialect}") String dialect,
            @Value("${hibernate.jdbc.fetch_size}") Integer fetchSize,
            @Value("${hibernate.jdbc.batch_size}") Integer batchSize,
            @Value("${hibernate.show_sql}") boolean showSql) {
        val properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.jdbc.fetch_size", fetchSize);
        properties.put("hibernate.jdbc.batch_size", batchSize);
        properties.put("hibernate.show_sql", showSql);
        return properties;
    }

}
