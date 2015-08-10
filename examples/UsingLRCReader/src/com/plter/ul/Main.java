package com.plter.ul;

import com.plter.lrceditor.lrc.LRCReader;

import java.io.*;

/**
 * Created by plter on 7/28/15.
 */
public class Main {

    public static void main(String[] args){
        File f = new File("sound.lrc");
        String lrc = null;
        LRCReader reader = null;
        try {
            FileInputStream in = new FileInputStream(f);
            reader = new LRCReader(new InputStreamReader(in,"UTF-8"));
            in.close();

            System.out.println(reader.getLrcMap());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
