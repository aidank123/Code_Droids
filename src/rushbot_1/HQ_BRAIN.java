package rushbot_1;

import battlecode.common.GameActionException;

public class HQ_BRAIN extends HQ {

    //this class takes in all of the current information and makes decisions about what to do next, returning a message to
    // the HQ run() method which will translate the decision into a message and transmit it to the team


    //All secret hq message commands. These will be passed into the communications methods
    // and placed into messages to the team

    static int DEFEND_HQ = 450;
    static int EARLY_GAME_RUSH = 145;

    public static void run() throws GameActionException {

        if(turnCount < 10){

        } else if (turnCount > 10){
            Communications.sendEntireTeamCommand(EARLY_GAME_RUSH);
        }else {

        }


    }
}
