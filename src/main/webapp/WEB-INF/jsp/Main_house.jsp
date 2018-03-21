<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="bean.article" import ="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="user-scalable=0" />
<title>小鹿开门</title>
<link href="css/main.css" type="text/css" rel="stylesheet"> 
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

#nav {
    border:2px solid #858585;
	height:100px;
	margin: 0px auto 0px auto;
	background: #ffffff;
	color: #ffffff;
}

#nav li {
    
	float: left;
	display: inline;
}

#nav li a {
	display: block;
	color: #000000;
	padding: 10px 30px;
	font-size: 40px;
	text-decoration: none;
}
</style>
</head>
<body>
<div class="all">
<div class="title">
	<form action="search" method="post">
	<hr class="hr15">
    <input name="" type="submit" value="搜索" class="button" />
    <input name="search" type="text" class="input-box" />
    </form>
</div>
<div id="nav">
     <hr class="hr10">
     <ul>
			<li><a href="Main_recommend?" >推荐</a></li>
			<li><a href="Main_house?" style="font-weight: bold;">房产</a></li>
			<li><a href="Main_finance?">金融</a></li>
			<li><a href="Main_entertainment?">文娱</a></li>
			<li><a href="Main_car?">汽车</a></li>
			<li><a href="Main_travel?">旅游</a></li>
	 </ul>
	 <div style="clear: both"></div>
</div>
<div class="content">
    <% 
    String user = (String)session.getAttribute("channel");
    ArrayList<article> al=(ArrayList)session.getAttribute("content");
    boolean ad=true;
    for(int i=0;i<al.size();i++){
    	if(i==1&&ad){%>
    		 <a href="http://www.2345.com" style="color:#000000">
    		 <div class="article" style="border:2px solid #EEEEE0;">
    		     <div style="margin:10px 10px 10px 10px;height:260px;background: url(./images/ad.jpg);background-size: 100% 100%"></div>
    		 </div>
    		 </a>
    		 
    	<% ad=false;
    	i--;
    	}else{
    %>
    <a href="article?get_time=<%=al.get(i).getGet_time()%>" style="color:#000000">
	<div class="article" style="border:2px solid #EEEEE0;">
		<div class="left"  style="float:left ;  width:70%;  height:100%; background-color:#ffffff">
		       <div  style="width:100% ;height:80%;border:none;font-size:60px;padding:20px 0 0 10px"><%=al.get(i).getTitle() %> </div>
		       <div  style="width:100% ;height:20%;border:none;font-size:40px;padding:0 0 20px 10px"><%=al.get(i).getPublish_time()+"   "+al.get(i).getAuthor() %> </div>
		</div>
		<div class="right" style="float:left ;  width:30%; height:280px;">
		   <div style="margin:10px 10px 10px 10px;height:260px;background: url(./images/<%=al.get(i).getGet_time() %>/cover.jpg);background-size: 100% 100%"></div>
		</div>
	</div>
	</a>
	<%} }%>
</div>

</div>

</body>
</html>