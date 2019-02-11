package mailservice.repository;

import mailservice.entities.Letter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LetterRepository extends CrudRepository<Letter, UUID> {
}
