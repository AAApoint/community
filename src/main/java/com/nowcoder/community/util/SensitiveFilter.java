package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ){
                String word;
                while((word = br.readLine()) != null){
                    this.addKeyWord(word);
                }
            }catch (IOException e){
                logger.error("读取敏感词文件错误！" + e.getMessage());
            }
    }

    private void addKeyWord(String word){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            TrieNode subNode = tempNode.getSubTrieNode(c);

            if(subNode == null){
                subNode = new TrieNode();
                tempNode.addSubTrieNode(c, subNode);
            }

            tempNode = subNode;
            if(i == word.length() - 1){
                tempNode.setWordEnd(true);
            }
        }
    }

    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while(begin < text.length()){
            if(position < text.length()){
                Character c = text.charAt(position);

                // 跳过符号
                if(isSymbol(c)){
                    if(tempNode == rootNode){
                        sb.append(c);
                        begin++;
                    }
                    position++;
                    continue;
                }

                // 检查下级节点
                tempNode = tempNode.getSubTrieNode(c);
                if(tempNode == null){
                    sb.append(text.charAt(begin));
                    position = ++begin;
                    tempNode = rootNode;
                }else if(tempNode.isWordEnd()){
                    StringBuilder replace = new StringBuilder();
                    for(int i = 0; i < position - begin + 1; i++){
                        replace.append("*");
                    }
                    sb.append(replace);
                    begin = ++position;
                }else{
                    position++;
                }
            }else{
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }
        }

        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode{
        // 标记敏感词的结尾
        private boolean isWordEnd = false;
        // 子节点
        private Map<Character, TrieNode> subTrieNodes = new HashMap<>();

        public boolean isWordEnd() {
            return isWordEnd;
        }

        public void setWordEnd(boolean wordEnd) {
            isWordEnd = wordEnd;
        }

        public void addSubTrieNode(Character c, TrieNode node){
            subTrieNodes.put(c, node);
        }

        public TrieNode getSubTrieNode(Character c){
            return subTrieNodes.get(c);
        }
    }
}
