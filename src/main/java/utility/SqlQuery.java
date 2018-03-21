package utility;

public class SqlQuery {
	public static final String getbiz = "select * from mpaccount  where type= ?";
	public static final String getall = "select * from article  where is_fail =  ? and biz= ?";
	public static final String getsearch = "select * from article  where is_fail =  ? ";
    public static final String getarticle = "select * from article  where get_time =  ? ";
    public static final String updateread = "update article set `read`= ? where get_time = ? ";
    public static final String getuser = "select * from user  where ip= ?";
    public static final String adduser = "insert into user values(?,?,?,?,?,?,?)";
    public static final String gettype = "select * from mpaccount  where biz= ?";
    public static final String updatelast = "update user set lastvisit = ? where ip = ? ";
}
