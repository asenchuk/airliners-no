package net.taviscaron.airliners.data;

import android.content.Context;
import net.taviscaron.airliners.model.SearchResult;

import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Search loader
 * @author Andrei Senchuk
 */
public class SearchLoader extends BaseLoader<SearchResult> {
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

    public static class RequestBuilder {
        private Map<String, Object> params = new HashMap<String, Object>();

        public RequestBuilder setAircraft(String aircraft) {
            params.put(AIRCRAFT_PARAM, aircraft);
            return this;
        }

        public RequestBuilder setAirline(String airline) {
            params.put(AIRLINE_PARAM, airline);
            return this;
        }

        public RequestBuilder setPlace(String place) {
            params.put(PLACE_PARAM, place);
            return this;
        }

        public RequestBuilder setCountry(String country) {
            params.put(COUNTRY_PARAM, country);
            return this;
        }

        public RequestBuilder setRemark(String remark) {
            params.put(REMARK_PARAM, remark);
            return this;
        }

        public RequestBuilder setReg(String reg) {
            params.put(REG_PARAM, reg);
            return this;
        }

        public RequestBuilder setCn(String cn) {
            params.put(CN_PARAM, cn);
            return this;
        }

        public RequestBuilder setCode(String code) {
            params.put(CODE_PARAM, code);
            return this;
        }

        public RequestBuilder setDate(String date) {
            params.put(DATE_PARAM, date);
            return this;
        }

        public RequestBuilder setYear(Integer year) {
            params.put(YEAR_PARAM, year);
            return this;
        }

        public RequestBuilder setLimit(Integer limit) {
            if(limit < 0) {
                throw new IllegalArgumentException("Limit value should be >= 0");
            }
            params.put(LIMIT_PARAM, limit);
            return this;
        }

        public RequestBuilder setPage(Integer page) {
            if(page < 0) {
                throw new IllegalArgumentException("Page value should be >= 0");
            }
            params.put(PAGE_PARAM, page);
            return this;
        }

        public RequestBuilder clear() {
            params.clear();
            return this;
        }

        public Map<String, Object> createParams() {
            return Collections.unmodifiableMap(params);
        }

        public RequestBuilder execute(SearchLoader loader, BaseLoaderCallback<SearchResult> callback) {
            loader.load(params, callback);
            return this;
        }
    }

    public static SearchLoader createTopLoader(Context context) {
        return createTopLoader(context, null);
    }

    public static SearchLoader createTopLoader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/top", urlStreamHandler);
    }

    public static SearchLoader createTop15Loader(Context context) {
        return createTop15Loader(context, null);
    }

    public static SearchLoader createTop15Loader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/top15", urlStreamHandler);
    }

    public static SearchLoader createSearchLoader(Context context) {
        return createSearchLoader(context, null);
    }

    public static SearchLoader createSearchLoader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/search", urlStreamHandler);
    }

    private SearchLoader(Context context, String relativeUrl, URLStreamHandler urlStreamHandler) {
        super(context, relativeUrl, SearchResult.class, urlStreamHandler);
    }
}
