package com.example.project.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class StatisticDateDto {

    private @NotNull Date start;
    private @NotNull Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
