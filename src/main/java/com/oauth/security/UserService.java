package com.oauth.security;
import com.oauth.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public UserDetails getUserDetails(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken authToken) {
            OAuth2User user = authToken.getPrincipal();

            Map<String, Object> attributes = user.getAttributes();

            String email = (String) attributes.get("email");
            String login = (String) attributes.get("login");
            // GitHub may use "login" instead of "name" for the username
            String name = (String) attributes.get("name");

            String pictureUrl = null;
            if (authToken.getAuthorizedClientRegistrationId().equals("google")) {
                pictureUrl = (String) attributes.get("picture");
            } else if (authToken.getAuthorizedClientRegistrationId().equals("github")) {
                pictureUrl = (String) attributes.get("avatar_url");
            }

            return UserDetails.builder()
                    .email(email)
                    .name(name)
                    .username(login)
                    .profile(pictureUrl)
                    .build();
        }
        return null;
    }

    public String getAccessToken(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId, oauthToken.getName());

            if (authorizedClient != null) {
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                return "Client id "+clientRegistrationId+" "+accessToken.getTokenValue()
                        +"Token type: "+ accessToken.getTokenType().getValue();
            } else {
                return "No authorized client found for " + clientRegistrationId;
            }
        }
        return "User not authenticated via OAuth2";
    }
}

