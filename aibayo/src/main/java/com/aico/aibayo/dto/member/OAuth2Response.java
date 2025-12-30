package com.aico.aibayo.dto.member;

public interface OAuth2Response {
    String getProvider(); // ex) naver, google
    String getProviderId();
    String getEmail();
    String getName();}
