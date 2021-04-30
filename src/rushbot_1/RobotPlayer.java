//Aidan Kelley, Matthew Barlow, Jack Peterson, Adam Shaw, Alexander Wood
//RobotPlayer.java
//4/29/21
//BattleCode
//We created a code to defend our HQ. The main goal is to farm enough soup to build walls around hq and defend using drones.
//HQ will send out commands at certain rounds and goals met to tell robots to change what they are doing.

package rushbot_1;
import java.util.*;

import battlecode.common.*;

// message [1] = 0 ==> broadcasted home hq
// message [1] = 1 ==> broadcasted enemy hq
public strictfp class RobotPlayer {
    //all robots will access and reset this info at the start of every turn
    static RobotInfo[] enemy_robots;
    static RobotInfo[] friendly_robots;

    static MapLocation curr_loc;

    static MapLocation closest_soup = new MapLocation(0,0);

    //closest soup variable, used by miners
    //static MapLocation closest_soup = new MapLocation(30, 30);

    //these will be roles given the bots from HQ brain. Each variable will be assigned an ID.
    static int SCOUT = 0;
    static int EXPLORER = 0;
    //may move back to individual robot classes^

    //All secret hq message commands. These will be passed into the communications methods
    // and placed into messages to the team

    static int DEFEND_HQ = 450;
    static int EARLY_GAME_RUSH = 145;
    static int DEFEND_AND_BUILD = 382;


    //global int will keep track of the current water level
    static int current_water_level;

    //int that will keep track of the soup bid for submitting a blockchain message
    static final int message_cost = 3;

    static RobotController rc;
    static MapLocation hq_location;
    static MapLocation enemy_hq_location;
    static MapLocation enHQ1;
    static MapLocation enHQ2;
    static MapLocation enHQ3;
    static HashMap<MapLocation, Integer> visited = new HashMap<MapLocation, Integer>();

//LISTS OF ALL CURRENT STATIONARY ROBOT POSITIONS

    static ArrayList<MapLocation> Design_Schools = new ArrayList<>();
    static ArrayList<MapLocation> Fulfillment_Centers = new ArrayList<>();
    static ArrayList<MapLocation> Refineries = new ArrayList<>();
    static ArrayList<MapLocation> Vaporators = new ArrayList<>();
    static ArrayList<MapLocation> NetGuns = new ArrayList<>();

    //arraylist to keep track of soup locations. Will be updated by hq and used by miners
    static ArrayList<MapLocation> soup_locations = new ArrayList<>();

//LISTS OF ALL CURRENT MOVING ROBOTS, USING THEIR ROBOT IDS SO THEY CAN ALSO BE SORTED

//    static ArrayList <Integer> Miners = new ArrayList<>();
//    static ArrayList <Integer> Landscapers = new ArrayList<>();
//    static ArrayList <Integer> Drones = new ArrayList<>();

//int that keeps track of the last block of pertinent commands from HQ for this robot.

//set in the receiveCommands() method in Communications

    static int CURRENT_HQ_COMMAND;

    static Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST
    };
    static RobotType[] spawnedByMiner = {RobotType.REFINERY, RobotType.VAPORATOR, RobotType.DESIGN_SCHOOL,
            RobotType.FULFILLMENT_CENTER, RobotType.NET_GUN};

    static int turnCount;
    static int numMiners = 0;
    static int numLandscapers = 0;
    static int numDrones = 0;
    static int numDesignSchools = 0;
    static int numRefinery = 0;
    static int numVaporators = 0;
    static int numFulfillmentCenters = 0;
    static int numNetGuns = 0;


    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {


        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;
        turnCount = 0;


        System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            current_water_level = calculateWaterLevels();
            //list of all robots within sensory distance
            enemy_robots = rc.senseNearbyRobots(RobotType.HQ.sensorRadiusSquared, rc.getTeam().opponent());
            friendly_robots = rc.senseNearbyRobots(RobotType.HQ.sensorRadiusSquared,rc.getTeam());
            curr_loc = rc.getLocation();
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //visited[turnCount - 1] = new MapLocation(curr_loc.x,curr_loc.y);
                //System.out.println("new visited spot: " + visited[turnCount - 1]);
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
                //System.out.println("I'm a " + rc.getType() + "! Location " + curr_loc);
                switch (rc.getType()) {
                    case HQ:
                        HQ.runHQ();
                        break;
                    case MINER:
                        Miner.runMiner();
                        break;
                    case REFINERY:
                        Refinery.runRefinery();
                        break;
                    case VAPORATOR:
                        Vaporator.runVaporator();
                        break;
                    case DESIGN_SCHOOL:
                        DesignSchool.runDesignSchool();
                        break;
                    case FULFILLMENT_CENTER:
                        FulfillmentCenter.runFulfillmentCenter();
                        break;
                    case LANDSCAPER:
                        Landscaper.runLandscaper();
                        break;
                    case DELIVERY_DRONE:
                        DeliveryDrone.runDeliveryDrone();
                        break;
                    case NET_GUN:
                        NetGun.runNetGun();
                        break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    //method takes our hq location and creates three possible enemy hq locations based on the map
    static void setEnemy_hq_location() throws GameActionException {

        int flippedY = rc.getMapHeight() - hq_location.y - 1;
        int flippedX = rc.getMapWidth() - hq_location.x - 1;

        enHQ1 = new MapLocation(hq_location.x, flippedY);
        enHQ2 = new MapLocation(flippedX, flippedY);
        enHQ3 = new MapLocation(flippedX, hq_location.y);

    }

    static boolean checkEnemyHQLocation(MapLocation m) throws GameActionException {

        if (curr_loc.isWithinDistanceSquared(m, 5) && enemy_hq_location == null) {
            //System.out.println("This location is not the enemy hq");
            if (m == enHQ1) {
                enHQ1 = null;
            } else if (m == enHQ2) {
                enHQ2 = null;
            } else {
                enHQ3 = null;
            }
            return false;
        } else {

            return true;
        }
    }


    static void findEnHQ() throws GameActionException {

        RobotInfo[] robots = rc.senseNearbyRobots();

        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.HQ && robot.team != rc.getTeam()) {
                enemy_hq_location = robot.location;
                Communications.sendEnemyHQLocation(enemy_hq_location);

            }
        }

        if (enemy_hq_location == null) {
            //System.out.println("Searching for enemy hq!");
            if (enHQ1 != null) {
                if (checkEnemyHQLocation(enHQ1) == true) {
                    Direction directions_to_enemy_HQ = curr_loc.directionTo(enHQ1);
                    if (goTo(directions_to_enemy_HQ)) {
                        //System.out.println("Moved toward enhq1");
                    }
                }
            } else if (enHQ2 != null) {
                if (checkEnemyHQLocation(enHQ2) == true) {
                    Direction directions_to_enemy_HQ = curr_loc.directionTo(enHQ2);
                    if (goTo(directions_to_enemy_HQ)) {
                        //System.out.println("Moved toward enhq2");
                    }
                }
            } else if (enHQ3 != null) {
                if (checkEnemyHQLocation(enHQ3) == true) {
                    Direction directions_to_enemy_HQ = curr_loc.directionTo(enHQ3);
                    if (goTo(directions_to_enemy_HQ)) {
                        //System.out.println("Moved toward enhq3");
                    }
                }
            }
            //System.out.println("searching for possible location 1");
        }

    }


//    static void possibleEnemyHQ(MapLocation m){
//
//    }


    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    static boolean nearbyRobot(RobotType target) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();
        for (RobotInfo r : robots) {
            if (r.getType() == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a random RobotType spawned by miners.
     *
     * @return a random RobotType
     */


//    static void setTemperature(int factor) {
//
//        temperature *= factor;
//
//    }


    static boolean pathTo(Direction dir) throws GameActionException {
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft()};
        //on the second iteration it will let unvisited spots be moved to
        for (int i = 1; i <= 2; i++) {
            for (Direction d : toTry) {
                //checks we will not fall in water
                //if(waterIncoming(d) == false){
                //if (!visited.contains(rc.adjacentLocation(d)) || i == 2) {
                    if (tryMove(d)) {
                        return true;
                        //System.out.println("Adding new location to visited list: " + rc.adjacentLocation(dir));
                        //System.out.println("Moved!");
                    }
                }

            //}
        }
        return false;
    }

    static boolean goTo(Direction dir) throws GameActionException {
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft()};
        //on the second iteration it will let unvisited spots be moved to
        for (Direction d : toTry) {
            //checks we will not fall in water
            //if(waterIncoming(d) == false){
            if (tryMove(d)) {
                //System.out.println("Adding new location to visited list: " + rc.adjacentLocation(dir));
                return true;
                //System.out.println("Moved!");
            }


        }

        return false;
    }

    //this is a method that will be used as a general exploring algorithm for all robots, hope its not shitty!

    //static Direction
    //dir.opposite().rotateRight(), dir.opposite().rotateLeft(), dir.opposite()

    static boolean goTo(MapLocation destination) throws GameActionException {
        return goTo(curr_loc.directionTo(destination));
    }

    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }

    static boolean tryMove() throws GameActionException {
        for (Direction dir : directions)
            if (tryMove(dir))
                return true;
        return false;
        // MapLocation loc = curr_loc;
        // if (loc.x < 10 && loc.x < loc.y)
        //     return tryMove(Direction.EAST);
        // else if (loc.x < 10)
        //     return tryMove(Direction.SOUTH);
        // else if (loc.x > loc.y)
        //     return tryMove(Direction.WEST);
        // else
        //     return tryMove(Direction.NORTH);
    }

    static boolean tryDig() throws GameActionException {
        Direction dir = randomDirection();
        if (rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        // System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.isReady() && rc.canMove(dir)) {

            if (visited.containsKey(rc.adjacentLocation(dir)) == false) {
                visited.put(rc.adjacentLocation(dir),1);
            }else {
                int i = visited.get(rc.adjacentLocation(dir));
                //adding one to the number of times visited
                i = i + 1;
                visited.replace(rc.adjacentLocation(dir),i);
            }

            rc.move(dir);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Attempts to build a given robot in a given direction.
     *
     * @param type The type of the robot to build
     * @param dir  The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryBuild(RobotType type, Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canBuildRobot(type, dir)) {
            rc.buildRobot(type, dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to mine soup in a given direction.
     *
     * @param dir The intended direction of mining
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMineSoup(dir)) {
            rc.mineSoup(dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to refine soup in a given direction.
     *
     * @param dir The intended direction of refining
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryRefine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, rc.getSoupCarrying());
            return true;
        } else return false;
    }


    static void tryBlockchain() throws GameActionException {
        if (turnCount < 3) {
            int[] message = new int[7];
            for (int i = 0; i < 7; i++) {
                message[i] = 123;
            }
            if (rc.canSubmitTransaction(message, 10))
                rc.submitTransaction(message, 10);
        }
        // System.out.println(rc.getRoundMessages(turnCount-1));
    }

//VARIOUS ASSORTED METHODS I THINK ALL ROBOTS COULD MAKE USE OF

    //sensing the closest enemy drone robot, given a RobotInfo list robots. Good for hq and netguns

    static void shootClosestEnemyDrone(RobotInfo[] info) throws GameActionException {

        RobotInfo closest_drone = null;
        for (int i = 0; i < info.length; i++) {

            //distance to robot
            int dist = curr_loc.distanceSquaredTo(info[i].location);

            if ((info[i].type == RobotType.DELIVERY_DRONE) && dist < (curr_loc.distanceSquaredTo(closest_drone.location))) {
                closest_drone = info[i];
            }

        }
        if(closest_drone != null) {
            if (rc.canShootUnit(closest_drone.ID)) {
                rc.shootUnit(closest_drone.ID);
            }
        }
    }

    //this is a method to calculate water levels based on the equation given on the game site
    static int calculateWaterLevels() {
        int waterLevel = 0;
        waterLevel = (int) Math.pow(Math.E, 0.0028 * turnCount - 1.38 * Math.sin(0.00157 * turnCount - 1.73) + 1.38 * Math.sin(-1.73)) - 1;
        return waterLevel;
    }

    //this is a method to find and mark soup locations, used by MINERS


    static MapLocation findClosestSoup() throws GameActionException {

        Random r = new Random();
        MapLocation[] nearby_soup = rc.senseNearbySoup();

        closest_soup = enHQ2;
//        if(!soup_locations.isEmpty()){
//            closest_soup = soup_locations.get(r.nextInt(soup_locations.size()));
//        } else{
//            closest_soup =
//        }

        MapLocation closest_soup = new MapLocation(0,0);
        MapLocation[] corners = {new MapLocation(0,rc.getMapHeight()), new MapLocation(rc.getMapWidth(),0), new MapLocation(rc.getMapWidth(),rc.getMapHeight())};
        for(MapLocation corner : corners){
            if(curr_loc.distanceSquaredTo(corner) < curr_loc.distanceSquaredTo(closest_soup)){
                closest_soup = corner;
            }
        }

        //first find the nearest sensed location, compare it to the random one in the inventory
        for (MapLocation m : nearby_soup) {
            if (soup_locations.contains(m) == false) {
                soup_locations.add(m);
                if (Communications.checkSoupLocSent(m.x, m.y) == false) {
                    //Communications.sendSoupLocations(m);
                    //System.out.println("Sending new soup location " + m);
                }
            }

            if(curr_loc.distanceSquaredTo(m) < curr_loc.distanceSquaredTo(closest_soup)){
                closest_soup = m;
            }
        }
        if(nearby_soup.length == 0){//If no soup is nearby
            for(MapLocation m : soup_locations){
                if(curr_loc.distanceSquaredTo(m) < curr_loc.distanceSquaredTo(closest_soup)){
                    closest_soup = m;
                }
            }
            if(soup_locations.isEmpty()){//If no soup is known
                closest_soup = new MapLocation(r.nextInt(rc.getMapWidth()),r.nextInt(rc.getMapHeight()));
            }
        }



        //if there is no nearby soup this method will return false and the miner randomly explores

        System.out.println("The closest soup is: " + closest_soup);
        return closest_soup;
//        nearby_soup = rc.senseNearbySoup()
    }


    static boolean waterIncoming(Direction direction) throws GameActionException {

        if (rc.senseFlooding(rc.adjacentLocation(direction))) {
            return true;
        } else {
            return false;
        }
    }

    static void modifySoupList(MapLocation m) {

        //if maplocation object passed into the method is in soup locations it will be removed
        soup_locations.removeIf(x -> x.equals(m));

    }

    //method in which the robot looks around at its surrounding robots. If it does not have a certain building location
    //in its memory it will add it

    static void checkSurroundings() throws GameActionException {

        //check and remove soup from list that should have been sensed but wasnt
        //boolean contains = false;


        for(MapLocation m : soup_locations) {
            if (m.isWithinDistanceSquared(curr_loc, RobotType.MINER.sensorRadiusSquared) && rc.canSenseLocation(m)) {
//
                if (rc.canSenseLocation(m) ){
                    if (rc.senseSoup(m) == 0){
                        System.out.println("This location already mined: " + m);
                        if(soup_locations.contains(m)) {
                            modifySoupList(m);
                        }
                }


                    }
            }
        }

        for(RobotInfo r : friendly_robots){
            switch(r.getType()){
                case REFINERY:
                    if(!Refineries.contains(r)){
                        Refineries.add(r.location);
                    }
                    break;
                case DESIGN_SCHOOL:
                    if(!Design_Schools.contains(r)){
                        Design_Schools.add(r.location);
                    }
                    break;
                case FULFILLMENT_CENTER:
                    if(!Fulfillment_Centers.contains(r)){
                        Fulfillment_Centers.add(r.location);
                    }
                    break;
                case VAPORATOR:
                    if(!Vaporators.contains(r)){
                        Vaporators.add(r.location);
                    }
                    break;
                case NET_GUN:
                    if(!NetGuns.contains(r)){
                        NetGuns.add(r.location);
                    }
                    break;
            }
        }
    }

    static MapLocation nearestRefinery() {
        MapLocation closest_refinery;

        closest_refinery = hq_location;
        for (MapLocation m : Refineries) {
            if (curr_loc.distanceSquaredTo(m) < curr_loc.distanceSquaredTo(closest_refinery)) {
                closest_refinery = m;
            }
        }

        return closest_refinery;
    }

    static void setExplorer(int ex){

        EXPLORER = ex;
    }
}




