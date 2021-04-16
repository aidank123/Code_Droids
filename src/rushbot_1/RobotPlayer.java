package rushbot_1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import battlecode.common.*;

// message [1] = 0 ==> broadcasted home hq
// message [1] = 1 ==> broadcasted enemy hq
public strictfp class RobotPlayer {

    //int that will keep track of the soup bid for submitting a blockchain message
    static final int message_cost = 3;

    static RobotController rc;
    static MapLocation hq_location;
    static MapLocation enemy_hq_location;
    static MapLocation enHQ1;
    static MapLocation enHQ2;
    static MapLocation enHQ3;
    static ArrayList<MapLocation> visited = new ArrayList<MapLocation>();

//LISTS OF ALL CURRENT STATIONARY ROBOT POSITIONS

    static ArrayList <MapLocation> Design_Schools = new ArrayList<>();
    static ArrayList <MapLocation> Fulfillment_Centers = new ArrayList<>();
    static ArrayList <MapLocation> Refineries = new ArrayList<>();
    static ArrayList <MapLocation> Vaporators = new ArrayList<>();
    static ArrayList <MapLocation> NetGuns = new ArrayList<>();


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
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //visited[turnCount - 1] = new MapLocation(rc.getLocation().x,rc.getLocation().y);
                //System.out.println("new visited spot: " + visited[turnCount - 1]);
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
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

    //method will search the graph, always choosing the nearest unvisited node, else a random direction
    static void searchGraph() throws GameActionException {

        Direction move_chosen;

        move_chosen = randomDirection();

        while (tryMove(move_chosen) == false || (visited.contains(rc.getLocation()) == true)) {
            if (tryMove(randomDirection())) {
                System.out.println("searching");
            }
        }
        visited.add(rc.getLocation());
        System.out.println("added new location to map");
        System.out.println(rc.getLocation());
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

        if(rc.getLocation().isWithinDistanceSquared(m,5) && enemy_hq_location == null){
            System.out.println("This location is not the enemy hq");
            if(m == enHQ1) {
                enHQ1 = null;
            } else if (m == enHQ2){
                enHQ2 = null;
            } else {
                enHQ3 = null;
            }
            return false;
        }
        else {

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
            System.out.println("Searching for enemy hq!");
            if (enHQ1 != null) {
                if (checkEnemyHQLocation(enHQ1) == true) {
                    Direction directions_to_enemy_HQ = rc.getLocation().directionTo(enHQ1);
                    if(tryMove(directions_to_enemy_HQ)){
                        System.out.println("Moved toward enhq1");
                    }
                }
            }
            else if (enHQ2 != null) {
                if (checkEnemyHQLocation(enHQ2) == true) {
                    Direction directions_to_enemy_HQ = rc.getLocation().directionTo(enHQ2);
                    if(tryMove(directions_to_enemy_HQ)){
                        System.out.println("Moved toward enhq2");
                    }
                }
            }
            else if (enHQ3 != null) {
                if (checkEnemyHQLocation(enHQ3) == true) {
                    Direction directions_to_enemy_HQ = rc.getLocation().directionTo(enHQ3);
                    if(tryMove(directions_to_enemy_HQ)){
                        System.out.println("Moved toward enhq3");
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

    static boolean goTo(Direction dir) throws GameActionException {
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight()};
        for (Direction d : toTry) {
            if (tryMove(d)) {
                return true;
            }
        }
        return false;
    }

    static boolean goTo(MapLocation destination) throws GameActionException{
        return goTo(rc.getLocation().directionTo(destination));
    }

    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }

    static boolean tryMove() throws GameActionException {
        for (Direction dir : directions)
            if (tryMove(dir))
                return true;
        return false;
        // MapLocation loc = rc.getLocation();
        // if (loc.x < 10 && loc.x < loc.y)
        //     return tryMove(Direction.EAST);
        // else if (loc.x < 10)
        //     return tryMove(Direction.SOUTH);
        // else if (loc.x > loc.y)
        //     return tryMove(Direction.WEST);
        // else
        //     return tryMove(Direction.NORTH);
    }

    static boolean tryDig() throws GameActionException{
        Direction dir = randomDirection();
        if(rc.canDigDirt(dir)){
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
            rc.move(dir);
            return true;
        } else return false;
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
}


