package uk.ac.ed.inf.pathfinding;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.model.Node;

import java.util.*;


public class PathFindingAlgorithm {
    private static LngLatHandling lngLatHandler = new LngLatHandler();
    private static final LngLat startPoint = new LngLat(-3.186874, 55.944494);

    /**
     * Finds the path from the start point to the goal point
     * @param goalPoint
     * @param noFlyZones
     * @param centralArea
     * @param order
     * @return List<Move> path
     */
    public List<Move> findPath(LngLat goalPoint, NamedRegion[] noFlyZones, NamedRegion centralArea, Order order){
        //Open set which contains nodes that have been discovered but not yet explored
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(currNode -> currNode.getGScore() +
                currNode.getHScore()));
        //nodeMap which contains all the LngLats that have been discovered
        Map<LngLat, Node> nodeMap = new HashMap<>();
        //Closed set which contains nodes that have been explored
        Set<LngLat> closedSet = new HashSet<>();

        boolean inCentral = true;

        //Initialise start node
        Node startNode = new Node(startPoint, null, 0.0, heuristic(startPoint, goalPoint), 999);
        nodeMap.put(startPoint, startNode);
        openSet.add(startNode);

        while(!openSet.isEmpty()){

            //Get the node with the lowest fScore
            Node currNode = openSet.poll();
            closedSet.add(currNode.getCurrLngLat());

            //If the current node is the goal node, return the path
            if (lngLatHandler.isCloseTo(currNode.getCurrLngLat(), goalPoint)){
                return reconstructPath(currNode, goalPoint, startPoint, order);
            }

            //if central area exists don't check condition
            if(centralArea != null){
                //If the current node is in the central area, set the flag to true
                if (!lngLatHandler.isInRegion(currNode.getCurrLngLat(), centralArea)){
                    inCentral = false;
                }
            }
            //Generate the next positions
            List<Node> nextPositions = generateNextPositions(currNode.getCurrLngLat(), noFlyZones, inCentral,
                    centralArea);
            for(Node nextPosition: nextPositions){
                //If the next position has already been explored, skip it
                if (closedSet.contains(nextPosition.getCurrLngLat())){
                    continue;
                }

                //Calculate the gScore of the next position
                double tentativeGScore = currNode.getGScore() + SystemConstants.DRONE_MOVE_DISTANCE;

                LngLat pos = nextPosition.getCurrLngLat();
                nextPosition.setParentNode(currNode);
                nextPosition.setGScore(tentativeGScore);
                nextPosition.setHScore(heuristic(nextPosition.getCurrLngLat(), goalPoint));

                if(nodeMap.containsKey(pos)){
                    Node node = nodeMap.get(pos);
                    if(node.getGScore() > tentativeGScore){
                        //Update the node map and the open set if we find a better gScore
                        nodeMap.put(pos, nextPosition);
                        openSet.remove(node);
                        openSet.add(nextPosition);
                    }
                }
                else{
                    //Add the next position to the open set and the node map
                    nodeMap.put(pos, nextPosition);
                    openSet.add(nextPosition);
                }
            }
        }
        return null;
    }

    /**
     * Reconstructs the path, and returns the path to and from the start point
     * @param currNode
     * @param End
     * @param Start
     * @param order
     * @return List<Move> path
     */
    public List<Move> reconstructPath(Node currNode, LngLat End, LngLat Start, Order order){
        List<Move> path = new ArrayList<>();
        List<Move> fullPath = new ArrayList<>();

        while(currNode.getParentNode() != null){
            Move move = new Move(order.getOrderNo(), currNode.getParentNode().getCurrLngLat().lng(),
                    currNode.getParentNode().getCurrLngLat().lat(), currNode.getCurrLngLat().lng(),
                    currNode.getCurrLngLat().lat(), currNode.getAngle());
            path.add(move);
            currNode = currNode.getParentNode();
        }

        fullPath.addAll(path);
        Collections.reverse(fullPath);

        fullPath.add(new Move(order.getOrderNo(), fullPath.get(fullPath.size()-1).getToLongitude(),
                fullPath.get(fullPath.size()-1).getToLatitude(), fullPath.get(fullPath.size()-1).getToLongitude(),
                fullPath.get(fullPath.size()-1).getToLatitude(), 999));

        for(Move move: path){
            Move revMove = new Move(move.getOrderNo(), move.getToLongitude(), move.getToLatitude(),
                    move.getFromLongitude(), move.getFromLatitude(), (move.getAngle() + 180) % 360);
            fullPath.add(revMove);
        }

        fullPath.add(new Move(order.getOrderNo(), Start.lng(), Start.lat(), Start.lng(), Start.lat(), 999));
        return fullPath;
    }

    /**
     * Generates the next positions
     * @param curr
     * @param noFlyZones
     * @param flag
     * @param centralArea
     * @return List<Node> nextPositions
     */
    private List<Node> generateNextPositions(LngLat curr, NamedRegion[] noFlyZones, boolean flag, NamedRegion centralArea){
        List<Node> nextPositions = new ArrayList<>();
        double[] legalMoves = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            boolean isValid = true;
            LngLat nextPosition = lngLatHandler.nextPosition(curr, legalMove);
            for(NamedRegion noFlyZone: noFlyZones){

                if(!flag){
                    if(lngLatHandler.isInRegion(nextPosition, centralArea)){
                        isValid = false;
                    }
                }

                if(lngLatHandler.isInRegion(nextPosition, noFlyZone)){
                    isValid = false;
                }
            }
            if (isValid){
                nextPositions.add(new Node(nextPosition, null, 0.0, 0.0, legalMove));
            }
        }
        return nextPositions;
    }

    /**
     * Calculates the heuristic value
     * @param pos
     * @param end
     * @return distance between pos and end
     */
    private double heuristic(LngLat pos, LngLat end){
        return lngLatHandler.distanceTo(pos, end);
    }
}
