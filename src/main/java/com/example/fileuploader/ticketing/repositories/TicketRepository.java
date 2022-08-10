package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {
    List<SystemTicketModel> findAllByUserId(long user_id);

    List<SystemTicketModel> findAllByUserIdAndStatus(long userId, TicketStatus status);

    SystemTicketModel findByUserIdAndStatus(long userId, TicketStatus status);

    SystemTicketModel findByTicketId(int ticketId);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.EXPIRED WHERE ticket.timestamp+(:#{#expirationTimeInMinutes}*60*1000) < :#{#currentTimeInMillis} AND ticket.status = :#{#status}")
    @Modifying
    void updateTicketStatusToExpired(TicketStatus status, int expirationTimeInMinutes, long currentTimeInMillis);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = :#{#status} WHERE ticket.ticketId = :#{#ticketId}")
    @Modifying
    void updateTicketStatus(TicketStatus status, int ticketId);

    @Query("UPDATE SystemTicketModel ticket SET ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.UPLOAD_ERROR WHERE ticket.timestamp+(:#{#expirationTimeInMinutes}*60*1000) < :#{#currentTimeInMillis} AND ticket.status = :#{#status}")
    @Modifying
    void updateStuckUploadingTickets(TicketStatus status, int expirationTimeInMinutes, long currentTimeInMillis);

    @Query("SELECT COUNT (ticket.ticketId) FROM SystemTicketModel ticket WHERE ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.UPLOADING OR ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.UPLOADED OR ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.STORING")
    Long getAllActiveTickets();

    @Query("SELECT COUNT (ticket.ticketId) FROM SystemTicketModel ticket WHERE ticket.userId = :#{#userId} AND (ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.UPLOADING OR ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.UPLOADED OR ticket.status = com.example.fileuploader.ticketing.models.TicketStatus.STORING)")
    Long getUserActiveTickets(long userId);
}
