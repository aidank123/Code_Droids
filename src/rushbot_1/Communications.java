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



    static final String[] messageType = {"HQ loc",};

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
        System.out.println("B L O C K C H A I N");
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == TeamSecret && mess[1] == 1) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
        }
    }

//THESE METHODS ARE WHAT BROADCAST AND RECEIVE CREATION AND LOCATION OF ALL NEW STATIC ROBOTS

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

    public static void getDesignSchoolCreation() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == DesignSchoolSecret && mess[1] == 0) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
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

    public static void getRefineryCreation() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == RefinerySecret && mess[1] == 0) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
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

    public static void getVaporatorCreation() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == VaporatorSecret && mess[1] == 0) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
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

    public static void getFulfillmentCenterCreation() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == FulfillmentCenterSecret && mess[1] == 0) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
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

    public static void getNetGunCreation() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(i)) {
                int[] mess = t.getMessage();
                if (mess[0] == NetGunSecret && mess[1] == 0) {
                    enemy_hq_location = new MapLocation(mess[2], mess[3]);
                }
            }
        }
    }

//THESE METHODS ARE WHAT ACCESS THE LOCATION OF ALL NEW STATIC ROBOTS


    public static void updateUnitCounts() throws GameActionException {
        for (int i = 1; i < rc.getRoundNum(); i++) {
            for (Transaction t : rc.getBlock(rc.getRoundNum() - 1)) {
                int[] mess = t.getMessage();
                if (mess[0] == TeamSecret && mess[1] == 1) {
                    numDesignSchools += 1;
//                    }else if ()
                }
            }
        }
    }
}
