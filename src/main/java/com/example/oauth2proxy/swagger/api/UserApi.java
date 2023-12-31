/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.swagger.model.Error;
import com.example.oauth2proxy.swagger.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "User", description = "the User API")
public interface UserApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /user/me : User Profile of Current Login User
     *
     * @return Request served successfully, returning result (status code 200)
     *         or System Internal Error. Please contact System Administrator (status code 200)
     */
    @Operation(
        operationId = "getCurrentUser",
        summary = "User Profile of Current Login User",
        tags = { "User" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Request served successfully, returning result", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)),
                @Content(mediaType = "application/vnd.error+json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "default", description = "System Internal Error. Please contact System Administrator", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                @Content(mediaType = "application/vnd.error+json", schema = @Schema(implementation = Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "bearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/user/me",
        produces = { "application/json", "application/vnd.error+json" }
    )
    default ResponseEntity<User> getCurrentUser(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"name\" : \"name\", \"avatar\" : \"avatar\", \"email\" : \"email\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/vnd.error+json"))) {
                    String exampleString = "Custom MIME type example not yet supported: application/vnd.error+json";
                    ApiUtil.setExampleResponse(request, "application/vnd.error+json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
