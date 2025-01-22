package joo.demo.todolist.repository;

import joo.demo.todolist.domain.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findProfileByEmail(String email);

    Profile findProfileByUsername(String username);
}
