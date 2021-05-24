package ChatApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysql.cj.x.protobuf.MysqlxCrud.Insert;

import ChatApp.Entities.Conversations;
import ChatApp.Entities.Login;
import ChatApp.Entities.Messages;

@Repository
public interface ConversationsRepo extends CrudRepository<Conversations, String>{
	
	@Query(value = "Select * from Conversations", nativeQuery = true)
	List<Conversations> getAllConversations();
	
	/*
	 * @Query(value =
	 * "select c.* from Conversations c join ConversationsToLogin ctl on (ctl.conversationsid =c.conversationsid)"
	 * , nativeQuery = true) List<Conversations>
	 * OnlyPassCertainUsersInConversations(Integer loginid);
	 */
	
	
}	