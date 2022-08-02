package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {
    List<SystemTicketModel> findAllByUserId(long user_id);
    SystemTicketModel findByUserIdAndUsed(long userId, boolean used);
    SystemTicketModel findByTicketId (int ticketId);
    void removeByTicketId(int ticketId);
    void removeAllByUsed(boolean used);
}
