package rushbot_1;
import battlecode.common.RobotInfo;
import battlecode.common.Direction;
import battlecode.common.RobotType;
import battlecode.common.GameActionException;

public class HQ_BRAIN extends HQ {

    //this class takes in all of the current information and makes decisions about what to do next, returning a message to
    // the HQ run() method which will translate the decision into a message and transmit it to the team



    public static void run() throws GameActionException {

        System.out.println(turnCount);
        if (turnCount < 50) {

            if(turnCount % 10 == 0) {
                Communications.sendEntireTeamCommand(EARLY_GAME_RUSH);
            }

            for (Direction dir : directions) {
                if (rc.canBuildRobot((RobotType.MINER), dir) && rc.getTeamSoup() >= (RobotType.MINER.cost + message_cost)) {
                    if (tryBuild((RobotType.MINER), dir)) {
                        Communications.sendMinerCreation(rc.adjacentLocation(dir));
                    }
                }
            }
            //send the first miner off as a scout
            if(friendly_robots.length == 1){
                Communications.sendScoutID(friendly_robots[0].ID);
            }


        }   else if (turnCount == 51) {

                } else {

                }


            }
        }


