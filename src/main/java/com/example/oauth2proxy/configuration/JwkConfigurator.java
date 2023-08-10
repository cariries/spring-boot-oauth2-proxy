package com.example.oauth2proxy.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwkConfigurator {
    public enum BuilderEnum {
        JWK_SET_URI("JwkSetUri"),
        ISSUER_LOCATION("IssuerLocation"),
        PUBLIC_KEY("PublicKey"),
        AUTO("Auto");

        private String name;

        BuilderEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static BuilderEnum fromString(String name) {
            Optional<BuilderEnum> builderEnumOpt = Arrays.stream(BuilderEnum.values()).filter(val -> val.getName().equalsIgnoreCase(name)).findFirst();
            return builderEnumOpt.isPresent() ? builderEnumOpt.get() : null;
        }
    }

    private String builder;
    private String jwkSetUri;
    private String issuerLocation;
    private String publicKey;
    private String privateKey;
    private Long tokenExpirationInMs = 900000L;
}
