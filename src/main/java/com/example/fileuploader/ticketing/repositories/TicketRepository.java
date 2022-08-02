package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {
    List<SystemTicketModel> findAllByUserId(long user_id);
    SystemTicketModel findByUserIdAndUsed(long userId, boolean used);
    SystemTicketModel findByTicketId (int ticketId);
    void removeByTicketId(int ticketId);
    void removeAllByUsedAndTimestampLessThan(boolean used, long timestamp);

    @Query("DELETE FROM SystemTicketModel ticket WHERE ticket.timestamp+(5*60*1000) < :#{#timeMillis}")
    @Modifying
    void removeExpiredTickets(long timeMillis);
}
