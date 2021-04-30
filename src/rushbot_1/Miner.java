package rushbot_1;

import battlecode.common.*;
public class Miner extends RobotPlayer {


    static int lastmine;

    public static void runMiner() throws GameActionException {

        //sets the closest soup location at the start of every turn


        //updates any missing or incorrect building or soup locations every 10 rounds
//        if(turnCount % 10 == 0) {
//            //checkSurroundings();
//        }
        System.out.println("Number of refineries" + numRefinery);

//        for (int i = 0; i < visited.size(); i++){
//
//            System.out.println(visited.get(i));
//        }
        //Communications.receiveExplorerID();

        //CHECKS EVERY OTHER ROUND ON EVEN NUMBERS
        if (turnCount % 2 == 0 && turnCount > 2) {
            //Communications.updateUnitCounts(10);
            Communications.receiveCommands();
            Communications.receiveUpdates();
            //receives which miners are explorers

        }

        if (hq_location == null) {
            Communications.getHQLocation();
            setEnemy_hq_location();
        }
//--------------------------------------------------------------------------//
        if (CURRENT_HQ_COMMAND == EARLY_GAME_RUSH || CURRENT_HQ_COMMAND == DEFEND_AND_BUILD) {

            //if the scout has not been set yet, check the blockchain for the command from HQ
            if (SCOUT == 0) {
                SCOUT = Communications.receiveScoutID();
            }

            if (turnCount - 50 > lastmine) {

                EXPLORER = rc.getID();
            }
            if (rc.getID() == 2) {
                System.out.println("Im a scout");
//                if (turnCount <= 100) {
//                    if (enemy_hq_location == null && hq_location != null) {
//                        Communications.getEnemyHQLocation();
//                        //System.out.println("Im the scout");
//
//                        findEnHQ();
//                        //once the enemy hq has been located, set the scout id to something other than 0.
//                        //Clock.yield();
//                    }
////                    } else {
////                        SCOUT = 1;
////                        ////Clock.yield();
////                    }
//                } else {

                    for (Direction dir : directions) {
                        if (tryRefine(dir)) {
                            //Clock.yield();
                        }
                    }
                    for (Direction dir : directions) {
                        if (tryMine(dir)) {
                            //Clock.yield();
                            lastmine = rc.getRoundNum();
                            System.out.println("I mined soup");
//                    } else if (rc.canMineSoup(dir) && soup_locations.contains(rc.adjacentLocation(dir))) {
//
//                        soup_locations.remove(rc.adjacentLocation(dir));
//                    }
                        }
                    }



                    if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
                        for (Direction dir : directions) {
                            if (goTo(nearestRefinery())) {
                                //System.out.println("nearest refinery at: " + nearestRefinery());
                            }
                        }
                    } else {

                        Pathing.sickPathing(randomDirection());

                        System.out.println("I moved towards the closest soup location: ");
                        //Clock.yield();
                    }


            } else if (rc.getID() == EXPLORER) {
                //will be randomly exploring the map, trying to find any new soup. It should still send
                //locations in the chat
                System.out.println("Im an explorer now");
                if (rc.senseNearbySoup().length > 0) {
                    System.out.println("I shouldnt be one anymore");
                    lastmine = rc.getRoundNum();
                    setExplorer(0);
                }

                if (Pathing.sickPathing(randomDirection())) {
                    //Clock.yield();
                }
            } else {
                //findClosestSoup();
                //System.out.println("other condition");
                //tryBlockchain();

//WHERE THR MINER DECIDES WHICH BUILDING TO CREATE

                for (Direction dir : directions) {
                    if (tryRefine(dir)) {
                        //Clock.yield();
                    }
                }
                for (Direction dir : directions) {
                    if (tryMine(dir)) {
                        //Clock.yield();
                        lastmine = rc.getRoundNum();
                        System.out.println("I mined soup");
//                    } else if (rc.canMineSoup(dir) && soup_locations.contains(rc.adjacentLocation(dir))) {
//
//                        soup_locations.remove(rc.adjacentLocation(dir));
//                    }
                    }
                }


                /*Start by building some refineries to make mining more efficient. They should be spaced out and far from hq or other refineriew.
                 * Miners should always build numRefineries = numMiners/3 */
                //could be worthwhile to make refineries when u find a large pool of soup?

            if (numVaporators < 1 && !nearbyRobot(RobotType.HQ)) {
                    for (Direction dir : directions)

                        if (rc.canBuildRobot((RobotType.VAPORATOR), dir) && rc.getTeamSoup() >= (RobotType.VAPORATOR.cost + message_cost)) {
                            if (tryBuild((RobotType.VAPORATOR), dir)) {
                                Communications.sendVaporatorCreation(rc.adjacentLocation(dir));
                                //Clock.yield();
                            }
                        }
                } else if (!nearbyRobot(RobotType.DESIGN_SCHOOL) && nearbyRobot(RobotType.HQ) && numDesignSchools < 1 && numVaporators >= 1) {
                    for (Direction dir : directions)
                        //if the robot can build a design school and the total team soup is greater than cost of a design school + cost to send a message

                        if (rc.canBuildRobot((RobotType.DESIGN_SCHOOL), dir) && rc.getTeamSoup() >= (RobotType.DESIGN_SCHOOL.cost + message_cost)) {
                            if (tryBuild((RobotType.DESIGN_SCHOOL), dir)) {
                                //System.out.println("Created a design school at: " + rc.adjacentLocation(dir));
                                Communications.sendDesignSchoolCreation(rc.adjacentLocation(dir));
                                //Clock.yield();
                            }
                        }
                }else if (nearbyRobot(RobotType.HQ) && numFulfillmentCenters < 2 && numDesignSchools >= 1) {
                    for (Direction dir : directions)
                        if (rc.canBuildRobot((RobotType.FULFILLMENT_CENTER), dir) && rc.getTeamSoup() >= (RobotType.FULFILLMENT_CENTER.cost + message_cost)) {
                            if (tryBuild((RobotType.FULFILLMENT_CENTER), dir)) {

                                Communications.sendFulfillmentCenterCreation(rc.adjacentLocation(dir));
                                //Clock.yield();
                            }
                        }
                }  else if (numRefinery < 1 && !nearbyRobot(RobotType.HQ) && numFulfillmentCenters >= 2) {
                    for (Direction dir : directions)

                        if (rc.canBuildRobot((RobotType.REFINERY), dir) && rc.getTeamSoup() >= (RobotType.REFINERY.cost + message_cost)) {
                            if (tryBuild((RobotType.REFINERY), dir)) {
                                Communications.sendRefineryCreation(rc.adjacentLocation(dir));
                                //Clock.yield();
                            }
                        }
                }

                if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
                    for (Direction dir : directions) {
                        if (Pathing.sickPathing(nearestRefinery())) {
                            //System.out.println("nearest refinery at: " + nearestRefinery());
                        }
                    }
                } else {
                    if (goTo(findClosestSoup().add(randomDirection()))) {

                        System.out.println("I moved towards the closest soup location: ");
                        //Clock.yield();
                    }

                }
            }
//                        } else {
//                            System.out.println("Random move chosen");
//                            goTo(randomDirection());
//                        }


        }
        //add in something that makes the miner start exploring


//                     }else {
//                            if(tryMove(randomDirection())){
//                                System.out.println("Random direction");
//                                //Clock.yield();
//                        }


    }


}




