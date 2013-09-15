package net.taviscaron.airliners.model;

import android.text.TextUtils;
import net.taviscaron.airliners.util.Validate;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Search params wrapper
 * @author Andrei Senchuk
 */
public class SearchParams implements Serializable {
    public static final String AIRCRAFT_PARAM = "aircraft";
    public static final String AIRLINE_PARAM = "airline";
    public static final String PLACE_PARAM = "place";
    public static final String COUNTRY_PARAM = "country";
    public static final String REMARK_PARAM = "remark";
    public static final String REG_PARAM = "reg";
    public static final String CN_PARAM = "cn";
    public static final String CODE_PARAM = "code";
    public static final String DATE_PARAM = "date";
    public static final String YEAR_PARAM = "year";
    public static final String LIMIT_PARAM = "limit";
    public static final String PAGE_PARAM = "page";

    public static final Integer DEFAULT_LIMIT = 15;

    private String aircraft;
    private String airline;
    private String place;
    private String country;
    private String remark;
    private String reg;
    private String cn;
    private String code;
    private String date;
    private String year;
    private Integer limit = DEFAULT_LIMIT;
    private Integer page = 1;

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        Validate.notNull(limit, "limit should be not null");
        Validate.makeSure(limit >= 1 && limit <= 1024, "1 <= limit <= 1000");
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        Validate.notNull(page, "page should be not null");
        Validate.makeSure(page >= 1, "page should be >= 1");
        this.page = page;
    }

    public boolean isEmpty() {
        boolean empty = true;
        empty = empty && TextUtils.isEmpty(aircraft);
        empty = empty && TextUtils.isEmpty(airline);
        empty = empty && TextUtils.isEmpty(place);
        empty = empty && TextUtils.isEmpty(country);
        empty = empty && TextUtils.isEmpty(date);
        empty = empty && TextUtils.isEmpty(year);
        empty = empty && TextUtils.isEmpty(reg);
        empty = empty && TextUtils.isEmpty(cn);
        empty = empty && TextUtils.isEmpty(code);
        empty = empty && TextUtils.isEmpty(remark);
        return empty;
    }

    public Object toLoaderParam() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        putValue(params, AIRCRAFT_PARAM, aircraft);
        putValue(params, AIRLINE_PARAM, airline);
        putValue(params, PLACE_PARAM, place);
        putValue(params, COUNTRY_PARAM, country);
        putValue(params, REMARK_PARAM, remark);
        putValue(params, REG_PARAM, reg);
        putValue(params, CN_PARAM, cn);
        putValue(params, CODE_PARAM, code);
        putValue(params, DATE_PARAM, date);
        putValue(params, YEAR_PARAM, year);
        putValue(params, LIMIT_PARAM, limit);
        putValue(params, PAGE_PARAM, page);
        return params;
    }

    private void putValue(HashMap<String, Object> map, String key, Object value) {
        if(value != null) {
            map.put(key, value);
        }
    }
}
