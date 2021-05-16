package com.emu.apps.qcm.infra.persistence.mappers;


import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.AccountEntity;
import com.emu.apps.qcm.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UuidMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    User modelToDto(AccountEntity user);

    AccountEntity dtoToModel(User userDto);

    AccountEntity dtoToModel(@MappingTarget AccountEntity user, User userDto);

}