package rushbot_1;

import battlecode.common.*;

import java.util.ArrayList;

public class DeliveryDrone extends RobotPlayer {

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