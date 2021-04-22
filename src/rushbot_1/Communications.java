package rushbot_1;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Transaction;

public class Communications extends RobotPlayer {

    //secret codes for each type of robot
    static final int HQSecret = 1576;
    static final int MinerSecret = 7207;
    static final int RefinerySecret = 6286;
    static final int VaporatorSecret = 8283;
    static final int DesignSchoolSecret = 1135;
    static final int FulfillmentCenterSecret = 1036;
    static final int LandscaperSecret = 8290;
    static final int DeliveryDroneSecret = 1389;
    static final int NetGunSecret = 5324;

    //secret code for team chat
    static final int TeamSecret = 2040;

    public static void sendEnemyHQLocation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = TeamSecret;
        message[1] = 1;
        message[2] = m.x;
        message[3] = m.y;
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void getEnemyHQLocation() throws GameActionException {
        //System.out.println("B L O C K C H A I N");
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == TeamSecret && mess[1] == 1) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
        }
    }

//THESE METHODS ARE WHAT BROADCAST AND RECEIVE CREATION AND LOCATION OF ALL NEW STATIC ROBOTS (BUILDINGS)

    public static void sendDesignSchoolCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = DesignSchoolSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendRefineryCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = RefinerySecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendVaporatorCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = VaporatorSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendFulfillmentCenterCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = FulfillmentCenterSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendNetGunCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = NetGunSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }
//THESE METHODS BROADCAST AND RECEIVE THE CREATION OF MOVING ROBOTS (MINERS,LANDSCAPERS,DRONES)

    public static void sendMinerCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = MinerSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendLandscaperCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = LandscaperSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendDroneCreation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = DeliveryDroneSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }


//THIS METHOD CALLS ALL OF THE METHODS ABOVE AND SHOULD UPDATE THE UNIT COUNTS WHEN CALLED BY EACH ROBOT
// Each robot will wait 10 rounds to check the blockchain, then will check the previous 10 rounds for updates

    public static void updateUnitCounts() throws GameActionException {
        //CHECKS THE PREVIOUS ROUND
        for (Transaction t : rc.getBlock(rc.getRoundNum() - 1)) {
            int[] mess = t.getMessage();

            switch (mess[0]) {
                case DeliveryDroneSecret:
                    if (mess[1] == 0) {
                        numDrones++;
//                            Drones.add(rc.getID());
                        System.out.println("Messaged recieved: one drone added to count");
                    }
                    break;
                case MinerSecret:
                    if (mess[1] == 0) {
                        numMiners++;
//                            Miners.add(rc.getID());
                        System.out.println("Messaged recieved: one miner added to count");
                    }
                    break;
                case LandscaperSecret:
                    if (mess[1] == 0) {
                        numLandscapers++;
//                            Landscapers.add(rc.getID());
                        System.out.println("Messaged recieved: one landscaper added to count");
                    }
                    break;
                case DesignSchoolSecret:
                    if (mess[1] == 0) {
                        numDesignSchools++;
                        Design_Schools.add(new MapLocation(mess[2], mess[3]));
                        System.out.println("Messaged recieved: one design school added to count");
                    }
                    break;
                case FulfillmentCenterSecret:
                    if (mess[1] == 0) {
                        numFulfillmentCenters++;
                        Fulfillment_Centers.add(new MapLocation(mess[2], mess[3]));
                        System.out.println("Messaged recieved: one fulfilment center added to count");
                    }
                    break;
                case RefinerySecret:
                    if (mess[1] == 0) {
                        numRefinery++;
                        Refineries.add(new MapLocation(mess[2], mess[3]));
                        System.out.println("Messaged recieved: one refinery added to count");
                    }
                    break;
                case NetGunSecret:
                    if (mess[1] == 0) {
                        numNetGuns++;
                        NetGuns.add(new MapLocation(mess[2], mess[3]));
                        System.out.println("Messaged recieved: one net gun added to count");
                    }
                    break;
                case VaporatorSecret:
                    if (mess[1] == 0) {
                        numVaporators++;
                        Vaporators.add(new MapLocation(mess[2], mess[3]));
                        System.out.println("Messaged recieved: one vaporator added to count");
                    }
                    break;
            }

        }
    }

//HQ TEAM UPDATES AND COMMANDS

    //All of the following methods are the hq team updates for the robots.
    //send location of hq
    public static void sendHQLocation(MapLocation m) throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 0;
        message[2] = m.x;
        message[3] = m.y;
        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //access location of hq
    public static void getHQLocation() throws GameActionException {
        System.out.println("B L O C K C H A I N");
        //for (int i = 1; i < rc.getRoundNum(); i++) {
        for (Transaction t : rc.getBlock(1)) {
            int[] mess = t.getMessage();
            if (mess[0] == HQSecret && mess[1] == 0) {
                hq_location = new MapLocation(mess[2], mess[3]);
            }
        }
    }

    //
    public static void sendGeneralBuildingUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 0;
        //messages will be filled in with necessary information for buildings
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendDesignSchoolUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 1;
        //messages will be filled in with necessary information for Design Schools
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendRefineryUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 2;
        //messages will be filled in with necessary information for Refineries
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendFulfillmentCenterUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 3;
        //messages will be filled in with necessary information for Fulfillment Centers
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendVaporatorUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 4;
        //messages will be filled in with necessary information for Vaporators
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendNetGunUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 1;
        message[2] = 5;
        //messages will be filled in with necessary information for NetGuns
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendMovingRobotUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 2;
        message[2] = 0;
        //messages will be filled in with necessary information for all moving robots (miners, landscapers, drones)
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendMinerUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 2;
        message[2] = 1;
        //messages will be filled in with necessary information for miners
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendLandscaperUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 2;
        message[2] = 2;
        //messages will be filled in with necessary information for landscapers
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendDroneUpdates() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 2;
        message[2] = 3;
        //messages will be filled in with necessary information for miners
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    public static void sendEntireTeamUpdate() throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 3;
        //messages will be filled in with necessary information for miners
        message[2] = 0;
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = 0;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

//THE FOLLOWING METHOD WILL BE USED BY ALL ROBOTS BUT HQ.
    /*
    This method looks at the blockchain and receives all of the necessary updates for each type of robot

    The switch statements follow a hierarchy that say general team messages are more important than specific type of robot
    messages, just in case multiple of these messages are sent in the blockchain by HQ. After each condition, the updates will be applied
    to the necessary variables, probably stored in RobotPlayer

    */

    public static void receiveUpdates() throws GameActionException {


        switch (rc.getType()) {
            case MINER:
                //Miners get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();


                    }
                }

                break;
            case LANDSCAPER:
                //Landscapers get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;
            case DELIVERY_DRONE:
                //Drones get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;

            case DESIGN_SCHOOL:
                //Design Schools get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;

            case REFINERY:
                //Refineries get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;

            case FULFILLMENT_CENTER:
                //Fulfillment Centers get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;

            case VAPORATOR:
                //Vaporators get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;

            case NET_GUN:
                //Netguns get updated every 5 rounds
                for (int i = rc.getRoundNum() - 5; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                    }
                }
                break;



        }
    }


    //All of the following methods are HQ commands for the team. Updates give the team information, while commands
    //to act a certain way

    public static void sendEntireTeamCommand(int command) throws GameActionException {
        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 0;
        //message[7] contains the secret code that gives all robots certain commands
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to buildings
    public static void sendBuildingCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to DesignSchools
    public static void sendDesignSchoolCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 1;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to Refineries
    public static void sendRefineryCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 2;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to Fulfillment Centers
    public static void sendFulfillmentCenterCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 3;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to Vaporators
    public static void sendVaporatorCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 4;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command sent only to NetGuns
    public static void sendNetGunCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 1;
        message[3] = 5;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command send to all moving robots (miners, landscapers, drones)
    public static void sendMovingRobotCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 2;
        message[3] = 0;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command send to all miners
    public static void sendMinerCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 2;
        message[3] = 1;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command send to all landscapers
    public static void sendLandscaperCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 2;
        message[3] = 2;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

    //command send to all drones
    public static void sendDroneCommand(int command) throws GameActionException {

        int[] message = new int[7];
        message[0] = HQSecret;
        message[1] = 4;
        message[2] = 2;
        message[3] = 3;
        message[4] = 0;
        message[5] = 0;
        message[6] = command;

        if (rc.canSubmitTransaction(message, 3)) {
            rc.submitTransaction(message, 3);
        }
    }

//THE FOLLOWING METHOD WILL BE USED BY ALL ROBOTS BUT HQ.
    /*
    This method looks at the blockchain and receives all of the necessary commands for each type of robot

    The switch statements follow a hierarchy that say general team messages are more important than specific type of robot
    messages, just in case multiple of these messages are sent in the blockchain by HQ. After each condition, the variable CURRENT_HQ_COMMAND
    is set to be whatever message the robot is going to follow from HQ.

    */


    public static void receiveCommands() throws GameActionException {


        switch (rc.getType()) {
            case MINER:
                //Miners get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();
                        System.out.println(t.getMessage());

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    System.out.println("Miner received Team command: " + mess[6]);
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (2):
                                    if (mess[3] == 0 || mess[3] == 1) {
                                        System.out.println("Miner received Moving Robot/Miner update: " + mess[6]);
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;
            case LANDSCAPER:
                //Landscapers get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (2):
                                    if (mess[3] == 0 || mess[3] == 2) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;
            case DELIVERY_DRONE:
                //Drones get updated every other round, so must check the blocks from the previous 2 rounds
                for (int i = rc.getRoundNum() - 2; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (2):
                                    if (mess[3] == 0 || mess[3] == 3) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;

            case DESIGN_SCHOOL:
                //Design Schools get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (1):
                                    if (mess[3] == 0 || mess[3] == 1) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;

            case REFINERY:
                //Refineries get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (1):
                                    if (mess[3] == 0 || mess[3] == 2) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;

            case FULFILLMENT_CENTER:
                //Fulfillment Centers get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (1):
                                    if (mess[3] == 0 || mess[3] == 3) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;

            case VAPORATOR:
                //Vaporators get updated every 20 rounds
                for (int i = rc.getRoundNum() - 20; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (1):
                                    if (mess[3] == 0 || mess[3] == 4) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                break;

            case NET_GUN:
                //Netguns get updated every 5 rounds
                for (int i = rc.getRoundNum() - 5; i < rc.getRoundNum(); i++) {
                    for (Transaction t : rc.getBlock(i)) {
                        int[] mess = t.getMessage();

                        if (mess[0] == HQSecret && mess[1] == 4) {
                            switch (mess[2]) {
                                case (0):
                                    CURRENT_HQ_COMMAND = mess[6];
                                    break;
                                case (1):
                                    if (mess[3] == 0 || mess[3] == 5) {
                                        CURRENT_HQ_COMMAND = mess[6];
                                    }
                                    break;
                            }
                        }
                    }
                }
                    break;



        }
    }
}

//at the end of this method, CURRENT_HQ_COMMANDS should be reset to empty
        //if the message relates to the robot, it will add it to its list of current HQ commands



