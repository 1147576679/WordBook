package com.example.zjy.entity;

/**
 * Created by zjy on 2016/9/7.
 */
public class WordEntity {
    private int id;
    private String word;
    private String chinese;

    public WordEntity() {
    }

    public WordEntity(int id, String word, String chinese) {
        this.id = id;
        this.word = word;
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return "WordEntity{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", chinese='" + chinese + '\'' +
                '}';
    }
}
