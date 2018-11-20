package com.emu.apps.users.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Map;

@Api(value = "user-store", description = "All operations ", tags = "Users")
@RequestMapping(UserApi.API_V1 +"/users/me")
public interface UserRestApi {

    @ApiOperation(value = "get Current user", response = Map.class, nickname = "getCurrentUser")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    Map <String, String> user(Principal principal);
}