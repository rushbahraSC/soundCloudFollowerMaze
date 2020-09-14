Sound Cloud Follower Maze Coding Challenge
			        --Rushmeet Bahra

			
Refactor:
- Modularized the original code into different classes so as to exericse OOPS concepts,
and make the code more readable.
- Created an interface IEvent which implements 2 kinds of events:
	- Valid Event
	- Invalid Event
- Different kinds of valid events inherit from ValidEvent class and can be used to populate the ValidEventQueue.
	- FollowEvent (F)
	- UnFollowEvent (U)
	- PrivateMessageEvent (P)
	- BroadcastEvent (B)
	- StatusUpdateEvent (S)
- InvalidEvent is used to maintain/manage invalid event types and used to populate the InvalidEventQueue.
- Added custom exceptions to be thrown and handled instead of a generic RuntimeException.
- Added logger to the classes at relevant places.
 
Dead Letter Queue:
- Created a queue to add messages that do not match the given valid types of events(becoming dead events).

Queue Implementation:
- Dead letter queue for invalid events. (As mentioned above)
- Added another queue to store the valid messages. This can be used to modify the client/server code.
- Use of PriorityBlockingQueue data structure for both the queues because it is a thread safe and can be easily extended for processing of events.

Logger Implementation:
- Created a custom logger class to handle displaying informative and error messages.
- Use of "who" to display the class name and "message" to display the custom error/info message.

FlowChart:
!(flowchart.jpg)

Future Work:
- The Dead Priority Queue can be further implemented to analyse the messages.
- The Valid Priority Queue for given event types can be used to implement the client/server socket code.
- Introduce proper error handling with the dead events.
- Add unit and component tests.
- Implement Retry logic for valid events.
	Valid requests that have been marked as invalid are added to the dead letter queue(DLQ).
	These valid requests from DLQ can then be retrieved based on a retry logic to check for validity and added to the valid queue.
- Performance Improvements.
	Make use of Spring framework based microservices to implement Follower Maze which is much faster.
