package com.ecom.Configuration;

import com.ecom.Utility.AppConstant;
import com.ecom.model.UserDtl;
import com.ecom.service.UserDtlService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    UserDtlService userDtlService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
       String email= request.getParameter("username");
       UserDtl user=userDtlService.getUserDtlByEmail(email);

       if(user.getIsEnabled()){
           if(user.getAccountNonLocked()){

                if(user.getFailedAttempt()==null || user.getFailedAttempt() < AppConstant.ATTEMPT_TIME){
                    userDtlService.increaseFailedAttempts(user);
                }
                else{
                    userDtlService.userAccountLock(user);
                    exception=new LockedException("User account is inactive || failed attempts 3");
                }
           }
           else{
               if(userDtlService.unlockAccountTimeExpired(user)){
                   exception=new LockedException("User account is Active ! Try again to login !");
               }
               else {
                   exception=new LockedException("User account is locked please try after sometime");
               }

           }
       }
       else{
           exception=new LockedException("User account is Blocked");
       }

       super.setDefaultFailureUrl("/signIn?error");
       super.onAuthenticationFailure(request, response, exception);
    }



}
