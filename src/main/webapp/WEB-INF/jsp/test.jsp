<%@ page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
	<title>test page</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
<script type="text/javascript">
	var max=10000;
	function grapRedPacket(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacket?redPacketId=1&userId="+i,
				success:function(result){}
			});
		}
	}
	function grapRedPacketForUpdate(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketForUpdate?redPacketId=2&userId="+i,
				success:function(result){}
			});
		}
	}
	function grapRedPacketForVersion(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketForVersion?redPacketId=3&userId="+i,
				success:function(result){}
			});
		}
	}
	function grapRedPacketForVersion2(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketForVersion2?redPacketId=4&userId="+i,
				success:function(result){}
			});
		}
	}
	function grapRedPacketForVersion3(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketForVersion3?redPacketId=5&userId="+i,
				success:function(result){}
			});
		}
	}
	function grapRedPacketByRedis(){
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketByRedis?redPacketId=6&userId="+i,
				success:function(result){}
			});
		}
	}
	function initRedis(){
		$.post({
			url:"<%=request.getContextPath()%>/userRedPacket/init?redPacketId=6",
			success:function(result){}
		});
	}
</script>
</head>
<body>
	<h2> test</h2>
	<button onclick="grapRedPacket()">grapRedPacket(普通方式，高并发有超发现象)</button><br>
	<button onclick="grapRedPacketForUpdate()">grapRedPacketForUpdate(数据库锁表，悲观锁方式，性能下降，速度慢)</button><br>
	<button onclick="grapRedPacketForVersion()">grapRedPacketForVersion(加版本号，乐观锁，时间跟普通方式差不多，存在大量失败请求)</button><br>
	<button onclick="grapRedPacketForVersion2()">grapRedPacketForVersion2(加版本号，乐观锁重入机制，失败后重抢红包，用时间戳方式来判断退出)</button><br>
	<button onclick="grapRedPacketForVersion3()">grapRedPacketForVersion3(加版本号，乐观锁重入机制，失败后重抢红包，设置重试次数来判断退出)</button><br>
	<button onclick="grapRedPacketByRedis()">grapRedPacketByRedis(先放入redis中，抢完再异步批量保存到数据库)</button><button onclick="initRedis()">初始化redis缓存</button>
</body>
</html>