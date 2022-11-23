package com.cj.devops.domin.domainexpiration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainExpiration {
    private String domainName;
    private String expiredDate;
}
