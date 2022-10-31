package bg.musala.test.yrm.jpa.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class PageWrapper<T> implements Serializable
{
    private List<T> content;
    private Pageable pageable;
    private int totalPages;
    private boolean last;
    private long totalElements;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;

    public PageWrapper(List<T> content, Pageable pageable, int totalPages, boolean last, long totalElements, int size,
                       int number, Sort sort, int numberOfElements, boolean first, boolean empty)
    {
        this.content = content;
        this.pageable = pageable;
        this.totalPages = totalPages;
        this.last = last;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
        this.sort = sort;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.empty = empty;
    }

    public PageWrapper (List<T> content, @NotNull Page<T> entityPage)
    {
        this.content = content;
        this.pageable = entityPage.getPageable();
        this.totalPages = entityPage.getTotalPages();
        this.last = entityPage.isLast();
        this.totalElements = entityPage.getTotalElements();
        this.size = entityPage.getSize();
        this.number = entityPage.getNumber();
        this.sort = entityPage.getSort();
        this.numberOfElements = entityPage.getNumberOfElements();
        this.first = entityPage.isFirst();
        this.empty = entityPage.isEmpty();
    }

    public List<T> getContent()
    {
        return content;
    }

    public void setContent(List<T> content)
    {
        this.content = content;
    }

    public Pageable getPageable()
    {
        return pageable;
    }

    public void setPageable(Pageable pageable)
    {
        this.pageable = pageable;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public boolean isLast()
    {
        return last;
    }

    public void setLast(boolean last)
    {
        this.last = last;
    }

    public long getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(long totalElements)
    {
        this.totalElements = totalElements;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public Sort getSort()
    {
        return sort;
    }

    public void setSort(Sort sort)
    {
        this.sort = sort;
    }

    public int getNumberOfElements()
    {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements)
    {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst()
    {
        return first;
    }

    public void setFirst(boolean first)
    {
        this.first = first;
    }

    public boolean isEmpty()
    {
        return empty;
    }

    public void setEmpty(boolean empty)
    {
        this.empty = empty;
    }
}