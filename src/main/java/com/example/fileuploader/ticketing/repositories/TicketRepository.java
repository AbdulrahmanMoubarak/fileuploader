package com.example.fileuploader.ticketing.repositories;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<SystemTicketModel, Integer> {

    List<SystemTicketModel> findAllByUserId(int user_id);
    SystemTicketModel findByUserIdAndUsed(int userId, boolean used);
    SystemTicketModel findByTicketId (int ticketId);

}
