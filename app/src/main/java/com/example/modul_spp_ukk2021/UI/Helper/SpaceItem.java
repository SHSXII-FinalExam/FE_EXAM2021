package com.example.modul_spp_ukk2021.UI.Helper;

import android.view.View;

public class Utils {
    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                500
        );
    }

    public static class Utils {
        public static void preventTwoClick(final View view){
            view.setEnabled(false);
            view.postDelayed(
                    ()-> view.setEnabled(true),
                    500
            );
        }
    }
}

