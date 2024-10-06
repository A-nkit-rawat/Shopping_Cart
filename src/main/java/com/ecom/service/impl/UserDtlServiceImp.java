package com.ecom.service.impl;

import com.ecom.Utility.AppConstant;
import com.ecom.model.UserDtl;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserDtlServiceImp implements UserDtlService {



    @Autowired
    UserRepository userRepository;

    @Override
    public UserDtl saveUserDtl(UserDtl userDetail) {
        if(ObjectUtils.isEmpty(userDetail)){
            return null;
        }
        return userRepository.save(userDetail);
    }

    @Override
    public UserDtl getUserDtl(int id) {
        if(id==0.00){
            return null;
        }
        UserDtl user= userRepository.findById(id).get();
        return user;
    }

    @Override
    public UserDtl getUserDtlByEmail(String email){
        if(email==null){
            System.out.println("email is null");
            return null;
        }
        UserDtl userDtl=userRepository.findByEmail(email);
        System.out.println(userDtl);
        return userDtl;
    }
    @Override
    public List<UserDtl> getUserDtls() {
        return userRepository.findAll();
    }

    public Boolean updateUserAccountStatus(int id,Boolean status){
        UserDtl userDtl=userRepository.findById(id).orElse(null);
        if(userDtl==null){
            return false;
        }
        userDtl.setIsEnabled(status);
        userRepository.save(userDtl);
        return true;
    }

    @Override
    public void userAccountLock(UserDtl userDtl) {
        userDtl.setAccountNonLocked(false);
        userDtl.setLockTime(new Date());
        userRepository.save(userDtl);
    }

    @Override
    public Boolean unlockAccountTimeExpired(UserDtl userDtl) {
        long CurrentUserUnlockTime=userDtl.getLockTime().getTime()+ AppConstant.UNLOCK_TIME;
        long CurrentUserTime=System.currentTimeMillis();
        if(CurrentUserUnlockTime<=CurrentUserTime){
            userDtl.setLockTime(null);
            userDtl.setAccountNonLocked(true);
            userDtl.setIsEnabled(true);
            userDtl.setFailedAttempt(0);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempts(UserDtl userDetail) {
        userDetail.setFailedAttempt(userDetail.getFailedAttempt()+1);
        userRepository.save(userDetail);

    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDtl getUserDtlByToken(String token) {
        if(token==null){
            System.out.println("token is null");
            return null;
        }
        return userRepository.findByToken(token);
    }


}
