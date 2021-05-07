package ChatApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ChatApp.Entities.Conversations;
import ChatApp.Entities.Login;

@Repository
public interface ConversationsRepo extends CrudRepository<Conversations, String>{
	
	@Query(value = "Select * from Conversations", nativeQuery = true)
	Iterable<Conversations> getAllConversations();
	
	
//	Iterable<Conversations> getAllConversationWhereLogininConversation(Login login);

}	