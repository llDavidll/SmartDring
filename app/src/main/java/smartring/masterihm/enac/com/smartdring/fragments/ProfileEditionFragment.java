package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

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

    private ProfilesFragment profileFragment;

    private int colorPicker = 0;
    private EditText profileName;

    public static ProfileEditionFragment getInstance(Profile profile, ProfilesFragment pf) {
        ProfileEditionFragment fragment = new ProfileEditionFragment();
        fragment.setProfilesFragment(pf);
        Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, profile);
        fragment.setArguments(args);

        return fragment;
    }

    public void setProfilesFragment(ProfilesFragment pf){
        profileFragment = pf;
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
        mp = MediaPlayer.create(fa, R.raw.blop);
        currentAudioMedia = am.getStreamVolume(STREAM_MUSIC);

        prepareMediaPlayer();
        prepareSeekBars(v);
        prepareProfilePersonaliser(v);

        return v;
    }

    private void prepareProfilePersonaliser(View v) {
        final Button profileIcon = (Button) v.findViewById(R.id.fragment_profile_edition_icon);
        profileIcon.setBackgroundColor(mProfile.getColor());
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = getNextColor();
                mProfile.setColor(c);
                profileIcon.setBackgroundColor(c);
            }
        });

        profileName = (EditText) v.findViewById(R.id.fragment_profile_edition_name);
        profileName.setText(mProfile.getName());

        Button deleteButton = (Button) v.findViewById(R.id.fragment_profile_edition_delete);
        if (mProfile.isDefault()) {
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.GONE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete the profile & quit the editor
                ((SmartDringActivity)getActivity()).getDB().delete(mProfile);
                quitFragment();
            }
        });

        Button saveButton = (Button) v.findViewById(R.id.fragment_profile_edition_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfile.setName(profileName.getText().toString());
                if (mProfile == null || mProfile.getName().length() == 0) {
                    ((SmartDringActivity)getActivity()).getDB().delete(mProfile);
                } else {
                    saveProfile();
                }
                quitFragment();

            }
        });
    }

    private void quitFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(ProfileEditionFragment.this)
                .commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
        getActivity().getSupportFragmentManager().popBackStack();
        profileFragment.updateAdapter();
    }

    private void playFeedBackSound(int stream, int lvl) {
        currentAudioMedia = am.getStreamVolume(STREAM_MUSIC);
        int maxmedia = am.getStreamMaxVolume(STREAM_MUSIC);
        int maxsystem = am.getStreamMaxVolume(stream);
        int after = lvl * maxmedia / maxsystem;
        am.setStreamVolume(STREAM_MUSIC, after, 0);
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.blop);
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
        SeekBar soundLevelPhone = (SeekBar) v.findViewById(R.id.fragment_profile_edition_soundLevelPhone);
        soundLevelPhone.setProgress(mProfile.getmPhoneLvl() * 100 / am.getStreamMaxVolume(STREAM_SYSTEM));
        soundLevelPhone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_SYSTEM) / 100;
                mProfile.setmPhoneLvl(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_SYSTEM, mProfile.getmPhoneLvl());
                saveProfile();
            }
        });
        SeekBar soundLevelNotif = (SeekBar) v.findViewById(R.id.fragment_profile_edition_soundLevelNotif);
        soundLevelNotif.setProgress(mProfile.getmNotifLvl() * 100 / am.getStreamMaxVolume(STREAM_NOTIFICATION));
        soundLevelNotif.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_NOTIFICATION) / 100;
                mProfile.setmNotifLvl(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_NOTIFICATION, mProfile.getmNotifLvl());
                saveProfile();
            }
        });
        SeekBar soundLevelMedia = (SeekBar) v.findViewById(R.id.fragment_profile_edition_soundLevelMedia);
        soundLevelMedia.setProgress(mProfile.getmMediaLvl() * 100 / am.getStreamMaxVolume(STREAM_MUSIC));
        soundLevelMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_MUSIC) / 100;
                mProfile.setmMediaLvl(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_MUSIC, mProfile.getmMediaLvl());
                saveProfile();
            }
        });
        SeekBar soundLevelCall = (SeekBar) v.findViewById(R.id.fragment_profile_edition_soundLevelCall);
        soundLevelCall.setProgress(mProfile.getmCallLvl() * 100 / am.getStreamMaxVolume(STREAM_RING));
        soundLevelCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_RING) / 100;
                mProfile.setmCallLvl(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_RING, mProfile.getmCallLvl());
                saveProfile();
            }
        });
        SeekBar soundLevelAlarm = (SeekBar) v.findViewById(R.id.fragment_profile_edition_soundLevelAlarm);
        soundLevelAlarm.setProgress(mProfile.getmAlarmLvl() * 100 / am.getStreamMaxVolume(STREAM_ALARM));
        soundLevelAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i * am.getStreamMaxVolume(STREAM_ALARM) / 100;
                mProfile.setmAlarmLvl(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFeedBackSound(STREAM_ALARM, mProfile.getmAlarmLvl());
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        mProfile.setName(profileName.getText().toString());
        ((SmartDringActivity)getActivity()).getDB().save(mProfile);
    }

    public int getNextColor() {
        colorPicker = (colorPicker + 1) % 7;
        int nextColor = 0;
        switch (colorPicker){        
            case 0: nextColor = Color.argb(255,51,181,229);
                break;
            case 1: nextColor = Color.argb(255,255,136,0);
                break;
            case 2: nextColor = Color.argb(255,229,65,65);
                break;
            case 3: nextColor = Color.argb(255,238,244,65);
                break;
            case 4: nextColor = Color.argb(255,204,65,244);
                break;
            case 5: nextColor = Color.argb(255,94,205,76);
                break;
            case 6: nextColor = Color.DKGRAY;
                break;
        }
        return nextColor;
    }
}
