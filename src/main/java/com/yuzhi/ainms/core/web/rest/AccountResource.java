package com.yuzhi.ainms.core.web.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yuzhi.ainms.core.domain.Province;
import com.yuzhi.ainms.core.domain.User;
import com.yuzhi.ainms.core.repository.ProvinceRepository;
import com.yuzhi.ainms.core.repository.UserRepository;
import com.yuzhi.ainms.core.service.ProvinceService;
import com.yuzhi.ainms.core.service.UserService;
import com.yuzhi.ainms.core.service.dto.AdminUserDTO;
import com.yuzhi.ainms.core.service.dto.UserDTO;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import com.yuzhi.ainms.core.web.rest.errors.LoginAlreadyUsedException;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;


@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final ProvinceService provinceService;

    private final UserRepository userRepository;

    private final ProvinceRepository provinceRepository;

    public AccountResource(UserService userService, ProvinceService provinceService, UserRepository userRepository, ProvinceRepository provinceRepository) {
        this.userService = userService;
        this.provinceService = provinceService;
        this.userRepository = userRepository;
        this.provinceRepository = provinceRepository;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @param principal the current user; resolves to {@code null} if not authenticated.
     * @return the current user.
     * @throws AccountResourceException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserVM getAccount(Principal principal) {
        if (principal == null) {
            //throw new AccountResourceException("User is not authenticated");
            return null;
        }
        log.debug("====REST request to get the current user" + principal.toString());
        if (principal instanceof AbstractAuthenticationToken) {
            return getUserFromAuthentication((AbstractAuthenticationToken) principal);
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    private class UserVM {
        private String login;
        private Set<String> authorities;
        private Long provinceId;

        @JsonCreator
        UserVM(String login, Set<String> authorities, Long provinceId) {
            this.login = login;
            this.authorities = authorities;
            this.provinceId = provinceId;
        }

        public boolean isActivated() {
            return true;
        }
        public Set<String> getAuthorities() {
            return authorities;
        }
        public String getLogin() {
            return login;
        }

        public Long getProvinceId() {
            return provinceId;
        }
    }

    private UserVM getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        // if get pincipal ,then compare with local user in database
        if (!(authToken instanceof OAuth2AuthenticationToken) && !(authToken instanceof JwtAuthenticationToken)) {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        String login = authToken.getName();
        Set<String> defaultAuthorities = authToken.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        log.debug("default login &&defaultAuthorities from sso::" + login + "::" + defaultAuthorities);
        //获取从SSO传过来的工号/用户名,然后通过工号/用户名查找本地用户的所有信息
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(login.toLowerCase());

        //如果SSO过来的工号/用户名在本地数据库中不存在，则创建一个默认用户,无省份
        AdminUserDTO adminUserDTO;
        if(user.isPresent()){
            //用户存在，开始查找权限
           adminUserDTO = new AdminUserDTO(user.get());
           log.debug("==check user exist:"+adminUserDTO.toString());
        } else {
            adminUserDTO = new AdminUserDTO();
            adminUserDTO.setLogin(login.toLowerCase());
            //给用户赋予省份,1501为默认的总公司，同时赋予默认权限
            adminUserDTO.setProvinceId(1501L);
            Set<String> authorities = new HashSet<>(Collections.singleton("ROLE_USER"));
            adminUserDTO.setAuthorities(authorities);
            User newUser = userService.createUser(adminUserDTO);
        }

        UserVM userVM = new UserVM(adminUserDTO.getLogin(), adminUserDTO.getAuthorities(), adminUserDTO.getProvinceId());
        //double check;所有用户都赋予user权限，通过判断省份来查看本省的设备
        //double check,判断userVM的权限是否包含ROLE_USER,如果不包含则添加
        userVM.getAuthorities().add("ROLE_USER");
        log.debug("userVM has been created:" + userVM.getLogin().toString()
            +":"+userVM.getAuthorities().toString() + ":" + userVM.getProvinceId());
        return userVM;
    }
}
