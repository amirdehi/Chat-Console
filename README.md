# Chat-Console
Chat Console Applications By Java Using UDP Protocols
-----------------------------------------------------

We have a udp connections between clients and server, A udp server always ready to listen for users.
When a new user opened the udp client program, They choose a username for themeselves.
After that, Server saves the username, IP and port of the client in an array list.
Each client can set its state to "search for others" or "wait for others".
If they choose first one, they can search other users by they username.
If server found that username, it returns username, ip and port.
Then asked from user to make a chat connection between them.
If user agreed and the other side was ready for listening, udp server send two packets to each sides to make one server and the other client.
Then they have each other properties and start to talking.
2021,04		Ali Ramezanpour & Sheida Tarani, My Love
