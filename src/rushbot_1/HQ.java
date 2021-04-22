package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQ extends RobotPlayer {

    public static void runHQ() throws GameActionException {

// HQ senses and shoots nearby enemy robots

        //list of all robots within sensory distance
        RobotInfo[] enemy_robots = rc.senseNearbyRobots(RobotType.HQ.sensorRadiusSquared,rc.getTeam().opponent());

        //IF THERE ARE ANY ENEMY ROBOTS NEAR HQ, WE ARE GOING WANT TO IMMEDIATELY DEFEND. SEND OUT A DEFENSIVE PROTOCOL
        //AND BEGIN SHOOTING AT ANY ENEMY DRONES.

        if(enemy_robots.length > 0){

            //method will return closest of enemy drones and shoot at it.
            shootClosestEnemyDrone(enemy_robots);

        }


        if (rc.getRoundNum() == 1) {
            //System.out.println(rc.);
            Communications.sendHQLocation(rc.getLocation());

        }
        //CHECKS EVERY PREVIOUS ROUND
        else {
            Communications.updateUnitCounts();
        }
//
//        System.out.println("Number of miners: " + numMiners);
//        System.out.println("Number of Landscapers: " + numLandscapers);
//        System.out.println("Number of drones: " + numDrones);
//        System.out.println("Number of DesignSchools: " + numDesignSchools);
//        System.out.println("Number of Refineries: " + numRefinery);
//        System.out.println("Number of Vaporators: " + numVaporators);
//        System.out.println("Number of NetGuns: " + numNetGuns);
//        System.out.println("Number of Fulfillment Centers: " + numFulfillmentCenters);
//
//
         if (numMiners < 5) {
            for (Direction dir : directions) {
                if (rc.canBuildRobot((RobotType.MINER),dir) && rc.getTeamSoup() >= (RobotType.MINER.cost + message_cost)) {
                    if (tryBuild((RobotType.MINER), dir)) {
                        Communications.sendMinerCreation(rc.adjacentLocation(dir));
                    }
                }

            }
        }
    }
}
