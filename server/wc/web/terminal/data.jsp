<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%

	String comm = request.getParameter("comm");
	String data1 = "";
	String data2 = "";
	
	
	if(comm == null)
		System.out.println("comm = null");
	else
		System.out.println("comm = "+ comm);
	
	if(comm != null){
		if(comm.equals("send")){
			data1 = request.getParameter("data1");
			data2 = request.getParameter("data2");
			System.out.println(data1+" "+data2);	
		}else if(comm.equals("recieve")){
			out.println("send from web" + data1+", "+ data2);
			data1 = "not used";
			data2 = "not used";
		}
	}
	//JScript도 Thread 사용하기
%>