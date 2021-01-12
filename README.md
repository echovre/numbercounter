To run:

Run SocketServer.java:main() to start server

Run SocketClient.java:main() to start a client that writes 10 random numbers and quits. Run multiple as desired to test the server's 5 connection limit.

Run SocketClientWithTerminate.java:main() to start a client that writes 10 random numbers and a terminate, to test the server's "shutdown everything" function. 

Exercise Instructions:

Here's the code challenge instructions. I appreciate you taking the time to take our challenge, I know your time is valuable!

    Don't hesitate to email me if you have any questions. 
    Please don't worry about making it perfect. Keep it simple!
    Limit yourself to about 4 hours of focused time.
    Send your buildable code back as tar or gzip.

Good luck! And again -- don't hesitate to send me any questions.

===================================
Code Challenge Instructions

Write a server (“Application”) in Java that opens a socket and restricts input to at most 5 concurrent clients. Clients will connect to the Application and write any number of 9 digit numbers, and then close the connection. The Application must write a de-duplicated list of these numbers to a log file in no particular order.
Primary Considerations
    
    The Application should work correctly as defined below in Requirements.
    The overall structure of the Application should be simple.
    The code of the Application should be descriptive and easy to read, and the build method and runtime parameters must be well-described and work.
    The design should be resilient with regard to data loss.
    The Application should be optimized for maximum throughput, weighed along with the other Primary Considerations and the Requirements below.

Requirements

1    The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000.

2    Input lines presented to the Application via its socket must either be composed of exactly nine decimal digits (e.g.: 314159265 or 007007009) immediately followed by a server-native newline sequence; or a termination sequence as detailed in #9, below.

3    Numbers presented to the Application must include leading zeros as necessary to ensure they are each 9 decimal digits.

4    The log file, to be named "numbers.log”, must be created anew and/or cleared when the Application starts.

5    Only numbers may be written to the log file. Each number must be followed by a server-native newline sequence.

6    No duplicate numbers may be written to the log file.

7    Any data that does not conform to a valid line of input should be discarded and the client connection terminated immediately and without comment.

8    Every 10 seconds, the Application must print a report to standard output:
        The difference since the last report of the count of new unique numbers that have been received.
        The difference since the last report of the count of new duplicate numbers that have been received.
        The total number of unique numbers received for this run of the Application.
        Example text for #8: Received 50 unique numbers, 2 duplicates. Unique total: 567231
        
9    If any connected client writes a single line with only the word "terminate" followed by a server-native newline sequence, the Application must disconnect all clients and perform a clean shutdown as quickly as possible.

10    Clearly state all of the assumptions you made in completing the Application.

Notes
    You may write tests at your own discretion.
    You may use common libraries in your project such as Apache Commons and Google Guava, particularly if their use helps improve Application simplicity and readability. However the use of large frameworks, such as Akka, is prohibited.
    Your Application may not for any part of its operation use or require the use of external systems, for example Apache Kafka or Redis.
    At your discretion, leading zeroes present in the input may be stripped—or not used—when writing output to the log or console.
    Robust implementations of the Application typically handle more than 2M numbers per 10-second reporting period on a modern MacBook Pro laptop (e.g.: 16 GiB of RAM and a 2.5 GHz Intel i7 processor).
