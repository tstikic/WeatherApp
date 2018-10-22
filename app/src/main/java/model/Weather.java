package model;

import java.util.Date;

public class Weather {
    public CurrentCondition currentCondition = new CurrentCondition();
    public City city;
    public Long date;
    public String icon;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
