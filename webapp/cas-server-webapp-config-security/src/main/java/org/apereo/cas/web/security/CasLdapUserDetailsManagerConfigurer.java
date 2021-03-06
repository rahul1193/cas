package org.apereo.cas.web.security;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authorization.LdapUserAttributesToRolesAuthorizationGenerator;
import org.apereo.cas.authorization.LdapUserGroupsToRolesAuthorizationGenerator;
import org.apereo.cas.configuration.model.core.web.security.AdminPagesSecurityProperties;
import org.apereo.cas.configuration.model.support.ldap.LdapAuthorizationProperties;
import org.apereo.cas.configuration.support.Beans;
import org.apereo.cas.web.ldap.LdapAuthenticationProvider;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.SearchExecutor;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;

/**
 * This is {@link CasLdapUserDetailsManagerConfigurer}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
public class CasLdapUserDetailsManagerConfigurer<B extends ProviderManagerBuilder<B>>
        extends SecurityConfigurerAdapter<AuthenticationManager, B> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasLdapUserDetailsManagerConfigurer.class);

    private final AdminPagesSecurityProperties adminPagesSecurityProperties;

    public CasLdapUserDetailsManagerConfigurer(final AdminPagesSecurityProperties securityProperties) {
        this.adminPagesSecurityProperties = securityProperties;
    }

    private AuthenticationProvider buildLdapAuthenticationProvider() {
        return new LdapAuthenticationProvider(build(), this.adminPagesSecurityProperties);
    }

    private AuthorizationGenerator<CommonProfile> build() {
        final LdapAuthorizationProperties ldapAuthz = adminPagesSecurityProperties.getLdap().getLdapAuthz();
        final ConnectionFactory connectionFactory = Beans.newLdaptivePooledConnectionFactory(adminPagesSecurityProperties.getLdap());

        if (StringUtils.isNotBlank(ldapAuthz.getGroupFilter()) && StringUtils.isNotBlank(ldapAuthz.getGroupAttribute())) {
            return new LdapUserGroupsToRolesAuthorizationGenerator(connectionFactory,
                    ldapAuthorizationGeneratorUserSearchExecutor(),
                    ldapAuthz.isAllowMultipleResults(),
                    ldapAuthz.getRoleAttribute(),
                    ldapAuthz.getRolePrefix(),
                    ldapAuthz.getGroupAttribute(),
                    ldapAuthz.getGroupPrefix(),
                    ldapAuthorizationGeneratorGroupSearchExecutor());
        }
        return new LdapUserAttributesToRolesAuthorizationGenerator(connectionFactory,
                ldapAuthorizationGeneratorUserSearchExecutor(),
                ldapAuthz.isAllowMultipleResults(),
                ldapAuthz.getRoleAttribute(),
                ldapAuthz.getRolePrefix());
    }

    private SearchExecutor ldapAuthorizationGeneratorUserSearchExecutor() {
        final LdapAuthorizationProperties ldapAuthz = adminPagesSecurityProperties.getLdap().getLdapAuthz();
        return Beans.newLdaptiveSearchExecutor(ldapAuthz.getBaseDn(), ldapAuthz.getSearchFilter());
    }

    private SearchExecutor ldapAuthorizationGeneratorGroupSearchExecutor() {
        final LdapAuthorizationProperties ldapAuthz = adminPagesSecurityProperties.getLdap().getLdapAuthz();
        return Beans.newLdaptiveSearchExecutor(ldapAuthz.getBaseDn(), ldapAuthz.getGroupFilter());
    }

    @Override
    public void configure(final B builder) throws Exception {
        final AuthenticationProvider provider = postProcess(buildLdapAuthenticationProvider());
        builder.authenticationProvider(provider);
    }
}
