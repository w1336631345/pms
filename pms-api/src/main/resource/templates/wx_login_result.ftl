<html>
<head>
    <script language="javascript" type="text/javascript">
        window.onload =function() {
            var data = {};
            data.status='${status}';
            <#if token??> data.token = '${token}'; </#if>
            <#if openId??> data.openId = '${openId}'; </#if>
            <#if unionId??> data.unionId = '${unionId}'; </#if>
            <#if msg??> data.msg = '${msg}'; </#if>
            <#if hotelCode??> data.hotelCode = '${hotelCode}'; </#if>
            <#if hotelCode1??> data.hotelCode1 = '${hotelCode1}'; </#if>
            <#if user??> data.user = '${user}'; </#if>
            window.parent.postMessage(data,'*');
        };
    </script>
</head>
<body>
</body>
</html>