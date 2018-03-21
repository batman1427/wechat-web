package dao;

import java.util.ArrayList;

import bean.article;

public interface UserDao {
	//全部文章
	public  ArrayList<article> getall(String channel);
	public String getarticle(String get_time,String ip) throws Exception;
	public  ArrayList<article> getsearch(String key);
	public  ArrayList<article> getRecommend(String ip);
	

}
