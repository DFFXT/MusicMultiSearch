package com.simple.tools;

import com.simple.bean.Lyrics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricsAnalysis {
    //[00:00.00]
    public ArrayList<Lyrics> lyricsList = new ArrayList<>();
    private String lyrics;
    private static int timeLength = 10;
    private static Pattern pattern=Pattern.compile("\\[\\d\\d:\\d\\d.\\d{2,3}]");

    public LyricsAnalysis(String lyrics) {
        this.lyrics = lyrics;
        Matcher m = pattern.matcher(lyrics);
        int preStart = -1;
        int start;
        while (m.find()) {
            start = m.start();
            timeLength = m.end() - m.start();
            add(preStart, start);
            preStart = start;
        }
        if (preStart >= 0) {//**添加最后一行歌词或只有一行歌词
            add(preStart, lyrics.length());
        }
    }

    /**
     * 添加一行歌词 [00:00.00]xxxxxx
     *
     * @param preStart 开始处
     * @param end      结束处
     */
    private void add(int preStart, int end) {
        int minute = 0;
        int second = 0;
        int msec = 0;
        if (preStart < 0) return;
        try {
            minute = Integer.parseInt(lyrics.substring(preStart + 1, preStart + 3));
            second = Integer.parseInt(lyrics.substring(preStart + 4, preStart + 6));
            msec = Integer.parseInt(lyrics.substring(preStart + 7, preStart + 8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //***事件 毫秒为单位
        Lyrics line = new Lyrics(
                (minute * 600 + second * 10 + msec) * 100,
                lyrics.substring(preStart + timeLength, end).trim());


        this.lyricsList.add(line);
    }
}
