package zju.se.management.controller;

import lombok.*;
import zju.se.management.utils.ResponseData;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response {
    private int code;
    private ResponseData data;
    private String message;
}
