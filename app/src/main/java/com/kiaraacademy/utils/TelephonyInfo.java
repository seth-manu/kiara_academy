package com.kiaraacademy.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.List;

public final class TelephonyInfo {

    private static TelephonyInfo telephonyInfo;
    private String imeiSIM1;
    private String imeiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;
    private int defaultSimm;
    private int smsDefaultSimm;
    private String numberSIM1;
    private String numberSIM2;

    private TelephonyInfo() {
    }

    public static TelephonyInfo getInstance(Context context) {

        if (telephonyInfo == null) {

            telephonyInfo = new TelephonyInfo();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

            telephonyInfo.imeiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imeiSIM2 = null;

            try {
                telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            } catch (GeminiMethodNotFoundException e) {
                e.printStackTrace();

                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //Call here for next manufacturer's predicted method name if you wish
                    e1.printStackTrace();
                }
            }

            telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
            telephonyInfo.isSIM2Ready = false;

            try {
                telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            } catch (GeminiMethodNotFoundException e) {

                e.printStackTrace();

                try {
                    telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                    telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //Call here for next manufacturer's predicted method name if you wish
                    e1.printStackTrace();
                }
            }

            Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method_getDefaultSim;

            try {
                method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
                method_getDefaultSim.setAccessible(true);
                telephonyInfo.defaultSimm = (Integer) method_getDefaultSim.invoke(tm);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Method method_getSmsDefaultSim;
            try {
                method_getSmsDefaultSim = tm.getClass().getDeclaredMethod("getSmsDefaultSim");
                telephonyInfo.smsDefaultSimm = (Integer) method_getSmsDefaultSim.invoke(tm);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager sm = SubscriptionManager.from(context);

                // it returns a list with a SubscriptionInfo instance for each simcard
                // there is other methods to retrieve SubscriptionInfos (see [2])
                List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();

                if (sis != null) {
                    for (int i = 0; i < sis.size(); i++) {
                        SubscriptionInfo si = sis.get(i);

                        if (si.getSimSlotIndex() == 0) {
                            telephonyInfo.numberSIM1 = si.getNumber();
                        } else {
                            telephonyInfo.numberSIM2 = si.getNumber();
                        }
                    }
                }
            }

        }

        return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        String imei = null;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                imei = ob_phone.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imei;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    public String getNumberSIM1() {
        return numberSIM1;
    }

    public String getNumberSIM2() {
        return numberSIM2;
    }

    public String getImsiSIM1() {
        return imeiSIM1;
    }

    public String getImsiSIM2() {
        return imeiSIM2;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public boolean isDualSIM() {
        return imeiSIM2 != null;
    }

    public int getDefaultSimm() {
        return defaultSimm;
    }

    public int getSMSDefaultSimm() {
        return smsDefaultSimm;
    }

    private static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
}