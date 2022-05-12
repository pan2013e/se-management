package zju.se.management.utils;

import lombok.*;
import zju.se.management.entity.User;

@Getter
@Setter
public class UserResponseData extends ResponseData {
    private final int id;
    private final String userName;

    public UserResponseData(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
    }
}
