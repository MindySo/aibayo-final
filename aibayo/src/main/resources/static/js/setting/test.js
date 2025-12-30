

$(document).ready(function(){
    let url = "https://e-childschoolinfo.moe.go.kr/api/notice/basicInfo.do";

    // API 키와 파라미터 설정
    let apiKey = "77e6a98ff03a4fa687221a817ce91948";
    let sidoCode = "27"; //나중에 선택 된 값으로 받아오기
    let sggCode = "27140"; // 나중에 선택된 값으로 받아오기

    // 쿼리 파라미터를 인코딩하여 추가
    let queryParams = `?${encodeURIComponent('key')}=${encodeURIComponent(apiKey)}`;
    queryParams += `&${encodeURIComponent('sidoCode')}=${encodeURIComponent(sidoCode)}`;
    queryParams += `&${encodeURIComponent('sggCode')}=${encodeURIComponent(sggCode)}`;

    let param = {
        key: apiKey,
        sidoCode: sidoCode,
        sggCode: sggCode
    }

    // 전체 URL
    let fullUrl = url + queryParams;

    // 콘솔에 URL 출력 (디버깅용)
    console.log(fullUrl);

    // AJAX 요청
    $.ajax({
        url: fullUrl,
        async: true,
        crossDomain: true,
        type: "POST", // GET 메서드로 수정
        // dataType: "json",
        data:param,
        success: function(result) {
            let resultTable = "<table style='border:1px solid #5b5b5b; padding:1px; margin:1px;'>";
            resultTable += "<tr>";

            // 헤더 생성
            $.each(result.kinderInfo[0], function(key) {
                resultTable += "<td style='border:1px solid; color:black;background-color:white;'>";
                resultTable += key;
                resultTable += "</td>";
            });
            resultTable += "</tr>";

            // 데이터 행 생성
            $.each(result.kinderInfo, function(key, row) {
                resultTable += "<tr>";
                $.each(result.kinderInfo[0], function(key) {
                    resultTable += "<td style='border:1px solid; color:black;background-color:white;'>";
                    resultTable += row[key]; // 수정된 부분: key를 사용하여 값을 가져옵니다.
                    resultTable += "</td>";
                });
                resultTable += "</tr>";
            });
            resultTable += "</table>";

            // 결과를 페이지에 추가
            $("body").append(resultTable);
        },
        error: function(xhr, status, error) {
            console.error("AJAX 요청 오류:", status, error);
            alert("데이터를 가져오는 데 실패했습니다. 콘솔을 확인하세요.");
        }
    });
});
