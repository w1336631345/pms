<html>
<head>
    <script language="javascript" type="text/javascript">
        window.onload =function() {
            var data = {};
            var hotelList = [];
            var hotel = {};
            data.status='${status}';
            <#if token??> data.token = '${token}'; </#if>
            <#if openId??> data.openId = '${openId}'; </#if>
            <#if unionId??> data.unionId = '${unionId}'; </#if>
            <#if msg??> data.msg = '${msg}'; </#if>
            <#if hotelList??>
                <#list hotelList as item>
                    hotel.id = '${item.id}';
                    hotel.hotelCode = '${item.hotel_code}';
                    hotel.name = '${item.name}';
                    hotelList.push(hotel);
                </#list>
                data.hotelList = hotelList;
            </#if>
            window.parent.postMessage(data,'*');
        };
    </script>
</head>
<body>
</body>
</html>