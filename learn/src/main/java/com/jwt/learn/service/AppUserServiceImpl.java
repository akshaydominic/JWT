package com.jwt.learn.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.learn.model.AppUser;
import com.jwt.learn.model.Role;
import com.jwt.learn.repo.AppUserRepo;
import com.jwt.learn.repo.RoleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class AppUserServiceImpl implements AppUserService,UserDetailsService{

    
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByEmail(email);
        if(appUser == null){
            log.error("user {} not found in the database",email);
            throw new UsernameNotFoundException("user not found in the database");
        }else{
            log.info("User->{} found in the database",appUser.getUsername());
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(),authorities);
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("appuser-> {} saved",appUser.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        return appUserRepo.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("role-> {} saved ",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToAppUser(String email, String roleName) {
        //TODO in production add more logic to validate
        AppUser appUser = appUserRepo.findByEmail(email);
        Role role = roleRepo.findByName(roleName);
        log.info("role-> {} added to user-> {}",roleName,email);
        appUser.getRoles().add(role);        
    }

    @Override
    public AppUser getAppUser(String email) {
        log.info("fetching user {}",email);
        
        return appUserRepo.findByEmail(email);
    }

    @Override
    public List<AppUser> getAppUsers() {
        log.info("fetching all users");
        return appUserRepo.findAll();
    }

    
    
}
