package com.plter.lrceditor.lrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by plter on 7/28/15.
 */
public class LRCReader {

    public LRCReader(String lrc){
        this(new StringReader(lrc));
    }

    public LRCReader(Reader lrc){
        BufferedReader br = new BufferedReader(lrc);

        String line = null;
        String content = null;
        int contentStartIndex = -1;
        int timeTag;
        while (true){
            try {
                line = br.readLine();

                if (line!=null){
                    contentStartIndex = line.lastIndexOf(']');
                    if (contentStartIndex>-1){
                        contentStartIndex++;
                        content = line.substring(contentStartIndex);

                        Matcher timeTagMatcher = timeTagPattern.matcher(line);
                        while (timeTagMatcher.find()){
                            timeTag = Integer.parseInt(timeTagMatcher.group(1))*60+Integer.parseInt(timeTagMatcher.group(2));
                            lrcMap.put(timeTag,content);
                        }
                    }
                }else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 根据时间获取歌词内容
     * @param timeTag 精确到秒
     * @return 歌词内容
     */
    public String getContent(int timeTag){
        return lrcMap.get(timeTag);
    }

    public Map<Integer, String> getLrcMap() {
        return lrcMap;
    }

    private Map<Integer, String> lrcMap = new HashMap<Integer, String>();
    private final Pattern timeTagPattern = Pattern.compile("\\[(\\d{2}):(\\d{2})");
}
