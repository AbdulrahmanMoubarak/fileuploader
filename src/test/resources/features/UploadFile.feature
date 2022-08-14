Feature: upload a file

  @UploadFile
  Scenario: user clicks on upload button
    When the user clicks on the upload button with a ticket id 1
    Then The ticket is activated and it's status is marked UPLOADING and the uploading process starts
    And a response with status code 200 is sent to indicated that ticket status is updated

  @FileUploaded
  Scenario: uploading process finished
    When an uploading process with ticket id 1 is for a file file.txt that has a checksum equals 12a555bcf is finished
    Then the ticket status is marked UPLOADED
    And a response with status code 200 is sent to indicated that uploading is finished

#  @FileUploadError
#  Scenario: file is corrupted when reached the server
#    When another uploading process with ticket id 1 is for a file file.txt that has a checksum equals 12a555bcf is finished
#    But the calculated checksum on the server is 29a557daf
#    Then a response with status code 500 is sent to indicated the uploading error