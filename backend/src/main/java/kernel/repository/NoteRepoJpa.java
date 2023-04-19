package kernel.repository;

import kernel.entity.Note;
import kernel.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepoJpa extends JpaRepository<Note, Integer>
{
    public List<Note> findAll();
    Optional<Note> findById(Integer id);
}
