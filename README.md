# weblogic-authentication
Simple Form Authentication Web Application

Requires a weblogic node configured with form authentication. The easiest way to deploy is with a Derby database running alongside WebLogic.

WebLogic Configuration

Domain > Web Application:

    <change-session-id-on-authentication>false</change-session-id-on-authentication>

Configure a database connection:

    <jdbc-system-resource>
      <name>derby-db</name>
      <target>AdminServer,node01</target>
      <descriptor-file-name>jdbc/derby-db-jdbc.xml</descriptor-file-name>
    </jdbc-system-resource>
    ..
    <?xml version='1.0' encoding='UTF-8'?>
    <jdbc-data-source xmlns="http://xmlns.oracle.com/weblogic/jdbc-data-source" xmlns:sec="http://xmlns.oracle.com/weblogic/security" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:wls="http://xmlns.oracle.com/weblogic/security/wls" xsi:schemaLocation="http://xmlns.oracle.com/weblogic/jdbc-data-source http://xmlns.oracle.com/weblogic/jdbc-data-source/1.5/jdbc-data-source.xsd">
      <name>derby-db</name>
      <datasource-type>GENERIC</datasource-type>
      <jdbc-driver-params>
        <url>jdbc:derby://localhost:1527/authdb;ServerName=localhost;databaseName=authdb;create=true</url>
        <driver-name>org.apache.derby.jdbc.ClientXADataSource</driver-name>
        <properties>
          <property>
            <name>user</name>
            <value>weblogic</value>
          </property>
          <property>
            <name>portNumber</name>
            <value>1527</value>
          </property>
          <property>
            <name>databaseName</name>
            <value>authdb;create=true</value>
          </property>
          <property>
            <name>serverName</name>
            <value>localhost</value>
          </property>
        </properties>
        <password-encrypted>{AES256}Bqx+yQJFO9RVu2bFmUUt+g1m8TkJK8rmVH+127tidEc=</password-encrypted>
      </jdbc-driver-params>
      <jdbc-connection-pool-params>
        <test-table-name>SQL SELECT 1 FROM SYS.SYSTABLES</test-table-name>
      </jdbc-connection-pool-params>
      <jdbc-data-source-params>
        <jndi-name>jdbc/derby-db</jndi-name>
        <global-transactions-protocol>TwoPhaseCommit</global-transactions-protocol>
      </jdbc-data-source-params>
    </jdbc-data-source>

Configure a SQL Authenticator in Security Realms:

    <sec:authentication-provider xsi:type="wls:read-only-sql-authenticatorType">
      <sec:name>sqlauth</sec:name>
      <sec:control-flag>SUFFICIENT</sec:control-flag>
      <wls:data-source-name>derby-db</wls:data-source-name>
      <wls:plaintext-passwords-enabled>true</wls:plaintext-passwords-enabled>
      <wls:sql-get-users-password>SELECT U_PASSWORD FROM USERS WHERE LOWER(U_NAME) = LOWER(?)</wls:sql-get-users-password>
      <wls:sql-user-exists>SELECT U_NAME FROM USERS WHERE LOWER(U_NAME) = LOWER(?)</wls:sql-user-exists>
      <wls:sql-list-member-groups>SELECT G_NAME FROM GROUPMEMBERS WHERE LOWER(G_MEMBER) = LOWER(?)</wls:sql-list-member-groups>
      <wls:sql-list-users>SELECT U_NAME FROM USERS WHERE LOWER(U_NAME) LIKE LOWER(?)</wls:sql-list-users>
      <wls:sql-get-user-description>SELECT U_DESCRIPTION FROM USERS WHERE LOWER(U_NAME) = LOWER(?)</wls:sql-get-user-description>
      <wls:sql-list-groups>SELECT G_NAME FROM GROUPS WHERE LOWER(G_NAME) LIKE LOWER(?)</wls:sql-list-groups>
      <wls:sql-group-exists>SELECT G_NAME FROM GROUPS WHERE LOWER(G_NAME) = LOWER(?)</wls:sql-group-exists>
      <wls:sql-is-member>SELECT G_MEMBER FROM GROUPMEMBERS WHERE LOWER(G_NAME) = LOWER(?) AND LOWER(G_MEMBER) = LOWER(?)</wls:sql-is-member>
      <wls:sql-get-group-description>SELECT G_DESCRIPTION FROM GROUPS WHERE LOWER(G_NAME) = LOWER(?)</wls:sql-get-group-description>
    </sec:authentication-provider>
