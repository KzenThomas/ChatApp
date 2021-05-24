package ChatApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ChatApp.Entities.Login;;

@Repository
public interface LoginRepo extends CrudRepository<Login, String>{
	
	@Query(value = "Select * from Login", nativeQuery = true)
	List<Login> getAllLogin();


}	