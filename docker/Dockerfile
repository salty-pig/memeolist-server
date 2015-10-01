FROM jboss/keycloak-adapter-wildfly

ENV ADMIN_USER=admin
ENV ADMIN_PASSWORD=admin

ENV postgres_module_dir=/opt/jboss/wildfly/modules/system/layers/base/postgresql/main/
# add postgresql-jdbc jar and module.xml files
RUN mkdir -p ${postgres_module_dir}
ADD module.xml ${postgres_module_dir}
WORKDIR ${postgres_module_dir}
#RUN curl -O http://central.maven.org/maven2/org/postgresql/postgresql/9.4-1201-jdbc41/postgresql-9.4-1201-jdbc41.jar

COPY postgresql-9.4-1201-jdbc41.jar ./postgresql-9.4-1201-jdbc41.jar

WORKDIR /opt/jboss

# Standalone.xml modifications.
RUN sed -i -e 's/<datasources>/&\n        <datasource jndi-name="java:jboss\/datasources\/MemeolistDS" use-java-context="true" pool-name="MemeolistDS">                    <connection-url>jdbc:postgresql:\/\/memeolistpostgres:5432\/memeolist<\/connection-url>                    <driver>postgresql<\/driver>                    <security>                      <user-name>memeolist<\/user-name>                      <password>memeolist<\/password>                    <\/security>                    <validation>                    	<check-valid-connection-sql>SELECT 1<\/check-valid-connection-sql>                    	<background-validation>true<\/background-validation>                	<\/validation>                	<pool>						<flush-strategy>IdleConnections<\/flush-strategy>					<\/pool>                <\/datasource>/' $JBOSS_HOME/standalone/configuration/standalone.xml


RUN sed -i -e 's/<drivers>/&\n   <driver name="postgresql" module="postgresql"><driver-class>org.postgresql.Driver<\/driver-class><xa-datasource-class>org.postgresql.xa.PGXADataSource<\/xa-datasource-class><\/driver>    /' $JBOSS_HOME/standalone/configuration/standalone.xml

EXPOSE 8080
EXPOSE 8443
EXPOSE 9990
EXPOSE 8787

#Add admin user
RUN /opt/jboss/wildfly/bin/add-user.sh $ADMIN_USER $ADMIN_PASSWORD
RUN echo $MEMEOLISTPOSTGRES_PORT_5432_TCP_PORT
RUN echo $MEMEOLISTPOSTGRES_PORT_5432_TCP_ADDR
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement","0.0.0.0"]
