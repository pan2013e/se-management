package zju.se.management.utils;

import lombok.Getter;
import lombok.Setter;
import zju.se.management.entity.Arrange;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArrangeListResponseData extends ResponseData {
    private final List<ArrangeResponseData> arranges;

    public ArrangeListResponseData(List<Arrange> arranges){
        this.arranges = new ArrayList<>(arranges.size());
        for (Arrange arrange : arranges) {
            this.arranges.add(new ArrangeResponseData(arrange));
        }
    }
}
