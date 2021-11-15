package com.net.demoTokenManager.securityold;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Information {
    private int id;
    private String name;
    private String lastName;
    private LocalDateTime timeStamp;
}

