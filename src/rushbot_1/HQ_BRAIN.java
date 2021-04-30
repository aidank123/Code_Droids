package rushbot_1;
import battlecode.common.RobotInfo;
import battlecode.common.Direction;
import battlecode.common.RobotType;
import battlecode.common.GameActionException;

public class HQ_BRAIN extends HQ {

    //this class takes in all of the current information and makes decisions about what to do next, returning a message to
    // the HQ run() method which will translate the decision into a message and transmit it to the team
    static boolean minerScout = false;


    public static void run() throws GameActionException {

        System.out.println(turnCount);
        if (turnCount < 450)  {

            if (turnCount % 10 == 0 || turnCount < 10) {
                Communications.sendEntireTeamCommand(EARLY_GAME_RUSH);
            }

            for (Direction dir : directions) {
                if ((rc.canBuildRobot((RobotType.MINER), dir) && rc.getTeamSoup() >= (RobotType.MINER.cost + message_cost) && numMiners < 5)) {
                    if (tryBuild((RobotType.MINER), dir)) {
                        Communications.sendMinerCreation(rc.adjacentLocation(dir));
                        //1 in 3 chance of creating an explorer, after creating one the first round
                        if(turnCount % 3 == 0){
                            //every three miners will be an explorer, meaning they only search for new soup locations
                            System.out.println("Calling send Explorer ID");
                            Communications.sendExplorerID(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).ID);
                        }
                    }
                }
            }
            //send the first miner off as a scout
            if (friendly_robots.length == 1 && minerScout == false) {
                Communications.sendScoutID(friendly_robots[0].ID);
                minerScout = true;
            }


        } else {
            if (turnCount % 10 == 0) {
                Communications.sendEntireTeamCommand(DEFEND_AND_BUILD);
            }


        }
    }

}
