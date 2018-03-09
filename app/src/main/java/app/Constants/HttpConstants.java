package app.src.main.constants;

/**
 * all constants that are related with HTTP codes should be
 * included in this class
 */
public final class HttpConstants
{
    // Everything is fine
    public static final int OK = 200;
    // Was created an object and the user will receive it
    public static final int CREATED = 201;
    // The search that you made doens't have any result
    public static final int NO_CONTENT = 204;
    // Was made a bad request
    public static final int BAD_REQUEST = 400;
    // The url that you searched was not found
    public static final int NOT_FOUND = 404;
    // Something unexpected happened
    public static final int INTERNAL_ERROR = 500;
}
