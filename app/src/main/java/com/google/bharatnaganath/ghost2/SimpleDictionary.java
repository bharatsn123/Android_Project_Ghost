

package com.google.bharatnaganath.ghost2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;



public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    int flag=-1;
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String selected=null;
        Random rn=new Random();
        if(prefix.equals(""))

            selected = words.get(rn.nextInt(words.size()));
        else {Log.i("TAG","Else loop entered");
            int beg=0,end=words.size(),mid;
            String midString;
            while(beg<=end)
            {

                mid=(beg+end)/2;
                midString=words.get(mid);
                if(midString.startsWith(prefix))
                    return midString;
                else if(midString.compareTo(prefix)<0)
                    beg=mid+1;
                else
                    end=mid-1;
            }
            Log.i("TAG","Else loop exit");
        }
        Log.i("TAG",""+selected);
        return selected;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected="XYZ";
        ArrayList<String> wordscpy=new ArrayList<>();
        ArrayList<String> temp=new ArrayList<>();
        Random rn=new Random();
        if(prefix.equals("")) {
            selected = words.get(rn.nextInt(words.size()));
            flag=0;
        }else {Log.i("TAG","Else loop entered");

            wordscpy.addAll(words);
            int beg=0,end=wordscpy.size(),mid;
            String midString;
            while(beg<=end)
            {

                mid=(beg+end)/2;
                midString=wordscpy.get(mid);
                if(midString.startsWith(prefix)) {
                    temp.add(wordscpy.get(mid));
                    wordscpy.remove(mid);

                }else if(midString.compareTo(prefix)<0)
                    beg=mid+1;
                else
                    end=mid-1;
            }
            Log.i("TAG","Else loop exit");
        }
        if(temp.size()!=0){

            ArrayList<String> even=new ArrayList<>();
            ArrayList<String> odd= new ArrayList<>();
            for(String s:temp)
            {
                if(s.length()%2==0)
                    even.add(s);
                else
                    odd.add(s);
            }
            Log.i("TAG","ODD SIZE: "+odd.size());
            Log.i("TAG","EVEN SIZE: "+even.size());
            if(flag==-1&&odd.size()!=0)
            {
                Log.i("TAG","IF LOOP");
                selected=odd.get(rn.nextInt(odd.size()));

            }
            else if(flag==0&&even.size()!=0){
                Log.i("TAG","ELSE IF LOOP");
                selected=even.get(rn.nextInt(even.size()));
            }
            else
                selected=temp.get(rn.nextInt(temp.size()));


        }
        Log.i("TAG","SELECTED: "+selected);
        return selected;
    }

}
