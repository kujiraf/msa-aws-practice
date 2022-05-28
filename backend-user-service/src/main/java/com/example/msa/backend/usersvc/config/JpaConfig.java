package com.example.msa.backend.usersvc.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.msa.backend.usersvc.domain.repository")
public class JpaConfig {

  @Autowired DataSource dataSource;

  @Bean
  public PlatformTransactionManager transactionManager() throws Exception {
    return new JpaTransactionManager();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    JpaVendorAdapter adopter = new HibernateJpaVendorAdapter();

    Properties properties = new Properties();
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "true");

    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPackagesToScan("com.example.msa.backend.usersvc.domain.model.entity");
    bean.setJpaProperties(properties);
    bean.setJpaVendorAdapter(adopter);
    bean.setDataSource(dataSource);

    return bean;
  }
}
