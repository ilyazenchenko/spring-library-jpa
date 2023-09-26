package ru.zenchenko.springcourse.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zenchenko.springcourse.model.Book;
import ru.zenchenko.springcourse.model.Person;
import ru.zenchenko.springcourse.repositories.BooksRepository;
import ru.zenchenko.springcourse.repositories.PeopleRepository;
import ru.zenchenko.springcourse.util.BooksUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    private final PeopleRepository peopleRepository;

    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setBookId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        Optional<Book> b = booksRepository.findById(id);
        b.ifPresent(booksRepository::delete);
    }

    @Transactional
    public void appointBook(int bookId, int personId) {
        Optional<Book> optBook = booksRepository.findById(bookId);
        Optional<Person> person = peopleRepository.findById(personId);
        if (optBook.isPresent() && person.isPresent()) {
            Book book = optBook.get();
            book.setOwner(person.get());
            book.setTakenAt(new Date());
        }
    }

    @Transactional
    public void releaseBook(int bookId) {
        Optional<Book> optBook = booksRepository.findById(bookId);
        optBook.ifPresent(b -> {
            b.setOwner(null);
            b.setTakenAt(null);
        });
    }

    public List<Book> findByPersonId(int id) {
        return booksRepository.findByOwnerId(id);
    }

    public Optional<Person> findOwnerByBookId(int id) {
        Optional<Book> optionalBook = booksRepository.findById(id);
        return optionalBook.map(Book::getOwner);
    }

    public BooksUtil findAll(String pageNumStr, String booksPerPageStr, String sortByYearStr) {
        int pageNum = -1, booksPerPage = -1;
        boolean sortByYear = false;
        try {
            pageNum = Integer.parseInt(pageNumStr);
            booksPerPage = Integer.parseInt(booksPerPageStr);
        }catch (NumberFormatException ignored){}
        try {
            sortByYear = Boolean.parseBoolean(sortByYearStr);
        }catch (Exception ignored){}

        boolean paginated = !(pageNum < 0 || booksPerPage < 1);

        return getBooksUtil(paginated, sortByYear, pageNum, booksPerPage);
    }

    private BooksUtil getBooksUtil(boolean paginated, boolean sortByYear, int pageNum, int booksPerPage) {
        if(paginated && sortByYear){
            return new BooksUtil(booksRepository.findAll(
                    PageRequest.of(pageNum, booksPerPage, Sort.by("year"))).getContent(),
                    true, pageNum, booksPerPage);
        } else if (paginated) {
            return new BooksUtil(booksRepository.findAll(PageRequest.of(pageNum, booksPerPage)).getContent(),
                    true, pageNum, booksPerPage);
        } else if (sortByYear) {
            return new BooksUtil(booksRepository.findAll(Sort.by("year")), false);
        }
        return new BooksUtil(booksRepository.findAll(), false);
    }

    public List<Book> findByPattern(String pattern){
        if(pattern == null || pattern.isEmpty()) return null;
        return booksRepository.findByNameStartingWith(pattern);
    }
}
