package com.vector.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author WangJiaHui
 * @description: 登录实体
 * @ClassName LoginEntity
 * @date 2022/9/8 16:44
 */
@Data
@NoArgsConstructor
public class LoginEntity implements UserDetails, Serializable {
    private Admin admin;

    private List<Role> permissions;

    public LoginEntity(Admin admin, List<Role> permissions) {
        this.admin = admin;
        this.permissions = permissions;
    }

    //存储SpringSecurity所需要的权限信息的集合
    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(Optional.ofNullable(authorities).isPresent()){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.admin.getEnabled();
    }
}
