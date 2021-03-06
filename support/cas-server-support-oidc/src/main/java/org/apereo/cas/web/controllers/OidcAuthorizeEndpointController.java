package org.apereo.cas.web.controllers;

import com.google.common.base.Throwables;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apereo.cas.OidcConstants;
import org.apereo.cas.OidcIdTokenGeneratorService;
import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.authentication.principal.ServiceFactory;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.OidcRegisteredService;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.support.oauth.OAuthConstants;
import org.apereo.cas.support.oauth.OAuthResponseTypes;
import org.apereo.cas.support.oauth.util.OAuthUtils;
import org.apereo.cas.support.oauth.validator.OAuth20Validator;
import org.apereo.cas.support.oauth.web.ConsentApprovalViewResolver;
import org.apereo.cas.support.oauth.web.OAuth20AuthorizeController;
import org.apereo.cas.ticket.accesstoken.AccessToken;
import org.apereo.cas.ticket.accesstoken.AccessTokenFactory;
import org.apereo.cas.ticket.code.OAuthCodeFactory;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.pac4j.core.context.J2EContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This is {@link OidcAuthorizeEndpointController}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class OidcAuthorizeEndpointController extends OAuth20AuthorizeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OidcAuthorizeEndpointController.class);

    @Autowired
    private CasConfigurationProperties casProperties;

    private final OidcIdTokenGeneratorService idTokenGenerator;

    public OidcAuthorizeEndpointController(final ServicesManager servicesManager,
                                           final TicketRegistry ticketRegistry,
                                           final OAuth20Validator validator,
                                           final AccessTokenFactory accessTokenFactory,
                                           final PrincipalFactory principalFactory,
                                           final ServiceFactory<WebApplicationService> webApplicationServiceServiceFactory,
                                           final OAuthCodeFactory oAuthCodeFactory,
                                           final ConsentApprovalViewResolver consentApprovalViewResolver,
                                           final OidcIdTokenGeneratorService idTokenGenerator) {
        super(servicesManager, ticketRegistry, validator, accessTokenFactory, principalFactory,
                webApplicationServiceServiceFactory, oAuthCodeFactory, consentApprovalViewResolver);
        this.idTokenGenerator = idTokenGenerator;
    }

    @GetMapping(value = '/' + OidcConstants.BASE_OIDC_URL + '/' + OAuthConstants.AUTHORIZE_URL)
    @Override
    public ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(request, response);
    }

    @Override
    protected String buildCallbackUrlForTokenResponseType(final J2EContext context, final Authentication authentication,
                                                          final Service service, final String redirectUri,
                                                          final String responseType,
                                                          final String clienttId) {
        if (!isResponseType(responseType, OAuthResponseTypes.IDTOKEN_TOKEN)) {
            return super.buildCallbackUrlForTokenResponseType(context, authentication, service,
                    redirectUri, responseType, clienttId);
        }

        LOGGER.debug("Handling callback for response type [{}]", responseType);
        return buildCallbackUrlForImplicitHybridTokenResponseType(context, authentication,
                service, redirectUri, clienttId, OAuthResponseTypes.IDTOKEN_TOKEN);
    }

    private String buildCallbackUrlForImplicitHybridTokenResponseType(final J2EContext context,
                                                                      final Authentication authentication,
                                                                      final Service service,
                                                                      final String redirectUri,
                                                                      final String clientId,
                                                                      final OAuthResponseTypes responseType) {
        try {
            final AccessToken accessToken = generateAccessToken(service, authentication, context);
            LOGGER.debug("Generated OAuth access token: [{}]", accessToken);
            final OidcRegisteredService oidcService = (OidcRegisteredService)
                    OAuthUtils.getRegisteredOAuthService(this.getServicesManager(), clientId);

            final long timeout = casProperties.getTicket().getTgt().getTimeToKillInSeconds();
            final String idToken = this.idTokenGenerator.generate(context.getRequest(),
                    context.getResponse(),
                    accessToken, timeout, responseType, oidcService);
            LOGGER.debug("Generated id token [{}]", idToken);

            final List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(OidcConstants.ID_TOKEN, idToken));
            return buildCallbackUrlResponseType(authentication, service, redirectUri, accessToken, params);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
