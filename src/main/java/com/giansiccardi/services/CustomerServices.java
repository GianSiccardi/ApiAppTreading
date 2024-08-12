package com.giansiccardi.services;

import com.giansiccardi.config.JwtProvider;
import com.giansiccardi.enums.VerificationType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.TwoFactorAuth;
import com.giansiccardi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServices {

private final CustomerRepository customerRepository;


private final CustomerUserDetailsService customerUserDetailsService;
public Customer createCustomer(Customer customer) throws Exception {

    Customer emailExist= customerRepository.findByEmail(customer.getEmail());
    if(emailExist!=null){
        throw new Exception("Email ocupado");
    }


    return customerRepository.save(customer);
}
public Authentication authentication(String email, String password){
    UserDetails userDetails=customerUserDetailsService.loadUserByUsername(email);

    if(userDetails==null){
        throw new BadCredentialsException("Email invaldio");
    }
    if(!password.equals(userDetails.getPassword())){
        throw new BadCredentialsException("Contraseña incorrecta");

    }
    return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
}

public Customer findByEmail(String email){
    return customerRepository.findByEmail(email);
}

public Customer findCustomerByJwt(String jwt) throws Exception {

    String email=JwtProvider.getEmailFromToken(jwt);
    Customer customer=customerRepository.findByEmail(email);
    if (customer == null) {
        throw new Exception("usuario no encontrado");
    }

    return customer;
}

public Customer findById(Long id){
    return customerRepository.findById(id).orElse(null);
}

public Customer enableTwoFactorAuthentication(VerificationType verificationType,String sendto,Customer customer){
    TwoFactorAuth twoFactorAuth= new TwoFactorAuth();
    twoFactorAuth.setEnabled(true);
    twoFactorAuth.setSendTo(verificationType);
    customer.setTwoFactorAuth(twoFactorAuth);
    return customerRepository.save(customer);
}

public Customer updatePassword(Long id,String password){
Customer customer=customerRepository.findById(id).orElse(null);
customer.setPassword(password);
return customerRepository.save(customer);

}
}
