<html>
<head>
    <script language="javascript" type="text/javascript">
        window.onload =function() {
            var data = {};
            data.status='${status}';
            <#if token??> data.token = '${token}'; </#if>
            <#if openId??> data.openId = '${openId}'; </#if>
            <#if unionId??> data.unionId = '${unionId}'; </#if>
            <#if nickName??> data.nickName = '${nickName}'; </#if>
            <#if msg??> data.msg = '${msg}'; </#if>
            window.parent.postMessage(data,'*');
        };
    </script>
</head>
<body>
</body>
</html>