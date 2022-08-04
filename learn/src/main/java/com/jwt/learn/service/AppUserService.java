package com.jwt.learn.service;

import java.util.List;

import com.jwt.learn.model.AppUser;
import com.jwt.learn.model.Role;

public interface AppUserService {
    AppUser saveAppUser(AppUser appUser);
    Role saveRole(Role role);
    //TODO check if the user already exists
    void addRoleToAppUser(String email,String roleName);
    AppUser getAppUser(String email);
    // TODO implement Pageable to optimize
    List<AppUser> getAppUsers();
}
