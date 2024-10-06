package com.ecom.Utility;

import com.ecom.model.UserDtl;
import com.ecom.service.UserDtlService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDtlService userDtlService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("username: " + email);
        if(ObjectUtils.isEmpty(email)){
            return null;
        }
        UserDtl user=userDtlService.getUserDtlByEmail(email);
//        System.out.println(user);
        return new UserDetailsImpl(userDtlService.getUserDtlByEmail(email));
    }
}
