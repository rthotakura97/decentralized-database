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
* RenoService will have it's own table that saves filenames, users attributed to file, and file sizes 

### Operations
* Write
    * Parameters
	* File
	* Filename
	* Secret key
    * Output
	* Void
* Read
    * Parameters
	* Filename
	* Secret key
    * Output
	* File
* Delete
    * Parameters
	* Filename
	* Secret key
    * Output
	* Void

### Dissassembly scheme
* Break up the file into k blocks of size N bytes
    * k-1th block will be filled with extraneous 0's to round it out
* Add a block order. Is a signed long that cannot be smaller than block sub i-1 or larger than block sub i+i
    * E.g. If we have 3 blocks, the first block's order could be -23212, the second block's order could be 2, and the 
    third block's order could be 9923. They just need to be ordered later on, they shouldn't contain where in the file this block
    exists
* Encrypt the blocks
* Create k block keys. The key of each block is a hash private key, filename, and block number
    * The block number is different than block order (i.e. they go 0, 1, 2, ..., k-1)
* Send those blocks to different servers randomly
* Save the blocks into hashmaps
    * TODO: Why hashmaps?


### Assembly scheme
* Assemble a list of keys from private key, filename, and block numbers
* JailCell servers will then need to lookup all potential keys (privatekey+filename+block#s 0 to k)
* Return all encrypted blocks in its server
* Reno will decrypt all blocks and then order them for reassembly
* Return the file to front end

### Auth
* Any request needs to contain user ID. If a filename doesn't belong to that user ID, operation is denied

## Database Back End High Level - JailCellService
* Contains the hashmaps that contain all the encrypted blocks, accessable by a hashed key

### Operations
* Write
    * Parameters
	* Set of hashed keys
	* List of encrypted blocks
	    * NOTE - These lists aren't assumed to be in order (i.e. keys[0] doesn't need to correspond to blocks[0])
    * Output
	* Void
    * Save a block with a unique key into the hashmap
* Read
    * Parameters
	* Set of hashed keys
    * Output
	* List of encrypted blocks
    * Go through all keys and get as many blocks as possible
	* keys.size() should NOT equal blocks.size()
* Delete
    * Parameters
	* Set of hashed keys
    * Output
	* Void
    * Go through all keys and remove those entries
	* Not all entries will exist

### Auth
* Ensure request comes from corresponding RenoService

## Block Structure
* Will contain N bytes [FIXME]: Add byte size
* Contains a signed long that indicates where in the order this block is in the file
    * This is NOT the block number

## Longterm Issues
* Currently, we pass around all the keys to all the servers. What happens when if we have millions of blocks across
thousands of servers for one file? Many servers will spend most of their time trying to look up keys that don't exist
* If a request between Reno and JailCell gets intercepted, a hacker has access to a list of keys and knows how many blocks
there are based off the number of keys.
    * Throw in junk keys? Useless keys that JailCell will know to ignore but a hacker may not
    * Traffic should be encrypted to begin with
