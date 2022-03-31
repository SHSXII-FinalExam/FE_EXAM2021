package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.punyaAdmin;

public class LottieAnimationView {
    public static final int GONE;
    private static int VISIBLE;

    static {
        GONE = 0;
    }

    public static int getVISIBLE() {
        return VISIBLE;
    }

    public static void setVISIBLE(int VISIBLE) {
        LottieAnimationView.VISIBLE = VISIBLE;
    }
}
