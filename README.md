# decentralized-database

## Abstract
In today's world we are using more and more data at levels we couldn't predict 15 years ago.
This comes with the need to find better solutions to save our data safely, securely, and efficiently.
This project will help us learn new solutions to solve this greater problem. We are proposing a decentralized
database that safely and securely encrypts broken up files and stores the pieces on different servers. 

## Front End High Level
* User can see a list of all their files saved on our database
* User can write, read, and delete files
    * No text editing capabilities in our service
* Just make CLI tools for now (python client)
    * Looking to expand to user interface in future
    * python2 urllib2 library to send http requests
* Takes in public key, secret key, path/to/file

## Intermediate Service - RenoService
* User's front end interacts with this service
* Handle HTTP requests with Jetty
* This service conducts all logic to assemble and break up files and sends them to their corresponding servers
* RenoService will have it's own table that saves filenames, public keys attributed to file, and file sizes 
    * Use guava MultiMap

### Operations
* Write
    * Parameters
	    * File
	    * Filename
	    * Secret key
    * Output
	    * Result Code
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
	    * Result Code
* List Files
    * Parameters
	    * Void
    * Output
	    * List of filenames
        * Must match user with files

### Dissassembly scheme
* Break up the file into k blocks of size N bytes
    * k-1th block will be filled with extraneous 0's to round it out
* Add a block order. Is a signed long that cannot be smaller than block sub i-1 or larger than block sub i+i
    * E.g. If we have 3 blocks, the first block's order could be -23212, the second block's order could be 2, and the 
    third block's order could be 9923. They just need to be ordered later on, they shouldn't contain where in the file this block
    exists
* Encrypt the blocks
* Create k block keys. The key of each block is a hash of the secret key, filename, and block number
    * The block number is different than block order (i.e. they go 0, 1, 2, ..., k-1)
* Send those blocks to different servers
    * Two options: random or algorithmic
        * Random --> more secure but slower assemble times
        * Algorithmic --> less secure but faster assemble times
* Save the blocks into hashmaps
    * Why hashmaps?
        * 0(1) access time
        * Multi-dimensional data storage
        * Easy way to store data


### Assembly scheme
* Assemble a list of keys from secret key, filename, and block numbers
* JailCell servers will then need to lookup all potential keys (secretkey+filename+block#s 0 to k)
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
	    * Result Code
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
	    * Result Code
    * Go through all keys and remove those entries
	* Not all entries will exist

### Auth
* Ensure request comes from corresponding RenoService

## Block Structure
* Contains N bytes of data
    * Variable amount of bytes
        * Limit to range of #blocks to prevent innefficient lookup times
* Contains metadata
    * A signed long that indicates where in the order this block is in the file
    * NOTE - This is NOT the block number

## Longterm Issues
* Currently, we pass around all the keys to all the servers. What happens when if we have millions of blocks across
thousands of servers for one file? Many servers will spend most of their time trying to look up keys that don't exist
    * Limit # of blocks by using variable byte sizes for blocks
    * Algorithmically spread out blocks across servers in a way that it can be reversed faster than it takes to look up all keys
* If a request between Reno and JailCell gets intercepted, a hacker has access to a list of keys and knows how many blocks
there are based off the number of keys.
    * Throw in junk keys? Useless keys that JailCell will know to ignore but a hacker may not
    * Traffic should be encrypted to begin with
* We should add a login system
