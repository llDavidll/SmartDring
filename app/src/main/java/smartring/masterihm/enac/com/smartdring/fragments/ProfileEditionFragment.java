package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioManager;
import android.widget.SeekBar;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.Profile;

/**
 * Created by David on 14/10/2014.
 * <p/>
 * Fragment used to create/modify a profile.
 */
public class ProfileEditionFragment extends Fragment {

    public final static String TAG = "ProfileEditionFragmentTag";
    private static final String PROFILE_KEY = "PROFILE_KEY";

    private AudioManager am;
    private SeekBar soundLevelPhone;
    private SeekBar soundLevelNotif;
    private SeekBar soundLevelMedia;
    private SeekBar soundLevelCall;
    private SeekBar soundLevelAlarm;

    public static ProfileEditionFragment getInstance(@Nullable Profile profile) {

        ProfileEditionFragment fragment = new ProfileEditionFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, profile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_edition, container, false);
        FragmentActivity fa = SmartDringActivity.mainActivity;
        am = (AudioManager)fa.getSystemService(Context.AUDIO_SERVICE);

        final TextView txt = (TextView) v.findViewById(R.id.soundLevelTxt);

        soundLevelPhone = (SeekBar) v.findViewById(R.id.soundLevelPhone);
        soundLevelPhone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100;
                am.setStreamVolume(AudioManager.STREAM_SYSTEM,scale,0);
                String r = "System : " + am.getStreamVolume(AudioManager.STREAM_SYSTEM);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        soundLevelNotif = (SeekBar) v.findViewById(R.id.soundLevelNotif);
        soundLevelNotif.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) / 100;
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION,scale,0);
                String r = "Notif : " + am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                txt.setText(r);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        soundLevelMedia = (SeekBar) v.findViewById(R.id.soundLevelMedia);
        soundLevelMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 100;
                am.setStreamVolume(AudioManager.STREAM_MUSIC,scale,0);
                String r = "Media: " + am.getStreamVolume(AudioManager.STREAM_MUSIC);
                txt.setText(r);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        soundLevelCall = (SeekBar) v.findViewById(R.id.soundLevelCall);
        soundLevelCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(AudioManager.STREAM_RING) / 100;
                am.setStreamVolume(AudioManager.STREAM_RING,scale,0);
                String r = "CALL: " + am.getStreamVolume(AudioManager.STREAM_RING);
                txt.setText(r);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        soundLevelAlarm = (SeekBar) v.findViewById(R.id.soundLevelAlarm);
        soundLevelAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(AudioManager.STREAM_ALARM) / 100;
                am.setStreamVolume(AudioManager.STREAM_ALARM,scale,0);
                String r = "Alarm: " + am.getStreamVolume(AudioManager.STREAM_ALARM);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }
}
