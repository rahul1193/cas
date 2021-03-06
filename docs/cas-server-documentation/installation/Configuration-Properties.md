---
layout: default
title: CAS Properties
---

# CAS Properties

Various properties can be specified in CAS [either inside configuration files or as command
line switches](Configuration-Management.html). This section provides a list common CAS properties and
references to the underlying modules that consume them.

<div class="alert alert-info"><strong>Be Selective</strong><p>
This section is meant as a guide only. Do <strong>NOT</strong> copy/paste the entire collection of settings
into your CAS configuration; rather pick only the properties that you need.</p></div>

Note that property names can be specified
in very relaxed terms. For instance `cas.someProperty`, `cas.some-property`, `cas.some_property`
and `CAS_SOME_PROPERTY` are all valid names.

The following list of properties are controlled by and provided to CAS. Each block, for most use cases, corresponds
to a specific CAS module that is expected to be included in the final CAS distribution prepared during the build
and deployment process.

<div class="alert alert-info"><strong>YAGNI</strong><p>Note that for nearly ALL use cases,
simply declaring and configuring properties listed below is sufficient. You should NOT have to
explicitly massage a CAS XML configuration file to design an authentication handler,
create attribute release policies, etc. CAS at runtime will auto-configure all required changes for you.</p></div>

## Remember

Take note of the following suggestions before you start with the configuration.

### Naming Convention

- Settings and properties that are controlled by the CAS platform directly always begin with the prefix `cas`.
All other settings are controlled and provided to CAS via other underlying frameworks and may have their own schemas
and syntax. **BE CAREFUL** with the distinction.

- Unrecognized properties are generally ignored by CAS and/or frameworks upon which CAS depends. This means if you somehow
misspell a property definition or fail to adhere to the dot-notation syntax and such, your setting is entirely
ignored by CAS and likely the feature it controls will never be activated in the way you intend.

### Indexed Settings

CAS settings able to accept multiple values are typically documented with an index, such as `cas.some.setting[0]=value`.
The index `[0]` is meant to be incremented by the adopter to allow for distinct multiple configuration blocks:

```properties
# cas.some.setting[0]=value1
# cas.some.setting[1]=value2
```

### Trust But Verify

If you are unsure about the meaning of a given CAS setting, do **NOT** simply turn it on without hesitation.
Review the codebase or better yet, [ask questions](/cas/Mailing-Lists.html) to clarify the intended behavior.

<div class="alert alert-info"><strong>Keep It Simple</strong><p>If you do not know or cannot tell what a setting does, you do not need it.</p></div>

### Time Unit of Measure

All CAS settings that deal with time units, unless noted otherwise,
should support the duration syntax for full clarity on unit of measure:

```bash
"PT20S"     -- parses as "20 seconds"
"PT15M"     -- parses as "15 minutes"
"PT10H"     -- parses as "10 hours"
"P2D"       -- parses as "2 days"
"P2DT3H4M"  -- parses as "2 days, 3 hours and 4 minutes"
```

The native numeric syntax is still supported though you will have to refer to the docs
in each case to learn the exact unit of measure.

## Configuration Storage

The following settings are to be loaded by the CAS configuration server, which bootstraps
the entire CAS running context. They are to be put inside the `src/main/resources/bootstrap.properties`.
See [this guide](Configuration-Management.html) for more info.

### Native

Load settings from external properties/yaml configuration files.

```properties
# spring.profiles.active=native
# spring.cloud.config.server.native.searchLocations=file:///etc/cas/config
```

### Git Repository

Load settings from an internal/external Git repository.

```properties
# spring.profiles.active=default
# spring.cloud.config.server.git.uri=https://github.com/repoName/config
# spring.cloud.config.server.git.uri=file://${user.home}/config
# spring.cloud.config.server.git.username=
# spring.cloud.config.server.git.password=
```

### Vault

Load settings from [HasiCorp's Vault](Configuration-Properties-Security.html).

```properties
# spring.cloud.vault.host=127.0.0.1
# spring.cloud.vault.port=8200
# spring.cloud.vault.connectionTimeout=3000
# spring.cloud.vault.readTimeout=5000
# spring.cloud.vault.enabled=true
# spring.cloud.vault.fail-fast=true
# spring.cloud.vault.token=1305dd6a-a754-f145-3563-2fa90b0773b7
# spring.cloud.vault.scheme=http
# spring.cloud.vault.generic.enabled=true
# spring.cloud.vault.generic.backend=secret
```

### MongoDb

Load settings from MongoDb.

```properties
# cas.spring.cloud.mongo.uri=mongodb://casuser:Mellon@ds061954.mongolab.com:61954/jasigcas
```

## Configuration Security

Encrypt and decrypt configuration via Spring Cloud.

```properties
# spring.cloud.config.server.encrypt.enabled=true
# encrypt.keyStore.location=file:///etc/cas/casconfigserver.jks
# encrypt.keyStore.password=keystorePassword
# encrypt.keyStore.alias=DaKey
# encrypt.keyStore.secret=changeme
```

To learn more about how sensitive CAS settings can be
secured, [please review this guide](Configuration-Properties-Security.html).


## Cloud Configuration Bus

CAS uses the Spring Cloud Bus to manage configuration in a distributed deployment. Spring Cloud Bus links nodes of a
distributed system with a lightweight message broker.

```properties
# spring.cloud.bus.enabled=false
# spring.cloud.bus.refresh.enabled=true
# spring.cloud.bus.env.enabled=true
# spring.cloud.bus.destination=CasCloudBus
# spring.cloud.bus.ack.enabled=true
```

To learn more about this topic, [please review this guide](Configuration-Management.html).

### RabbitMQ

Broadcast CAS configuration updates to other nodes in the cluster
via [RabbitMQ](http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/#_rabbitmq_binder).

```properties
# spring.rabbitmq.host=
# spring.rabbitmq.port=
# spring.rabbitmq.username=
# spring.rabbitmq.password=

# Or all of the above in one line
# spring.rabbitmq.addresses=
```

### Kafka

Broadcast CAS configuration updates to other nodes in the cluster
via [Kafka](http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/#_apache_kafka_binder).

```properties
# spring.cloud.stream.bindings.output.content-type=application/json
# spring.cloud.stream.kafka.binder.zkNodes=...
# spring.cloud.stream.kafka.binder.brokers=...
```

## Embedded Tomcat

The following properties are related to the embedded Tomcat container that ships with CAS.

```properties
server.contextPath=/cas
server.port=8443
server.ssl.keyStore=file:/etc/cas/thekeystore
server.ssl.keyStorePassword=changeit
server.ssl.keyPassword=changeit
server.maxHttpHeader-size=2097152
server.useForwardHeaders=true
server.connectionTimeout=20000

# server.ssl.ciphers=
# server.ssl.clientAuth=
# server.ssl.enabled=
# server.ssl.keyAlias=
# server.ssl.keyStoreProvider=
# server.ssl.keyStoreType=
# server.ssl.protocol=
# server.ssl.trustStore=
# server.ssl.trustStorePassword=
# server.ssl.trustStoreProvider=
# server.ssl.trustStoreType=

server.tomcat.basedir=build/tomcat

server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.pattern=%t %a "%r" %s (%D ms)
server.tomcat.accesslog.suffix=.log

server.tomcat.maxHttpPostSize=20971520
server.tomcat.maxThreads=5
server.tomcat.portHeader=X-Forwarded-Port
server.tomcat.protocolHeader=X-Forwarded-Proto
server.tomcat.protocolHeaderHttpsValue=https
server.tomcat.remoteIpHeader=X-FORWARDED-FOR
server.tomcat.uriEncoding=UTF-8

server.useForwardHeaders=true
```

### HTTP/AJP

Enable HTTP/AJP connections for the embedded Tomcat container.

```properties
# cas.server.http.port=8080
# cas.server.http.protocol=org.apache.coyote.http11.Http11NioProtocol
# cas.server.http.enabled=true

# cas.server.ajp.secure=false
# cas.server.ajp.enabled=false
# cas.server.ajp.proxyPort=-1
# cas.server.ajp.protocol=AJP/1.3
# cas.server.ajp.asyncTimeout=5000
# cas.server.ajp.scheme=http
# cas.server.ajp.maxPostSize=20971520
# cas.server.ajp.port=8009
# cas.server.ajp.enableLookups=false
# cas.server.ajp.redirectPort=-1
# cas.server.ajp.allowTrace=false
```

### Rewrite Valve

If and when you choose to deploy CAS at root and remove the default context path,
CAS by default attempts to deploy a special [`RewriteValve`](https://tomcat.apache.org/tomcat-8.0-doc/rewrite.htm)
for the embedded container that knows how to reroute urls and such for backward compatibility reasons.

```properties
# cas.server.rewriteValveConfigLocation=classpath:/container/tomcat/rewrite.config
```

### Extended Access Log

Enable the [extended access log](https://tomcat.apache.org/tomcat-8.0-doc/api/org/apache/catalina/valves/ExtendedAccessLogValve.html)
for the embedded Tomcat container.

```properties
# cas.server.extAccessLog.enabled=false
# cas.server.extAccessLog.pattern=c-ip s-ip cs-uri sc-status time X-threadname x-H(secure) x-H(remoteUser)
# cas.server.extAccessLog.suffix=.log
# cas.server.extAccessLog.prefix=localhost_access_extended
# cas.server.extAccessLog.directory=
```

## CAS Server

Identify the CAS server. `name` and `prefix` are always required settings.

A CAS host is automatically appended to the ticket ids generated by CAS.
If none is specified, one is automatically detected and used by CAS.

```properties
# cas.server.name=https://cas.example.org:8443
# cas.server.prefix=https://cas.example.org:8443/cas
# cas.host.name=
```

## CAS Banner

On startup, CAS will display a banner along with some diagnostics info.
In order to skip this step and summarize, set the system property `-DCAS_BANNER_SKIP=true`.

## Admin Status Endpoints

The following properties describe access controls and settings for the `/status`
endpoint of CAS which provides administrative functionality and oversight into the CAS software.
To learn more about this topic, [please review this guide](Monitoring-Statistics.html).

```properties
# endpoints.enabled=true
# endpoints.sensitive=true

management.contextPath=/status
management.security.enabled=true
management.security.roles=ACTUATOR,ADMIN
management.security.sessions=if_required

# Each of the below endpoints can either be disabled
# or can be individually marked as 'sensitive' (or not) 
# to enable authentication.

# endpoints.restart.enabled=false
# endpoints.shutdown.enabled=false
# endpoints.autoconfig.enabled=true
# endpoints.beans.enabled=true
# endpoints.bus.enabled=true
# endpoints.configprops.enabled=true
# endpoints.dump.enabled=true
# endpoints.env.enabled=true
# endpoints.health.enabled=true
# endpoints.features.enabled=true
# endpoints.info.enabled=true
# endpoints.loggers.enabled=true
# endpoints.logfile.enabled=true
# endpoints.trace.enabled=true
# endpoints.docs.enabled=false
# endpoints.heapdump.enabled=true

# IP address may be enough to protect all endpoints.
# If you wish to protect the admin pages via CAS itself, configure the rest.
# cas.adminPagesSecurity.ip=127\.0\.0\.1
# cas.adminPagesSecurity.loginUrl=https://sso.example.org/cas/login
# cas.adminPagesSecurity.service=https://sso.example.org/cas/status/dashboard
# cas.adminPagesSecurity.users=file:/etc/cas/config/adminusers.properties
# cas.adminPagesSecurity.adminRoles[0]=ROLE_ADMIN

# cas.adminPagesSecurity.actuatorEndpointsEnabled=true
```

The format of the `adminusers.properties` file which houses a list of authorized users to access the admin pages via CAS is:

```properties
# casuser=notused,ROLE_ADMIN
```

The format of the file is as such:

- `casuser`: This is the authenticated user id received from CAS
- `notused`: This is the password field that isn't used by CAS. You could literally put any value you want in its place.
- `ROLE_ADMIN`: Role assigned to the authorized user as an attribute, which is then cross checked against CAS configuration.

### Admin Status Endpoints With Spring Security

Status endpoints may also be secured by Spring Security. You can define the authentication scheme/paths via the below settings.

```properties
# security.ignored[0]=/**
security.filterOrder=0
security.requireSsl=true
security.sessions=if_required
security.user.name=<predefined-userid>
security.user.password=<predefined-password>
security.user.role=ACTUATOR
```

#### Basic AuthN

```properties
security.basic.authorizeMode=none|role|authenticated
security.basic.enabled=true
security.basic.path=/cas/status/**
security.basic.realm=CAS
```

#### JDBC

```properties
# cas.adminPagesSecurity.jdbc.query=SELECT username,password,enabled FROM users WHERE username=?
# cas.adminPagesSecurity.jdbc.healthQuery=
# cas.adminPagesSecurity.jdbc.isolateInternalQueries=false
# cas.adminPagesSecurity.jdbc.url=jdbc:hsqldb:mem:cas-hsql-database
# cas.adminPagesSecurity.jdbc.failFast=true
# cas.adminPagesSecurity.jdbc.isolationLevelName=ISOLATION_READ_COMMITTED
# cas.adminPagesSecurity.jdbc.dialect=org.hibernate.dialect.HSQLDialect
# cas.adminPagesSecurity.jdbc.leakThreshold=10
# cas.adminPagesSecurity.jdbc.propagationBehaviorName=PROPAGATION_REQUIRED
# cas.adminPagesSecurity.jdbc.batchSize=1
# cas.adminPagesSecurity.jdbc.user=sa
# cas.adminPagesSecurity.jdbc.ddlAuto=create-drop
# cas.adminPagesSecurity.jdbc.maxAgeDays=180
# cas.adminPagesSecurity.jdbc.password=
# cas.adminPagesSecurity.jdbc.autocommit=false
# cas.adminPagesSecurity.jdbc.driverClass=org.hsqldb.jdbcDriver
# cas.adminPagesSecurity.jdbc.idleTimeout=5000
```

#### LDAP


```properties
# cas.adminPagesSecurity.ldap.type=AD|AUTHENTICATED|DIRECT|ANONYMOUS|SASL

# cas.adminPagesSecurity.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.adminPagesSecurity.ldap.connectionStrategy=
# cas.adminPagesSecurity.ldap.useSsl=true
# cas.adminPagesSecurity.ldap.useStartTls=false
# cas.adminPagesSecurity.ldap.connectTimeout=5000
# cas.adminPagesSecurity.ldap.baseDn=dc=example,dc=org
# cas.adminPagesSecurity.ldap.userFilter=cn={user}
# cas.adminPagesSecurity.ldap.subtreeSearch=true
# cas.adminPagesSecurity.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.adminPagesSecurity.ldap.bindCredential=Password

# cas.adminPagesSecurity.ldap.enhanceWithEntryResolver=true
# cas.adminPagesSecurity.ldap.dnFormat=uid=%s,ou=people,dc=example,dc=org
# cas.adminPagesSecurity.ldap.principalAttributePassword=password

# cas.adminPagesSecurity.ldap.saslMechanism=GSSAPI|DIGEST_MD5|CRAM_MD5|EXTERNAL
# cas.adminPagesSecurity.ldap.saslRealm=EXAMPLE.COM
# cas.adminPagesSecurity.ldap.saslAuthorizationId=
# cas.adminPagesSecurity.ldap.saslMutualAuth=
# cas.adminPagesSecurity.ldap.saslQualityOfProtection=
# cas.adminPagesSecurity.ldap.saslSecurityStrength=

# cas.adminPagesSecurity.ldap.trustCertificates=
# cas.adminPagesSecurity.ldap.keystore=
# cas.adminPagesSecurity.ldap.keystorePassword=
# cas.adminPagesSecurity.ldap.keystoreType=JKS|JCEKS|PKCS12

# cas.adminPagesSecurity.ldap.minPoolSize=3
# cas.adminPagesSecurity.ldap.maxPoolSize=10
# cas.adminPagesSecurity.ldap.validateOnCheckout=true
# cas.adminPagesSecurity.ldap.validatePeriodically=true
# cas.adminPagesSecurity.ldap.validatePeriod=600

# cas.adminPagesSecurity.ldap.ldapAuthz.groupAttribute=
# cas.adminPagesSecurity.ldap.ldapAuthz.groupPrefix=
# cas.adminPagesSecurity.ldap.ldapAuthz.groupFilter=
# cas.adminPagesSecurity.ldap.ldapAuthz.rolePrefix=ROLE_
# cas.adminPagesSecurity.ldap.ldapAuthz.roleAttribute=uugid
```

## Web Application Session

Control the web application session behavior.

```properties
# server.session.timeout=300
# server.session.cookie.httpOnly=true
# server.session.trackingModes=COOKIE
```

## Views

Control how CAS should treat views and other UI elements.

To learn more about this topic, [please review this guide](User-Interface-Customization-Views.html).

```properties
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.cache=false
# spring.thymeleaf.prefix=classpath:/templates/

# cas.view.cas2.success=protocol/2.0/casServiceValidationSuccess
# cas.view.cas2.failure=protocol/2.0/casServiceValidationFailure
# cas.view.cas2.proxy.success=protocol/2.0/casProxySuccessView
# cas.view.cas2.proxy.failure=protocol/2.0/casProxyFailureView

# cas.view.cas3.success=protocol/3.0/casServiceValidationSuccess
# cas.view.cas3.failure=protocol/3.0/casServiceValidationFailure
# cas.view.cas3.releaseProtocolAttributes=true

# cas.view.defaultRedirectUrl=https://www.github.com
```

## Logging

Control the location and other settings of the CAS logging configuration.
To learn more about this topic, [please review this guide](Logging.html).

```properties
# logging.config=file:/etc/cas/log4j2.xml
server.contextParameters.isLog4jAutoInitializationDisabled=true

# Control log levels via properties
# logging.level.org.apereo.cas=DEBUG
```

To disable log sanitization, start the container with the system property `CAS_TICKET_ID_SANITIZE_SKIP=true`.

## AspectJ Configuration

```properties
# spring.aop.auto=true
# spring.aop.proxyTargetClass=true
```

## Authentication Attributes

Set of authentication attributes that are retrieved by the principal resolution process,
typically via some component of [Person Directory](..\integration\Attribute-Resolution.html)
from a number of attribute sources unless noted otherwise by the specific authentication scheme.

If multiple attribute repository sources are defined, they are added into a list
and their results are cached and merged.

```properties
# cas.authn.attributeRepository.expireInMinutes=30
# cas.authn.attributeRepository.maximumCacheSize=10000
# cas.authn.attributeRepository.merger=REPLACE|ADD|MERGE

# Attributes that you wish to resolve for the principal
# cas.authn.attributeRepository.attributes.uid=uid
# cas.authn.attributeRepository.attributes.displayName=displayName
# cas.authn.attributeRepository.attributes.cn=commonName
# cas.authn.attributeRepository.attributes.affiliation=groupMembership
```

Since all attributes for all sources are defined in the same common block,
that means all sources will attempt to resolve the same block of defined attributes equally.
CAS does not care about the source owner of attributes. It finds them where they can be found and otherwise, it moves on.
This means that certain number of attributes can be resolved via one source, and the remaining attributes
may be resolved via another. If there are commonalities across sources, the merger shall decide the final result and behavior.

The story in plain english is:

- I have a bunch of attributes that I wish to resolve for the authenticated principal.
- I have a bunch of sources from which said attributes are retrieved.
- Figure it out.

Note that attribute repository sources, if/when defined, execute in a specific order.
This is important to take into account when attribute merging may take place.
By default, the execution order is the following but can be adjusted per source:

1. LDAP
2. JDBC
3. JSON
4. Groovy
5. Shibboleth
6. Static Stub

Note that if no other attribute source is defined and if attributes are not directly retrieved
as part of primary authentication, then a stub/static source will be created
based on the defined attributes, if any.

Note that if no *explicit* attribute mappings are defined, all permitted attributes on the record
may be retrieved by CAS from the attribute repository source and made available to the principal. On the other hand,
if explicit attribute mappings are defined, then *only mapped attributes* are retrieved.

### LDAP

If you wish to directly and separately retrieve attributes from an LDAP source,
the following settings are then relevant:

```properties
# cas.authn.attributeRepository.ldap[0].ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.authn.attributeRepository.ldap[0].connectionStrategy=
# cas.authn.attributeRepository.ldap[0].order=0
# cas.authn.attributeRepository.ldap[0].useSsl=true
# cas.authn.attributeRepository.ldap[0].useStartTls=false
# cas.authn.attributeRepository.ldap[0].connectTimeout=5000
# cas.authn.attributeRepository.ldap[0].baseDn=dc=example,dc=org
# cas.authn.attributeRepository.ldap[0].userFilter=cn={user}
# cas.authn.attributeRepository.ldap[0].subtreeSearch=true
# cas.authn.attributeRepository.ldap[0].bindDn=cn=Directory Manager,dc=example,dc=org
# cas.authn.attributeRepository.ldap[0].bindCredential=Password
# cas.authn.attributeRepository.ldap[0].trustCertificates=
# cas.authn.attributeRepository.ldap[0].keystore=
# cas.authn.attributeRepository.ldap[0].keystorePassword=
# cas.authn.attributeRepository.ldap[0].keystoreType=JKS|JCEKS|PKCS12
# cas.authn.attributeRepository.ldap[0].minPoolSize=3
# cas.authn.attributeRepository.ldap[0].maxPoolSize=10
# cas.authn.attributeRepository.ldap[0].validateOnCheckout=true
# cas.authn.attributeRepository.ldap[0].validatePeriodically=true
# cas.authn.attributeRepository.ldap[0].validatePeriod=600
# cas.authn.attributeRepository.ldap[0].failFast=true
# cas.authn.attributeRepository.ldap[0].idleTime=500
# cas.authn.attributeRepository.ldap[0].prunePeriod=600
# cas.authn.attributeRepository.ldap[0].blockWaitTime=5000
# cas.authn.attributeRepository.ldap[0].providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider

# cas.authn.attributeRepository.ldap[0].validator.type=NONE|SEARCH|COMPARE
# cas.authn.attributeRepository.ldap[0].validator.baseDn=
# cas.authn.attributeRepository.ldap[0].validator.searchFilter=(objectClass=*)
# cas.authn.attributeRepository.ldap[0].validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.authn.attributeRepository.ldap[0].validator.attributeName=objectClass
# cas.authn.attributeRepository.ldap[0].validator.attributeValues=top
# cas.authn.attributeRepository.ldap[0].validator.dn=
```

### Groovy

If you wish to directly and separately retrieve attributes from a Groovy script,
the following settings are then relevant:

```properties
# cas.authn.attributeRepository.groovy[0].config.location=file:/etc/cas/attributes.groovy
# cas.authn.attributeRepository.groovy[0].caseInsensitive=false
# cas.authn.attributeRepository.groovy[0].order=0
```

The Groovy script may be designed as:

```groovy
import java.util.List
import java.util.Map

class SampleGroovyPersonAttributeDao {
    def Map<String, List<Object>> run(final Object... args) {
        def uid = args[0]
        def logger = args[1];
        def casProperties = args[2]
        def casApplicationContext = args[3]

        logger.debug("[{}]: The received uid is [{}]", this.class.simpleName, uid)
        return[username:[uid], likes:["cheese", "food"], id:[1234,2,3,4,5], another:"attribute"]
    }
}
```

### JSON

If you wish to directly and separately retrieve attributes from a static JSON source,
the following settings are then relevant:

```properties
# cas.authn.attributeRepository.json[0].config.location=file://etc/cas/attribute-repository.json
# cas.authn.attributeRepository.json[0].order=0
```

The format of the file may be:

```json
{
    "user1": {
        "firstName":["Json1"],
        "lastName":["One"]
    },
    "user2": {
        "firstName":["Json2"],
        "eduPersonAffiliation":["employee", "student"]
    }
}
```

### JDBC

If you wish to directly and separately retrieve attributes from a JDBC source,
the following settings are then relevant:

```properties
# cas.authn.attributeRepository.jdbc[0].singleRow=true
# cas.authn.attributeRepository.jdbc[0].order=0
# cas.authn.attributeRepository.jdbc[0].requireAllAttributes=true
# cas.authn.attributeRepository.jdbc[0].caseCanonicalization=NONE|LOWER|UPPER
# cas.authn.attributeRepository.jdbc[0].queryType=OR|AND

# Used only when there is a mapping of many rows to one user
# cas.authn.attributeRepository.jdbc[0].columnMappings.columnAttrName1=columnAttrValue1
# cas.authn.attributeRepository.jdbc[0].columnMappings.columnAttrName2=columnAttrValue2
# cas.authn.attributeRepository.jdbc[0].columnMappings.columnAttrName3=columnAttrValue3

# cas.authn.attributeRepository.jdbc[0].sql=SELECT * FROM table WHERE {0}
# cas.authn.attributeRepository.jdbc[0].username=uid
# cas.authn.attributeRepository.jdbc[0].healthQuery=
# cas.authn.attributeRepository.jdbc[0].isolateInternalQueries=false
# cas.authn.attributeRepository.jdbc[0].url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.attributeRepository.jdbc[0].failFast=true
# cas.authn.attributeRepository.jdbc[0].isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.attributeRepository.jdbc[0].dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.attributeRepository.jdbc[0].leakThreshold=10
# cas.authn.attributeRepository.jdbc[0].propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.attributeRepository.jdbc[0].batchSize=1
# cas.authn.attributeRepository.jdbc[0].user=sa
# cas.authn.attributeRepository.jdbc[0].ddlAuto=create-drop
# cas.authn.attributeRepository.jdbc[0].password=
# cas.authn.attributeRepository.jdbc[0].autocommit=false
# cas.authn.attributeRepository.jdbc[0].driverClass=org.hsqldb.jdbcDriver
# cas.authn.attributeRepository.jdbc[0].idleTimeout=5000
# cas.authn.attributeRepository.jdbc[0].pool.suspension=false
# cas.authn.attributeRepository.jdbc[0].pool.minSize=6
# cas.authn.attributeRepository.jdbc[0].pool.maxSize=18
# cas.authn.attributeRepository.jdbc[0].pool.maxIdleTime=1000
# cas.authn.attributeRepository.jdbc[0].pool.maxWait=2000
```

### Shibboleth Attribute Resolver

To learn more about this topic, [please review this guide](../integration/Attribute-Resolution.html).

```properties
# cas.shibAttributeResolver.resources=classpath:/attribute-resolver.xml
```


### Default Bundle

If you wish to release a default bundle of attributes to all applications,
and you would rather not duplicate the same attribute per every service definition,
then the following settings are relevant:

```properties
# cas.authn.attributeRepository.defaultAttributesToRelease=cn,givenName,uid,affiliation
```

To learn more about this topic, [please review this guide](../integration/Attribute-Release.html).

## Principal Resolution

In the event that a separate resolver is put into place, control
how the final principal should be constructed by default.

```properties
# cas.personDirectory.principalAttribute=
# cas.personDirectory.returnNull=false
# cas.personDirectory.principalResolutionFailureFatal=false
```

## Authentication Policy

Global authentication policy that is applied when
CAS attempts to vend and validate tickets.

```properties
# cas.authn.policy.requiredHandlerAuthenticationPolicyEnabled=false
```

### Any

Satisfied if any handler succeeds. Supports a tryAll flag to avoid short circuiting
and try every handler even if one prior succeeded.

```properties
# cas.authn.policy.any.tryAll=false
```

### All

Satisfied if and only if all given credentials are successfully authenticated.
Support for multiple credentials is new in CAS and this handler
would only be acceptable in a multi-factor authentication situation.

```properties
# cas.authn.policy.all.enabled=true
```

### NotPrevented

Satisfied if an only if the authentication event is not blocked by a `PreventedException`.

```properties
# cas.authn.policy.notPrevented.enabled=true
```

### Required

Satisfied if an only if a specified handler successfully authenticates its credential.

```properties
# cas.authn.policy.req.tryAll=false
# cas.authn.policy.req.handlerName=handlerName
# cas.authn.policy.req.enabled=true
```

## Authentication Throttling

CAS provides a facility for limiting failed login attempts to support password guessing and related abuse scenarios.
To learn more about this topic, [please review this guide](Configuring-Authentication-Throttling.html).


```properties
# cas.authn.throttle.usernameParameter=username
# cas.authn.throttle.startDelay=10000
# cas.authn.throttle.repeatInterval=20000
# cas.authn.throttle.appcode=CAS

# cas.authn.throttle.failure.threshold=100
# cas.authn.throttle.failure.code=AUTHENTICATION_FAILED
# cas.authn.throttle.failure.rangeSeconds=60
```

### Database

Queries the data source used by the CAS audit facility to prevent successive failed login attempts for a particular username from the
same IP address.

```properties
# cas.authn.throttle.jdbc.auditQuery=SELECT AUD_DATE FROM COM_AUDIT_TRAIL WHERE AUD_CLIENT_IP = ? AND AUD_USER = ? AND AUD_ACTION = ? AND APPLIC_CD = ? AND AUD_DATE >= ? ORDER BY AUD_DATE DESC
# cas.authn.throttle.jdbc.healthQuery=
# cas.authn.throttle.jdbc.isolateInternalQueries=false
# cas.authn.throttle.jdbc.url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.throttle.jdbc.failFast=true
# cas.authn.throttle.jdbc.isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.throttle.jdbc.dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.throttle.jdbc.leakThreshold=10
# cas.authn.throttle.jdbc.propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.throttle.jdbc.batchSize=1
# cas.authn.throttle.jdbc.user=sa
# cas.authn.throttle.jdbc.ddlAuto=create-drop
# cas.authn.throttle.jdbc.maxAgeDays=180
# cas.authn.throttle.jdbc.password=
# cas.authn.throttle.jdbc.autocommit=false
# cas.authn.throttle.jdbc.driverClass=org.hsqldb.jdbcDriver
# cas.authn.throttle.jdbc.idleTimeout=5000

# cas.authn.throttle.jdbc.pool.suspension=false
# cas.authn.throttle.jdbc.pool.minSize=6
# cas.authn.throttle.jdbc.pool.maxSize=18
# cas.authn.throttle.jdbc.pool.maxIdleTime=1000
# cas.authn.throttle.jdbc.pool.maxWait=2000
```

## Adaptive Authentication

Control how CAS authentication should adapt itself to incoming client requests.
To learn more about this topic, [please review this guide](Configuring-Adaptive-Authentication.html).

```properties
# cas.authn.adaptive.rejectCountries=United.+
# cas.authn.adaptive.rejectBrowsers=Gecko.+
# cas.authn.adaptive.rejectIpAddresses=127.+

# cas.authn.adaptive.requireMultifactor.mfa-duo=127.+|United.+|Gecko.+
```

## Risk-based Authentication

Evaluate suspicious authentication requests and take action.
To learn more about this topic, [please review this guide](Configuring-RiskBased-Authentication.html).

```properties
# cas.authn.adaptive.risk.threshold=0.6
# cas.authn.adaptive.risk.daysInRecentHistory=30

# cas.authn.adaptive.risk.ip.enabled=false

# cas.authn.adaptive.risk.agent.enabled=false

# cas.authn.adaptive.risk.geoLocation.enabled=false

# cas.authn.adaptive.risk.dateTime.enabled=false
# cas.authn.adaptive.risk.dateTime.windowInHours=2

# cas.authn.adaptive.risk.response.blockAttempt=false

# cas.authn.adaptive.risk.response.mfaProvider=
# cas.authn.adaptive.risk.response.riskyAuthenticationAttribute=triggeredRiskBasedAuthentication

# cas.authn.adaptive.risk.response.mail.from=
# cas.authn.adaptive.risk.response.mail.text=
# cas.authn.adaptive.risk.response.mail.subject=
# cas.authn.adaptive.risk.response.mail.cc=
# cas.authn.adaptive.risk.response.mail.bcc=
# cas.authn.adaptive.risk.response.mail.attributeName=mail

# cas.authn.adaptive.risk.response.sms.from=
# cas.authn.adaptive.risk.response.sms.text=
# cas.authn.adaptive.risk.response.sms.attributeName=phone
```

## Email Submissions

```properties
# spring.mail.host=
# spring.mail.port=
# spring.mail.username=
# spring.mail.password=
# spring.mail.testConnection=true
# spring.mail.properties.mail.smtp.auth=true
# spring.mail.properties.mail.smtp.starttls.enable=true
```

## SMS Messaging

To learn more about this topic, [please review this guide](SMS-Messaging-Configuration.html).

### Twillio

```properties
# cas.twilio.accountId=
# cas.twilio.token=
```

### TextMagic

```properties
# cas.textMagic.username=
# cas.textMagic.token=
```

## GeoTracking

To learn more about this topic, [please review this guide](GeoTracking-Authentication-Requests.html).

### GoogleMaps GeoTracking

Used to geo-profile authentication events.

```properties
# cas.googleMaps.apiKey=
# cas.googleMaps.clientId=
# cas.googleMaps.clientSecret=
# cas.googleMaps.connectTimeout=3000
# cas.googleMaps.googleAppsEngine=false
```

### Maxmind GeoTracking

Used to geo-profile authentication events.

```properties
# cas.maxmind.cityDatabase=file:/etc/cas/maxmind/GeoLite2-City.mmdb
# cas.maxmind.countryDatabase=file:/etc/cas/maxmind/GeoLite2-Country.mmdb
```

## Digest Authentication

To learn more about this topic, [please review this guide](Digest-Authentication.html).

```properties
# cas.authn.digest.users.casuser=3530292c24102bac7ced2022e5f1036a
# cas.authn.digest.users.anotheruser=7530292c24102bac7ced2022e5f1036b
# cas.authn.digest.realm=CAS
# cas.authn.digest.name=
# cas.authn.digest.authenticationMethod=auth
```

## Radius Authentication

To learn more about this topic, [please review this guide](RADIUS-Authentication.html).

```properties
# cas.authn.radius.server.nasPortId=-1
# cas.authn.radius.server.nasRealPort=-1
# cas.authn.radius.server.protocol=EAP_MSCHAPv2
# cas.authn.radius.server.retries=3
# cas.authn.radius.server.nasPortType=-1
# cas.authn.radius.server.nasPort=-1
# cas.authn.radius.server.nasIpAddress=
# cas.authn.radius.server.nasIpv6Address=
# cas.authn.radius.server.nasIdentifier=-1

# cas.authn.radius.client.authenticationPort=1812
# cas.authn.radius.client.sharedSecret=N0Sh@ar3d$ecReT
# cas.authn.radius.client.socketTimeout=0
# cas.authn.radius.client.inetAddress=localhost
# cas.authn.radius.client.accountingPort=1813

# cas.authn.radius.name=
# cas.authn.radius.failoverOnException=false
# cas.authn.radius.failoverOnAuthenticationFailure=false

# cas.authn.radius.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.radius.passwordEncoder.characterEncoding=
# cas.authn.radius.passwordEncoder.encodingAlgorithm=
# cas.authn.radius.passwordEncoder.secret=
# cas.authn.radius.passwordEncoder.strength=16

# cas.authn.radius.principalTransformation.suffix=
# cas.authn.radius.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.radius.principalTransformation.prefix=
```

## File (Whitelist) Authentication

To learn more about this topic, [please review this guide](Whitelist-Authentication.html).

```properties
# cas.authn.file.separator=::
# cas.authn.file.filename=file:///path/to/users/file
# cas.authn.file.name=

# cas.authn.file.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.file.passwordEncoder.characterEncoding=
# cas.authn.file.passwordEncoder.encodingAlgorithm=
# cas.authn.file.passwordEncoder.secret=
# cas.authn.file.passwordEncoder.strength=16

# cas.authn.file.principalTransformation.suffix=
# cas.authn.file.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.file.principalTransformation.prefix=
```

## Reject Users (Blacklist) Authentication

To learn more about this topic, [please review this guide](Blacklist-Authentication.html).

```properties
# cas.authn.reject.users=user1,user2
# cas.authn.reject.name=

# cas.authn.reject.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.reject.passwordEncoder.characterEncoding=
# cas.authn.reject.passwordEncoder.encodingAlgorithm=
# cas.authn.reject.passwordEncoder.secret=
# cas.authn.reject.passwordEncoder.strength=16

# cas.authn.reject.principalTransformation.suffix=
# cas.authn.reject.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.reject.principalTransformation.prefix=
```

## Database Authentication

To learn more about this topic, [please review this guide](Database-Authentication.html).

### Query Database Authentication

Authenticates a user by comparing the user password (which can be encoded with a password encoder)
against the password on record determined by a configurable database query.

```properties
# cas.authn.jdbc.query[0].sql=SELECT password FROM table WHERE name=?
# cas.authn.jdbc.query[0].healthQuery=
# cas.authn.jdbc.query[0].isolateInternalQueries=false
# cas.authn.jdbc.query[0].url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.jdbc.query[0].failFast=true
# cas.authn.jdbc.query[0].isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.jdbc.query[0].dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.jdbc.query[0].leakThreshold=10
# cas.authn.jdbc.query[0].propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.jdbc.query[0].batchSize=1
# cas.authn.jdbc.query[0].user=user
# cas.authn.jdbc.query[0].ddlAuto=create-drop
# cas.authn.jdbc.query[0].maxAgeDays=180
# cas.authn.jdbc.query[0].password=secret
# cas.authn.jdbc.query[0].autocommit=false
# cas.authn.jdbc.query[0].driverClass=org.hsqldb.jdbcDriver
# cas.authn.jdbc.query[0].idleTimeout=5000
# cas.authn.jdbc.query[0].credentialCriteria=
# cas.authn.jdbc.query[0].name=
# cas.authn.jdbc.query[0].order=0

# cas.authn.jdbc.query[0].passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.jdbc.query[0].passwordEncoder.characterEncoding=
# cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm=
# cas.authn.jdbc.query[0].passwordEncoder.secret=
# cas.authn.jdbc.query[0].passwordEncoder.strength=16

# cas.authn.jdbc.query[0].principalTransformation.suffix=
# cas.authn.jdbc.query[0].principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.jdbc.query[0].principalTransformation.prefix=
```

### Search Database Authentication

Searches for a user record by querying against a username and password; the user is authenticated if at least one result is found.

```properties
# cas.authn.jdbc.search[0].fieldUser=
# cas.authn.jdbc.search[0].tableUsers=
# cas.authn.jdbc.search[0].fieldPassword=
# cas.authn.jdbc.search[0].healthQuery=
# cas.authn.jdbc.search[0].isolateInternalQueries=false
# cas.authn.jdbc.search[0].url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.jdbc.search[0].failFast=true
# cas.authn.jdbc.search[0].isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.jdbc.search[0].dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.jdbc.search[0].leakThreshold=10
# cas.authn.jdbc.search[0].propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.jdbc.search[0].batchSize=1
# cas.authn.jdbc.search[0].user=sa
# cas.authn.jdbc.search[0].ddlAuto=create-drop
# cas.authn.jdbc.search[0].maxAgeDays=180
# cas.authn.jdbc.search[0].password=
# cas.authn.jdbc.search[0].autocommit=false
# cas.authn.jdbc.search[0].driverClass=org.hsqldb.jdbcDriver
# cas.authn.jdbc.search[0].idleTimeout=5000
# cas.authn.jdbc.search[0].credentialCriteria=
# cas.authn.jdbc.search[0].name=
# cas.authn.jdbc.search[0].order=0

# cas.authn.jdbc.search[0].passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.jdbc.search[0].passwordEncoder.characterEncoding=
# cas.authn.jdbc.search[0].passwordEncoder.encodingAlgorithm=
# cas.authn.jdbc.search[0].passwordEncoder.secret=
# cas.authn.jdbc.search[0].passwordEncoder.strength=16

# cas.authn.jdbc.search[0].principalTransformation.suffix=
# cas.authn.jdbc.search[0].principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.jdbc.search[0].principalTransformation.prefix=
```

### Bind Database Authentication

Authenticates a user by attempting to create a database connection using the username and (hashed) password.

```properties
# cas.authn.jdbc.bind[0].healthQuery=
# cas.authn.jdbc.bind[0].isolateInternalQueries=false
# cas.authn.jdbc.bind[0].url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.jdbc.bind[0].failFast=true
# cas.authn.jdbc.bind[0].isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.jdbc.bind[0].dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.jdbc.bind[0].leakThreshold=10
# cas.authn.jdbc.bind[0].propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.jdbc.bind[0].batchSize=1
# cas.authn.jdbc.bind[0].user=sa
# cas.authn.jdbc.bind[0].ddlAuto=create-drop
# cas.authn.jdbc.bind[0].maxAgeDays=180
# cas.authn.jdbc.bind[0].password=
# cas.authn.jdbc.bind[0].autocommit=false
# cas.authn.jdbc.bind[0].driverClass=org.hsqldb.jdbcDriver
# cas.authn.jdbc.bind[0].idleTimeout=5000
# cas.authn.jdbc.bind[0].credentialCriteria=
# cas.authn.jdbc.bind[0].name=
# cas.authn.jdbc.bind[0].order=0

# cas.authn.jdbc.bind[0].passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.jdbc.bind[0].passwordEncoder.characterEncoding=
# cas.authn.jdbc.bind[0].passwordEncoder.encodingAlgorithm=
# cas.authn.jdbc.bind[0].passwordEncoder.secret=
# cas.authn.jdbc.bind[0].passwordEncoder.strength=16

# cas.authn.jdbc.bind[0].principalTransformation.suffix=
# cas.authn.jdbc.bind[0].principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.jdbc.bind[0].principalTransformation.prefix=
```

### Encode Database Authentication

A JDBC querying handler that will pull back the password and the private salt value for a user and validate the encoded
password using the public salt value. Assumes everything is inside the same database table. Supports settings for
number of iterations as well as private salt.

This password encoding method combines the private Salt and the public salt which it prepends to the password before hashing.
If multiple iterations are used, the bytecode hash of the first iteration is rehashed without the salt values. The final hash
is converted to hex before comparing it to the database value.

```properties
# cas.authn.jdbc.encode[0].numberOfIterations=0
# cas.authn.jdbc.encode[0].numberOfIterationsFieldName=numIterations
# cas.authn.jdbc.encode[0].saltFieldName=salt
# cas.authn.jdbc.encode[0].staticSalt=
# cas.authn.jdbc.encode[0].sql=
# cas.authn.jdbc.encode[0].algorithmName=
# cas.authn.jdbc.encode[0].passwordFieldName=password
# cas.authn.jdbc.encode[0].healthQuery=
# cas.authn.jdbc.encode[0].isolateInternalQueries=false
# cas.authn.jdbc.encode[0].url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.jdbc.encode[0].failFast=true
# cas.authn.jdbc.encode[0].isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.jdbc.encode[0].dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.jdbc.encode[0].leakThreshold=10
# cas.authn.jdbc.encode[0].propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.jdbc.encode[0].batchSize=1
# cas.authn.jdbc.encode[0].user=sa
# cas.authn.jdbc.encode[0].ddlAuto=create-drop
# cas.authn.jdbc.encode[0].maxAgeDays=180
# cas.authn.jdbc.encode[0].password=
# cas.authn.jdbc.encode[0].autocommit=false
# cas.authn.jdbc.encode[0].driverClass=org.hsqldb.jdbcDriver
# cas.authn.jdbc.encode[0].idleTimeout=5000
# cas.authn.jdbc.encode[0].credentialCriteria=
# cas.authn.jdbc.encode[0].name=
# cas.authn.jdbc.encode[0].order=0

# cas.authn.jdbc.encode[0].passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.jdbc.encode[0].passwordEncoder.characterEncoding=
# cas.authn.jdbc.encode[0].passwordEncoder.encodingAlgorithm=
# cas.authn.jdbc.encode[0].passwordEncoder.secret=
# cas.authn.jdbc.encode[0].passwordEncoder.strength=16

# cas.authn.jdbc.encode[0].principalTransformation.suffix=
# cas.authn.jdbc.encode[0].principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.jdbc.encode[0].principalTransformation.prefix=
```

## MongoDb Authentication

To learn more about this topic, [please review this guide](MongoDb-Authentication.html).

```properties
# cas.authn.mongo.mongoHostUri=mongodb://uri
# cas.authn.mongo.usernameAttribute=username
# cas.authn.mongo.attributes=
# cas.authn.mongo.passwordAttribute=password
# cas.authn.mongo.collectionName=users
# cas.authn.mongo.name=

# cas.authn.mongo.principalTransformation.suffix=
# cas.authn.mongo.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.mongo.principalTransformation.prefix=

# cas.authn.mongo.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.mongo.passwordEncoder.characterEncoding=
# cas.authn.mongo.passwordEncoder.encodingAlgorithm=
# cas.authn.mongo.passwordEncoder.secret=
# cas.authn.mongo.passwordEncoder.strength=16
```

## LDAP Authentication

CAS authenticates a username/password against an LDAP directory such as Active Directory or OpenLDAP.
There are numerous directory architectures and we provide configuration for four common cases.

Note that CAS will automatically create the appropriate components internally
based on the settings specified below. If you wish to authenticate against more than one LDAP
server, simply increment the index and specify the settings for the next LDAP server.

**Note:** Failure to specify adequate properties such as `type`, `ldapUrl`, `baseDn`, etc
will simply deactivate LDAP authentication altogether silently.

**Note:** Attributes retrieved as part of LDAP authentication are merged with all attributes
retrieved from [other attribute repository sources](#authentication-attributes), if any.
Attributes retrieved directly as part of LDAP authentication trump all other attributes.

To learn more about this topic, [please review this guide](LDAP-Authentication.html).

### Active Directory

Users authenticate with `sAMAccountName`.

### Authenticated Search

Manager bind/search type of authentication.

- If `principalAttributePassword` is empty then a user simple bind is done to validate credentials
- Otherwise the given attribute is compared with the given `principalAttributePassword` using the `SHA` encrypted value of it

### Anonymous Search

Anonymous search.

- If `principalAttributePassword` is empty then a user simple bind is done to validate credentials
- Otherwise the given attribute is compared with the given `principalAttributePassword` using the `SHA` encrypted value of it

### Direct Bind

Compute user DN from format string and perform simple bind. This is relevant when
no search is required to compute the DN needed for a bind operation.

There are two requirements for this use case:

1. All users are under a single branch in the directory, e.g. `ou=Users,dc=example,dc=org`.
2. The username provided on the CAS login form is part of the DN, e.g. `uid=%s,ou=Users,dc=exmaple,dc=org`.

### Connection Strategies

If multiple URLs are provided as the ldapURL this describes how each URL will be processed.

| Provider              | Description              
|-----------------------|-----------------
| `DEFAULT`             | The default JNDI provider behavior will be used.    
| `ACTIVE_PASSIVE`      | First LDAP will be used for every request unless it fails and then the next shall be used.    
| `ROUND_ROBIN`         | For each new connection the next url in the list will be used.      
| `RANDOM`              | For each new connection a random LDAP url will be selected.
| `DNS_SRV`             | LDAP urls based on DNS SRV records of the configured/given LDAP url will be used.  

```properties
# cas.authn.ldap[0].type=AD|AUTHENTICATED|DIRECT|ANONYMOUS|SASL

# cas.authn.ldap[0].ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.authn.ldap[0].connectionStrategy=
# cas.authn.ldap[0].useSsl=true
# cas.authn.ldap[0].useStartTls=false
# cas.authn.ldap[0].connectTimeout=5000
# cas.authn.ldap[0].baseDn=dc=example,dc=org
# cas.authn.ldap[0].userFilter=cn={user}
# cas.authn.ldap[0].subtreeSearch=true
# cas.authn.ldap[0].usePasswordPolicy=true
# cas.authn.ldap[0].bindDn=cn=Directory Manager,dc=example,dc=org
# cas.authn.ldap[0].bindCredential=Password

# cas.authn.ldap[0].enhanceWithEntryResolver=true
# cas.authn.ldap[0].dnFormat=uid=%s,ou=people,dc=example,dc=org
# cas.authn.ldap[0].principalAttributeId=uid
# cas.authn.ldap[0].principalAttributePassword=password
# cas.authn.ldap[0].principalAttributeList=sn,cn:commonName,givenName,eduPersonTargettedId:SOME_IDENTIFIER
# cas.authn.ldap[0].allowMultiplePrincipalAttributeValues=true
# cas.authn.ldap[0].allowMissingPrincipalAttributeValue=true
# cas.authn.ldap[0].additionalAttributes=
# cas.authn.ldap[0].credentialCriteria=

# cas.authn.ldap[0].saslMechanism=GSSAPI|DIGEST_MD5|CRAM_MD5|EXTERNAL
# cas.authn.ldap[0].saslRealm=EXAMPLE.COM
# cas.authn.ldap[0].saslAuthorizationId=
# cas.authn.ldap[0].saslMutualAuth=
# cas.authn.ldap[0].saslQualityOfProtection=
# cas.authn.ldap[0].saslSecurityStrength=

# cas.authn.ldap[0].trustCertificates=
# cas.authn.ldap[0].keystore=
# cas.authn.ldap[0].keystorePassword=
# cas.authn.ldap[0].keystoreType=JKS|JCEKS|PKCS12

# cas.authn.ldap[0].minPoolSize=3
# cas.authn.ldap[0].maxPoolSize=10
# cas.authn.ldap[0].validateOnCheckout=true
# cas.authn.ldap[0].validatePeriodically=true
# cas.authn.ldap[0].validatePeriod=600

# cas.authn.ldap[0].failFast=true
# cas.authn.ldap[0].idleTime=5000
# cas.authn.ldap[0].prunePeriod=5000
# cas.authn.ldap[0].blockWaitTime=5000

# cas.authn.ldap[0].providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.authn.ldap[0].allowMultipleDns=false

# cas.authn.ldap[0].searchEntryHandlers[0].type=CASE_CHANGE|DN_ATTRIBUTE_ENTRY|MERGE|OBJECT_GUID|OBJECT_SID|PRIMARY_GROUP|RANGE_ENTRY|RECURSIVE_ENTRY

# cas.authn.ldap[0].searchEntryHandlers[0].caseChange.dnCaseChange=NONE|LOWER|UPPER
# cas.authn.ldap[0].searchEntryHandlers[0].caseChange.attributeNameCaseChange=NONE|LOWER|UPPER
# cas.authn.ldap[0].searchEntryHandlers[0].caseChange.attributeValueCaseChange=NONE|LOWER|UPPER
# cas.authn.ldap[0].searchEntryHandlers[0].caseChange.attributeNames=

# cas.authn.ldap[0].searchEntryHandlers[0].dnAttribute.dnAttributeName=entryDN
# cas.authn.ldap[0].searchEntryHandlers[0].dnAttribute.addIfExists=false

# cas.authn.ldap[0].searchEntryHandlers[0].primaryGroupId.groupFilter=(&(objectClass=group)(objectSid={0}))
# cas.authn.ldap[0].searchEntryHandlers[0].primaryGroupId.baseDn=

# cas.authn.ldap[0].searchEntryHandlers[0].mergeAttribute.mergeAttributeName=
# cas.authn.ldap[0].searchEntryHandlers[0].mergeAttribute.attribueNames=

# cas.authn.ldap[0].searchEntryHandlers[0].recursive.searchAttribute=
# cas.authn.ldap[0].searchEntryHandlers[0].recursive.mergeAttributes=

# cas.authn.ldap[0].name=
# cas.authn.ldap[0].order=0

# cas.authn.ldap[0].passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.ldap[0].passwordEncoder.characterEncoding=
# cas.authn.ldap[0].passwordEncoder.encodingAlgorithm=
# cas.authn.ldap[0].passwordEncoder.secret=
# cas.authn.ldap[0].passwordEncoder.strength=16

# cas.authn.ldap[0].principalTransformation.suffix=
# cas.authn.ldap[0].principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.ldap[0].principalTransformation.prefix=

# cas.authn.ldap[0].validator.type=NONE|SEARCH|COMPARE
# cas.authn.ldap[0].validator.baseDn=
# cas.authn.ldap[0].validator.searchFilter=(objectClass=*)
# cas.authn.ldap[0].validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.authn.ldap[0].validator.attributeName=objectClass
# cas.authn.ldap[0].validator.attributeValues=top
# cas.authn.ldap[0].validator.dn=

# cas.authn.ldap[0].passwordPolicy.type=GENERIC|AD|FreeIPA|EDirectory
# cas.authn.ldap[0].passwordPolicy.enabled=true
# cas.authn.ldap[0].passwordPolicy.policyAttributes.accountLocked=javax.security.auth.login.AccountLockedException
# cas.authn.ldap[0].passwordPolicy.loginFailures=5
# cas.authn.ldap[0].passwordPolicy.warningAttributeValue=
# cas.authn.ldap[0].passwordPolicy.warningAttributeName=
# cas.authn.ldap[0].passwordPolicy.displayWarningOnMatch=true
# cas.authn.ldap[0].passwordPolicy.warnAll=true
# cas.authn.ldap[0].passwordPolicy.warningDays=30
# cas.authn.ldap[0].passwordPolicy.url=https://password.example.edu/change
```

## REST Authentication

This allows the CAS server to reach to a remote REST endpoint via a `POST`.
To learn more about this topic, [please review this guide](Rest-Authentication.html).

```properties
# cas.authn.rest.uri=https://...
# cas.authn.rest.name=

# cas.authn.rest.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.rest.passwordEncoder.characterEncoding=
# cas.authn.rest.passwordEncoder.encodingAlgorithm=
# cas.authn.rest.passwordEncoder.secret=
# cas.authn.rest.passwordEncoder.strength=16
```

## Google Apps Authentication

Authenticate via CAS into Google Apps services and applications.
To learn more about this topic, [please review this guide](../integration/Google-Apps-Integration.html).

```properties
# cas.googleApps.publicKeyLocation=file:/etc/cas/public.key
# cas.googleApps.keyAlgorithm=RSA
# cas.googleApps.privateKeyLocation=file:/etc/cas/private.key
```

## OpenID Authentication

Allow CAS to become an OpenID authentication provider.
To learn more about this topic, [please review this guide](../protocol/OpenID-Protocol.html).

```properties
# cas.authn.openid.enforceRpId=false
# cas.authn.openid.principal.principalAttribute=
# cas.authn.openid.principal.returnNull=false
# cas.authn.openid.name=
```

## SPNEGO Authentication

To learn more about this topic, [please review this guide](SPNEGO-Authentication.html).

```properties
# cas.authn.spnego.kerberosConf=
# cas.authn.spnego.mixedModeAuthentication=false
# cas.authn.spnego.cachePolicy=600
# cas.authn.spnego.timeout=300000
# cas.authn.spnego.jcifsServicePrincipal=HTTP/cas.example.com@EXAMPLE.COM
# cas.authn.spnego.jcifsNetbiosWins=
# cas.authn.spnego.loginConf=
# cas.authn.spnego.ntlmAllowed=true
# cas.authn.spnego.hostNamePatternString=.+
# cas.authn.spnego.jcifsUsername=
# cas.authn.spnego.useSubjectCredsOnly=false
# cas.authn.spnego.supportedBrowsers=MSIE,Trident,Firefox,AppleWebKit
# cas.authn.spnego.jcifsDomainController=
# cas.authn.spnego.dnsTimeout=2000
# cas.authn.spnego.hostNameClientActionStrategy=hostnameSpnegoClientAction
# cas.authn.spnego.kerberosKdc=172.10.1.10
# cas.authn.spnego.alternativeRemoteHostAttribute=alternateRemoteHeader
# cas.authn.spnego.jcifsDomain=
# cas.authn.spnego.ipsToCheckPattern=127.+
# cas.authn.spnego.kerberosDebug=
# cas.authn.spnego.send401OnAuthenticationFailure=true
# cas.authn.spnego.kerberosRealm=EXAMPLE.COM
# cas.authn.spnego.ntlm=false
# cas.authn.spnego.principalWithDomainName=false
# cas.authn.spnego.jcifsServicePassword=
# cas.authn.spnego.jcifsPassword=
# cas.authn.spnego.spnegoAttributeName=distinguishedName
# cas.authn.spnego.name=

# cas.authn.spnego.principal.principalAttribute=
# cas.authn.spnego.principal.returnNull=false

# cas.authn.spnego.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.authn.spnego.ldap.connectionStrategy=
# cas.authn.spnego.ldap.baseDn=dc=example,dc=org
# cas.authn.spnego.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.authn.spnego.ldap.bindCredential=Password
# cas.authn.spnego.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.authn.spnego.ldap.connectTimeout=5000
# cas.authn.spnego.ldap.trustCertificates=
# cas.authn.spnego.ldap.keystore=
# cas.authn.spnego.ldap.keystorePassword=
# cas.authn.spnego.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.authn.spnego.ldap.minPoolSize=3
# cas.authn.spnego.ldap.maxPoolSize=10
# cas.authn.spnego.ldap.validateOnCheckout=true
# cas.authn.spnego.ldap.validatePeriodically=true
# cas.authn.spnego.ldap.validatePeriod=600
# cas.authn.spnego.ldap.failFast=true
# cas.authn.spnego.ldap.idleTime=500
# cas.authn.spnego.ldap.prunePeriod=600
# cas.authn.spnego.ldap.blockWaitTime=5000
# cas.authn.spnego.ldap.subtreeSearch=true
# cas.authn.spnego.ldap.useSsl=true
# cas.authn.spnego.ldap.useStartTls=false
# cas.authn.spnego.ldap.searchFilter=host={host}

# cas.authn.spnego.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.authn.spnego.ldap.validator.searchFilter=(objectClass=*)
# cas.authn.spnego.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.authn.spnego.ldap.validator.attributeName=objectClass
# cas.authn.spnego.ldap.validator.attributeValues=top
# cas.authn.spnego.ldap.validator.dn=
```

### NTLM Authentication

```properties
# cas.authn.ntlm.includePattern=
# cas.authn.ntlm.loadBalance=true
# cas.authn.ntlm.domainController=
# cas.authn.ntlm.name=
```

## JAAS Authentication

To learn more about this topic, [please review this guide](JAAS-Authentication.html).

```properties
# cas.authn.jaas.realm=CAS
# cas.authn.jaas.kerberosKdcSystemProperty=
# cas.authn.jaas.kerberosRealmSystemProperty=
# cas.authn.jaas.name=

# cas.authn.jaas.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.jaas.passwordEncoder.characterEncoding=
# cas.authn.jaas.passwordEncoder.encodingAlgorithm=
# cas.authn.jaas.passwordEncoder.secret=
# cas.authn.jaas.passwordEncoder.strength=16

# cas.authn.jaas.principalTransformation.suffix=
# cas.authn.jaas.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.jaas.principalTransformation.prefix=
```

## JWT/Token Authentication

To learn more about this topic, [please review this guide](JWT-Authentication.html).

```properties
# cas.authn.token.name=

# cas.authn.token.principalTransformation.suffix=
# cas.authn.token.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.token.principalTransformation.prefix=
```

### JWT Service Tickets

```properties
# cas.authn.token.crypto.signing.key=
# cas.authn.token.crypto.signing.keySize=512
# cas.authn.token.crypto.encryption.key=
# cas.authn.token.crypto.encryption.keySize=16
# cas.authn.token.crypto.alg=AES
```

## Stormpath Authentication

To learn more about this topic, [please review this guide](Stormpath-Authentication.html).

```properties
# cas.authn.stormpath.apiKey=
# cas.authn.stormpath.secretkey=
# cas.authn.stormpath.applicationId=
# cas.authn.stormpath.name=

# cas.authn.stormpath.principalTransformation.suffix=
# cas.authn.stormpath.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.stormpath.principalTransformation.prefix=
```

## Remote Address Authentication

To learn more about this topic, [please review this guide](Remote-Address-Authentication.html).

```properties
# cas.authn.remoteAddress.ipAddressRange=
# cas.authn.remoteAddress.name=
```


## Accept Users Authentication

<div class="alert alert-warning"><strong>Default Credentials</strong><p>To test the default authentication scheme in CAS,
use <strong>casuser</strong> and <strong>Mellon</strong> as the username and password respectively. These are automatically
configured via the static authencation handler, and <strong>MUST</strong> be removed from the configuration
prior to production rollouts.</p></div>

```properties
# cas.authn.accept.users=
# cas.authn.accept.name=

# cas.authn.accept.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT
# cas.authn.accept.passwordEncoder.characterEncoding=
# cas.authn.accept.passwordEncoder.encodingAlgorithm=
# cas.authn.accept.passwordEncoder.secret=
# cas.authn.accept.passwordEncoder.strength=16

# cas.authn.accept.principalTransformation.suffix=
# cas.authn.accept.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.accept.principalTransformation.prefix=
```

## X509 Authentication

To learn more about this topic, [please review this guide](X509-Authentication.html).

### CRL Fetching / Revocation

CAS provides a flexible policy engine for certificate revocation checking. This facility arose due to lack of configurability 
in the revocation machinery built into the JSSE.

Available policies cover the following events:

- CRL Expiration
- CRL Unavailability

In either event, the following options are available:

- Allow authentication to proceed.
- Deny authentication and block.
- Applicable to CRL expiration, throttle the request whereby expired data is permitted up
to a threshold period of time but not afterward.

Revocation certificate checking can be carried out in one of the following ways:

- None.
- A CRL hosted at a fixed location. The CRL is fetched at periodic intervals and cached.
- The CRL URI(s) mentioned in the certificate `cRLDistributionPoints` extension field. Caches are available to prevent excessive
IO against CRL endpoints; CRL data is fetched if does not exist in the cache or if it is expired.

To fetch CRLs, the following options are available:

- By default, all revocation checks use fixed resources to fetch the CRL resource from the specified location.
- A CRL resource may be fetched from a pre-configured attribute, in the event that the CRL resource location is an LDAP URI.

```properties
# cas.authn.x509.crlExpiredPolicy=DENY|ALLOW|THRESHOLD
# cas.authn.x509.crlUnavailablePolicy=DENY|ALLOW|THRESHOLD
# cas.authn.x509.crlResourceExpiredPolicy=DENY|ALLOW|THRESHOLD
# cas.authn.x509.crlResourceUnavailablePolicy=DENY|ALLOW|THRESHOLD

# cas.authn.x509.revocationChecker=NONE|CRL|RESOURCE
# cas.authn.x509.crlFetcher=RESOURCE|LDAP

# cas.authn.x509.crlResources[0]=file:/...

# cas.authn.x509.cacheMaxElementsInMemory=1000
# cas.authn.x509.cacheDiskOverflow=false
# cas.authn.x509.cacheEternal=false
# cas.authn.x509.cacheTimeToLiveSeconds=7200
# cas.authn.x509.cacheTimeToIdleSeconds=1800

# cas.authn.x509.checkKeyUsage=false
# cas.authn.x509.revocationPolicyThreshold=172800

# cas.authn.x509.regExSubjectDnPattern=.+
# cas.authn.x509.regExTrustedIssuerDnPattern=.+
# cas.authn.x509.trustedIssuerDnPattern=.+

# cas.authn.x509.name=
# cas.authn.x509.principalDescriptor=
# cas.authn.x509.maxPathLength=1
# cas.authn.x509.throwOnFetchFailure=false
# cas.authn.x509.valueDelimiter=,
# cas.authn.x509.checkAll=false
# cas.authn.x509.requireKeyUsage=false
# cas.authn.x509.serialNumberPrefix=SERIALNUMBER=
# cas.authn.x509.refreshIntervalSeconds=3600
# cas.authn.x509.maxPathLengthAllowUnspecified=false
# cas.authn.x509.certificateAttribute=certificateRevocationList

# cas.authn.x509.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.authn.x509.ldap.connectionStrategy=
# cas.authn.x509.ldap.useSsl=true
# cas.authn.x509.ldap.useStartTls=false
# cas.authn.x509.ldap.connectTimeout=5000
# cas.authn.x509.ldap.baseDn=dc=example,dc=org
# cas.authn.x509.ldap.searchFilter=cn={user}
# cas.authn.x509.ldap.subtreeSearch=true
# cas.authn.x509.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.authn.x509.ldap.bindCredential=Password
# cas.authn.x509.ldap.trustCertificates=
# cas.authn.x509.ldap.keystore=
# cas.authn.x509.ldap.keystorePassword=
# cas.authn.x509.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.authn.x509.ldap.minPoolSize=3
# cas.authn.x509.ldap.maxPoolSize=10
# cas.authn.x509.ldap.validateOnCheckout=true
# cas.authn.x509.ldap.validatePeriodically=true
# cas.authn.x509.ldap.validatePeriod=600
# cas.authn.x509.ldap.failFast=true
# cas.authn.x509.ldap.idleTime=500
# cas.authn.x509.ldap.prunePeriod=600
# cas.authn.x509.ldap.blockWaitTime=5000
# cas.authn.x509.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider

# cas.authn.x509.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.authn.x509.ldap.validator.baseDn=
# cas.authn.x509.ldap.validator.searchFilter=(objectClass=*)
# cas.authn.x509.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.authn.x509.ldap.validator.attributeName=objectClass
# cas.authn.x509.ldap.validator.attributeValues=top
# cas.authn.x509.ldap.validator.dn=

# cas.authn.x509.principal.principalAttribute=
# cas.authn.x509.principal.returnNull=false
# cas.authn.x509.principalType=SERIAL_NO|SERIAL_NO_DN|SUBJECT|SUBJECT_ALT_NAME|SUBJECT_DN
```

## Shiro Authentication

To learn more about this topic, [please review this guide](Shiro-Authentication.html).

```properties
# cas.authn.shiro.requiredPermissions=value1,value2,...
# cas.authn.shiro.requiredRoles=value1,value2,...
# cas.authn.shiro.config.location=classpath:shiro.ini
# cas.authn.shiro.name=

# cas.authn.shiro.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.shiro.passwordEncoder.characterEncoding=
# cas.authn.shiro.passwordEncoder.encodingAlgorithm=
# cas.authn.shiro.passwordEncoder.secret=
# cas.authn.shiro.passwordEncoder.strength=16

# cas.authn.shiro.principalTransformation.suffix=
# cas.authn.shiro.principalTransformation.caseConversion=NONE|UPPERCASE|LOWERCASE
# cas.authn.shiro.principalTransformation.prefix=
```


## Trusted Authentication

To learn more about this topic, [please review this guide](Trusted-Authentication.html).

```properties
# cas.authn.trusted.principalAttribute=
# cas.authn.trusted.returnNull=false
# cas.authn.trusted.name=
```

## WS-Fed Authentication

To learn more about this topic, [please review this guide](../integration/ADFS-Integration.html).

```properties
# cas.authn.wsfed.identityProviderUrl=https://adfs.example.org/adfs/ls/
# cas.authn.wsfed.identityProviderIdentifier=https://adfs.example.org/adfs/services/trust
# cas.authn.wsfed.relyingPartyIdentifier=urn:cas:localhost
# cas.authn.wsfed.attributesType=WSFED
# cas.authn.wsfed.signingCertificateResources=classpath:adfs-signing.crt
# cas.authn.wsfed.tolerance=10000
# cas.authn.wsfed.identityAttribute=upn
# cas.authn.wsfed.attributeResolverEnabled=true
# cas.authn.wsfed.autoRedirect=true
# cas.authn.wsfed.name=

# cas.authn.wsfed.principal.principalAttribute=
# cas.authn.wsfed.principal.returnNull=false

# Private/Public keypair used to decrypt assertions, if any.
# cas.authn.wsfed.encryptionPrivateKey=classpath:private.key
# cas.authn.wsfed.encryptionCertificate=classpath:certificate.crt
# cas.authn.wsfed.encryptionPrivateKeyPassword=NONE
```


## Multifactor Authentication

To learn more about this topic, [please review this guide](Configuring-Multifactor-Authentication.html).

```properties
# cas.authn.mfa.globalProviderId=mfa-duo

# cas.authn.mfa.globalAuthenticationAttributeNameTriggers=memberOf,eduPersonPrimaryAffiliation
# cas.authn.mfa.globalAuthenticationAttributeValueRegex=faculty|staff

# cas.authn.mfa.globalPrincipalAttributeNameTriggers=memberOf,eduPersonPrimaryAffiliation
# cas.authn.mfa.globalPrincipalAttributeValueRegex=faculty|staff

# cas.authn.mfa.restEndpoint=https://entity.example.org/mfa

# cas.authn.mfa.grouperGroupField=NAME|EXTENSION|DISPLAY_NAME|DISPLAY_EXTENSION

# cas.authn.mfa.requestParameter=authn_method
# cas.authn.mfa.globalFailureMode=CLOSED
# cas.authn.mfa.authenticationContextAttribute=authnContextClass
# cas.authn.mfa.contentType=application/cas
```

### Multifactor Trusted Device/Browser

To learn more about this topic, [please review this guide](Multifactor-TrustedDevice-Authentication.html).

```properties
# cas.authn.mfa.trusted.authenticationContextAttribute=isFromTrustedMultifactorAuthentication
# cas.authn.mfa.trusted.encryptionKey=
# cas.authn.mfa.trusted.signingKey=
# cas.authn.mfa.trusted.cipherEnabled=true
# cas.authn.mfa.trusted.deviceRegistrationEnabled=true
# cas.authn.mfa.trusted.expiration=30
# cas.authn.mfa.trusted.timeUnit=SECONDS|MINUTES|HOURS|DAYS
```

### JDBC Storage

```properties
# cas.authn.mfa.trusted.jpa.healthQuery=
# cas.authn.mfa.trusted.jpa.isolateInternalQueries=false
# cas.authn.mfa.trusted.jpa.url=jdbc:hsqldb:mem:cas-jdbc-storage
# cas.authn.mfa.trusted.jpa.failFast=true
# cas.authn.mfa.trusted.jpa.dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.mfa.trusted.jpa.leakThreshold=10
# cas.authn.mfa.trusted.jpa.batchSize=1
# cas.authn.mfa.trusted.jpa.defaultCatalog=
# cas.authn.mfa.trusted.jpa.defaultSchema=
# cas.authn.mfa.trusted.jpa.user=sa
# cas.authn.mfa.trusted.jpa.ddlAuto=create-drop
# cas.authn.mfa.trusted.jpa.password=
# cas.authn.mfa.trusted.jpa.autocommit=false
# cas.authn.mfa.trusted.jpa.driverClass=org.hsqldb.jdbcDriver
# cas.authn.mfa.trusted.jpa.idleTimeout=5000

# cas.authn.mfa.trusted.jpa.pool.suspension=false
# cas.authn.mfa.trusted.jpa.pool.minSize=6
# cas.authn.mfa.trusted.jpa.pool.maxSize=18
# cas.authn.mfa.trusted.jpa.pool.maxIdleTime=1000
# cas.authn.mfa.trusted.jpa.pool.maxWait=2000
```

### MongoDb Storage

```properties
# cas.authn.mfa.trusted.mongodb.clientUri=
# cas.authn.mfa.trusted.mongodb.dropCollection=false
# cas.authn.mfa.trusted.mongodb.collection=MongoDbCasTrustedAuthnMfaRepository
```

### REST Storage

```properties
# cas.authn.mfa.trusted.rest.endpoint=https://api.example.org/trustedBrowser
```


### Cleaner

A cleaner process is scheduled to run in the background to clean up expired and stale tickets.
This section controls how that process should behave.

```properties
# cas.authn.mfa.trusted.cleaner.startDelay=10000
# cas.authn.mfa.trusted.cleaner.repeatInterval=60000
# cas.authn.mfa.trusted.cleaner.enabled=true
```

### Google Authenticator

To learn more about this topic, [please review this guide](GoogleAuthenticator-Authentication.html).

```properties
# cas.authn.mfa.gauth.windowSize=3
# cas.authn.mfa.gauth.issuer=
# cas.authn.mfa.gauth.codeDigits=6
# cas.authn.mfa.gauth.label=
# cas.authn.mfa.gauth.timeStepSize=30
# cas.authn.mfa.gauth.rank=0
# cas.authn.mfa.gauth.trustedDeviceEnabled=true
# cas.authn.mfa.gauth.name=

# cas.authn.mfa.gauth.cleaner.enabled=true
# cas.authn.mfa.gauth.cleaner.startDelay=20000
# cas.authn.mfa.gauth.cleaner.repeatInterval=60000

# cas.authn.mfa.gauth.bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.gauth.bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.gauth.bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.gauth.bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.gauth.bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.gauth.bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.gauth.bypass.credentialClassType=UsernamePassword.+
```

#### Google Authenticator JSON

```properties
# cas.authn.mfa.gauth.json.config.location=file:/somewhere.json
```

#### Google Authenticator MongoDb

```properties
# cas.authn.mfa.gauth.mongodb.clientUri=
# cas.authn.mfa.gauth.mongodb.dropCollection=false
# cas.authn.mfa.gauth.mongodb.collection=MongoDbGoogleAuthenticatorRepository
# cas.authn.mfa.gauth.mongodb.tokenCollection=MongoDbGoogleAuthenticatorTokenRepository
```

#### Google Authenticator JPA

```properties
# cas.authn.mfa.gauth.jpa.database.healthQuery=
# cas.authn.mfa.gauth.jpa.database.isolateInternalQueries=false
# cas.authn.mfa.gauth.jpa.database.url=jdbc:hsqldb:mem:cas-gauth
# cas.authn.mfa.gauth.jpa.database.failFast=true
# cas.authn.mfa.gauth.jpa.database.dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.mfa.gauth.jpa.database.leakThreshold=10
# cas.authn.mfa.gauth.jpa.database.batchSize=1
# cas.authn.mfa.gauth.jpa.database.user=sa
# cas.authn.mfa.gauth.jpa.database.ddlAuto=create-drop
# cas.authn.mfa.gauth.jpa.database.password=
# cas.authn.mfa.gauth.jpa.database.autocommit=false
# cas.authn.mfa.gauth.jpa.database.driverClass=org.hsqldb.jdbcDriver
# cas.authn.mfa.gauth.jpa.database.idleTimeout=5000

# cas.authn.mfa.gauth.jpa.database.pool.suspension=false
# cas.authn.mfa.gauth.jpa.database.pool.minSize=6
# cas.authn.mfa.gauth.jpa.database.pool.maxSize=18
# cas.authn.mfa.gauth.jpa.database.pool.maxIdleTime=1000
# cas.authn.mfa.gauth.jpa.database.pool.maxWait=2000
```

### YubiKey

To learn more about this topic, [please review this guide](YubiKey-Authentication.html).

```properties
# cas.authn.mfa.yubikey.clientId=
# cas.authn.mfa.yubikey.secretKey=
# cas.authn.mfa.yubikey.rank=0
# cas.authn.mfa.yubikey.apiUrls=
# cas.authn.mfa.yubikey.trustedDeviceEnabled=true
# cas.authn.mfa.yubikey.name=

# cas.authn.mfa.yubikey.bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.yubikey.bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.yubikey.bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.yubikey.bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.yubikey.bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.yubikey.bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.yubikey.bypass.credentialClassType=UsernamePassword.+
```

### Radius OTP

To learn more about this topic, [please review this guide](RADIUS-Authentication.html).

```properties
# cas.authn.mfa.radius.failoverOnAuthenticationFailure=false
# cas.authn.mfa.radius.failoverOnException=false
# cas.authn.mfa.radius.rank=0
# cas.authn.mfa.radius.trustedDeviceEnabled=true
# cas.authn.mfa.radius.name=

# cas.authn.mfa.radius.client.socketTimeout=0
# cas.authn.mfa.radius.client.sharedSecret=N0Sh@ar3d$ecReT
# cas.authn.mfa.radius.client.authenticationPort=1812
# cas.authn.mfa.radius.client.accountingPort=1813
# cas.authn.mfa.radius.client.inetAddress=localhost

# cas.authn.mfa.radius.server.retries=3
# cas.authn.mfa.radius.server.nasPortType=-1
# cas.authn.mfa.radius.server.protocol=EAP_MSCHAPv2
# cas.authn.mfa.radius.server.nasRealPort=-1
# cas.authn.mfa.radius.server.nasPortId=-1
# cas.authn.mfa.radius.server.nasIdentifier=-1
# cas.authn.mfa.radius.server.nasPort=-1
# cas.authn.mfa.radius.server.nasIpAddress=
# cas.authn.mfa.radius.server.nasIpv6Address=

# cas.authn.mfa.radius.bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.radius.bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.radius.bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.radius.bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.radius.bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.radius.bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.radius.bypass.credentialClassType=UsernamePassword.+
```

### DuoSecurity

To learn more about this topic, [please review this guide](DuoSecurity-Authentication.html).

```properties
# cas.authn.mfa.duo[0].duoSecretKey=
# cas.authn.mfa.duo[0].rank=0
# cas.authn.mfa.duo[0].duoApplicationKey=
# cas.authn.mfa.duo[0].duoIntegrationKey=
# cas.authn.mfa.duo[0].duoApiHost=
# cas.authn.mfa.duo[0].trustedDeviceEnabled=true
# cas.authn.mfa.duo[0].id=mfa-duo
# cas.authn.mfa.duo[0].name=

# cas.authn.mfa.duo[0].bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.duo[0].bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.duo[0].bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.duo[0].bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.duo[0].bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.duo[0].bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.duo[0].bypass.credentialClassType=UsernamePassword.+
```

### Microsoft Azure

To learn more about this topic, [please review this guide](MicrosoftAzure-Authentication.html).

```properties
# cas.authn.mfa.azure.phoneAttribute=phone
# cas.authn.mfa.azure.configDir=/etc/cas/azure
# cas.authn.mfa.azure.privateKeyPassword=
# cas.authn.mfa.azure.mode=POUND|PIN
# cas.authn.mfa.azure.rank=0
# cas.authn.mfa.azure.name=
# cas.authn.mfa.azure.allowInternationalCalls=false

# cas.authn.mfa.azure.bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.azure.bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.azure.bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.azure.bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.azure.bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.azure.bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.azure.bypass.credentialClassType=UsernamePassword.+
```

### Authy

To learn more about this topic, [please review this guide](AuthyAuthenticator-Authentication.html).

```properties
# cas.authn.mfa.authy.apiKey=
# cas.authn.mfa.authy.apiUrl=
# cas.authn.mfa.authy.phoneAttribute=phone
# cas.authn.mfa.authy.mailAttribute=mail
# cas.authn.mfa.authy.forceVerification=true
# cas.authn.mfa.authy.trustedDeviceEnabled=true
# cas.authn.mfa.authy.name=

# cas.authn.mfa.authy.bypass.principalAttributeName=bypass|skip
# cas.authn.mfa.authy.bypass.principalAttributeValue=true|enabled.+
# cas.authn.mfa.authy.bypass.authenticationAttributeName=bypass|skip
# cas.authn.mfa.authy.bypass.authenticationAttributeValue=allowed.+|enabled.+
# cas.authn.mfa.authy.bypass.authenticationHandlerName=AcceptUsers.+
# cas.authn.mfa.authy.bypass.authenticationMethodName=LdapAuthentication.+
# cas.authn.mfa.authy.bypass.credentialClassType=UsernamePassword.+
```

## SAML Core

Control core SAML functionality within CAS.

```properties
# cas.samlCore.ticketidSaml2=false
# cas.samlCore.skewAllowance=0
# cas.samlCore.attributeNamespace=http://www.ja-sig.org/products/cas/
# cas.samlCore.issuer=localhost
# cas.samlCore.securityManager=org.apache.xerces.util.SecurityManager
```


## SAML IdP

Allow CAS to become a SAML2 identity provider.
To learn more about this topic, [please review this guide](Configuring-SAML2-Authentication.html).


```properties
# cas.authn.samlIdp.entityId=https://cas.example.org/idp
# cas.authn.samlIdp.scope=example.org
# cas.authn.samlIdp.authenticationContextClassMappings[0]=urn:oasis:names:tc:SAML:2.0:ac:classes:SomeClassName->mfa-duo

# cas.authn.samlIdp.metadata.cacheExpirationMinutes=30
# cas.authn.samlIdp.metadata.failFast=true
# cas.authn.samlIdp.metadata.location=file:/etc/cas/saml
# cas.authn.samlIdp.metadata.privateKeyAlgName=RSA
# cas.authn.samlIdp.metadata.requireValidMetadata=true

# cas.authn.samlIdp.metadata.basicAuthnUsername=
# cas.authn.samlIdp.metadata.basicAuthnPassword=
# cas.authn.samlIdp.metadata.supportedContentTypes=

# cas.authn.samlIdp.logout.forceSignedLogoutRequests=true
# cas.authn.samlIdp.logout.singleLogoutCallbacksDisabled=false

# cas.authn.samlIdp.response.skewAllowance=0
# cas.authn.samlIdp.response.signError=false
# cas.authn.samlIdp.response.useAttributeFriendlyName=true
# cas.authn.samlIdp.response.attributeNameFormats=username->basic|uri|unspecified|custom-format-etc,...

# cas.authn.samlIdp.algs.overrideSignatureCanonicalizationAlgorithm=
# cas.authn.samlIdp.algs.overrideDataEncryptionAlgorithms=
# cas.authn.samlIdp.algs.overrideKeyEncryptionAlgorithms=
# cas.authn.samlIdp.algs.overrideBlackListedEncryptionAlgorithms=
# cas.authn.samlIdp.algs.overrideWhiteListedAlgorithms=
# cas.authn.samlIdp.algs.overrideSignatureReferenceDigestMethods=
# cas.authn.samlIdp.algs.overrideSignatureAlgorithms=
# cas.authn.samlIdp.algs.overrideBlackListedSignatureSigningAlgorithms=
# cas.authn.samlIdp.algs.overrideWhiteListedSignatureSigningAlgorithms=
```


## SAML SPs

Allow CAS to register and enable a number of built-in SAML service provider integrations.
To learn more about this topic, [please review this guide](../integration/Configuring-SAML-SP-Integrations.html).

### Dropbox

```properties
# cas.samlSP.dropbox.metadata=/etc/cas/saml/dropbox.xml
# cas.samlSP.dropbox.name=Dropbox
# cas.samlSP.dropbox.description=Dropbox Integration
# cas.samlSP.dropbox.nameIdAttribute=mail
# cas.samlSP.dropbox.signatureLocation=
```

### TestShib

```properties
# cas.samlSP.testShib.metadata=http://www.testshib.org/metadata/testshib-providers.xml
# cas.samlSP.testShib.name=TestShib
# cas.samlSP.testShib.description=TestShib Integration
# cas.samlSP.testShib.attributes=eduPersonPrincipalName
# cas.samlSP.testShib.signatureLocation=
```

### Office365

```properties
# cas.samlSP.office365.metadata=/etc/cas/saml/azure.xml
# cas.samlSP.office365.name=O365
# cas.samlSP.office365.description=O365 Integration
# cas.samlSP.office365.nameIdAttribute=scopedImmutableID
# cas.samlSP.office365.attributes=IDPEmail,ImmutableID
# cas.samlSP.office365.signatureLocation=
```

### SAManage

```properties
# cas.samlSP.saManage.metadata=/etc/cas/saml/samanage.xml
# cas.samlSP.saManage.name=SAManage
# cas.samlSP.saManage.description=SAManage Integration
# cas.samlSP.saManage.nameIdAttribute=mail
# cas.samlSP.saManage.signatureLocation=
```

### Workday

```properties
# cas.samlSP.workday.metadata=/etc/cas/saml/workday.xml
# cas.samlSP.workday.name=Workday
# cas.samlSP.workday.description=Workday Integration
# cas.samlSP.workday.signatureLocation=
```

### Salesforce

```properties
# cas.samlSP.salesforce.metadata=/etc/cas/saml/salesforce.xml
# cas.samlSP.salesforce.name=Salesforce
# cas.samlSP.salesforce.description=Salesforce Integration
# cas.samlSP.salesforce.attributes=mail,eduPersonPrincipalName
# cas.samlSP.salesforce.signatureLocation=
```

### Zoom

```properties
# cas.samlSP.zoom.metadata=/etc/cas/saml/zoom.xml
# cas.samlSP.zoom.name=Zoom
# cas.samlSP.zoom.description=Zoom Integration
# cas.samlSP.zoom.attributes=mail,sn,givenName
# cas.samlSP.zoom.nameIdAttribute=mail
```

### Evernote

```properties
# cas.samlSP.evernote.metadata=/etc/cas/saml/evernote.xml
# cas.samlSP.evernote.name=Evernote
# cas.samlSP.evernote.description=Evernote Integration
# cas.samlSP.evernote.nameIdAttribute=mail
# cas.samlSP.evernote.nameIdFormat=emailAddress
```

### Tableau

```properties
# cas.samlSP.tableau.metadata=/etc/cas/saml/tableau.xml
# cas.samlSP.tableau.name=Tableau
# cas.samlSP.tableau.description=Tableau Integration
# cas.samlSP.tableau.attributes=username
```

### Asana

```properties
# cas.samlSP.asana.metadata=/etc/cas/saml/asana.xml
# cas.samlSP.asana.name=Asana
# cas.samlSP.asana.description=Asana Integration
# cas.samlSP.asana.nameIdAttribute=mail
# cas.samlSP.asana.nameIdFormat=emailAddress
```

### Box

```properties
# cas.samlSP.box.metadata=/etc/cas/saml/box.xml
# cas.samlSP.box.name=Box
# cas.samlSP.box.description=Box Integration
# cas.samlSP.box.attributes=email,firstName,lastName
# cas.samlSP.box.signatureLocation=
```

### Service Now

```properties
# cas.samlSP.serviceNow.metadata=/etc/cas/saml/serviceNow.xml
# cas.samlSP.serviceNow.name=ServiceNow
# cas.samlSP.serviceNow.description=serviceNow Integration
# cas.samlSP.serviceNow.attributes=eduPersonPrincipalName
# cas.samlSP.serviceNow.signatureLocation=
```

### Net Partner

```properties
# cas.samlSP.netPartner.metadata=/etc/cas/saml/netPartner.xml
# cas.samlSP.netPartner.name=Net Partner
# cas.samlSP.netPartner.description=Net Partner Integration
# cas.samlSP.netPartner.nameIdAttribute=studentId
# cas.samlSP.netPartner.attributes=
# cas.samlSP.netPartner.signatureLocation=
```

### Webex

```properties
# cas.samlSP.webex.metadata=/etc/cas/saml/webex.xml
# cas.samlSP.webex.name=Webex
# cas.samlSP.webex.description=Webex Integration
# cas.samlSP.webex.nameIdAttribute=email
# cas.samlSP.webex.attributes=firstName,lastName
```

### InCommon

Multiple entity ids can be specified to filter [the InCommon metadata](https://spaces.internet2.edu/display/InCFederation/Metadata+Aggregates).
EntityIds can be regular expression patterns and are mapped to CAS' `serviceId` field in the registry.
The signature location MUST BE the public key used to sign the metadata.

```properties
# cas.samlSP.inCommon.metadata=http://md.incommon.org/InCommon/InCommon-metadata.xml
# cas.samlSP.inCommon.name=InCommon Aggregate
# cas.samlSP.inCommon.description=InCommon Metadata Aggregate
# cas.samlSP.inCommon.attributes=eduPersonPrincipalName,givenName,cn,sn
# cas.samlSP.inCommon.signatureLocation=/etc/cas/saml/inc-md-public-key.pem
# cas.samlSP.inCommon.entityIds[0]=sampleSPEntityId
```

## OpenID Connect

Allow CAS to become an OpenID Connect provider (OP). To learn more about this topic, [please review this guide](OIDC-Authentication.html).

```properties
# cas.authn.oidc.issuer=http://localhost:8080/cas/oidc
# cas.authn.oidc.skew=5
# cas.authn.oidc.jwksFile=file:/keystore.jwks
# cas.authn.oidc.dynamicClientRegistrationMode=OPEN|PROTECTED
# cas.authn.oidc.subjectTypes=public,pairwise
# cas.authn.oidc.scopes=openid,profile,email,address,phone,offline_access
# cas.authn.oidc.claims=sub,name,preferred_username,family_name,given_name,middle_name,given_name,profile,picture,nickname,website,zoneinfo,locale,updated_at,birthdate,email,email_verified,phone_number,phone_number_verified,address
```

## Pac4j Delegated AuthN

Act as a proxy, and delegate authentication to external identity providers.
To learn more about this topic, [please review this guide](../integration/Delegate-Authentication.html).

```properties
# cas.authn.pac4j.typedIdUsed=false
# cas.authn.pac4j.autoRedirect=false
# cas.authn.pac4j.name=
```

### CAS

Delegate authentication to an external CAS server.

```properties
# cas.authn.pac4j.cas.loginUrl=
# cas.authn.pac4j.cas.protocol=
```

### Facebook

Delegate authentication to Facebook.

```properties
# cas.authn.pac4j.facebook.fields=
# cas.authn.pac4j.facebook.id=
# cas.authn.pac4j.facebook.secret=
# cas.authn.pac4j.facebook.scope=
```

### LinkedIn

Delegate authentication to LinkedIn.

```properties
# cas.authn.pac4j.linkedIn.fields=
# cas.authn.pac4j.linkedIn.id=
# cas.authn.pac4j.linkedIn.secret=
# cas.authn.pac4j.linkedIn.scope=
```

### Twitter

Delegate authentication to Twitter.

```properties
# cas.authn.pac4j.twitter.id=
# cas.authn.pac4j.twitter.secret=
```

### OpenID Connect

Delegate authentication to an external OpenID Connect server.

```properties
# cas.authn.pac4j.oidc.discoveryUri=
# cas.authn.pac4j.oidc.maxClockSkew=
# cas.authn.pac4j.oidc.customParamKey2=
# cas.authn.pac4j.oidc.customParamValue2=
# cas.authn.pac4j.oidc.scope=
# cas.authn.pac4j.oidc.id=
# cas.authn.pac4j.oidc.secret=
# cas.authn.pac4j.oidc.customParamKey1=
# cas.authn.pac4j.oidc.customParamValue1=
# cas.authn.pac4j.oidc.useNonce=
# cas.authn.pac4j.oidc.preferredJwsAlgorithm=
```

### SAML

Delegate authentication to an external SAML2 IdP (do not use the `resource:` or `classpath:`
prefixes for the `keystorePath` or `identityProviderMetadataPath` property).

```properties

# Settings required for CAS SP metadata generation process
# The keystore will be automatically generated by CAS with
# keys required for the metadata generation and/or exchange.
#
# cas.authn.pac4j.saml.keystorePassword=
# cas.authn.pac4j.saml.privateKeyPassword=
# cas.authn.pac4j.saml.keystorePath=

# The entityID assigned to CAS acting as the SP
# cas.authn.pac4j.saml.serviceProviderEntityId=

# Path to the auto-generated CAS SP metadata
# cas.authn.pac4j.saml.serviceProviderMetadataPath=

# cas.authn.pac4j.saml.maximumAuthenticationLifetime=

# Path/URL to delegated IdP metadata
# cas.authn.pac4j.saml.identityProviderMetadataPath=
```

Examine the generated metadata after accessing the CAS login screen to ensure all ports and endpoints are correctly adjusted.  
Finally, share the CAS SP metadata with the delegated IdP and register CAS as an authorized relying party.

### Yahoo

Delegate authentication to Yahoo.

```properties
# cas.authn.pac4j.yahoo.id=
# cas.authn.pac4j.yahoo.secret=
```

### Dropbox

Delegate authentication to Dropbox.

```properties
# cas.authn.pac4j.dropbox.id=
# cas.authn.pac4j.dropbox.secret=
```

### Github

Delegate authentication to Github.

```properties
# cas.authn.pac4j.github.id=
# cas.authn.pac4j.github.secret=
```

### Foursquare

Delegate authentication to Foursquare.

```properties
# cas.authn.pac4j.foursquare.id=
# cas.authn.pac4j.foursquare.secret=
```

### WindowsLive

Delegate authentication to WindowsLive.

```properties
# cas.authn.pac4j.windowsLive.id=
# cas.authn.pac4j.windowsLive.secret=
```

### Google

Delegate authentication to Google.

```properties
# cas.authn.pac4j.google.id=
# cas.authn.pac4j.google.secret=
# cas.authn.pac4j.google.scope=
```

## OAuth2

Allows CAS to act as an OAuth2 provider. Here you can control how
long various tokens issued by CAS should last, etc.

To learn more about this topic, [please review this guide](OAuth-OpenId-Authentication.html).


```properties
# cas.authn.oauth.refreshToken.timeToKillInSeconds=2592000

# cas.authn.oauth.code.timeToKillInSeconds=30
# cas.authn.oauth.code.numberOfUses=1

# cas.authn.oauth.accessToken.timeToKillInSeconds=7200
# cas.authn.oauth.accessToken.maxTimeToLiveInSeconds=28800
```


## Localization

To learn more about this topic, [please review this guide](User-Interface-Customization-Localization.html).

```properties
# cas.locale.paramName=locale
# cas.locale.defaultValue=en
```

## Global SSO Behavior

```properties
# cas.sso.missingService=true
# cas.sso.renewedAuthn=true
```

## Warning Cookie

Created by CAS if and when users are to be warned when accessing CAS protected services.

```properties
# cas.warningCookie.path=
# cas.warningCookie.maxAge=-1
# cas.warningCookie.domain=
# cas.warningCookie.name=CASPRIVACY
# cas.warningCookie.secure=true
```

## Ticket Granting Cookie

```properties
# cas.tgc.path=
# cas.tgc.maxAge=-1
# cas.tgc.domain=
# cas.tgc.signingKey=
# cas.tgc.name=TGC
# cas.tgc.encryptionKey=
# cas.tgc.secure=true
# cas.tgc.rememberMeMaxAge=1209600
# cas.tgc.cipherEnabled=true
```

## Logout

Control various settings related to CAS logout functionality.
To learn more about this topic, [please review this guide](Logout-Single-Signout.html).

```properties
# cas.logout.followServiceRedirects=false
```

## Single Logout

To learn more about this topic, [please review this guide](Logout-Single-Signout.html).

```properties
# cas.slo.disabled=false
# cas.slo.asynchronous=true
```

## Clearpass

Capture and cache user credentials and optionally release them to trusted applications.
To learn more about this topic, [please review this guide](../integration/ClearPass.html).


<div class="alert alert-warning"><strong>Usage Warning!</strong><p>ClearPass is turned off by default.
Think <strong>VERY CAREFULLY</strong> before turning on this feature, as it <strong>MUST</strong> be
the last resort in getting an integration to work...maybe not even then.</p></div>

```properties
# cas.clearpass.cacheCredential=false
```

## Message Bundles

To learn more about this topic, [please review this guide](User-Interface-Customization-Localization.html).

```properties
# cas.messageBundle.encoding=UTF-8
# cas.messageBundle.fallbackSystemLocale=false
# cas.messageBundle.cacheSeconds=180
# cas.messageBundle.useCodeMessage=true
# cas.messageBundle.baseNames=classpath:custom_messages,classpath:messages
```



## Audits

Control how audit messages are formatted.
To learn more about this topic, [please review this guide](Audits.html).

```properties
# cas.audit.auditFormat=DEFAULT
# cas.audit.ignoreAuditFailures=false
# cas.audit.singlelineSeparator=|
# cas.audit.useSingleLine=false
# cas.audit.appCode=CAS
# cas.audit.alternateServerAddrHeaderName=
# cas.audit.alternateClientAddrHeaderName=X-Forwarded-For
# cas.audit.useServerHostAddress=false
```

### Database Audits

Store audit logs inside a database.

```properties
# cas.audit.jdbc.healthQuery=
# cas.audit.jdbc.isolateInternalQueries=false
# cas.audit.jdbc.url=jdbc:hsqldb:mem:cas-hsql-database
# cas.audit.jdbc.failFast=true
# cas.audit.jdbc.isolationLevelName=ISOLATION_READ_COMMITTED
# cas.audit.jdbc.dialect=org.hibernate.dialect.HSQLDialect
# cas.audit.jdbc.leakThreshold=10
# cas.audit.jdbc.propagationBehaviorName=PROPAGATION_REQUIRED
# cas.audit.jdbc.batchSize=1
# cas.audit.jdbc.user=sa
# cas.audit.jdbc.ddlAuto=create-drop
# cas.audit.jdbc.maxAgeDays=180
# cas.audit.jdbc.password=
# cas.audit.jdbc.autocommit=false
# cas.audit.jdbc.driverClass=org.hsqldb.jdbcDriver
# cas.audit.jdbc.idleTimeout=5000

# cas.audit.jdbc.pool.suspension=false
# cas.audit.jdbc.pool.minSize=6
# cas.audit.jdbc.pool.maxSize=18
# cas.audit.jdbc.pool.maxIdleTime=1000
# cas.audit.jdbc.pool.maxWait=2000
```

## Monitoring

To learn more about this topic, [please review this guide](Monitoring-Statistics.html).

### Ticket Granting Tickets

Decide how CAS should monitor the generation of TGTs.

```properties
# cas.monitor.tgt.warn.threshold=10
# cas.monitor.tgt.warn.evictionThreshold=0
```

### Service Tickets

Decide how CAS should monitor the generation of STs.

```properties
# cas.monitor.st.warn.threshold=10
# cas.monitor.st.warn.evictionThreshold=0
```

### Cache Monitors

Decide how CAS should monitor the internal state of various cache storage services.

```properties
# cas.monitor.warn.threshold=10
# cas.monitor.warn.evictionThreshold=0
```

### Database Monitoring

Decide how CAS should monitor the internal state of JDBC connections used
for authentication or attribute retrieval.

```properties
# cas.monitor.jdbc.validationQuery=SELECT 1
# cas.monitor.jdbc.maxWait=5000
# cas.monitor.jdbc.healthQuery=
# cas.monitor.jdbc.isolateInternalQueries=false
# cas.monitor.jdbc.url=jdbc:hsqldb:mem:cas-hsql-database
# cas.monitor.jdbc.failFast=true
# cas.monitor.jdbc.isolationLevelName=ISOLATION_READ_COMMITTED
# cas.monitor.jdbc.dialect=org.hibernate.dialect.HSQLDialect
# cas.monitor.jdbc.leakThreshold=10
# cas.monitor.jdbc.propagationBehaviorName=PROPAGATION_REQUIRED
# cas.monitor.jdbc.batchSize=1
# cas.monitor.jdbc.user=sa
# cas.monitor.jdbc.ddlAuto=create-drop
# cas.monitor.jdbc.maxAgeDays=180
# cas.monitor.jdbc.password=
# cas.monitor.jdbc.autocommit=false
# cas.monitor.jdbc.driverClass=org.hsqldb.jdbcDriver
# cas.monitor.jdbc.idleTimeout=5000
```

### LDAP Connection Pool

Decide how CAS should monitor the internal state of LDAP connections
used for authentication, etc.

```properties
# Define the thread pool that will ping on the LDAP connection pool.
# cas.monitor.ldap.pool.suspension=false
# cas.monitor.ldap.pool.minSize=6
# cas.monitor.ldap.pool.maxSize=18
# cas.monitor.ldap.pool.maxIdleTime=1000
# cas.monitor.ldap.pool.maxWait=2000

# cas.monitor.ldap.maxWait=5000

# Define the LDAP connection pool settings for monitoring
# cas.monitor.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.monitor.ldap.connectionStrategy=
# cas.monitor.ldap.baseDn=dc=example,dc=org
# cas.monitor.ldap.userFilter=cn={user}
# cas.monitor.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.monitor.ldap.bindCredential=Password
# cas.monitor.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.monitor.ldap.connectTimeout=5000
# cas.monitor.ldap.trustCertificates=
# cas.monitor.ldap.keystore=
# cas.monitor.ldap.keystorePassword=
# cas.monitor.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.monitor.ldap.minPoolSize=3
# cas.monitor.ldap.maxPoolSize=10
# cas.monitor.ldap.validateOnCheckout=true
# cas.monitor.ldap.validatePeriodically=true
# cas.monitor.ldap.validatePeriod=600
# cas.monitor.ldap.failFast=true
# cas.monitor.ldap.idleTime=500
# cas.monitor.ldap.prunePeriod=600
# cas.monitor.ldap.blockWaitTime=5000
# cas.monitor.ldap.subtreeSearch=true
# cas.monitor.ldap.useSsl=true
# cas.monitor.ldap.useStartTls=false

# cas.monitor.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.monitor.ldap.validator.baseDn=
# cas.monitor.ldap.validator.searchFilter=(objectClass=*)
# cas.monitor.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.monitor.ldap.validator.attributeName=objectClass
# cas.monitor.ldap.validator.attributeValues=top
# cas.monitor.ldap.validator.dn=
```

### Memory

Decide how CAS should monitor the internal state of JVM memory available at runtime.

```properties
# cas.monitor.freeMemThreshold=10
```

## Themes

To learn more about this topic, [please review this guide](User-Interface-Customization-Themes.html).

```properties
# cas.theme.paramName=theme
# cas.theme.defaultThemeName=cas-theme-default
```


## Events

Decide how CAS should track authentication events.
To learn more about this topic, [please review this guide](Configuring-Authentication-Events.html).


```properties
# cas.events.trackGeolocation=false
```

### Database Events

Decide how CAS should store authentication events inside a database instance.

```properties
# cas.events.jpa.healthQuery=
# cas.events.jpa.isolateInternalQueries=false
# cas.events.jpa.url=jdbc:hsqldb:mem:cas-events
# cas.events.jpa.failFast=true
# cas.events.jpa.dialect=org.hibernate.dialect.HSQLDialect
# cas.events.jpa.leakThreshold=10
# cas.events.jpa.batchSize=1
# cas.events.jpa.defaultCatalog=
# cas.events.jpa.defaultSchema=
# cas.events.jpa.user=sa
# cas.events.jpa.ddlAuto=create-drop
# cas.events.jpa.password=
# cas.events.jpa.autocommit=false
# cas.events.jpa.driverClass=org.hsqldb.jdbcDriver
# cas.events.jpa.idleTimeout=5000

# cas.events.jpa.pool.suspension=false
# cas.events.jpa.pool.minSize=6
# cas.events.jpa.pool.maxSize=18
# cas.events.jpa.pool.maxIdleTime=1000
# cas.events.jpa.pool.maxWait=2000
```

### MongoDb Events

Decide how CAS should store authentication events inside a MongoDb instance.

```properties
# cas.events.mongodb.clientUri=
# cas.events.mongodb.dropCollection=false
# cas.events.mongodb.collection=MongoDbCasEventRepository
```

## Http Web Requests

Control how CAS should respond and validate incoming HTTP requests.

```properties
# cas.httpWebRequest.header.xframe=true
# cas.httpWebRequest.header.xss=true
# cas.httpWebRequest.header.hsts=true
# cas.httpWebRequest.header.xcontent=true
# cas.httpWebRequest.header.cache=true

# cas.httpWebRequest.cors.enabled=false
# cas.httpWebRequest.cors.allowCredentials=false
# cas.httpWebRequest.cors.allowOrigins[0]=
# cas.httpWebRequest.cors.allowMethods[0]=*
# cas.httpWebRequest.cors.allowHeaders[0]=*
# cas.httpWebRequest.cors.maxAge=3600
# cas.httpWebRequest.cors.exposedHeaders[0]=

# cas.httpWebRequest.web.forceEncoding=true
# cas.httpWebRequest.web.encoding=UTF-8

# cas.httpWebRequest.allowMultiValueParameters=false
# cas.httpWebRequest.onlyPostParams=username,password
# cas.httpWebRequest.paramsToCheck=ticket,service,renew,gateway,warn,method,target,SAMLart,pgtUrl,pgt,pgtId,pgtIou,targetService,entityId,token

spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true
```

## Http Client

Control how CAS should attempt to contact resources on the web
via its own Http Client. This is most commonly used when responding
to ticket validation events and/or single logout.

In the event that local certificates are to be imported into the CAS running environment,
a local truststore is provided by CAS to improve portability of configuration across environments.

```properties
# cas.httpClient.connectionTimeout=5000
# cas.httpClient.asyncTimeout=5000
# cas.httpClient.readTimeout=5000

# cas.httpClient.truststore.psw=changeit
# cas.httpClient.truststore.file=classpath:/truststore.jks
```

## Service Registry

```properties
# cas.serviceRegistry.watcherEnabled=true
# cas.serviceRegistry.repeatInterval=120000
# cas.serviceRegistry.startDelay=15000
# cas.serviceRegistry.initFromJson=false
```

### Resource-based (JSON/YAML) Service Registry

If the underlying service registry is using local system resources
to locate service definitions, decide how those resources should be found.

```properties
# cas.serviceRegistry.config.location=classpath:/services
```

To learn more about this topic, [please review this guide](JSON-Service-Management.html)
or [this guide](YAML-Service-Management.html).

### MongoDb Service Registry

Store CAS service definitions inside a MongoDb instance.
To learn more about this topic, [please review this guide](Mongo-Service-Management.html).

```properties
# cas.serviceRegistry.mongo.idleTimeout=30000
# cas.serviceRegistry.mongo.port=27017
# cas.serviceRegistry.mongo.dropCollection=false
# cas.serviceRegistry.mongo.socketKeepAlive=false
# cas.serviceRegistry.mongo.password=
# cas.serviceRegistry.mongo.collectionName=cas-service-registry
# cas.serviceRegistry.mongo.databaseName=cas-mongo-database
# cas.serviceRegistry.mongo.timeout=5000
# cas.serviceRegistry.mongo.userId=
# cas.serviceRegistry.mongo.writeConcern=NORMAL
# cas.serviceRegistry.mongo.host=localhost

# cas.serviceRegistry.mongo.conns.lifetime=60000
# cas.serviceRegistry.mongo.conns.perHost=10
```

### LDAP Service Registry

Control how CAS services should be found inside an LDAP instance.
To learn more about this topic, [please review this guide](LDAP-Service-Management.html)

```properties
# cas.serviceRegistry.ldap.serviceDefinitionAttribute=description
# cas.serviceRegistry.ldap.idAttribute=uid
# cas.serviceRegistry.ldap.objectClass=casRegisteredService

# cas.serviceRegistry.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.serviceRegistry.ldap.connectionStrategy=
# cas.serviceRegistry.ldap.baseDn=dc=example,dc=org
# cas.serviceRegistry.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.serviceRegistry.ldap.bindCredential=Password
# cas.serviceRegistry.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.serviceRegistry.ldap.connectTimeout=5000
# cas.serviceRegistry.ldap.trustCertificates=
# cas.serviceRegistry.ldap.keystore=
# cas.serviceRegistry.ldap.keystorePassword=
# cas.serviceRegistry.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.serviceRegistry.ldap.minPoolSize=3
# cas.serviceRegistry.ldap.maxPoolSize=10
# cas.serviceRegistry.ldap.validateOnCheckout=true
# cas.serviceRegistry.ldap.validatePeriodically=true
# cas.serviceRegistry.ldap.validatePeriod=600
# cas.serviceRegistry.ldap.failFast=true
# cas.serviceRegistry.ldap.idleTime=500
# cas.serviceRegistry.ldap.prunePeriod=600
# cas.serviceRegistry.ldap.blockWaitTime=5000
# cas.serviceRegistry.ldap.useSsl=true
# cas.serviceRegistry.ldap.useStartTls=false

# cas.serviceRegistry.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.serviceRegistry.ldap.validator.baseDn=
# cas.serviceRegistry.ldap.validator.searchFilter=(objectClass=*)
# cas.serviceRegistry.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.serviceRegistry.ldap.validator.attributeName=objectClass
# cas.serviceRegistry.ldap.validator.attributeValues=top
# cas.serviceRegistry.ldap.validator.dn=
```

### Couchbase Service Registry

Control how CAS services should be found inside a Couchbase instance.
To learn more about this topic, [please review this guide](Couchbase-Service-Management.html)

```properties
# cas.serviceRegistry.couchbase.nodeSet=localhost:8091
# cas.serviceRegistry.couchbase.password=
# cas.serviceRegistry.couchbase.queryEnabled=true
# cas.serviceRegistry.couchbase.bucket=default
# cas.serviceRegistry.couchbase.timeout=10
```

### Database Service Registry

Control how CAS services should be found inside a database instance.
To learn more about this topic, [please review this guide](JPA-Service-Management.html)

```properties
# cas.serviceRegistry.jpa.healthQuery=
# cas.serviceRegistry.jpa.isolateInternalQueries=false
# cas.serviceRegistry.jpa.url=jdbc:hsqldb:mem:cas-service-registry
# cas.serviceRegistry.jpa.failFast=true
# cas.serviceRegistry.jpa.dialect=org.hibernate.dialect.HSQLDialect
# cas.serviceRegistry.jpa.leakThreshold=10
# cas.serviceRegistry.jpa.batchSize=1
# cas.serviceRegistry.jpa.user=sa
# cas.serviceRegistry.jpa.ddlAuto=create-drop
# cas.serviceRegistry.jpa.password=
# cas.serviceRegistry.jpa.autocommit=false
# cas.serviceRegistry.jpa.driverClass=org.hsqldb.jdbcDriver
# cas.serviceRegistry.jpa.idleTimeout=5000

# cas.serviceRegistry.jpa.pool.suspension=false
# cas.serviceRegistry.jpa.pool.minSize=6
# cas.serviceRegistry.jpa.pool.maxSize=18
# cas.serviceRegistry.jpa.pool.maxIdleTime=1000
# cas.serviceRegistry.jpa.pool.maxWait=2000
```

## Ticket Registry

To learn more about this topic, [please review this guide](Configuring-Ticketing-Components.html).


### Cleaner

A cleaner process is scheduled to run in the background to clean up expired and stale tickets.
This section controls how that process should behave.

```properties
# cas.ticket.registry.cleaner.appId=cas-ticket-registry-cleaner
# cas.ticket.registry.cleaner.startDelay=10000
# cas.ticket.registry.cleaner.repeatInterval=60000
# cas.ticket.registry.cleaner.enabled=true
```

### JPA Ticket Registry

To learn more about this topic, [please review this guide](JPA-Ticket-Registry.html).

```properties
# cas.ticket.registry.jpa.jpaLockingTimeout=3600
# cas.ticket.registry.jpa.healthQuery=
# cas.ticket.registry.jpa.isolateInternalQueries=false
# cas.ticket.registry.jpa.url=jdbc:hsqldb:mem:cas-ticket-registry
# cas.ticket.registry.jpa.failFast=true
# cas.ticket.registry.jpa.dialect=org.hibernate.dialect.HSQLDialect
# cas.ticket.registry.jpa.leakThreshold=10
# cas.ticket.registry.jpa.jpaLockingTgtEnabled=true
# cas.ticket.registry.jpa.batchSize=1
# cas.ticket.registry.jpa.defaultCatalog=
# cas.ticket.registry.jpa.defaultSchema=
# cas.ticket.registry.jpa.user=sa
# cas.ticket.registry.jpa.ddlAuto=create-drop
# cas.ticket.registry.jpa.password=
# cas.ticket.registry.jpa.autocommit=false
# cas.ticket.registry.jpa.driverClass=org.hsqldb.jdbcDriver
# cas.ticket.registry.jpa.idleTimeout=5000

# cas.ticket.registry.jpa.pool.suspension=false
# cas.ticket.registry.jpa.pool.minSize=6
# cas.ticket.registry.jpa.pool.maxSize=18
# cas.ticket.registry.jpa.pool.maxIdleTime=1000
# cas.ticket.registry.jpa.pool.maxWait=2000

# cas.ticket.registry.jpa.crypto.signing.key=
# cas.ticket.registry.jpa.crypto.signing.keySize=512
# cas.ticket.registry.jpa.crypto.encryption.key=
# cas.ticket.registry.jpa.crypto.encryption.keySize=16
# cas.ticket.registry.jpa.crypto.alg=AES
```

### Couchbase Ticket Registry

To learn more about this topic, [please review this guide](Couchbase-Ticket-Registry.html).

```properties
# cas.ticket.registry.couchbase.timeout=10
# cas.ticket.registry.couchbase.nodeSet=localhost:8091
# cas.ticket.registry.couchbase.password=
# cas.ticket.registry.couchbase.queryEnabled=true
# cas.ticket.registry.couchbase.bucket=default

# cas.ticket.registry.couchbase.crypto.signing.key=
# cas.ticket.registry.couchbase.crypto.signing.keySize=512
# cas.ticket.registry.couchbase.crypto.encryption.key=
# cas.ticket.registry.couchbase.crypto.encryption.keySize=16
# cas.ticket.registry.couchbase.crypto.alg=AES
```

### Hazelcast Ticket Registry

To learn more about this topic, [please review this guide](Hazelcast-Ticket-Registry.html).


```properties
# cas.ticket.registry.hazelcast.pageSize=500
# cas.ticket.registry.hazelcast.mapName=tickets
# cas.ticket.registry.hazelcast.configLocation=

# cas.ticket.registry.hazelcast.cluster.evictionPolicy=LRU
# cas.ticket.registry.hazelcast.cluster.maxNoHeartbeatSeconds=300
# cas.ticket.registry.hazelcast.cluster.multicastEnabled=false
# cas.ticket.registry.hazelcast.cluster.tcpipEnabled=true
# cas.ticket.registry.hazelcast.cluster.members=localhost
# cas.ticket.registry.hazelcast.cluster.loggingType=slf4j
# cas.ticket.registry.hazelcast.cluster.instanceName=localhost
# cas.ticket.registry.hazelcast.cluster.port=5701
# cas.ticket.registry.hazelcast.cluster.portAutoIncrement=true
# cas.ticket.registry.hazelcast.cluster.maxHeapSizePercentage=85
# cas.ticket.registry.hazelcast.cluster.backupCount=1
# cas.ticket.registry.hazelcast.cluster.asyncBackupCount=0
# cas.ticket.registry.hazelcast.cluster.maxSizePolicy=USED_HEAP_PERCENTAGE
# cas.ticket.registry.hazelcast.cluster.timeout=5

# cas.ticket.registry.hazelcast.cluster.multicastTrustedInterfaces=
# cas.ticket.registry.hazelcast.cluster.multicastPort=
# cas.ticket.registry.hazelcast.cluster.multicastGroup=
# cas.ticket.registry.hazelcast.cluster.multicastTimeout=2
# cas.ticket.registry.hazelcast.cluster.multicastTimeToLive=32

# cas.ticket.registry.hazelcast.crypto.signing.key=
# cas.ticket.registry.hazelcast.crypto.signing.keySize=512
# cas.ticket.registry.hazelcast.crypto.encryption.key=
# cas.ticket.registry.hazelcast.crypto.encryption.keySize=16
# cas.ticket.registry.hazelcast.crypto.alg=AES
```

### Infinispan Ticket Registry

To learn more about this topic, [please review this guide](Infinispan-Ticket-Registry.html).

```properties
# cas.ticket.registry.infinispan.cacheName=
# cas.ticket.registry.infinispan.configLocation=/infinispan.xml

# cas.ticket.registry.infinispan.crypto.signing.key=
# cas.ticket.registry.infinispan.crypto.signing.keySize=512
# cas.ticket.registry.infinispan.crypto.encryption.key=
# cas.ticket.registry.infinispan.crypto.encryption.keySize=16
# cas.ticket.registry.infinispan.crypto.alg=AES
```

### InMemory Ticket Registry

This is typically the default ticket registry instance where tickets
are kept inside the runtime environment memory.

```properties
# cas.ticket.registry.inMemory.loadFactor=1
# cas.ticket.registry.inMemory.concurrency=20
# cas.ticket.registry.inMemory.initialCapacity=1000

# cas.ticket.registry.inMemory.crypto.signing.key=
# cas.ticket.registry.inMemory.crypto.signing.keySize=512
# cas.ticket.registry.inMemory.crypto.encryption.key=
# cas.ticket.registry.inMemory.crypto.encryption.keySize=16
# cas.ticket.registry.inMemory.crypto.alg=AES
```

### Ehcache Ticket Registry

To learn more about this topic, [please review this guide](Ehcache-Ticket-Registry.html).

```properties
# cas.ticket.registry.ehcache.replicateUpdatesViaCopy=true
# cas.ticket.registry.ehcache.cacheManagerName=ticketRegistryCacheManager
# cas.ticket.registry.ehcache.replicatePuts=true
# cas.ticket.registry.ehcache.replicateUpdates=true
# cas.ticket.registry.ehcache.memoryStoreEvictionPolicy=LRU
# cas.ticket.registry.ehcache.configLocation=classpath:/ehcache-replicated.xml
# cas.ticket.registry.ehcache.maximumBatchSize=100
# cas.ticket.registry.ehcache.shared=false
# cas.ticket.registry.ehcache.replicationInterval=10000
# cas.ticket.registry.ehcache.cacheTimeToLive=2147483647
# cas.ticket.registry.ehcache.diskExpiryThreadIntervalSeconds=0
# cas.ticket.registry.ehcache.replicateRemovals=true
# cas.ticket.registry.ehcache.maxChunkSize=5000000
# cas.ticket.registry.ehcache.maxElementsOnDisk=0
# cas.ticket.registry.ehcache.maxElementsInCache=0
# cas.ticket.registry.ehcache.maxElementsInMemory=10000
# cas.ticket.registry.ehcache.cacheName=org.apereo.cas.ticket.TicketCache
# cas.ticket.registry.ehcache.eternal=false
# cas.ticket.registry.ehcache.loaderAsync=true
# cas.ticket.registry.ehcache.replicatePutsViaCopy=true
# cas.ticket.registry.ehcache.cacheTimeToIdle=0
# cas.ticket.registry.ehcache.persistence=LOCALTEMPSWAP|NONE|LOCALRESTARTABLE|DISTRIBUTED
# cas.ticket.registry.ehcache.synchronousWrites=

# cas.ticket.registry.ehcache.crypto.signing.key=
# cas.ticket.registry.ehcache.crypto.signing.keySize=512
# cas.ticket.registry.ehcache.crypto.encryption.key=
# cas.ticket.registry.ehcache.crypto.encryption.keySize=16
# cas.ticket.registry.ehcache.crypto.alg=AES
```

### Ignite Ticket Registry

To learn more about this topic, [please review this guide](Ignite-Ticket-Registry.html).

```properties
# cas.ticket.registry.ignite.keyAlgorithm=
# cas.ticket.registry.ignite.protocol=
# cas.ticket.registry.ignite.trustStorePassword=
# cas.ticket.registry.ignite.keyStoreType=
# cas.ticket.registry.ignite.keyStoreFilePath=
# cas.ticket.registry.ignite.keyStorePassword=
# cas.ticket.registry.ignite.trustStoreType=
# cas.ticket.registry.ignite.igniteAddresses=localhost:47500
# cas.ticket.registry.ignite.trustStoreFilePath=
# cas.ticket.registry.ignite.heartbeatFrequency=2000
# cas.ticket.registry.ignite.joinTimeout=1000
# cas.ticket.registry.ignite.localAddress=
# cas.ticket.registry.ignite.localPort=-1
# cas.ticket.registry.ignite.networkTimeout=5000
# cas.ticket.registry.ignite.socketTimeout=5000
# cas.ticket.registry.ignite.threadPriority=10
# cas.ticket.registry.ignite.forceServerMode=false

# cas.ticket.registry.ignite.ticketsCache.writeSynchronizationMode=FULL_SYNC
# cas.ticket.registry.ignite.ticketsCache.atomicityMode=TRANSACTIONAL
# cas.ticket.registry.ignite.ticketsCache.cacheName=TicketsCache
# cas.ticket.registry.ignite.ticketsCache.cacheMode=REPLICATED

# cas.ticket.registry.ignite.crypto.signing.key=
# cas.ticket.registry.ignite.crypto.signing.keySize=512
# cas.ticket.registry.ignite.crypto.encryption.key=
# cas.ticket.registry.ignite.crypto.encryption.keySize=16
# cas.ticket.registry.ignite.crypto.alg=AES
```

### Memcached Ticket Registry

To learn more about this topic, [please review this guide](Memcached-Ticket-Registry.html).

```properties
# cas.ticket.registry.memcached.servers=localhost:11211
# cas.ticket.registry.memcached.locatorType=ARRAY_MOD
# cas.ticket.registry.memcached.failureMode=Redistribute
# cas.ticket.registry.memcached.hashAlgorithm=FNV1_64_HASH

# cas.ticket.registry.memcached.crypto.signing.key=
# cas.ticket.registry.memcached.crypto.signing.keySize=512
# cas.ticket.registry.memcached.crypto.encryption.key=
# cas.ticket.registry.memcached.crypto.encryption.keySize=16
# cas.ticket.registry.memcached.crypto.alg=AES
```

### MongoDb Ticket Registry

To learn more about this topic, [please review this guide](MongoDb-Ticket-Registry.html).

```properties
# cas.ticket.registry.mongo.idleTimeout=30000
# cas.ticket.registry.mongo.port=27017
# cas.ticket.registry.mongo.dropCollection=false
# cas.ticket.registry.mongo.socketKeepAlive=false
# cas.ticket.registry.mongo.password=
# cas.ticket.registry.mongo.collectionName=cas-ticket-registry
# cas.ticket.registry.mongo.databaseName=cas-database
# cas.ticket.registry.mongo.timeout=5000
# cas.ticket.registry.mongo.userId=
# cas.ticket.registry.mongo.writeConcern=NORMAL
# cas.ticket.registry.mongo.host=localhost

# cas.ticket.mongo.conns.lifetime=60000
# cas.ticket.mongo.conns.perHost=10
```

### Redis Ticket Registry

To learn more about this topic, [please review this guide](Redis-Ticket-Registry.html).

```properties
## Redis server host.
# cas.ticket.registry.redis.host=localhost
#
## Database index used by the connection factory.
# cas.ticket.registry.redis.database=0
#
## Redis server port.
# cas.ticket.registry.redis.port=6379
#
## Login password of the redis server.
# cas.ticket.registry.redis.password=
#
## Connection timeout in milliseconds
# cas.ticket.registry.redis.timeout=
#
##
# cas.ticket.registry.redis.pool.max-active=20
#
## Max number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle connections.
# cas.ticket.registry.redis.pool.maxIdle=8
#
## Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if it is positive.
# cas.ticket.registry.redis.pool.minIdle=0
#
## Max number of connections that can be allocated by the pool at a given time. Use a negative value for no limit.
# cas.ticket.registry.redis.pool.maxActive=8
#
## Maximum amount of time (in milliseconds) a connection allocation should block
#  before throwing an exception when the pool is exhausted. Use a negative value to block indefinitely.
# cas.ticket.registry.redis.pool.maxWait=-1
```

## Protocol Ticket Security

Controls whether tickets issued by the CAS server should be secured via signing and encryption
when shared with client applications on outgoing calls.

```properties
# cas.ticket.security.cipherEnabled=true
# cas.ticket.security.encryptionKey=
# cas.ticket.security.signingKey=
```

## Service Tickets Behavior

Controls the expiration policy of service tickets, as well as other properties
applicable to STs.

```properties
# cas.ticket.st.maxLength=20

# cas.ticket.st.numberOfUses=1
# cas.ticket.st.timeToKillInSeconds=10
```

## Proxy Granting Tickets Behavior

```properties
# cas.ticket.pgt.maxLength=50
```

## Proxy Tickets Behavior

```properties
# cas.ticket.pt.timeToKillInSeconds=10
# cas.ticket.pt.numberOfUses=1
```

## Ticket Granting Tickets Behavior

```properties
# cas.ticket.tgt.onlyTrackMostRecentSession=true
# cas.ticket.tgt.maxLength=50
```

## TGT Expiration Policy

### Default

Provides a hard-time out as well as a sliding window.

```properties
# Set to a negative value to never expire tickets
# cas.ticket.tgt.maxTimeToLiveInSeconds=28800
# cas.ticket.tgt.timeToKillInSeconds=7200
```

### Remember Me

```properties
# cas.ticket.tgt.rememberMe.enabled=true
# cas.ticket.tgt.rememberMe.timeToKillInSeconds=28800
```

### Timeout

The expiration policy applied to TGTs provides for most-recently-used expiration policy, similar to a Web server session timeout.

```properties
# cas.ticket.tgt.timeout.maxTimeToLiveInSeconds=28800
```

### Throttled Timeout

The throttled timeout policy extends the Timeout policy with the concept of throttling where a ticket may be used at most every N seconds.

```properties
# cas.ticket.tgt.throttledTimeout.timeToKillInSeconds=28800
# cas.ticket.tgt.throttledTimeout.timeInBetweenUsesInSeconds=5
```

### Hard Timeout

The hard timeout policy provides for finite ticket lifetime as measured from the time of creation.

```properties
# cas.ticket.tgt.hardTimeout.timeToKillInSeconds=28800
```

## Management Webapp

To learn more about this topic, [please review this guide](Installing-ServicesMgmt-Webapp.html).

```properties
# cas.mgmt.adminRoles[0]=ROLE_ADMIN
# cas.mgmt.adminRoles[1]=ROLE_SUPER_USER

# cas.mgmt.userPropertiesFile=classpath:/user-details.properties
# cas.mgmt.serverName=https://localhost:8443
# cas.mgmt.defaultLocale=en

# cas.mgmt.authzAttributes[0]=memberOf
# cas.mgmt.authzAttributes[1]=groupMembership
```

### LDAP Authorization

```properties
# cas.mgmt.ldap.ldapAuthz.groupAttribute=
# cas.mgmt.ldap.ldapAuthz.groupPrefix=
# cas.mgmt.ldap.ldapAuthz.groupFilter=
# cas.mgmt.ldap.ldapAuthz.rolePrefix=ROLE_
# cas.mgmt.ldap.ldapAuthz.roleAttribute=uugid
# cas.mgmt.ldap.ldapAuthz.searchFilter=cn={user}
# cas.mgmt.ldap.ldapAuthz.baseDn=

# cas.mgmt.ldap.allowMultipleResults=false
# cas.mgmt.ldap.baseDn=dc=example,dc=org
# cas.mgmt.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.mgmt.ldap.connectionStrategy=
# cas.mgmt.ldap.baseDn=dc=example,dc=org
# cas.mgmt.ldap.userFilter=cn={user}
# cas.mgmt.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.mgmt.ldap.bindCredential=Password
# cas.mgmt.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.mgmt.ldap.connectTimeout=5000
# cas.mgmt.ldap.trustCertificates=
# cas.mgmt.ldap.keystore=
# cas.mgmt.ldap.keystorePassword=
# cas.mgmt.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.mgmt.ldap.minPoolSize=3
# cas.mgmt.ldap.maxPoolSize=10
# cas.mgmt.ldap.validateOnCheckout=true
# cas.mgmt.ldap.validatePeriodically=true
# cas.mgmt.ldap.validatePeriod=600
# cas.mgmt.ldap.failFast=true
# cas.mgmt.ldap.idleTime=500
# cas.mgmt.ldap.prunePeriod=600
# cas.mgmt.ldap.blockWaitTime=5000
# cas.mgmt.ldap.subtreeSearch=true
# cas.mgmt.ldap.useSsl=true
# cas.mgmt.ldap.useStartTls=false
```

## Google reCAPTCHA Integration

Display Google's reCAPTCHA widget on the CAS login page.

```properties
# cas.googleRecaptcha.verifyUrl=https://www.google.com/recaptcha/api/siteverify
# cas.googleRecaptcha.siteKey=
# cas.googleRecaptcha.secret=
```

## Google Analytics Integration

To learn more about this topic, [please review this guide](../integration/Configuring-Google-Analytics.html).

```properties
# cas.googleAnalytics.googleAnalyticsTrackingId=
```

## Spring Webflow

Control how Spring Webflow's conversational session state should be managed by CAS,
and all other webflow related settings.

To learn more about this topic, [please review this guide](Webflow-Customization.html) or [this guide](Webflow-Customization-Sessions.html).

```properties
# cas.webflow.autoconfigure=true
# cas.webflow.alwaysPauseRedirect=false
# cas.webflow.refresh=true
# cas.webflow.redirectSameState=false

# cas.webflow.session.lockTimeout=30
# cas.webflow.session.compress=false
# cas.webflow.session.maxConversations=5
# cas.webflow.session.storage=true

# Manage session storage via Hazelcast
# cas.webflow.session.hzLocation=classpath:/hazelcast.xml

# Manage session storage via Mongo
# spring.data.mongodb.host=mongo-srv
# spring.data.mongodb.port=27018
# spring.data.mongodb.database=prod

# Manage session storage via Redis
# spring.session.store-type=redis
# spring.redis.host=localhost
# spring.redis.password=secret
# spring.redis.port=6379

# cas.webflow.signing.key=
# cas.webflow.signing.keySize=512

# cas.webflow.encryption.keySize=16
# cas.webflow.encryption.key=

# cas.webflow.alg=AES
```

### Authentication Exceptions

Map custom authentication exceptions in the CAS webflow and link them to custom messages defined in message bundles.

To learn more about this topic, [please review this guide](Webflow-Customization-Exceptions.html).

```properties
# cas.authn.exceptions.exceptions=value1,value2,...
```

### Acceptable Usage Policy

Decide how CAS should attempt to determine whether AUP is accepted.
To learn more about this topic, [please review this guide](User-Interface-Customization-AUP.html).


```properties
# cas.acceptableUsagePolicy.aupAttributeName=aupAccepted
```

#### LDAP

If AUP is controlled via LDAP, decide how choices should be remembered back inside the LDAP instance.

```properties
# cas.acceptableUsagePolicy.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.acceptableUsagePolicy.ldap.connectionStrategy=
# cas.acceptableUsagePolicy.ldap.baseDn=dc=example,dc=org
# cas.acceptableUsagePolicy.ldap.userFilter=cn={user}
# cas.acceptableUsagePolicy.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.acceptableUsagePolicy.ldap.bindCredential=Password
# cas.acceptableUsagePolicy.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider
# cas.acceptableUsagePolicy.ldap.connectTimeout=5000
# cas.acceptableUsagePolicy.ldap.trustCertificates=
# cas.acceptableUsagePolicy.ldap.keystore=
# cas.acceptableUsagePolicy.ldap.keystorePassword=
# cas.acceptableUsagePolicy.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.acceptableUsagePolicy.ldap.minPoolSize=3
# cas.acceptableUsagePolicy.ldap.maxPoolSize=10
# cas.acceptableUsagePolicy.ldap.validateOnCheckout=true
# cas.acceptableUsagePolicy.ldap.validatePeriodically=true
# cas.acceptableUsagePolicy.ldap.validatePeriod=600
# cas.acceptableUsagePolicy.ldap.failFast=true
# cas.acceptableUsagePolicy.ldap.idleTime=500
# cas.acceptableUsagePolicy.ldap.prunePeriod=600
# cas.acceptableUsagePolicy.ldap.blockWaitTime=5000
# cas.acceptableUsagePolicy.ldap.useSsl=true
# cas.acceptableUsagePolicy.ldap.useStartTls=false

# cas.acceptableUsagePolicy.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.acceptableUsagePolicy.ldap.validator.baseDn=
# cas.acceptableUsagePolicy.ldap.validator.searchFilter=(objectClass=*)
# cas.acceptableUsagePolicy.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.acceptableUsagePolicy.ldap.validator.attributeName=objectClass
# cas.acceptableUsagePolicy.ldap.validator.attributeValues=top
# cas.acceptableUsagePolicy.ldap.validator.dn=
```


## REST API

To learn more about this topic, [please review this guide](../protocol/REST-Protocol.html).

```properties
# cas.rest.attributeName=
# cas.rest.attributeValue=
# cas.rest.throttler=neverThrottle
```

## Metrics & Performance Stats

To learn more about this topic, [please review this guide](Monitoring-Statistics.html).

```properties
# cas.metrics.loggerName=perfStatsLogger
# cas.metrics.refreshInterval=30
```

## Groovy Shell

Control access and configuration of the embedded Groovy shell in CAS.
To learn more about this topic, [please review this guide](Configuring-Groovy-Console.html).

```properties
# shell.commandRefreshInterval=15
# shell.commandPathPatterns=classpath*:/commands/**
# shell.auth.simple.user.name=
# shell.auth.simple.user.password=
# shell.ssh.enabled=true
# shell.ssh.port=2000
# shell.telnet.enabled=false
# shell.telnet.port=5000
# shell.ssh.authTimeout=3000
# shell.ssh.idleTimeout=30000
```

## SAML Metadata UI

Control how SAML MDUI elements should be displayed on the main CAS login page
in the event that CAS is handling authentication for an external SAML2 IdP.

To learn more about this topic, [please review this guide](../integration/Shibboleth.html).

```properties
# cas.samlMetadataUi.requireValidMetadata=true
# cas.samlMetadataUi.repeatInterval=120000
# cas.samlMetadataUi.startDelay=30000
# cas.samlMetadataUi.resources=classpath:/sp-metadata::classpath:/pub.key,http://md.incommon.org/InCommon/InCommon-metadata.xml::classpath:/inc-md-pub.key
# cas.samlMetadataUi.maxValidity=0
# cas.samlMetadataUi.requireSignedRoot=false
# cas.samlMetadataUi.parameter=entityId
```

## Hibernate & JDBC

Control global properties that are relevant to Hibernate,
when CAS attempts to employ and utilize database resources,
connections and queries.

```properties
# cas.jdbc.showSql=true
# cas.jdbc.genDdl=true
```

## Provisioning

### SCIM

Provision the authenticated CAS principal via SCIM.

```properties
# cas.scim.version=2
# cas.scim.target=
# cas.scim.oauthToken=
# cas.scim.username=
# cas.scim.password=
```

## Password Management

Allow the user to update their account password, etc in-place.
To learn more about this topic, [please review this guide](Password-Policy-Enforcement.html).

```properties
# cas.authn.pm.enabled=true

# Minimum 8 and Maximum 10 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character
# cas.authn.pm.policyPattern=^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,10}

# cas.authn.pm.reset.text=Reset your password with this link: %s
# cas.authn.pm.reset.subject=Password Reset Request
# cas.authn.pm.reset.from=
# cas.authn.pm.reset.expirationMinutes=1
# cas.authn.pm.reset.emailAttribute=mail

# Used to sign/encrypt the password-reset link
# cas.authn.pm.reset.security.encryptionKey=
# cas.authn.pm.reset.security.signingKey=
```

### LDAP Password Management

```properties
# cas.authn.pm.ldap.type=GENERIC|AD|FreeIPA|EDirectory
# cas.authn.pm.ldap.ldapUrl=ldaps://ldap1.example.edu ldaps://ldap2.example.edu
# cas.authn.pm.ldap.connectionStrategy=
# cas.authn.pm.ldap.useSsl=true
# cas.authn.pm.ldap.useStartTls=false
# cas.authn.pm.ldap.connectTimeout=5000
# cas.authn.pm.ldap.baseDn=dc=example,dc=org
# cas.authn.pm.ldap.userFilter=cn={user}
# cas.authn.pm.ldap.subtreeSearch=true
# cas.authn.pm.ldap.bindDn=cn=Directory Manager,dc=example,dc=org
# cas.authn.pm.ldap.bindCredential=Password
# cas.authn.pm.ldap.trustCertificates=
# cas.authn.pm.ldap.keystore=
# cas.authn.pm.ldap.keystorePassword=
# cas.authn.pm.ldap.keystoreType=JKS|JCEKS|PKCS12
# cas.authn.pm.ldap.minPoolSize=3
# cas.authn.pm.ldap.maxPoolSize=10
# cas.authn.pm.ldap.validateOnCheckout=true
# cas.authn.pm.ldap.validatePeriodically=true
# cas.authn.pm.ldap.validatePeriod=600
# cas.authn.pm.ldap.failFast=true
# cas.authn.pm.ldap.idleTime=500
# cas.authn.pm.ldap.prunePeriod=600
# cas.authn.pm.ldap.blockWaitTime=5000
# cas.authn.pm.ldap.providerClass=org.ldaptive.provider.unboundid.UnboundIDProvider

# cas.authn.pm.ldap.securityQuestionsAttributes.attrQuestion1=attrAnswer1
# cas.authn.pm.ldap.securityQuestionsAttributes.attrQuestion2=attrAnswer2
# cas.authn.pm.ldap.securityQuestionsAttributes.attrQuestion3=attrAnswer3

# cas.authn.pm.ldap.validator.type=NONE|SEARCH|COMPARE
# cas.authn.pm.ldap.validator.baseDn=
# cas.authn.pm.ldap.validator.searchFilter=(objectClass=*)
# cas.authn.pm.ldap.validator.scope=OBJECT|ONELEVEL|SUBTREE
# cas.authn.pm.ldap.validator.attributeName=objectClass
# cas.authn.pm.ldap.validator.attributeValues=top
# cas.authn.pm.ldap.validator.dn=
```

### JDBC Password Management

```properties
# cas.authn.pm.jdbc.sqlSecurityQuestions=SELECT question, answer FROM table WHERE user=?
# cas.authn.pm.jdbc.sqlFindEmail=SELECT email FROM table WHERE user=?
# cas.authn.pm.jdbc.sqlChangePassword=UPDATE table SET password=? WHERE user=?

# cas.authn.pm.jdbc.healthQuery=
# cas.authn.pm.jdbc.isolateInternalQueries=false
# cas.authn.pm.jdbc.url=jdbc:hsqldb:mem:cas-hsql-database
# cas.authn.pm.jdbc.failFast=true
# cas.authn.pm.jdbc.isolationLevelName=ISOLATION_READ_COMMITTED
# cas.authn.pm.jdbc.dialect=org.hibernate.dialect.HSQLDialect
# cas.authn.pm.jdbc.leakThreshold=10
# cas.authn.pm.jdbc.propagationBehaviorName=PROPAGATION_REQUIRED
# cas.authn.pm.jdbc.batchSize=1
# cas.authn.pm.jdbc.user=sa
# cas.authn.pm.jdbc.ddlAuto=create-drop
# cas.authn.pm.jdbc.maxAgeDays=180
# cas.authn.pm.jdbc.password=
# cas.authn.pm.jdbc.autocommit=false
# cas.authn.pm.jdbc.driverClass=org.hsqldb.jdbcDriver
# cas.authn.pm.jdbc.idleTimeout=5000

# cas.authn.pm.jdbc.passwordEncoder.type=NONE|DEFAULT|STANDARD|BCRYPT|com.example.CustomPasswordEncoder
# cas.authn.pm.jdbc.passwordEncoder.characterEncoding=
# cas.authn.pm.jdbc.passwordEncoder.encodingAlgorithm=
# cas.authn.pm.jdbc.passwordEncoder.secret=
# cas.authn.pm.jdbc.passwordEncoder.strength=16
````

### REST Password Management

```properties
# cas.authn.pm.rest.endpointUrlEmail=
# cas.authn.pm.rest.endpointUrlSecurityQuestions=
# cas.authn.pm.rest.endpointUrlChange=
```
