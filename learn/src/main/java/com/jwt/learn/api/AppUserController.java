package com.jwt.learn.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jwt.learn.model.AppUser;
import com.jwt.learn.model.Role;
import com.jwt.learn.service.AppUserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1")
public class AppUserController {
    private final AppUserService appUserService;
    @GetMapping(path = "/users")
    public ResponseEntity<List<AppUser>>getUsers(){
        return ResponseEntity.ok().body(
            appUserService.getAppUsers()
        );
    }
    @PostMapping(path = "/user/save")
    public ResponseEntity<AppUser>saveUser(
        @RequestBody AppUser appUser
    ){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());

        return ResponseEntity.created(uri).body(
            appUserService.saveAppUser(appUser)
        );
    }
    @PostMapping(path = "/role/save")
    public ResponseEntity<Role>saveRole(
        @RequestBody Role role
    ){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/save").toUriString());

        return ResponseEntity.created(uri).body(
           appUserService.saveRole(role)
        );
    }
    @PostMapping(path = "/role/addtouser")
    public ResponseEntity<?>saveRoleToUser(
        @RequestBody RoleToAppUserform roleToAppUserform
    ){
        appUserService.addRoleToAppUser(roleToAppUserform.getUseremail(), roleToAppUserform.getRolename());
        return ResponseEntity.ok().build();
    }
    @GetMapping(path="/eg")
    public String testing(){
        return "Checking ROLE only ADMIN can view";
    }
   
}
@Data
class RoleToAppUserform{
    private String useremail;
    private String rolename;
}

