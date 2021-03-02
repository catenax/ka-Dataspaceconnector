package com.microsoft.dagx.iam.oauth2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.dagx.spi.security.CertificateResolver;
import com.microsoft.dagx.spi.security.PrivateKeyResolver;

public class OAuth2Configuration {
    private String tokenUrl;
    private String clientId;
    private String audience;
    private String privateKeyAlias;
    private String publicCertificateAlias;

    private PrivateKeyResolver privateKeyResolver;
    private CertificateResolver certificateResolver;
    private CertificateResolver identityProviderCertificateResolver;

    private ObjectMapper objectMapper;

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAudience() {
        return audience;
    }

    public String getPrivateKeyAlias() {
        return privateKeyAlias;
    }

    public String getPublicCertificateAlias() {
        return publicCertificateAlias;
    }

    public PrivateKeyResolver getPrivateKeyResolver() {
        return privateKeyResolver;
    }

    public CertificateResolver getCertificateResolver() {
        return certificateResolver;
    }

    public CertificateResolver getIdentityProviderPublicKeyResolver() {
        return identityProviderCertificateResolver;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private OAuth2Configuration() {
    }

    public static class Builder {
        private OAuth2Configuration configuration;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder tokenUrl(String url) {
            this.configuration.tokenUrl = url;
            return this;
        }

        public Builder clientId(String clientId) {
            this.configuration.clientId = clientId;
            return this;
        }

        public Builder audience(String audience) {
            this.configuration.audience = audience;
            return this;
        }

        public Builder privateKeyResolver(PrivateKeyResolver resolver) {
            this.configuration.privateKeyResolver = resolver;
            return this;
        }

        /**
         * Resolves this runtime's certificate containing its public key.
         */
        public Builder certificateResolver(CertificateResolver resolver) {
            this.configuration.certificateResolver = resolver;
            return this;
        }

        /**
         * Resolves the certificate containing the identity provider's public key.
         */
        public Builder identityProviderCertificateResolver(CertificateResolver resolver) {
            this.configuration.identityProviderCertificateResolver = resolver;
            return this;
        }

        public Builder privateKeyAlias(String alias) {
            this.configuration.privateKeyAlias = alias;
            return this;
        }

        public Builder publicCertificateAlias(String alias) {
            this.configuration.publicCertificateAlias = alias;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.configuration.objectMapper = objectMapper;
            return this;
        }

        public OAuth2Configuration build() {
            return configuration;
        }

        private Builder() {
            configuration = new OAuth2Configuration();
        }

    }
}