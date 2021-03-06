package Landscaping_Bot;
import battlecode.common.*;
import java.lang.Math;

public strictfp class RobotPlayer {
    static RobotController rc;
    static MapLocation hq_location;

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
    static int numDesignSchools = 0;

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
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                findHQ();
                switch (rc.getType()) {
                    case HQ:                 runHQ();                break;
                    case MINER:              runMiner();             break;
                    case REFINERY:           runRefinery();          break;
                    case VAPORATOR:          runVaporator();         break;
                    case DESIGN_SCHOOL:      runDesignSchool();      break;
                    case FULFILLMENT_CENTER: runFulfillmentCenter(); break;
                    case LANDSCAPER:         runLandscaper();        break;
                    case DELIVERY_DRONE:     runDeliveryDrone();     break;
                    case NET_GUN:            runNetGun();            break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    static void findHQ(){
        if (hq_location == null) {
            RobotInfo[] robots = rc.senseNearbyRobots();
            for(RobotInfo robot : robots){
                if (robot.type == RobotType.HQ && robot.team == rc.getTeam()) {
                    hq_location = robot.location;
                }
            }
        }
    }

    static void runHQ() throws GameActionException {
        if (numMiners < 10) {
            for (Direction dir : directions)
                if (tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                }
        }
    }

    static void runMiner() throws GameActionException {
        tryBlockchain();
        //tryMove(randomDirection());
        // for (Direction dir : directions)
        //     tryBuild(RobotType.FULFILLMENT_CENTER, dir);

        for (Direction dir : directions)
            if (tryRefine(dir))
                System.out.println("I refined soup! " + rc.getTeamSoup());
        for (Direction dir : directions)
            if (tryMine(dir))
                System.out.println("I mined soup! " + rc.getSoupCarrying());


        //creating a design school to build landscapers
        if (!nearbyRobot(RobotType.DESIGN_SCHOOL)) {
            if (tryBuild(RobotType.DESIGN_SCHOOL, randomDirection())) {
                System.out.println("Created a design school");
            }
        }

        //creating a refinery for miners to deposit soup quicker
//            if(!nearbyRobot(RobotType.REFINERY)){
//                if(tryBuild(RobotType.REFINERY, randomDirection())){
//                    System.out.println("Created a Refinery");
//                }
//            }

        //creating a vaporator
//        if(!nearbyRobot(RobotType.VAPORATOR)){
//            if(tryBuild(RobotType.VAPORATOR, randomDirection())){
//                System.out.println("Created a vaporator");
//            }
//        }
        if (rc.getSoupCarrying() == 100) {
            System.out.println("at soup limit");
            Direction directions_to_HQ = rc.getLocation().directionTo(hq_location);
            if (tryMove(directions_to_HQ)) {
                System.out.println("I moved towards hq");
            }

        } else if (tryMove(randomDirection())) {
            System.out.println("I moved!");
        }
    }


    static void runRefinery() throws GameActionException {
        // System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));




    }

    static void runVaporator() throws GameActionException {

    }

    static void runDesignSchool() throws GameActionException {
        for(Direction dir : directions){
            if(tryBuild(RobotType.LANDSCAPER, dir)){
                System.out.println("Made a landscaper");
            }
        }

    }

    static void runFulfillmentCenter() throws GameActionException {
        for (Direction dir : directions)
            tryBuild(RobotType.DELIVERY_DRONE, dir);
    }

    static void runLandscaper() throws GameActionException {
        if(rc.getDirtCarrying() == 0){
            tryDig();
        }
        if(hq_location != null){
            MapLocation bestPlaceToBuildWall = null;
            int lowestElevation = 10000;
            for(Direction dir : directions){

                MapLocation tileToCheck = hq_location.add(dir);

                if(rc.getLocation().distanceSquaredTo(tileToCheck) < 4
                        && rc.canDepositDirt(rc.getLocation().directionTo(tileToCheck))){

                    if(rc.senseElevation(tileToCheck) < lowestElevation) {
                        lowestElevation = rc.senseElevation(tileToCheck);
                        bestPlaceToBuildWall = tileToCheck;
                    }
                }
            }
            if(Math.random() < 0.5) {
                if (bestPlaceToBuildWall != null) {
                    rc.depositDirt(rc.getLocation().directionTo(bestPlaceToBuildWall));
                    System.out.println("building a wall");
                }
            }
        }
        if(hq_location != null){
            goTo(hq_location);
        }else{
            tryMove(randomDirection());
        }
    }

    static void runDeliveryDrone() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);

            if (robots.length > 0) {
                // Pick up a first robot within range
                rc.pickUpUnit(robots[0].getID());
                System.out.println("I picked up " + robots[0].getID() + "!");
            }
        } else {
            // No close robots, so search for robots within sight radius
            tryMove(randomDirection());
        }
    }

    static void runNetGun() throws GameActionException {

    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Returns a random RobotType spawned by miners.
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }

    static boolean nearbyRobot(RobotType target)throws GameActionException{
        RobotInfo[] robots = rc.senseNearbyRobots();
        for(RobotInfo r : robots){
            if(r.getType() == target){
                return true;
            }
        }
        return false;
    }

    static boolean tryDig() throws GameActionException{
        Direction dir = randomDirection();
        if(rc.canDigDirt(dir)){
            rc.digDirt(dir);
            return true;
        }
        return false;
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

    /**
     * Attempts to build a given robot in a given direction.
     *
     * @param type The type of the robot to build
     * @param dir The intended direction of movement
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
