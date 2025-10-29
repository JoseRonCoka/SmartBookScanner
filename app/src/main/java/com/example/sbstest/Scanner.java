//Project: SBS
//Author: Jose Ron Coka
//File: Scanner.java
//Version: Working Prototype 2
//Date: 09/18/2025



package com.example.sbstest;

import com.journeyapps.barcodescanner.ScanOptions;




public class Scanner{

    private String isbn;

    public String scanned;

    public Scanner () {
    }


    //Scanner helper class, getScanOptions returns scan options.
    public ScanOptions getScanOptions() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        return options;
    }

}
