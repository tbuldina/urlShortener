# URL Shortener #

Application simplifies urls by converting it to shorter one.

Opening short url will redirect user to original url.

## Approach ##

##### Encoding
1. long url -> id

   Match long URL with identificator
   (insert url to database and use its autoincremented identificator,
in case database already contain such url their identificator used)

2. id -> encoded short url

   Convert identificator to base62 string

3. Save short url (base62) in database

## How to use ##
1. *mvn exec:java*

2. Application starts by default on port 8680
and accessible on http://localhost:8680/index

3. Also you can run it on different port

   *mvn exec:java -Djetty.port={port}*

## Technologies ##
- Embedded Jetty server
- Java in-memory database

##### Unit tests #####
Method encodeUrl(longUrl) is covered by *junit* tests



