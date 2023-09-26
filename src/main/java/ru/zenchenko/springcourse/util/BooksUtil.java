package ru.zenchenko.springcourse.util;

import ru.zenchenko.springcourse.model.Book;

import java.util.List;

public class BooksUtil {
    private List<Book> bookList;
    private boolean isPaginated;

    private int pageNum;
    private int booksPerPage;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getBooksPerPage() {
        return booksPerPage;
    }

    public void setBooksPerPage(int booksPerPage) {
        this.booksPerPage = booksPerPage;
    }

    public BooksUtil(List<Book> bookList, boolean isPaginated) {
        this.bookList = bookList;
        this.isPaginated = isPaginated;
    }

    public BooksUtil(List<Book> bookList, boolean isPaginated, int pageNum, int booksPerPage) {
        this.bookList = bookList;
        this.isPaginated = isPaginated;
        this.pageNum = pageNum;
        this.booksPerPage = booksPerPage;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public boolean isPaginated() {
        return isPaginated;
    }

    public void setPaginated(boolean paginated) {
        isPaginated = paginated;
    }
}
