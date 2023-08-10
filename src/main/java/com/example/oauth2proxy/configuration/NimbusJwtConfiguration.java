package com.example.oauth2proxy.configuration;

import com.example.oauth2proxy.util.RsaKeyUtils;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class NimbusJwtConfiguration implements InitializingBean {

    @Autowired(required = false)
    OAuth2TokenValidator<Jwt> oAuth2TokenValidator;

    @Autowired(required = false)
    JwkConfigurator jwkConfigurator;

    PublicKey publicKey;

    PrivateKey privateKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jwkConfigurator != null) {
            String builder = jwkConfigurator.getBuilder();
            JwkConfigurator.BuilderEnum selectedJwtDecoderBuilder = null;
            try {
                selectedJwtDecoderBuilder = JwkConfigurator.BuilderEnum.fromString(builder);
            } catch (IllegalArgumentException illegalArgumentException) {
                Assert.notNull(null, "secret.jwk.builder must be correctly specified [JwkSetUri, IssuerLocation, PublicKey, Auto]");
            }

            log.debug("Selected JwtDecoder Builder: {}", selectedJwtDecoderBuilder.name());
            switch (selectedJwtDecoderBuilder) {
                case JWK_SET_URI:
                    String jwkSetUri = jwkConfigurator.getJwkSetUri();
                    Assert.hasLength(jwkSetUri, "secret.jwk.jwkSetUri must be specified");
                    break;
                case ISSUER_LOCATION:
                    String issuerLocation = jwkConfigurator.getIssuerLocation();
                    Assert.hasLength(issuerLocation, "secret.jwk.issuerLocation must be specified");
                    if (oAuth2TokenValidator == null) {
                        oAuth2TokenValidator = createDefaultValidator(issuerLocation);
                    }
                    break;
                case PUBLIC_KEY:
                    String publicKeyText = jwkConfigurator.getPublicKey();
                    String privateKeyText = jwkConfigurator.getPrivateKey();
                    Assert.hasLength(publicKeyText, "secret.jwk.publicKey must be specified");
                    publicKey = RsaKeyUtils.parseRsaPublicKey(publicKeyText);
                    if (StringUtils.isNotEmpty(privateKeyText)) {
                        privateKey = RsaKeyUtils.parseRsaPrivateKey(privateKeyText);
                    }
                    break;
                case AUTO:
                    final KeyPair generatedKeyPair = RsaKeyUtils.generateKeyPair();
                    publicKey = generatedKeyPair.getPublic();
                    privateKey = generatedKeyPair.getPrivate();
                    break;
                default:
                    break;
            }

            if (oAuth2TokenValidator == null) {
                oAuth2TokenValidator = createDefaultValidator();
            }
        }
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        Assert.notNull(privateKey, "Private Key must be well specified if using jwtEncoder");
        Assert.notNull(publicKey, "Public Key must be well specified if using jwtEncoder");

        JWK jwk = new RSAKey.Builder((RSAPublicKey) publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        Assert.notNull(publicKey, "Public Key must be well specified if using jwtDecoder");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
        if (oAuth2TokenValidator != null) {
            jwtDecoder.setJwtValidator(oAuth2TokenValidator);
        }
        return jwtDecoder;
    }

    private OAuth2TokenValidator<Jwt> createDefaultValidator() {
        return createDefaultValidator(null);
    }

    private OAuth2TokenValidator<Jwt> createDefaultValidator(String issuerLocation) {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();

        JwtTimestampValidator jwtTimestampValidator = new JwtTimestampValidator();
        jwtTimestampValidator.setClock(Clock.systemDefaultZone());

        validators.add(jwtTimestampValidator);

        if (StringUtils.isNotBlank(issuerLocation)) {
            validators.add(new JwtIssuerValidator(issuerLocation));
        }

        return new DelegatingOAuth2TokenValidator<>(validators);
    }
}