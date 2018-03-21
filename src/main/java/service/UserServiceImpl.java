package service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UserDao;
import bean.article;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	
	//获取全部文章
	public ArrayList<article> getall(String channel) {
		// TODO Auto-generated method stub
		ArrayList<article> al=userDao.getall(channel);
		/*for(int i=0;i<al.size();i++) {
			System.out.println(al.get(i).getId());
			System.out.println(al.get(i).getTitle());
			System.out.println(al.get(i).getAuthor());
			System.out.println(al.get(i).getTime());
			System.out.println(al.get(i).getCover());
		}*/
		ArrayList<article> result=sort_common(sort_time(al));
		/*for(int i=0;i<result.size();i++) {
			System.out.println(result.get(i).getId());
			System.out.println(result.get(i).getTitle());
			System.out.println(result.get(i).getAuthor());
			System.out.println(result.get(i).getTime());
			System.out.println(result.get(i).getCover());
		}*/
		return result;
	}
    
	public static ArrayList<article> sort_time(ArrayList<article> al) {
        Collections.sort(al, new Comparator<article>(){  
        	
            public int compare(article o1, article o2) {  
              
                //时间排序 
            	String a=o1.getPublish_time();
            	String b=o2.getPublish_time();
            	int ayear=Integer.valueOf(a.split("-")[0]);
            	int amonth=Integer.valueOf(a.split("-")[1]);
            	int aday=Integer.valueOf(a.split("-")[2]);
            	int byear=Integer.valueOf(b.split("-")[0]);
            	int bmonth=Integer.valueOf(b.split("-")[1]);
            	int bday=Integer.valueOf(b.split("-")[2]);
                if(ayear<byear||(ayear==byear&&amonth<bmonth)||(ayear==byear&&amonth==bmonth&&aday<bday)){  
                    return 1;  
                }  
                if(ayear==byear&&amonth==bmonth&&aday==bday){  
                    return 0;  
                }  
                return -1;  
            }  
        }); 
		return al;
		
	}
	
	//普通频道的排序是最新-最热-最新-最新-最新......
	public static ArrayList<article> sort_common(ArrayList<article> al) {
		ArrayList<article> result=new ArrayList<article>();
        int max=0;
        int temp=0;
        for(int i=0;i<al.size();i++) {
        	if(Integer.valueOf((al.get(i).getRead()))>max) {
        		max=Integer.valueOf((al.get(i).getRead()));
        		temp=i;
        	}
        }
        if(temp==0) {
        	return al;
        }else {
        	article ar=al.get(temp);
        	result.add(al.get(0));
        	result.add(ar);
        	al.remove(temp);
        	al.remove(0);
        	for(int i=0;i<al.size();i++) {
        		result.add(al.get(i));
        	}
        }
		return result;
		
	}
	
	
	//
	public String getarticle(String get_time,String ip) throws Exception {
		// TODO Auto-generated method stub
		return userDao.getarticle(get_time,ip);
	}
	
	//搜索的文章
	public ArrayList<article> getsearch(String key) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<article> al=userDao.getsearch(key);
		ArrayList<article> result=sort_time(al);
		return result;
	}

	public ArrayList<article> getRecommend(String ip) {
		ArrayList<article> al=userDao.getRecommend(ip);
		return al;
	}
	
	
	
	
	


}
