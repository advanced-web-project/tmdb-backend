package com.movie.tdmb.mapper;

import com.movie.tdmb.dto.SignUpDto;
import com.movie.tdmb.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User SignUpDtoToUser(SignUpDto signUpDto);
}
