package com.rock.werool.piensunmaize;

/**
 * Created by Martin on 15-Aug-17.
 */

public class BarcodeAction {
    public enum BarcodeDetectedAction {
        UPDATE_PRODUCT, FIND_PRODUCT_INFO, FIND_PRODUCT_COMPARE,
    }

    private BarcodeDetectedAction necessaryAction;       //Defines which action to execute after barcode is read

    public BarcodeDetectedAction getNecessaryAction() {
        return necessaryAction;
    }

    public void setNecessaryAction(BarcodeDetectedAction necessaryAction) {
        this.necessaryAction = necessaryAction;
    }

    public static void executeActionFromBarcode(BarcodeDetectedAction action, String barcodeContent) {
        switch (action) {
            case UPDATE_PRODUCT : {

                break;
            } case FIND_PRODUCT_INFO: {

                break;
            } case FIND_PRODUCT_COMPARE: {

                break;
            }
        }
    }
}
