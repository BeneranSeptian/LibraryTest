package com.librarytest;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;

public class ZebraModule extends ReactContextBaseJavaModule {
    ZebraModule(ReactApplicationContext context){
        super(context);
    }

    @ReactMethod
    public void initBarcodeScanner(){
//        Toast.makeText(getReactApplicationContext(),String.valueOf(isZebraDevice()), Toast.LENGTH_SHORT).show();
        if(isZebraDevice()) {
            EmdkImpl emdk = new EmdkImpl();
            EMDKResults results = EMDKManager.getEMDKManager(getReactApplicationContext(),emdk );
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
                return;
            }
            Toast.makeText(getReactApplicationContext(), "Ini Zebra device, bisa di nyalain scannya", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getReactApplicationContext(),"Bukan Device Zebra cuk!", Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    @Override
    public String getName() {
        return "ZebraModule";
    }

    public boolean isZebraDevice() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        // Check if the manufacturer is Zebra and the model contains "TC" or "MC" (for Zebra devices)
        return manufacturer.equalsIgnoreCase("Zebra Technologies") && (model.contains("TC") || model.contains("MC"));
    }

//    private void startScanning(){
//        if(scanner != null) {
//            try {
//                // Start scanning
//                scanner.read();
//            } catch (ScannerException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    class EmdkImpl implements EMDKManager.EMDKListener{

        private EMDKManager emdkManager;
        private Scanner scanner;

        @Override
        public void onOpened(EMDKManager emdkManager) {
            if(!isZebraDevice()){
                return;
            }
            this.emdkManager = emdkManager;
            BarcodeManager barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);

            if(scanner != null){
                scanner.addDataListener(scanDataCollection -> {
                    if(scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS){
                        for (ScanDataCollection.ScanData data : scanDataCollection.getScanData()) {
                            String barcodeData = data.getData();
                            Log.d("BARCODE CUK", barcodeData);
                        }
                    }
                });
            }
        }

        @Override
        public void onClosed() {
            if(!isZebraDevice()){
                return;
            }

            if (scanner != null) {
                // Disable and release the scanner
                try {
                    scanner.disable();
                    scanner.release();
                } catch (ScannerException e) {
                    e.printStackTrace();
                }
                scanner = null;
            }
            if (emdkManager != null) {
                // Release the EMDK manager
                emdkManager.release();
                emdkManager = null;
            }
        }
    }
}
