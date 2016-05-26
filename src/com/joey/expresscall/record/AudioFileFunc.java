package com.joey.expresscall.record;

import java.io.File;

import android.media.MediaRecorder;
import android.os.Environment;

import com.joey.expresscall.AppConsts;
import com.joey.general.utils.DateUtil;

public class AudioFileFunc {
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 8000;  //44.1KHz,普遍使用的频率
    //录音输出文件
    private final static String AUDIO_RAW_FILENAME = ".raw";
    private final static String AUDIO_WAV_FILENAME = ".wav";
    public final static String AUDIO_AMR_FILENAME = ".amr";
    private final static String FILE_DIR = AppConsts.RECORD_DIR;

    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取麦克风输入的原始音频流文件路径
     *
     * @return
     */
    public static String getRawFilePath() {
        String mAudioRawPath = "";
        if (isSdcardExit()) {

            mAudioRawPath = FILE_DIR + "/" + DateUtil.getTime(DateUtil.DATE_FORMMAT_STR_4) + AUDIO_RAW_FILENAME;
        }

        return mAudioRawPath;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     *
     * @return
     */
    public static String getWavFilePath() {
        String mAudioWavPath = "";
        if (isSdcardExit()) {

            mAudioWavPath = FILE_DIR + "/" + DateUtil.getTime(DateUtil.DATE_FORMMAT_STR_4) + AUDIO_WAV_FILENAME;
        }
        return mAudioWavPath;
    }


    /**
     * 获取编码后的AMR格式音频文件路径
     *
     * @return
     */
    public static String getAMRFilePath() {
        String mAudioAMRPath = "";
        if (isSdcardExit()) {
            mAudioAMRPath = FILE_DIR + "/" + DateUtil.getTime(DateUtil.DATE_FORMMAT_STR_4) + AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    }


    /**
     * 获取文件大小
     *
     * @param path,文件的绝对路径
     * @return
     */
    public static long getFileSize(String path) {
        File mFile = new File(path);
        if (!mFile.exists())
            return -1;
        return mFile.length();
    }

    public static String getFileName(String path) {
        File mFile = new File(path);
        if (!mFile.exists())
            return null;
        return mFile.getName();
    }
}