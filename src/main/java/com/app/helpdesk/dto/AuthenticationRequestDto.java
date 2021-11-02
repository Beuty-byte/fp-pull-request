package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequestDto {

    private static final String FIELDS_IS_EMPTY = "Please fill out the required field.";
    private static final String NOT_VALID_DATA = "Please make sure you are using a valid email or password";

    @NotBlank(message = FIELDS_IS_EMPTY)
    @Email(regexp = "^[^@|\\.].+@.+\\..+[^@|\\.]$", message = NOT_VALID_DATA)
    @Size(max = 100)
    private String username;

    @NotBlank(message = FIELDS_IS_EMPTY)
    @Pattern(regexp = "((?=.*d)(?=.*[\\p{Lu}])(?=.*[\\d])(?=.*[\\p{Ll}])(?=.*[\\p{Punct}]).{6,20})",
            message = NOT_VALID_DATA)
    private String password;
}
