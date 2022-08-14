Feature: store file

  @FileStoring
  Scenario: file finishes uploading successfully
    When a file finishes uploading with ticket id 1
    Then storing process starts and ticket status is marked STORING

  @FileStored
  Scenario: file is stored successfully
    When a file storing process with ticket id 1 is finished
    Then ticket is marked STORED
    And phone numbers are stored in database