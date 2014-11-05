package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.Profile;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

import static android.media.AudioManager.STREAM_ALARM;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_NOTIFICATION;
import static android.media.AudioManager.STREAM_RING;
import static android.media.AudioManager.STREAM_SYSTEM;

/**
 * Created by David on 14/10/2014.
 * <p/>
 * Fragment used to create/modify a profile.
 */
public class ProfileEditionFragment extends Fragment {

    public final static String TAG = "ProfileEditionFragmentTag";
    private static final String PROFILE_KEY = "PROFILE_KEY";

    private Profile mProfile;

    private AudioManager am;
    private MediaPlayer mp;
    private int currentAudioMedia;

    private SeekBar soundLevelPhone;
    private SeekBar soundLevelNotif;
    private SeekBar soundLevelMedia;
    private SeekBar soundLevelCall;
    private SeekBar soundLevelAlarm;

    public static ProfileEditionFragment getInstance(Profile profile) {

        ProfileEditionFragment fragment = new ProfileEditionFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, profile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfile = (Profile) getArguments().getSerializable(PROFILE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_edition, container, false);

        FragmentActivity fa = getActivity();
        am = (AudioManager) fa.getSystemService(Context.AUDIO_SERVICE);
        mp = MediaPlayer.create(SmartDringActivity.mainActivity, R.raw.fart);
        currentAudioMedia = am.getStreamVolume(STREAM_MUSIC);

        prepareMediaPlayer();
        prepareSeekBars(v);

        return v;
    }

    private void playFeedBackSound(int streamSystem) {
        currentAudioMedia = am.getStreamVolume(STREAM_MUSIC);
        int maxmedia = am.getStreamMaxVolume(STREAM_MUSIC);
        int maxsystem = am.getStreamMaxVolume(streamSystem);
        int after = am.getStreamVolume(streamSystem) * maxmedia / maxsystem;
        am.setStreamVolume(STREAM_MUSIC, after, 0);
        MediaPlayer mediaPlayer = MediaPlayer.create(SmartDringActivity.mainActivity, R.raw.fart);
        mediaPlayer.start();
    }

    private void prepareMediaPlayer() {
        mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                am.setStreamVolume(STREAM_MUSIC, currentAudioMedia, 0);
            }
        });
    }

    private void prepareSeekBars(View v) {

        final TextView txt = (TextView) v.findViewById(R.id.soundLevelTxt);
        soundLevelPhone = (SeekBar) v.findViewById(R.id.soundLevelPhone);
        soundLevelPhone.setProgress(mProfile.getmPhoneLvl() * 100 / am.getStreamMaxVolume(STREAM_SYSTEM));
        soundLevelPhone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_SYSTEM) / 100;
                am.setStreamVolume(STREAM_SYSTEM, scale, 0);
                mProfile.setmPhoneLvl(scale);
                String r = "System : " + am.getStreamVolume(STREAM_SYSTEM);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_SYSTEM);
                saveProfile();
            }
        });
        soundLevelNotif = (SeekBar) v.findViewById(R.id.soundLevelNotif);
        soundLevelNotif.setProgress(mProfile.getmNotifLvl() * 100 / am.getStreamMaxVolume(STREAM_NOTIFICATION));
        soundLevelNotif.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_NOTIFICATION) / 100;
                am.setStreamVolume(STREAM_NOTIFICATION, scale, 0);
                mProfile.setmNotifLvl(scale);
                String r = "Notif : " + am.getStreamVolume(STREAM_NOTIFICATION);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_NOTIFICATION);
                saveProfile();
            }
        });
        soundLevelMedia = (SeekBar) v.findViewById(R.id.soundLevelMedia);
        soundLevelMedia.setProgress(mProfile.getmMediaLvl() * 100 / am.getStreamMaxVolume(STREAM_MUSIC));
        soundLevelMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_MUSIC) / 100;
                am.setStreamVolume(STREAM_MUSIC, scale, 0);
                mProfile.setmMediaLvl(scale);
                String r = "Media: " + am.getStreamVolume(STREAM_MUSIC);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_MUSIC);
                saveProfile();
            }
        });
        soundLevelCall = (SeekBar) v.findViewById(R.id.soundLevelCall);
        soundLevelCall.setProgress(mProfile.getmCallLvl() * 100 / am.getStreamMaxVolume(STREAM_RING));
        soundLevelCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_RING) / 100;
                am.setStreamVolume(STREAM_RING, scale, 0);
                mProfile.setmCallLvl(scale);
                String r = "CALL: " + am.getStreamVolume(STREAM_RING);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_RING);
                saveProfile();
            }
        });
        soundLevelAlarm = (SeekBar) v.findViewById(R.id.soundLevelAlarm);
        soundLevelAlarm.setProgress(mProfile.getmAlarmLvl() * 100 / am.getStreamMaxVolume(STREAM_ALARM));
        soundLevelAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_ALARM) / 100;
                am.setStreamVolume(STREAM_ALARM, scale, 0);
                mProfile.setmAlarmLvl(scale);
                String r = "Alarm: " + am.getStreamVolume(STREAM_ALARM);
                txt.setText(r);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_ALARM);
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        SmartDringDB.getDatabase(SmartDringDB.APP_DB).saveProfile(mProfile);
    }
}
