 package com.getjavajob.training.okhanzhin.socialnetwork.webapp.config;

 import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.StringUtils;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;

 @Configuration
public class TomcatConfig {
    @Value("${spring.datasource.jndi-name}")
    String jndiName;
    @Value("${spring.datasource.tomcat.data-source-j-n-d-i}")
    String resourceName;
    @Value("${spring.datasource.tomcat.driver-class-name}")
    String driverClassName;
    @Value("${spring.datasource.tomcat.url}")
    String url;
    @Value("${spring.datasource.tomcat.username}")
    String username;
    @Value("${spring.datasource.tomcat.password}")
    String password;
    @Value("${spring.datasource.tomcat.min-idle}")
    String minIdle;
    @Value("${spring.datasource.tomcat.max-idle}")
    String maxIdle;
    @Value("${spring.datasource.tomcat.max-wait}")
    String maxWaitMillis;

    static Logger log = LoggerFactory.getLogger(TomcatConfig.class);

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                if (factory instanceof TomcatServletWebServerFactory) {
                    TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) factory;
                    String pathToWebapp;
                    if ((pathToWebapp = System.getProperty("WEBAPP_DIR")) != null) {
                        tomcat.setDocumentRoot(new File(pathToWebapp));
                    }
                }
            }
        };
    }

    @Bean
//    @ConditionalOnProperty(name = "webapp-1.0-SNAPSHOT.war")
    public TomcatServletWebServerFactory tomcatFactory(
//                                                       @Value("${external.war.file}") String path,
//                                                       @Value("${external.war.context}") String contextPath
                                                       ) {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
//                // webapps directory does not exist by default, needs to be created
//                new File(tomcat.getServer().getCatalinaBase(), "webapps").mkdirs();
//
//                // Add a war with given context path
//                // Can add multiple wars this way with different context paths
//                tomcat.addWebapp(path, contextPath);

                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                ContextResource resource = new ContextResource();
                resource.setName(resourceName);
                resource.setType(DataSource.class.getName());
                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                resource.setProperty("driverClassName", driverClassName);
                resource.setProperty("url", url);
                resource.setProperty("username", username);
                resource.setProperty("password", password);
                context.getNamingResources().addResource(resource);
            }
        };
    }

    @Bean(destroyMethod="")
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
}
