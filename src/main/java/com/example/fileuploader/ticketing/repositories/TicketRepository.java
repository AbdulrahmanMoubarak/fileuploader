package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.services.TicketService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {

    List<SystemTicketModel> findAllByUserId(long user_id);

    List<SystemTicketModel> findAllByUserIdAndStatus(long userId, TicketStatus status);



    SystemTicketModel findByUserIdAndStatus(long userId, TicketStatus status);

    SystemTicketModel findByTicketId(int ticketId);

    void removeByTicketId(int ticketId);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.EXPIRED WHERE ticket.timestamp+(3*60*1000) < :#{#timeMillis} AND ticket.status = :#{#status}")
    @Modifying
    void setExpiredTickets(long timeMillis, TicketStatus status);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = :#{#status} WHERE ticket.ticketId = :#{#ticketId}")
    @Modifying
    void updateTicketStatus(TicketStatus status, int ticketId);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = :#{#targetStatus} WHERE ticket.timestamp+(3*60*1000) < :#{#timeMillis} AND ticket.status = :#{#status}")
    @Modifying
    void updateStuckUploadingTickets(TicketStatus status, TicketStatus targetStatus, long timeMillis);
}
