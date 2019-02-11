package mailservice.repository;

import mailservice.entities.Letter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterRepository extends CrudRepository<Letter, Long> {
}
