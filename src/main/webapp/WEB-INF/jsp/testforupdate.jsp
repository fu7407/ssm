<%@ page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
	<title>test page</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
<script type="text/javascript">
	$(document).ready(function (){
		var max=30000;
		for(var i=0;i<max;i++){
			$.post({
				url:"<%=request.getContextPath()%>/userRedPacket/grapRedPacketForUpdate?redPacketId=2&userId="+i,
				success:function(result){}
			});
		}
	});
</script>
</head>
<body>
	<h2> test</h2>
</body>
</html>