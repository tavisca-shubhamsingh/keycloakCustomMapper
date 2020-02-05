FROM jboss/keycloak:5.0.0

# Add own mapper to keycloak
# Idea is based on https://github.com/arielcarrera/keycloak-docker-oracle
ADD ./protocol-mapper/src/main/module/changeProvider.xsl /opt/jboss/keycloak/
RUN java -jar /usr/share/java/saxon.jar -s:/opt/jboss/keycloak/standalone/configuration/standalone.xml -xsl:/opt/jboss/keycloak/changeProvider.xsl -o:/opt/jboss/keycloak/standalone/configuration/standalone.xml; java -jar /usr/share/java/saxon.jar -s:/opt/jboss/keycloak/standalone/configuration/standalone-ha.xml -xsl:/opt/jboss/keycloak/changeProvider.xsl -o:/opt/jboss/keycloak/standalone/configuration/standalone-ha.xml; rm /opt/jboss/keycloak/changeProvider.xsl

RUN mkdir -p /opt/jboss/keycloak/modules/system/layers/base/com/tavisca/protocol-mapper/main
ADD ./protocol-mapper/target/protocol-mapper.jar /opt/jboss/keycloak/modules/system/layers/base/com/tavisca/protocol-mapper/main/protocol-mapper.jar
ADD ./protocol-mapper/src/main/module/module.xml /opt/jboss/keycloak/modules/system/layers/base/com/tavisca/protocol-mapper/main
