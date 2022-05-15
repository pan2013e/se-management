package zju.se.management.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import zju.se.management.service.MQService;
import zju.se.management.utils.Response;
import zju.se.management.utils.ResponseData;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mq")
public class MQController extends BaseController {
    @Resource
    private MQService mqService;

    @Getter
    @Setter
    @AllArgsConstructor
    static class MQResponseData extends ResponseData{
        private List<Map<String, String>> mqList;
    }

    @GetMapping("/consume")
    public Response<MQResponseData> consume(
            @RequestParam(value = "userName") String userName) {
        return ResponseOK(new MQResponseData(mqService.consume(userName)),"获取成功");
    }

    @PostMapping("/produce")
    public Response<?> produce(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content){
        HashMap<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        mqService.produce(userName, map);
        return ResponseOK("保存成功");
    }
}
