var url = "https://e-childschoolinfo.moe.go.kr/api/notice/basicInfo.do?";
var apiKey = "key=APIKEY";
var param = "sidoCode=11&sggCode=11140";
var resultTable;
$(function(){
    $.ajax({
        url: url + apiKey + param,
        async: true,
        crossDomain: true,
        type: "post",
        dataType: "json",
        success : function(result){
            resultTable = "<table style='border:1px solid #b5b5b5; padding:1px; margin:1px;'>";
            resultTable += "<tr>";
            $.each(result.kinderInfo[0],function(key,row){
                resultTable += "<td style='border:1px solid; color:black;background-color:white;'>";
                resultTable += key;
                resultTable += "</td>";
            });
            resultTable += "</tr>";
            $.each(result.kinderInfo,function(key,row){
                resultTable += "<tr>";
                for(var value in row){
                    resultTable += "<td style='border:1px solid; color:black;background-color:white;'>";
                    resultTable += row[value];
                    resultTable += "</td>";
                }
                resultTable += "</tr>";
            });
            resultTable += "</table>";
            $("body").append(resultTable);
        },
        error : function(request, status){
            alert(status);
        }
    });
});
