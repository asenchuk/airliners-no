package net.taviscaron.airliners.model;

import java.util.Iterator;

/**
 * Search result entity class
 * @author Andrei Senchuk
 */
public class SearchResult implements Iterable<AircraftSearchResult> {
    protected long from;
    protected long to;
    protected long total;
    protected long count;
    protected AircraftSearchResult[] items;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public AircraftSearchResult[] getItems() {
        return items;
    }

    public void setItems(AircraftSearchResult[] items) {
        this.items = items;
    }

    @Override
    public Iterator<AircraftSearchResult> iterator() {
        return new Iterator<AircraftSearchResult>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return (index < items.length);
            }

            @Override
            public AircraftSearchResult next() {
                return items[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Removing items from the search result is unsupported");
            }
        };
    }
}
