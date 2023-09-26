package ru.zenchenko.springcourse.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zenchenko.springcourse.model.Person;
import ru.zenchenko.springcourse.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id){
        return peopleRepository.findById(id);
    }

    @Transactional
    public void save(Person person){
        person.setCreatedAt(new Date());

        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person){
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id){
        Optional<Person> p = peopleRepository.findById(id);
        p.ifPresent(peopleRepository::delete);
    }

    public boolean containsName(String name) {
        Optional<Person> p = peopleRepository.findByName(name);

        return p.isPresent();
    }
}
