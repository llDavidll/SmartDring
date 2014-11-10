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

    public void start() {
        mTelephonyManager.listen(this, LISTEN_CALL_STATE);
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            mPhoneStateListener.phoneCallStateChanged(incomingNumber, true);
        }
        else if (state == TelephonyManager.CALL_STATE_OFFHOOK || state == TelephonyManager.CALL_STATE_IDLE) {
            mPhoneStateListener.phoneCallStateChanged("", false);
        }
        //CALL_STATE_RINGING
            // if incomingNumber is in white list, load special LOUD profile
            // else if incomingNumber is in black list, load special SILENCE profile
            // else change nothing

        //CALL_STATE_OFFHOOK
            // restore the previous profile or the awaited profile
    }
}
