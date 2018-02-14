package it.eng.repository;

import it.eng.domain.File;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.lang.String;
import java.util.List;


/**
 * Spring Data JPA repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
	
	public Page<File> findByLogin(Pageable pageable, String name);
	
}
