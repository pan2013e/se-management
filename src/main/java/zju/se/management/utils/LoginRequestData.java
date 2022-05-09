package zju.se.management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestData extends RequestData {
    private String userName;
    private String password;
}
