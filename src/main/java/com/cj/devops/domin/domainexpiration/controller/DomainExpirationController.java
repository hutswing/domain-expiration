package com.cj.devops.domin.domainexpiration.controller;

import com.cj.devops.domin.domainexpiration.dto.DomainExpiration;
import com.cj.devops.domin.domainexpiration.service.DomainExpirationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DomainExpirationController {
    private final DomainExpirationService domainExpirationService;

    @GetMapping("/domain/expiration")
    public DomainExpiration getDomainExpiration(@RequestParam("domainName") String domainName){
        return domainExpirationService.getDomainExpiration(domainName);
    }
}
