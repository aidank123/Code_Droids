package rushbot_1;
import battlecode.common.RobotInfo;
import battlecode.common.Direction;
import battlecode.common.RobotType;
import battlecode.common.GameActionException;

public class HQ_BRAIN extends HQ {

    //this class takes in all of the current information and makes decisions about what to do next, returning a message to
    // the HQ run() method which will translate the decision into a message and transmit it to the team


    //All secret hq message commands. These will be passed into the communications methods
    // and placed into messages to the team

    static int DEFEND_HQ = 450;
    static int EARLY_GAME_RUSH = 145;

    public static void run() throws GameActionException {

        System.out.println(turnCount);
        if (turnCount < 50) {


            for (Direction dir : directions) {
                if (rc.canBuildRobot((RobotType.MINER), dir) && rc.getTeamSoup() >= (RobotType.MINER.cost + message_cost)) {
                    if (tryBuild((RobotType.MINER), dir)) {
                        Communications.sendMinerCreation(rc.adjacentLocation(dir));
                    }
                }
                }

        }   else if (turnCount == 51) {
                    Communications.sendEntireTeamCommand(EARLY_GAME_RUSH);
                } else {

                }


            }
        }


