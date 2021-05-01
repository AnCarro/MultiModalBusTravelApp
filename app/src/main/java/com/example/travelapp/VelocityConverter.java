package com.example.travelapp;

public class VelocityConverter {

    public enum Units {
        MPH,
        KMPH,
        MPS,
        FPS
    }

   //public static Units NativeUnitType;

 /*   public VelocityConverter(Units unitType){
        NativeUnitType = unitType;
    }*/

    public VelocityConverter(){
    }

    private float MPStoKMPH(float vel) {
        float retVal =  (float) (vel*3.6);
        retVal = round(retVal, 1);
        return retVal;
    }

    private float MPStoKMPH(String vel) {
        float retVal;
        try {
            float val = Float.parseFloat(vel);
            retVal = (float) (val * 3.6);
            retVal = round(retVal, 1);
        } catch (Exception e) {
            e.printStackTrace();
            retVal = -1;
        }
        return retVal;
    }

    private float MPStoFPS (float vel) {
        float retVal =  (float) (vel*3.2808);
        retVal = round(retVal, 2);
        return retVal;
    }

    private float MPStoFPS (String vel) {
        float retVal;
        try {
            float val = Float.parseFloat(vel);
            retVal = (float) (val * 3.2808);
            retVal = round(retVal, 2);
        } catch (Exception e) {
            e.printStackTrace();
            retVal = -1;
        }
        return retVal;
    }

    private float MPStoMPH(float vel) {
        float retVal = (float) (vel*2.237);
        retVal = round(retVal, 1);
        return retVal;
    }

    private float MPStoMPH(String vel) {
        float retVal;
        try {
            float velocity = Float.parseFloat(vel);
            retVal = (float) (velocity*2.237);
            retVal = round(retVal, 1);
        } catch (Exception e) {
            e.printStackTrace();
            retVal = -1;
        }
        return retVal;
    }

    public float ConvertMPSTo(Units unitType, float velocity) {
        float retVal;
        switch (unitType){
            case MPH:
                retVal = MPStoMPH(velocity);
            break;
            case FPS:
                retVal = MPStoFPS(velocity);
            break;
            case KMPH:
                retVal = MPStoKMPH(velocity);
            break;
            case MPS:
                retVal = velocity;
                retVal = round(retVal, 2);
            break;
            default:
                retVal = 0;
                System.out.println("Error in Converstion process! \n" +
                        "Something did not go right in choosing the conversion method!");
        }
        return retVal;
    }

    public float ConvertMPSTo(Units unitType, String velocity) {
        float retVal;
        switch (unitType){
            case MPH:
                retVal = MPStoMPH(velocity);
                break;
            case FPS:
                retVal = MPStoFPS(velocity);
                break;
            case KMPH:
                retVal = MPStoKMPH(velocity);
                break;
            case MPS:
                try {
                    retVal = Float.parseFloat(velocity);
                    retVal = round(retVal, 2);
                } catch (Exception e){
                    System.out.println("Error converting velocity string to Double! \n");
                    e.printStackTrace();
                    retVal = 0;
                }
                break;
            default:
                retVal = 0;
                System.out.println("Error in Converstion process! \n" +
                        "Something did not go right in choosing the conversion method!");
        }
        return retVal;
    }

//converts units to string
    public String unitsToString (VelocityConverter.Units unit) {
        String stringUnits;
        switch (unit) {
            case MPH:
                stringUnits = "MPH";
                break;
            case FPS:
                stringUnits = "FPS";
                break;
            case MPS:
                stringUnits = "MPS";
                break;
            case KMPH:
                stringUnits = "KMPH";
                break;
            default:
                stringUnits = "MPH";
                System.out.println("Error unitsToString conversion from string in VelocityConverter. Units default set to MPH");
        }
        return stringUnits;
    }

    //converts String to units
    public Units StringToUnits (String sUnits) {
        Units units;
        switch (sUnits) {
            case "MPH":
                units = VelocityConverter.Units.MPH;
                break;
            case "KMPH":
                units = VelocityConverter.Units.KMPH;
                break;
            case "MPS":
                units = VelocityConverter.Units.MPS;
                break;
            case "FPS":
                units = VelocityConverter.Units.FPS;
                break;
            default:
                System.out.println("Error StringToUnits conversion from string in VelocityConverter. Units default set to MPH");
                units = VelocityConverter.Units.MPH;
        }
        return units;
    }

    //accepts a value to be rounded, and the precision to be rounded to
    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

}
