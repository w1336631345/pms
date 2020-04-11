<html>
<head>
    <script language="javascript" type="text/javascript">
        window.onload =function() {
            var data = {};
            data.status='${status}';
            <#if status==0>
                data.status = '${token}';
            </#if>
            <#if status == 1>
                    data.openId = '${openId}';
             </#if>
            window.parent.postMessage(data,'*');
        };
    </script>
</head>
<body>
</body>
</html>