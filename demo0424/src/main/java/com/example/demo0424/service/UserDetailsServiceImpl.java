package com.example.demo0424.service;

import com.example.demo0424.dao.UserDao;
import com.example.demo0424.entity.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserDao userDao ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userDao.getUserByUsername(username);
        if (userDto == null) {
            return null;
        }
        List<String>privileges = userDao.findPrivilegesByUserId(userDto.getId());
        String[] privilegeArray = new String[privileges.size()];
        privileges.toArray(privilegeArray);
        UserDetails userDetails = User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities(privilegeArray).build();
        return userDetails;
    }
}