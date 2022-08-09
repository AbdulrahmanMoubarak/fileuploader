package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {
    List<SystemTicketModel> findAllByUserId(long user_id);
    SystemTicketModel findByUserIdAndStatus(long userId, TicketStatus status);
    SystemTicketModel findByTicketId (int ticketId);
    void removeByTicketId(int ticketId);
    @Query("DELETE FROM SystemTicketModel ticket WHERE ticket.timestamp+(5*60*1000) < :#{#timeMillis} AND ticket.status = :#{#status}")
    @Modifying
    void removeExpiredTickets(long timeMillis, TicketStatus status);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = :#{#status} WHERE ticket.ticketId = :#{#ticketId}")
    @Modifying
    void updateTicketStatus(TicketStatus status, int ticketId);
}
