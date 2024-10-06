package com.ecom.service;

import com.ecom.model.UserDtl;

import java.util.List;

public interface UserDtlService {
    public UserDtl saveUserDtl(UserDtl userDetail);
    public UserDtl getUserDtl(int id);
    public UserDtl getUserDtlByEmail(String email);
    public List<UserDtl> getUserDtls();
    public Boolean updateUserAccountStatus(int id, Boolean status);
    public void userAccountLock(UserDtl userDetail);
    public Boolean unlockAccountTimeExpired(UserDtl userDetail);
    public void increaseFailedAttempts(UserDtl userDetail);
    public boolean emailExist(String email);
    public UserDtl getUserDtlByToken(String token);
}
