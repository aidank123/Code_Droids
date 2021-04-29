package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQ extends RobotPlayer {

    static int outgoing_message;

    public static void runHQ() throws GameActionException {

        //STEP 1: TAKE IN ALL NECESSARY DATA FROM THE BLOCKCHAIN

        if (rc.getRoundNum() == 1) {
            //System.out.println(rc.);
            Communications.sendHQLocation(curr_loc);
            Communications.sendEntireTeamCommand(EARLY_GAME_RUSH);

        }
        //checks blockchain from every previous round
        else {

            Communications.updateUnitCounts();


            if(turnCount % 10 == 0) {
                //updates unit counts, then sends counts out to the team. These updates will be sent out every 10 rounds
                Communications.sendGeneralBuildingUpdates();
                Communications.sendMovingRobotUpdates();
            }
        }

        //STEP 2: TAKE IN ALL DATA FROM SURROUNDINGS


        // HQ senses and shoots nearby enemy robots


        //IF THERE ARE ANY ENEMY ROBOTS NEAR HQ, WE ARE GOING WANT TO IMMEDIATELY DEFEND. SEND OUT A DEFENSIVE PROTOCOL
        //AND BEGIN SHOOTING AT ANY ENEMY DRONES.

        if (enemy_robots.length > 0) {

            //method will return closest of enemy drones and shoot at it.
            shootClosestEnemyDrone(enemy_robots);

        }

//This line runs the HQ_BRAIN and receives what messages will be sent to the team based on all updated information,
        HQ_BRAIN.run();



//         if (numMiners < 5) {
//
//                if (rc.canBuildRobot((RobotType.MINER),dir) && rc.getTeamSoup() >= (RobotType.MINER.cost + message_cost)) {
//                    if (tryBuild((RobotType.MINER), dir)) {
//                        Communications.sendMinerCreation(rc.adjacentLocation(dir));
//                    }
//                }
//
//            }
//        }
        }
    }

