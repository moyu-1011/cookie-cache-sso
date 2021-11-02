package com.moyu.sc.domain.dto;


import com.moyu.sc.validation.annotation.ValidPassword;
import com.moyu.sc.validation.annotation.ValidPasswordMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ValidPasswordMatch
public class UserDto implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "Invalid Username")
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 20, message = "Invalid Password")
    @ValidPassword
    private String password;
    private String matchPassword;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "Invalid Name")
    private String name;
}
