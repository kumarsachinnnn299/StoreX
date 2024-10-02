package com.ecommerce.project.security.response;
//This is same as UserInfoResponse
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private List<String>roles;

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
