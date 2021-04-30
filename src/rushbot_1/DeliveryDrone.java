package rushbot_1;

import battlecode.common.*;

import java.util.ArrayList;

public class DeliveryDrone extends RobotPlayer {


    static boolean hascow;

    public static void runDeliveryDrone() throws GameActionException {

     ArrayList list = new ArrayList();
     //arraylist stores robot IDS and resets every time method is ran
     list.clear();
     //setting team for opponents
     Team enemy = rc.getTeam().opponent();
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

//               Team enemy = rc.getTeam().opponent();
//            if (!rc.isCurrentlyHoldingUnit()) {
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


        MapLocation cowloc;



        boolean hasenemyunit = false;
        boolean hascow = false;

        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any robots within sense radius
            RobotInfo[] robots = rc.senseNearbyRobots();

            if (robots.length > 0) {
                for (RobotInfo robot : robots) {

                    if (robot.team == enemy) {
                        //checking for enemy robots
                        list.add(robot.getID());
                        MapLocation enemyseen = robot.getLocation();
                        //sets location and direction to move towards
                        Direction pickupenemy = rc.getLocation().directionTo(enemyseen);
                        //if the drone is next to the enemy unit, drone picks it up
                        if (rc.getLocation().isAdjacentTo(enemyseen)) {
                            rc.pickUpUnit(robot.getID());
                            hasenemyunit = true;
                        } else {
                            tryMove(pickupenemy);
                        }
                    }
                    //check all in radius and check for any cows and make sure its early
                    if (robot.type == RobotType.COW && turnCount < 200) {
                        //check all in radius and check for any cows
                        //add those that are cows to ann arraylist to be able to tell when there are none
                        list.add(robot.getID());
                        //if robot in list is a cow then mark location
                        cowloc = robot.location;
                        //make directions to cow
                        Direction directions_to_cow = rc.getLocation().directionTo(cowloc);
                        //move towards cow and if we are adjacent, pick it up, make hascow true
                        tryMove(directions_to_cow);
                        if (rc.getLocation().isAdjacentTo(cowloc)) {
                            rc.pickUpUnit(robot.getID());
                            hascow = true;
                            break;
                        }

                    }

                }
//                if there are no enemies or cows in the vicinity then the drone will move around
                //to find cows or enemies
                if (list.isEmpty()) {
                    Pathing.sickPathing(randomDirection());
                }

            } else {

                if (hasenemyunit == true) {
                    //if it has an enemy unit looks for closest water
                    for (Direction dir : directions) {
                        if (rc.senseFlooding(rc.getLocation().add(dir))) {
                            //if it is next to or above water, drops the unit in,
                            //then sets hasenemyunit back to false.
                            rc.dropUnit(dir);
                            hasenemyunit = false;
                        } else {
                            Pathing.sickPathing(randomDirection());
                        }

                    }
                }
                if (hascow == true) {
                    //if the drone has a cow, the drones set a location and direction for enemy hq
                    Direction toenemyhq = rc.getLocation().directionTo(enemy_hq_location);
                    if (rc.getLocation().isAdjacentTo(enemy_hq_location)) {
                        rc.dropUnit(Direction.EAST);
                        //if it is next to, it will drop the cow next to the hq and hascow will become false again
                        hascow = false;
                    } else {
                        if (!rc.canMove(toenemyhq)) {
                            Pathing.sickPathing(randomDirection());
                            //if you can't move closer, random simulated annealing
                        } else {
                            tryMove(toenemyhq);
                        }

                    }

                }
//        for (Direction dir : directions) {
//
//            MapLocation defensivedrones = hq_location.add(dir);
//            Direction defensetoHQ = rc.getLocation().directionTo(defensivedrones);
//            if (rc.getLocation().isAdjacentTo(hq_location)) {
//                break;
//            } else if (!rc.canMove(defensetoHQ)) {
//                Pathing.sickPathing(randomDirection());
//
//            } else {
//                tryMove(defensetoHQ);


            }


        }



            }
        }








