package smartring.masterihm.enac.com.smartdring.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import static smartring.masterihm.enac.com.smartdring.service.ContextChangeDetector.ContextChangeInterface;

/**
 * Created by arnaud on 26/10/2014.
 */
public class IncallListener extends PhoneStateListener {
    private TelephonyManager mTelephonyManager;
    private ContextChangeInterface mPhoneStateListener;
    private int jej;

    public IncallListener(Context context, ContextChangeInterface pListener) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = pListener;
    }

    public void startDetection() {
        mTelephonyManager.listen(this, LISTEN_CALL_STATE);
    }

    public void stopDetection() {
        mTelephonyManager.listen(this, LISTEN_NONE);
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            mPhoneStateListener.phoneCallStateChanged(incomingNumber, true);
        }
        else if (state == TelephonyManager.CALL_STATE_OFFHOOK || state == TelephonyManager.CALL_STATE_IDLE) {
            mPhoneStateListener.phoneCallStateChanged("", false);
        }
    }
}
