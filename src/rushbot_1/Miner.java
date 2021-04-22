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
        //CHECKS EVERY OTHER ROUND ON EVEN NUMBERS
        if(rc.getRoundNum() % 2 == 0) {
            //Communications.updateUnitCounts(10);
            Communications.receiveCommands();
        }

        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        }

        else if(enemy_hq_location == null && hq_location != null){
            Communications.getEnemyHQLocation();
            //System.out.println("Searching for enemy hq");
            findEnHQ();
        }


        else {
            //System.out.println("other condition");
            tryBlockchain();
            for (Direction dir : directions)
                if (tryRefine(dir)){}
                    //System.out.println("I refined soup! " + rc.getTeamSoup());

            for (Direction dir : directions)
                if (tryMine(dir)){}
                    //System.out.println("I mined soup! " + rc.getSoupCarrying());

                //To do: make it so you cannot build a building if you do not have enough soup to also transmit you are doing so
            if (!nearbyRobot(RobotType.DESIGN_SCHOOL) && numDesignSchools < 1) {
                for (Direction dir : directions)
                    //if the robot can build a design school and the total team soup is greater than cost of a design school + cost to send a message

                    if (rc.canBuildRobot((RobotType.DESIGN_SCHOOL),dir) && rc.getTeamSoup() >= (RobotType.DESIGN_SCHOOL.cost + message_cost)) {
                        if (tryBuild((RobotType.DESIGN_SCHOOL), dir)) {
                            //System.out.println("Created a design school at: " + rc.adjacentLocation(dir));
                            Communications.sendDesignSchoolCreation(rc.adjacentLocation(dir));
                        }
                    }
            }
            if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
                //System.out.println("at soup limit");
                Direction directions_to_HQ = rc.getLocation().directionTo(hq_location);
                if (goTo(directions_to_HQ)) {
                    //System.out.println("I moved towards hq");
                }
            } else if (goTo(randomDirection())) {
                //System.out.println("I moved!");
            }
        }
    }

}
