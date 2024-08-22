// textarea 사이즈 자동조절
function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// textarea 글자수 제한
function chkword(obj, maxWord) {

    var strValue = obj.value;
    var strLen = strValue.length;
    var totalByte = 0;
    var len = 0;
    var oneChar = "";
    var str2 = "";

    for (var i = 0; i < strLen; i++) {
        oneChar = strValue.charAt(i);
        if (escape(oneChar).length > 4) {
            totalByte += 2;
        } else {
            totalByte++;
        }

        // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
        if (totalByte <= maxWord) {
            // len = i + 1;
            len = maxWord;
        }
    }

    // 넘어가는 글자는 자른다.
    if (strLen > maxWord) {
        alert(maxWord + "자를 초과 입력 할 수 없습니다.");
        str2 = strValue.substr(0, len);
        obj.value = str2;
        strLen = maxWord
        chkword(obj, maxWord);
    }

    $('#textcnt').html("( " + strLen + " / " + maxWord + " )");
}

// 체크 개수 세기
function checkCnt(){
    var checked = $("input[class='form-check-input']:checked").length;

    if($("#checkAll").is(":checked")){
        checked--
    }

    if ($("input[class='form-check-input']").length - 1 != checked){
        $("input[id='checkAll']").prop("checked", false)
    }

    document.getElementById('checkboxCnt').innerText
        = '(' + checked + '개)';
}

var mapLng = null;
var mapLat = null;

function hideMap(){
    $(".set-loc").show();
    $(".map_wrap").hide();
}

function showMap(){
    $(".set-loc").hide();
    $(".map_wrap").show();
    map.relayout();
    map.setCenter(new kakao.maps.LatLng(mapLat, mapLng));
}

function hideOrShowMap(){
    var ifLatlngExist = typeof $(".input-loc").attr('data-map-lat') !== 'undefined';
    if (ifLatlngExist) {
        mapLat = $(".input-loc").attr('data-map-lat');
        mapLng = $(".input-loc").attr('data-map-lng');
        showMap();
    } else{
        hideMap();
    }
}

$(document).ready(function() {

    // modify에서 기존 classList 받아와서 체크하기
    if($('#checked_class_div')){
        var originClass = $('.origin_class').map(function() {
                return this.id;
        }).get();

        console.log(originClass);

        if(originClass.includes('0')){
            $("input[class='form-check-input']").prop("checked", true);
        }else{
            $("input[class='form-check-input']").each(function() {
                if (originClass.includes(this.id)) {
                    $(this).prop("checked", true);
                }
            });
        }

        checkCnt();
    }

    $(".set-loc").click(function () {
        showMap();
    });

    $(".cancel-loc").click(function (){
        hideMap();
    })

    // checkbox 전체 선택 + 체크된 개수 표시
    $("#checkAll").click(function() {
        if($("#checkAll").is(":checked")) $("input[class='form-check-input']").prop("checked", true);
        else $("input[class='form-check-input']").prop("checked", false);
    });

    $(".form-check-input").click(function() {
        checkCnt();
    });

    // startDate 오늘 이후로 설정 + endDate보다 뒤일 시 endDate = null
    $('.startDate').on('change', function () {
        var startDate = new Date($('.startDate').val()).setHours(0, 0, 0, 0);
        // console.log(startDate);
        var today = new Date().setHours(0, 0, 0, 0);
        // console.log(today);
        if(startDate <= today){
            Swal.fire({
                text: "오늘 이후의 일정만 추가할 수 있습니다.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.startDate').val(null);
            });
        }

        var endDate = new Date($('.endDate').val()).setHours(0, 0, 0, 0);
        // console.log(endDate);
        if(endDate < startDate){
            $('.endDate').val(null);
        }
    });

    // endDate 오늘 이후로 설정
    $('.endDate').on('change', function () {
        var endDate = new Date($('.endDate').val()).setHours(0, 0, 0, 0);
        // console.log(endDate);
        var startDate = new Date($('.startDate').val()).setHours(0, 0, 0, 0);
        // console.log(startDate);
        if(endDate < startDate){
            Swal.fire({
                text: "종료일은 시작일 이후로 설정할 수 있습니다.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.endDate').val(null);
            });
        }
    });

    $(document).on('click', '.button-submit', function () {

        if(!$('.startDate').val()){
            Swal.fire({
                text: "시작일을 입력해주세요.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.startDate').focus();
            });
        }else if($('.form-check-input:checked').length === 0){
            Swal.fire({
                text: "1개 이상의 반을 선택해 주세요.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.form-check-input').focus();
            });
        }else if(!$('.default-wrapper').val()){
            Swal.fire({
                text: "제목을 입력해주세요.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.default-wrapper').focus();
            });
        }else if(!$('.calendar-textarea').val()){
            Swal.fire({
                text: "내용을 입력해주세요.",
                icon: "warning",
                confirmButtonColor: "#dc3545",
                confirmButtonText: "확인",

            }).then((result) => {
                $('.calendar-textarea').focus();
            });
        }else{
            var startDate = $('.startDate').val()
                    .replace('.','-').replace('.','-')
                + 'T00:00';
            // console.log(startDate);

            var endDate = $('.endDate').val();
            if(endDate === ''){
                endDate = startDate;
            }else{
                endDate = endDate.replace('.','-').replace('.','-') + 'T00:00'
            }
            // console.log(endDate);

            if($('.map_wrap').css("display") === "none"){
                mapLat = null;
                mapLng = null;
            }

            var classList = $('.form-check-input:checked').map(function() {
                if(this.id !== 'checkAll')
                    return this.id;
            }).get();

            if(classList.length === $("input[class='form-check-input']").length - 1){
                var classList = []; // 배열 초기화 (비우기)
                classList.push(0);
            }

            // 추가 수정 구분해서 처리
            if(!$('#checked_class_div').length){    // 추가

                let param = {
                    startDate: startDate,
                    endDate: endDate,
                    classList : classList,
                    boardTitle : $('.default-wrapper').val(),
                    boardContents : $('.calendar-textarea').val(),
                    mapLng : mapLng,
                    mapLat : mapLat
                };
                console.log("param:" + JSON.stringify(param));

                let url = "/schedule/admin/addSchedule";
                commonAjax(url, 'POST', param);

            }else{  // 수정

                // boardNo 추가
                var boardNo = $('#board_no_div').data('value');

                // scheduleNo 추가
                var scheduleNo = $('#schedule_no_div').data('value');

                // originClassList param에 추가
                var originClassList = [];
                if ($('#checked_class_div').length) {
                    $('.origin_class').each(function () {
                        originClassList.push(this.id);
                    });
                }

                let param = {
                    boardNo: boardNo,
                    scheduleNo: scheduleNo,
                    startDate: startDate,
                    endDate: endDate,
                    originClassList: originClassList,
                    classList : classList,
                    boardTitle : $('.default-wrapper').val(),
                    boardContents : $('.calendar-textarea').val(),
                    mapLng : mapLng,
                    mapLat : mapLat
                };
                console.log("param:" + JSON.stringify(param));

                let url = "/schedule/admin/modifySchedule";
                commonAjax(url, 'POST', param);
            }
        }
    });
});

var map = null;

// 카카오지도 api
document.addEventListener('DOMContentLoaded', function() {
    mapLat = mapLat !== null ? mapLat : 37.566826;
    mapLng = mapLng !== null ? mapLng : 126.9786567;
    console.log('latlng : ' + mapLat + ',' + mapLng);
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(mapLat, mapLng), // 지도의 중심좌표
            level: 1 // 지도의 확대 레벨
        };

    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    hideOrShowMap();

    // 주소-좌표 변환 객체를 생성합니다
    var geocoder = new kakao.maps.services.Geocoder();

    // 클릭한 위치에 대한 주소를 표시할 인포윈도우입니다
    var infowindow = new kakao.maps.InfoWindow({zindex:1});
    var iwPosition = new kakao.maps.LatLng(mapLat, mapLng);

    // 마커의 기본 주소를 설정하여 생성합니다
    var markerPosition = new kakao.maps.LatLng(mapLat, mapLng);
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });
    // 마커와 인포윈도우가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);
    searchDetailAddrFromCoords(mapLat, mapLng).then((infoContent) => {
        var iwContent = infoContent;

        infowindow = new kakao.maps.InfoWindow({
            position: iwPosition,
            content: iwContent
        });

        infowindow.open(map, marker);

        $('.iwcontent_div').parent().parent().css({
            'width': '0',
            'height': '0'
        });

    }).catch((error) => {
        console.error(error);
    });

    // 지도를 클릭했을 때 클릭 위치 좌표에 대한 주소정보를 표시하도록 이벤트를 등록합니다
    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {

        // 마커를 클릭한 위치에 표시합니다
        marker.setPosition(mouseEvent.latLng);

        mapLat = mouseEvent.latLng.getLat();
        mapLng = mouseEvent.latLng.getLng();

        searchDetailAddrFromCoords(mapLat, mapLng).then((infoContent) => {
            infowindow.close();

            var iwContent = infoContent;

            iwPosition = new kakao.maps.LatLng(mapLat, mapLng);

            infowindow = new kakao.maps.InfoWindow({
                position: iwPosition,
                content: iwContent
            });

            infowindow.open(map, marker);

            $('.iwcontent_div').parent().parent().css({
                'width': '0',
                'height': '0'
            });

        }).catch((error) => {
            console.error(error);
        });
    });


    function searchDetailAddrFromCoords(mapLat, mapLng) {
        return new Promise((resolve, reject) => {

            // 좌표로 법정동 상세 주소 정보를 요청합니다
            geocoder.coord2Address(mapLng, mapLat, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {

                    var searchAddr = !!result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;

                    var detailAddr = !!result[0].road_address ?
                        '<div><div class="addrtitle">도로명주소</div><div>' + result[0].road_address.address_name + '</div></div>' : '';
                    detailAddr += '<div><div class="addrtitle">지번</div><div>' + result[0].address.address_name + '</div></div>';

                    var addrString =
                        '<div class="bAddr">' +
                        detailAddr +
                        '</div>';

                    var infoContent =
                        '<div class="iwcontent_div">' +
                        '<div>' + addrString + '</div>' +
                        '<div class="iwcontent_adiv">' +
                        '<a href="https://map.kakao.com/link/map/' + searchAddr+ ',' + mapLat + ',' + mapLng + '" target="_blank">큰지도보기</a>' +
                        '<a href="https://map.kakao.com/link/to/' + searchAddr+ ',' + mapLat + ',' + mapLng + '" target="_blank">길찾기</a>' +
                        '</div>' +
                        '</div>';

                    resolve(infoContent);
                }else{
                    reject('주소를 찾을 수 없습니다.');
                }
            });
        });
    }




});


function afterSuccess(response) {
    // 추가 수정 구분해서 처리
    if($('#checked_class_div')){
        Swal.fire({
            title: "수정 완료",
            text: "창을 닫으면 일정표 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            window.location.href = window.location.origin + '/schedule/admin/scheduleMain';
        });

    }else{
        Swal.fire({
            title: "추가 완료",
            text: "창을 닫으면 일정표 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            window.location.href = window.location.origin + '/schedule/admin/scheduleMain';
        });
    }
}