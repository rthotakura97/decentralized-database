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

## Intermediate Service - RenoService
* User's front end interacts with this service
* This service conducts all logic to assemble and break up files and sends them to their corresponding servers

### Dissassembly scheme
* Break up the file into blocks of size N bytes
* Send those blocks to different servers randomly
* In each server, a block is saved into a hashmap. The key of each block is a user's private key, filename, and block number
* RenoService will have it's own table that saves filenames, users attributed to file, and block sizes 

### Assembly scheme
* Assemble a pseudo key using the users private key passed in and filename. From there, pass the pseudokey and filesize k to all JailCell servers.
* JailCell servers will then need to lookup all potential keys (privatekey+filename+block#s 0 to k)
* Return all blocks in its server
* Reno will assemble the file back together from block 0 to block k
* Return the file to front end

## Database Back End High Level - JailCellService
* Save data in a hashmap in each server
    * use concurrent hashmap if we need threadsafe
* User has a private key to access their data
* To access a specific file, we need to collect all the bytes and then piece them back together

### Writing a file
* Completely break up the file and save it
* Delete old file

### Reading a file
* Retrieve the file

### Deleting a file
* Remove all artifacts of file from tree
