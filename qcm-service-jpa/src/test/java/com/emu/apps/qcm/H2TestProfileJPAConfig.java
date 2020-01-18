/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c)  2019 qcm-rest-api
 * Author  Eric Muller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.emu.apps.qcm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static java.util.Collections.singletonMap;

@Configuration
@EnableJpaRepositories(basePackages = {"com.emu.apps"})
@EnableTransactionManagement
public class H2TestProfileJPAConfig {

    public static final String USER_TEST = "user";
    public static final String USER_PASSWORD = "password";
 
    @Bean
    @Profile("test")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
 
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,  DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.emu.apps.qcm.services.jpa.entity","com.emu.apps.users.services.jpa.entity")
                .persistenceUnit("qcm")
                .properties(singletonMap("hibernate.hbm2ddl.auto", "create-drop"))
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory
                                                                 entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
     
    // configure entityManagerFactory
    // configure transactionManager
    // configure additional Hibernate properties
}
