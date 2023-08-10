package com.example.oauth2proxy.constant;

public final class JwtExtendedClaimNames {
    private JwtExtendedClaimNames() {

    }

    /**
     * {@code oid} - the unique identifier to indicate user id provider by oauth2 server
     */
    public static final String OID = "oid";

    /**
     * {@code email} - the email address identifies user primary name and contact email
     */
    public static final String EMAIL = "email";

    /**
     * {@code avatar} - the image url of avatar
     */
    public static final String AVATAR = "avatar";

    /**
     * {@code name} - the display name provided by oauth2 server
     */
    public static final String NAME = "name";

    /**
     * {@code first_name} - the first name provided by oauth2 server
     */
    public static final String FIRST_NAME = "first_name";

    /**
     * {@code first_name} - the first name provided by oauth2 server
     */
    public static final String LAST_NAME = "last_name";
}