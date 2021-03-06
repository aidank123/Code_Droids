

package CodeDroid_1;

import battlecode.common.*;

import java.util.ArrayList;

public strictfp class RobotPlayer {
    static RobotController rc;
    static MapLocation hq_location;
    static MapLocation enemy_hq_location;
    static MapLocation enHQ1;
    static MapLocation enHQ2;
    static MapLocation enHQ3;
    static ArrayList<MapLocation> visited = new ArrayList<MapLocation>();

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
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
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
                sendEnemyHQLocation(enemy_hq_location);

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
    static final int teamSecret = 333333333;
    static final String[] messageType = {"HQ loc",};

    public static void sendHQLocation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void getHQLocation() throws GameActionException {
        System.out.println("B L O C K C H A I N");
        //for (int i = 1; i < rc.getRoundNum(); i++) {
        for (Transaction t : rc.getBlock(1)) {
            int[] mess = t.getMessage();
            if (mess[0] == teamSecret && mess[1] == 0) {
                hq_location = new MapLocation(mess[2], mess[3]);
            }
        }
    }
    //}

    public static void sendEnemyHQLocation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 1;
        message[2] = m.x;
        message[3] = m.y;
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void getEnemyHQLocation() throws GameActionException {
        System.out.println("B L O C K C H A I N");
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == teamSecret && mess[1] == 1) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
        }
    }



    public static boolean broadcastedCreation = false;

    public static void broadcastDesignSchoolCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = teamSecret;
        message[1] = 1;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
            broadcastedCreation = true;
        }
    }

    public static void updateUnitCounts() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(rc.getRoundNum() - 1)) {
                int[] mess = t.getMessage();
                if (mess[0] == teamSecret && mess[1] == 1) {
                    numDesignSchools += 1;
//                    }else if ()
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
        RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED);
        for (RobotInfo robot : robots) {
            if (robot.team != rc.getTeam() && robot.type == RobotType.DELIVERY_DRONE) {
                rc.shootUnit(robot.getID());
            }
        }
    }

    static void runMiner() throws GameActionException {
        System.out.println("HQ Location:" + hq_location);
        System.out.println("Enemy HQ Location:" + enemy_hq_location);
//        for (int i = 0; i < visited.size(); i++){
//
//            System.out.println(visited.get(i));
//        }

        if(hq_location == null){
            getHQLocation();
            setEnemy_hq_location();
        }

        else if(enemy_hq_location == null && hq_location != null){
            getEnemyHQLocation();
            //System.out.println("Searching for enemy hq");
            findEnHQ();
        }

        tryBlockchain();


        tryMove(randomDirection());
        for (Direction dir : directions)
            tryBuild(RobotType.FULFILLMENT_CENTER, dir);
        for (Direction dir : directions)
            if (tryRefine(dir))
                System.out.println("I refined soup! " + rc.getTeamSoup());
        for (Direction dir : directions)
            if (tryMine(dir))
                System.out.println("I mined soup! " + rc.getSoupCarrying());
        if(!nearbyRobot(RobotType.DESIGN_SCHOOL)){
            if(tryBuild(RobotType.DESIGN_SCHOOL, randomDirection())){
                System.out.println("Created a design school");
            }
        }
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
                if(numDrones < 6) {
                    if (tryBuild(RobotType.LANDSCAPER, dir)) {
                        System.out.println("Made a landscaper");
                        numDrones++;
                    }
                }else{
                    System.out.print("There are already 2 drones");
                }
            }

    }

    static void runFulfillmentCenter() throws GameActionException {
        for (Direction dir : directions) {
            if (tryBuild(RobotType.DELIVERY_DRONE, dir)) {
                System.out.println("Made a Delivery Drone");
            }
        }
    }

    static void runLandscaper() throws GameActionException {

    }

    static void runDeliveryDrone() throws GameActionException {
        ArrayList list = new ArrayList();
        MapLocation cowloc = null;
        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);
            if (robots.length > 0) {
                for (RobotInfo robot : robots) {

                    //check all in radius and check for any cows
                    if (robot.type == RobotType.COW) {
                        list.add(robot.getID());
                        //if robot in list is a cow then mark location
                        cowloc = robot.location;
                        Direction directions_to_cow = rc.getLocation().directionTo(cowloc);
                        tryMove(directions_to_cow);
                        if (rc.getLocation().isAdjacentTo(cowloc)) {
                            rc.pickUpUnit(robot.getID());
                            break;
                        }

                    }

                }
                if(list.isEmpty()){
                    searchGraph();
                }

            }

        } else {
            MapLocation nexttoenemyhq = enemy_hq_location.add(randomDirection());
            Direction drone_to_HQ = rc.getLocation().directionTo(nexttoenemyhq);
            tryMove(drone_to_HQ);
            if (rc.getLocation().isAdjacentTo(enemy_hq_location) && rc.canDropUnit(Direction.EAST)) {
                rc.dropUnit(Direction.EAST);
            }

        }
        if (hq_location == null) {
            getHQLocation();
        } else {
            for (Direction dir : directions) {

                MapLocation defensivedrones = hq_location.add(dir);
                Direction defensetoHQ = rc.getLocation().directionTo(defensivedrones);
                if (rc.getLocation().isAdjacentTo(hq_location)) {
                    break;
                } else if (!rc.canMove(defensetoHQ)) {
                        tryMove(randomDirection());

                    }else{
                    tryMove(defensetoHQ);


                }


            }


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
