package smartring.masterihm.enac.com.smartdring.service;

import android.telephony.PhoneStateListener;

/**
 * Created by arnaud on 26/10/2014.
 */
public class IncallListener extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        //CALL_STATE_RINGING
            // if incomingNumber is in white list, load special LOUD profile
            // else if incomingNumber is in black list, load special SILENCE profile
            // else change nothing

        //CALL_STATE_OFFHOOK
            // restore the previous profile or the awaited profile

        super.onCallStateChanged(state, incomingNumber);
    }
}
