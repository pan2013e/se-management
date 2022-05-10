package zju.se.management.utils;

import lombok.*;
import zju.se.management.entity.User;

@Getter
@Setter
public class UserResponseData extends ResponseData {
    private final User user;

    public UserResponseData(User user) {
        this.user = user;
    }
}
