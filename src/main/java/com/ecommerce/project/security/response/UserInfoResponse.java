package com.ecommerce.project.security.response;
//This is same as UserInfoResponse
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private List<String>roles;
}
