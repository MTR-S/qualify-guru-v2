package com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain.AuthRequest;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.password()))")
    public abstract UserEntity toEntity(AuthRequest request);
}
