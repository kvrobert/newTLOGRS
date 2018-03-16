package com.rkissvincze.User;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"sub",
"nickname",
"name",
"picture",
"updated_at",
"email",
"email_verified"
})
public class UserInfo {

@JsonProperty("sub")
public String sub;
@JsonProperty("nickname")
public String nickname;
@JsonProperty("name")
public String name;
@JsonProperty("picture")
public String picture;
@JsonProperty("updated_at")
public String updatedAt;
@JsonProperty("email")
public String email;
@JsonProperty("email_verified")
public Boolean emailVerified;

}