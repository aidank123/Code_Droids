package rushbot_1;

import battlecode.common.*;

import java.util.ArrayList;

public class DeliveryDrone extends RobotPlayer {


    public static void runDeliveryDrone() throws GameActionException {
        int enemiesdrowned = 0;
        //step 1 set hq location
        if (hq_location == null) {
            Communications.getHQLocation();
        } else if (enemy_hq_location == null) {
            Communications.getEnemyHQLocation();
        }

        //CHECKS EVERY OTHER ROUND ON EVEN NUMBERS
        if (rc.getRoundNum() % 2 == 0) {
            Communications.receiveCommands();
            //Communications.updateUnitCounts(10);
        }

 //       Team enemy = rc.getTeam().opponent();
//        if (!rc.isCurrentlyHoldingUnit()) {
//            // See if there are any enemy robots within capturing range
//            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);
//
//            if (robots.length > 0) {
//                // Pick up a first robot within range
//                rc.pickUpUnit(robots[0].getID());
//                System.out.println("I picked up " + robots[0].getID() + "!");
//            }
//        } else {
//            // No close robots, so search for robots within sight radius
//            tryMove(randomDirection());
//        }

        ArrayList list = new ArrayList() ;
        MapLocation cowloc;


        Team enemy = rc.getTeam().opponent();
        boolean hasenemyunit = false;
        boolean hascow = false;

        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);
            if (robots.length > 0) {
                for (RobotInfo robot : robots) {

                    if(robot.team == enemy){
                        list.add(robot.getID());
                        MapLocation enemyseen = robot.getLocation();
                        Direction pickupenemy = rc.getLocation().directionTo(enemyseen);
                        if(rc.getLocation().isAdjacentTo(enemyseen)){
                            rc.pickUpUnit(robot.getID());
                            hasenemyunit = true;
                        }else{
                            tryMove(pickupenemy);
                        }
                    }
                    //check all in radius and check for any cows
                    if (robot.type == RobotType.COW && enemiesdrowned > 5) {


                    //check all in radius and check for any cows
                    if (robot.type == RobotType.COW) {

                        list.add(robot.getID());
                        //if robot in list is a cow then mark location
                        cowloc = robot.location;
                        Direction directions_to_cow = rc.getLocation().directionTo(cowloc);
                        tryMove(directions_to_cow);
                        if (rc.getLocation().isAdjacentTo(cowloc)) {
                            rc.pickUpUnit(robot.getID());

                            hascow = true;

                            break;
                        }

                    }

                }
                if (list.isEmpty()) {
                    Direction enemyHQ = rc.getLocation().directionTo(enemy_hq_location);
                    tryMove(enemyHQ);
                }


            }

        } else {

            if(hasenemyunit = true) {
                for (Direction dir : directions) {
                    if (rc.senseFlooding(rc.getLocation().add(dir))) {
                        rc.dropUnit(dir);
                        enemiesdrowned++;
                    } else {
                        tryMove(randomDirection());
                    }

                }
            }
            if(hascow = true){
                Direction toenemyhq = rc.getLocation().directionTo(enemy_hq_location);
                if(rc.getLocation().isAdjacentTo(enemy_hq_location)){
                    rc.dropUnit(Direction.EAST);
                }else{
                    tryMove(toenemyhq);
                }

            }

        }
        if (hq_location == null) {
            rushbot_1.Communications.getHQLocation();
        } else {

                if (list.isEmpty()) {
                    pathTo(randomDirection());
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
            Communications.getHQLocation();
        } else {

            for (Direction dir : directions) {

                MapLocation defensivedrones = hq_location.add(dir);
                Direction defensetoHQ = rc.getLocation().directionTo(defensivedrones);
                if (rc.getLocation().isAdjacentTo(hq_location)) {
                    break;
                } else if (!rc.canMove(defensetoHQ)) {
                    tryMove(randomDirection());

                } else {
                    tryMove(defensetoHQ);


                }


            }


        }
    }
}