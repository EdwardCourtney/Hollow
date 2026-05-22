package model.response;

import java.util.List;

public class PageResponse<T> {
    public List<T> content;
    public int number;
    public int size;
    public int totalPages;
    public long totalElements;
    public boolean first;
    public boolean last;

    public PageResponse() {
    }
}
