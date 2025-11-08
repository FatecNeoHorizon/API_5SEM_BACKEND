package com.neohorizon.api.security;

import java.util.ArrayList;
import java.util.List;

import com.neohorizon.api.enums.RoleType;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {

    private String email;
    private String password;
    private List<String> roleTypes;

    public Login() {
        this.roleTypes = new ArrayList<>();
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Login(String email, String password, List<String> roles) {
        this.email = email;
        this.password = password;
        this.roleTypes = roles;
    }

    public List<String> getRoleTypes() {
        List<String> normalizedRoles = new ArrayList<>();
        if (this.roleTypes == null) {
            return normalizedRoles;
        }
        for (String role : this.roleTypes) {
            if (role == null) {
                continue;
            }
            try {
                RoleType rt = RoleType.valueOf(role.trim());
                normalizedRoles.add(rt.name());
            } catch (IllegalArgumentException ex) {
                // ignore invalid role values
            }
        }
        return normalizedRoles;
    }

    public void addRole(String role) {
        if (this.roleTypes == null) {
            this.roleTypes = new ArrayList<>();
        }
        this.roleTypes.add(role);
    }
    
    public List<String> getAuthorities() {
        // return normalized role names (safe for nulls/invalid values)
        return getRoleTypes();
    }

}
