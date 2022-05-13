package zju.se.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zju.se.management.entity.Arrange;
import zju.se.management.service.ArrangeService;
import zju.se.management.utils.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@ResponseStatus(HttpStatus.OK)
public class ArrangeController extends BaseController {

    private final ArrangeService arrangeService;

    @Autowired
    public ArrangeController(ArrangeService arrangeService) {
        this.arrangeService = arrangeService;
    }

    @GetMapping("/arrange")
    @Deprecated
    public Response<ArrangeListResponseData> getArrange() {
        return ResponseOK(new ArrangeListResponseData(arrangeService.getAllArranges()), "查询成功");
    }


}
