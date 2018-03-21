package service;

import java.util.ArrayList;

import bean.*;

public interface UserService {
	//获取文章列表
	public ArrayList<article> getall(String channel);
	public String getarticle(String get_time,String ip) throws Exception;
	public ArrayList<article> getsearch(String key) throws Exception;
	public ArrayList<article> getRecommend(String ip);
	

}
