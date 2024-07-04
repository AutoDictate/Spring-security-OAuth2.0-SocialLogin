package com.oauth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {

    private String username;
    private String email;
    private String name;
    private String profile;
}
