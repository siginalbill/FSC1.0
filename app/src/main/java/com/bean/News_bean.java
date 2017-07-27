package com.bean;

/**
 * Created by hxy on 2017/7/27.
 */


public class News_bean {
    private Long news_id;
    private String news_title;
    private String news_content;
    private String news_date;
    private String news_img;

    public News_bean(Long news_id,String news_content, String news_title, String news_date) {
        this.news_id =news_id;
        this.news_content=news_content;
        this.news_title = news_title;
        this.news_date = news_date;
        this.news_img = news_img;
    }
    public Long getNews_id() {
        return news_id;
    }
    public void setNews_id(Long news_id) {
        this.news_id = news_id;
    }
    public String getNews_title() {
        return news_title;
    }
    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }
    public  String getNews_content(){
        return news_content;
    }
    public void setNews_content(){
        this.news_content=news_content;
    }
    public String getNews_date() {
        return news_date;
    }
    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }
    public String getNews_img() {
        return news_img;
    }
    public void setNews_img(String news_date) {
        this.news_img = news_img;
    }
}