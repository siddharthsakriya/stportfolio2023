# Informatics Large Practical (2023) - Pizza Delivery Drone

This is my implementation of the informatics large practical (ILP) coursework for the corresponding course at the University of Edinburgh.
It is close to being completed with the final step being outputting all the drones moves into a GeoJson file.

## The Task

In essence, the goal was to find a path from the start point to a end goal, and back. The scenario set out for us was that the program was a order processing and pathfinding system for a pizza delivery drone which delivers pizzas to Appleton Tower. The drone was only allowed to move in the 16 cardinal directions for a fixed distance at a time.
One step in a cardinal direction was referred to as a 'move'. The system is to be data-driven, utilising the latest data from the rest server.

## The Solution

My solution can be split into 3 logical sections:

    - Data Retrieval
    - Pathfinding
    - Data Processing & Output

I have implemented an adapted A* algorithm (https://en.wikipedia.org/wiki/A*_search_algorithm) which follows a more greedy approach. I am utilising spring rest template to retrieve the data from the REST server, "https://ilp-rest.azurewebsites.net/".




