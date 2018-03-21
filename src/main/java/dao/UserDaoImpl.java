package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bean.article;
import bean.weight;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ArrayList<article> getall(String channel) {
		// TODO Auto-generated method stub
		ArrayList<article> al=new ArrayList<article>();
		Object args[] = new Object[]{channel};
		List<Map<String,Object>> result = jdbcTemplate.queryForList(utility.SqlQuery.getbiz,args);
		for(int i=0;i<result.size();i++) {
			String tempbiz=(String) result.get(i).get("biz");
			Object arg[] = new Object[]{"1",tempbiz};
			List<Map<String,Object>> temp = jdbcTemplate.queryForList(utility.SqlQuery.getall,arg);
			for(int j=0;j<temp.size();j++) {
				int id=(Integer) temp.get(j).get("id");
				String biz=(String) temp.get(j).get("biz");;
				int read=(Integer) temp.get(j).get("read");
				int like=(Integer) temp.get(j).get("like");	
			    String publish_time=(String) temp.get(j).get("publish_time");
			    String get_time=(String) temp.get(j).get("get_time");
			    String title=(String) temp.get(j).get("title");
				String get_url=(String) temp.get(j).get("get_url");	
				String source_url=(String) temp.get(j).get("source_url");
				String summary=(String) temp.get(j).get("summary");
				int is_fail=(Integer) temp.get(j).get("is_fail");
			    String author=(String) temp.get(j).get("author");
			    article ar=new article(id,biz,read,like,publish_time,get_time,title,get_url,source_url,summary,is_fail,author);
			    al.add(ar);
			}
		}
		
		return al;
	}
	
	public String getarticle(String get_time,String ip) throws Exception {
		// TODO Auto-generated method stub
		File html=new File("g:/article/"+get_time+".html");
		Document doc=Jsoup.parse(html,"UTF-8");
		String result=doc.toString();
		while(result.contains("</script>")) {
			result=searchIndex(result);
		}
		result=result.replaceAll("data-src", "src");
		result=result.replaceAll("https://mmbiz.qpic.cn/mmbiz_gif/", "./images/"+get_time+"/");
		result=result.replaceAll("https://mmbiz.qpic.cn/mmbiz_png/", "./images/"+get_time+"/");
		result=result.replaceAll("https://mmbiz.qpic.cn/mmbiz_jpg/", "./images/"+get_time+"/");
		result=result.replaceAll("https://mmbiz.qpic.cn/mmbiz_jpeg/", "./images/"+get_time+"/");
		result=result.replaceAll("https://mmbiz.qpic.cn/mmbiz/", "./images/"+get_time+"/");
		result=result.replaceAll("http://mmbiz.qpic.cn/mmbiz/", "./images/"+get_time+"/");
		result=result.replaceAll("http://mmbiz.qpic.cn/mmbiz_gif/", "./images/"+get_time+"/");
		result=result.replaceAll("http://mmbiz.qpic.cn/mmbiz_png/", "./images/"+get_time+"/");
		result=result.replaceAll("http://mmbiz.qpic.cn/mmbiz_jpg/", "./images/"+get_time+"/");
		result=result.replaceAll("http://mmbiz.qpic.cn/mmbiz_jpeg/", "./images/"+get_time+"/");
		result=result.replaceAll("jpeg", "jpg");
		result=result.replaceAll("jpeg", "jpg");
		result=result.replaceAll("\\/\\/res.wx.qq.com\\/mmbizwap\\/zh_CN\\/htmledition\\/images\\/icon\\/common\\/favicon22c41b.ico", "");
		result=result.replaceAll("\\/640\\?wx_fmt=", ".");
		result=result.replaceAll("\\/640\\?", ".jpg");
		result=result.replaceAll("\\/640.png\\?", ".png");
		result=result.replaceAll("\\/0\\?wx_fmt=", ".");
		result=result.replaceAll("阅读原文", "");
		//增加文章的阅读量   更新用户信息
		update(get_time,ip);
		//System.out.println(result);//完整的html字符串
		return result;
	}
	
	private static String searchIndex(String str) {
		//System.out.println(1);
        int a = str.indexOf("<script");
        int b = str.indexOf("</script>")+9;
        str=str.substring(0, a)+str.substring(b);
		return str;
    }
	
	private void update(String get_time,String ip) {
		//文章阅读量加1
		Object arg[] = new Object[]{get_time};
		List<Map<String,Object>> temp = jdbcTemplate.queryForList(utility.SqlQuery.getarticle,arg);
		int read=(Integer) temp.get(0).get("read")+1;
		String biz=(String) temp.get(0).get("biz");
		//System.out.println(biz);
		Object readupdate[] = new Object[]{read,get_time};
		jdbcTemplate.update(utility.SqlQuery.updateread,readupdate);
		Object type[] = new Object[]{biz};
		List<Map<String,Object>> typetemp = jdbcTemplate.queryForList(utility.SqlQuery.gettype,type);
		//System.out.println(typetemp.size());
		//System.out.println(typetemp.get(0).get("type"));
		String user_type=(String) typetemp.get(0).get("type");
		//更新用户信息
		Object userdetail[] = new Object[]{user_type,ip};
		jdbcTemplate.update(utility.SqlQuery.updatelast,userdetail);
		updateweight(user_type,ip);
    }
	
	private void updateweight(String type,String ip) {
		//用户权重加1
		Object arg[] = new Object[]{ip};
		//System.out.println(ip);
		//System.out.println(type);
		List<Map<String,Object>> temp = jdbcTemplate.queryForList(utility.SqlQuery.getuser,arg);
		int num=(Integer) temp.get(0).get(type)+1;
		
		Object weight[] = new Object[]{num,ip};
		jdbcTemplate.update("update user set "+type+" = ? where ip = ? ",weight);
		

    }
	
	//搜索文章关键字
	public ArrayList<article> getsearch(String key) {
			ArrayList<article> al=new ArrayList<article>();
	        Object arg[] = new Object[]{"1"};
			List<Map<String,Object>> temp = jdbcTemplate.queryForList(utility.SqlQuery.getsearch,arg);
			for(int j=0;j<temp.size();j++) {
				//System.out.println(key);
				//System.out.println(((String) temp.get(j).get("title")));
				if(((String) temp.get(j).get("title")).contains(key)) {
					int id=(Integer) temp.get(j).get("id");
					String biz=(String) temp.get(j).get("biz");;
					int read=(Integer) temp.get(j).get("read");
					int like=(Integer) temp.get(j).get("like");	
					String publish_time=(String) temp.get(j).get("publish_time");
					String get_time=(String) temp.get(j).get("get_time");
					String title=(String) temp.get(j).get("title");
					String get_url=(String) temp.get(j).get("get_url");	
					String source_url=(String) temp.get(j).get("source_url");
					String summary=(String) temp.get(j).get("summary");
					int is_fail=(Integer) temp.get(j).get("is_fail");
					String author=(String) temp.get(j).get("author");
					article ar=new article(id,biz,read,like,publish_time,get_time,title,get_url,source_url,summary,is_fail,author);
					al.add(ar);
					//System.out.println(al.size());
				}
			}
		
		
		return al;
	}

	public ArrayList<article> getRecommend(String userip) {
		  ArrayList<article> result=new ArrayList<article>();
		//判断是否为新用户
		Object arg[] = new Object[]{userip};
	    List<Map<String,Object>> temp = jdbcTemplate.queryForList(utility.SqlQuery.getuser,arg);
	    //System.out.println(temp.size());
	    if(temp.size()==0) {
	    	 // System.out.println("新用户");
	    	//新用户   返回房产和文娱最新
	    	Object newuser[] = new Object[]{userip,0,0,0,0,0,"无"};
		    jdbcTemplate.update(utility.SqlQuery.adduser,newuser);
		    ArrayList<article> mix=new ArrayList<article>();
		    ArrayList<article> house=getall("house");
		    ArrayList<article> entertainment=getall("entertainment");
		    house.addAll(entertainment);
		    mix=sort_time(house);
		    return mix;
	    }else {
	    	 // System.out.println("老用户");
	    	String ip=(String) temp.get(0).get("ip");
	    	int house=(Integer) temp.get(0).get("house");
	    	int finance=(Integer) temp.get(0).get("finance");
	    	int entertainment=(Integer) temp.get(0).get("entertainment");
	    	int car=(Integer) temp.get(0).get("car");
	    	int travel=(Integer) temp.get(0).get("travel");
	    	weight w1=new weight(house,"house");
	    	weight w2=new weight(finance,"finance");
	    	weight w3=new weight(entertainment,"entertainment");
	    	weight w4=new weight(car,"car");
	    	weight w5=new weight(travel,"travel");
	    	ArrayList<weight> w=new ArrayList<weight>();
	    	w.add(w1);
	    	w.add(w2);
	    	w.add(w3);
	    	w.add(w4);
	    	w.add(w5);
	    	w=sort_num(w);
	    	//推荐内容为    权重最大（最新 最热）  上次访问（最新） 权重第二（最新最热）权重第三......
			String lastvisit=(String) temp.get(0).get("lastvisit");
			//第一部分
			ArrayList<article> part1=sort_common(sort_time(getall(w.get(0).getType())));
			 // System.out.println("第一部分");
			//System.out.println(part1.size());
			if(part1.size()>2) {
				result.add(part1.get(0));
				result.add(part1.get(1));
			}else {
				result.addAll(part1);
			}
			//上次访问最新
			ArrayList<article> last=sort_time(getall(lastvisit));
			if(lastvisit.equals(w.get(0).getType())) {
				//已经出现在第一部分
			}else {
				if(last.size()>0) {
				    result.add(last.get(0));
				}
			}
			 //System.out.println("第二部分");
				//System.out.println(last.size());
			//其他部分
			for(int i=1;i<w.size();i++) {
				 //System.out.println("第三部分");
				if(!w.get(i).getType().equals(lastvisit)) {
					ArrayList<article> parttemp=sort_common(sort_time(getall(w.get(i).getType())));
					if(parttemp.size()>2) {
						result.add(parttemp.get(0));
						result.add(parttemp.get(1));
					}else {
						result.addAll(parttemp);
					}
				}else {
					ArrayList<article> parttemp=last;
					if(last.size()>1) {
					    result.add(last.get(1));
					}
				}
			}
			
	    }
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
    
	//普通频道最新最热为前两条
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
	
	
	public static ArrayList<weight> sort_num(ArrayList<weight> al) {
        Collections.sort(al, new Comparator<weight>(){  
        	
            public int compare(weight o1, weight o2) {  
              
             
            	int anum=o1.getNum();
            	int bnum=o2.getNum();
                if(anum<bnum){  
                    return 1;  
                }  
                if(anum==bnum){  
                    return 0;  
                }  
                return -1;  
            }  
        }); 
		return al;
		
	}

}
