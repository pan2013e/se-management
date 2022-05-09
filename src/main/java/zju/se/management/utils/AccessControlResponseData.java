package zju.se.management.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccessControlResponseData extends ResponseData {
    private String userName;
    private String role;
}
