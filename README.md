# decentralized-database

## Abstract
In today's world we are using more and more data at levels we couldln't predict 15 years ago.
This comes with the need to find better solutions to save our data safely, securely, and efficiently.
This project will help us learn how new solutions to solve this greater data problem work.

## Front End High Level
* User can see a list of all their files saved on our database
* User can write, read, and delete files
    * No text editing capabilities in our service
* Just make CLI tools for now (python client)

## Intermediate Service
* User's front end interacts with this service
* This service conducts all logic to assemble and break up files and sends them to their corresponding servers

## Database Back End High Level
* Save data in a hashmap in each server
    * use concurrent hashmap if we need threadsafe
* User has a private key to access their data
* To access a specific file, we need to collect all the bytes and then piece them back together

### Writing a file
FIXME

### Reading a file
FIXME

### Deleting a file
FIXME
