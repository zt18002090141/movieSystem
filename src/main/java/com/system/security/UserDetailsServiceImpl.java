package com.system.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.entity.User;
import com.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userService.getOne(new QueryWrapper<User>().eq("username",username));
        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new AccountUser(user.getId(),user.getUsername(),user.getPassword(),getUserAuthority(user.getId()));
    }

    public List<GrantedAuthority> getUserAuthority(Long userId){
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userService.getUserAuthorityInfo(userId));
    }
}
