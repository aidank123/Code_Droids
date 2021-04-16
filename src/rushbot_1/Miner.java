package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class Miner extends RobotPlayer {

    public static void runMiner() throws GameActionException {

//        System.out.println("HQ Location:" + hq_location);
//        System.out.println("Enemy HQ Location:" + enemy_hq_location);
//        for (int i = 0; i < visited.size(); i++){
//
//            System.out.println(visited.get(i));
//        }

        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        }

        else if(enemy_hq_location == null && hq_location != null){
            Communications.getEnemyHQLocation();
            //System.out.println("Searching for enemy hq");
            findEnHQ();
        }
//        m[turnCount] = new MapLocation(rc.getLocation().x,rc.getLocation().y);
//        System.out.println("visited spot: " + m[turnCount]);
        //miner finds location of hq

        //possibleEnemyHQ(hq_location);
//        if (enemy_hq_location == null) {
//                System.out.println("looking for enemy hq");
//                RobotInfo[] robots = rc.senseNearbyRobots();
//                tryMove(rc.getLocation().directionTo(enHQ1));
//                for (RobotInfo robot : robots) {
//                    if (robot.type == RobotType.HQ && robot.team == robot.getTeam().opponent()) {
//                        enemy_hq_location = robot.location;
//                    }
//                }
//        }

        else {
            System.out.println("other condition");
            tryBlockchain();
            for (Direction dir : directions)
                if (tryRefine(dir))
                    System.out.println("I refined soup! " + rc.getTeamSoup());

            for (Direction dir : directions)
                if (tryMine(dir))
                    System.out.println("I mined soup! " + rc.getSoupCarrying());

            if (!nearbyRobot(RobotType.DESIGN_SCHOOL) && numDesignSchools < 1) {
                if (tryBuild(RobotType.DESIGN_SCHOOL, randomDirection())) {
                    System.out.println("Created a design school");
                    numDesignSchools++;
                }
            }
            if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
                System.out.println("at soup limit");
                Direction directions_to_HQ = rc.getLocation().directionTo(hq_location);
                if (goTo(directions_to_HQ)) {
                    System.out.println("I moved towards hq");
                }
            } else if (goTo(randomDirection())) {
                System.out.println("I moved!");
            }
        }
    }

}
