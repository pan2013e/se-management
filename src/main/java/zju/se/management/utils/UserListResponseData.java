package zju.se.management.utils;

import lombok.*;
import zju.se.management.entity.User;

import java.util.List;

@Getter
@Setter
public class UserListResponseData extends ResponseData {
    private final List<User> users;

    public UserListResponseData(List<User> users){
        this.users = users;
    }
}
