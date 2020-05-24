package com.emu.apps.qcm.services;

import com.emu.apps.qcm.domain.UserDOService;
import com.emu.apps.qcm.domain.entity.users.User;
import com.emu.apps.qcm.web.dtos.UserDto;
import com.emu.apps.qcm.mappers.UserMapper;
import com.emu.apps.shared.security.PrincipalUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * User Business Delegate
 *<p>
 *
 * @since 2.2.0
 * @author eric
 */
@Service
public class UserService {

    @Value("${spring.profiles.active}")
    private String profiles;

    private final UserDOService userDOService;

    private final UserMapper userMapper;

    public UserService(UserDOService userDOService, UserMapper userMapper) {
        this.userDOService = userDOService;
        this.userMapper = userMapper;
    }

    public Map <String, String> principal(Principal principal) {
        Map <String, String> map = new LinkedHashMap <>();
        if (Objects.nonNull(principal)) {
            map.put("name", PrincipalUtils.getEmail(principal));
            map.put("profiles", profiles);
        }
        return map;
    }

    /**
     *
     * @param principal
     * @return the current user
     */
    public UserDto user(Principal principal) {
        UserDto userDto;
        if (Objects.nonNull(principal)) {
            String email = PrincipalUtils.getEmail(principal);
            User user = userDOService.findByEmailContaining(email);
            if (Objects.isNull(user)) {
                userDto = new UserDto();
                userDto.setEmail(email);
            } else {
                userDto = userMapper.modelToDto(user);
            }
        } else {
            userDto = new UserDto();
        }

        return userDto;

    }

    /**
     * Update a user
     * @param userDto
     * @param principal
     * @return the updated user DTO
     */

    public UserDto updateUser(@RequestBody UserDto userDto, Principal principal) {
        var user = userDOService.findByEmailContaining(userDto.getEmail());
        if (Objects.isNull(user)) {
            user = new User();
        }
        userMapper.dtoToModel(user, userDto);
        return userMapper.modelToDto(userDOService.save(user));

    }

}