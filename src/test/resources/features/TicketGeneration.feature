Feature: Generate a ticket

  @GenerateTicket
  Scenario: user posts metadata to generate an upload ticket
    Given The following ticket
      | ticketId | userId | size | fileName   | status  |
      | 1        | 1      | 9000 | random.txt | CREATED |
    When a user with id 1 selects a file at the front end application with a name random.txt and a size of 9000 bytes
    Then a ticket id generated for this operation
