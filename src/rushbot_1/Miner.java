package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
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
        if (turnCount % 2 == 0 && turnCount > 2) {
            //Communications.updateUnitCounts(10);
            Communications.receiveCommands();
            Communications.receiveUpdates();
        }

        if (hq_location == null) {
            Communications.getHQLocation();
            setEnemy_hq_location();
        }
//--------------------------------------------------------------------------//
        if (CURRENT_HQ_COMMAND == EARLY_GAME_RUSH) {

            //if the scout has not been set yet, check the blockchain for the command from HQ
            if(SCOUT == 0) {
                SCOUT = Communications.receiveScoutID();
            }

            if (rc.getID() == SCOUT) {
                if (enemy_hq_location == null && hq_location != null) {
                    Communications.getEnemyHQLocation();
                    System.out.println("Im the scout");
                    findEnHQ();
                    //once the enemy hq has been located, set the scout id to something other than 0.
                } else{
                    SCOUT = 1;
                }

            } else {
                //System.out.println("other condition");
                //tryBlockchain();
                for (Direction dir : directions)
                    if (tryRefine(dir)) {
                    }
                //System.out.println("I refined soup! " + rc.getTeamSoup());

                for (Direction dir : directions) {
                    if (tryMine(dir)) {
                        if (soup_locations.contains(rc.adjacentLocation(dir)) == false) {
                            soup_locations.add(rc.adjacentLocation(dir));
                            Communications.sendSoupLocations(rc.adjacentLocation(dir));
                            System.out.println("Sending new soup location ");
                        } else {
                            System.out.println("I already know that location");
                        }
                    } else if(rc.canMineSoup(dir) == false && soup_locations.contains(rc.adjacentLocation(dir))){
                        System.out.println("Remove this soup location from your list, it has been mined");
                        soup_locations.remove(rc.adjacentLocation(dir));
                    }
                }
                //System.out.println("I mined soup! " + rc.getSoupCarrying());

                //To do: make it so you cannot build a building if you do not have enough soup to also transmit you are doing so
                if (!nearbyRobot(RobotType.DESIGN_SCHOOL) && numDesignSchools < 1) {
                    for (Direction dir : directions)
                        //if the robot can build a design school and the total team soup is greater than cost of a design school + cost to send a message

                        if (rc.canBuildRobot((RobotType.DESIGN_SCHOOL), dir) && rc.getTeamSoup() >= (RobotType.DESIGN_SCHOOL.cost + message_cost)) {
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
                } else if (goTo(findClosestSoup())) {
                    System.out.println("I moved! towards the closest soup location");
                } else if(goTo(randomDirection())){
                    System.out.println("moved a random direction");
                }
            }
        }
    }
}
