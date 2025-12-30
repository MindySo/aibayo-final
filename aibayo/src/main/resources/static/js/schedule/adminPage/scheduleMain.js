var ifdeleted = false;
let calendar;

$(document).ready(function() {

    showCalendar();

    $(document).on('change', '.dropdown-class', function () {
        let classNo = $('.dropdown-class').val();
        // console.log("반 변경:" + classNo);

        let param = {
            classNo: classNo == null ? null : classNo
        };

        console.log("param:" + JSON.stringify(param));

        let url = "/schedule/admin/scheduleMainByClass";

        commonAjax(url, 'POST', param);

    });

    $(document).on('click', '#delete_button', function () {

        Swal.fire({
            title: "삭제 확인",
            text: "일정을 삭제하시겠습니까?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "승인",
            cancelButtonText: "취소",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            if (result.isConfirmed){
                var boardNo = $(this).attr('data-schedule-no');

                let param = {
                    boardNo: boardNo
                };
                console.log("param:" + JSON.stringify(param));

                ifdeleted = true;
                let url = "/schedule/admin/scheduleDelete";

                commonAjax(url, 'POST', param);
            }
        });
    });
});

function showCalendar(){
    // 변수가 유지되도록
    var initialSelectedValue;
    var selectedValue;

    var calendarEl = document.getElementById('calendar');

    calendar = new FullCalendar.Calendar(calendarEl, {
        googleCalendarApiKey: "AIzaSyAKURukCy6rYdcfKAFsNYhY6wpn7XLzRqA",
        height: "700px",
        expandRows: true,
        locale: 'ko',
        events:
            {
                googleCalendarId : 'ko.south_korea#holiday@group.v.calendar.google.com',
                backgroundColor: 'red',
                borderColor : 'red',
                eventDataTransform: function(eventData) {
                    // URL 속성 제거
                    delete eventData.url;
                    return eventData;
                }
            }
        ,

        themeSystem: 'bootstrap5',
        headerToolbar: {
            left: 'prev',
            // center: 'title today',
            center: 'title',
            right: 'next'
        },
        dayMaxEvents: true,
        buttonText: {
            today: '이번달',
            month: '월간',
            week: '주간',
            day: '일간',
            list: '목록'
        },

        // 선택한 날짜 출력
        dateClick: function(dateInfo) {
            //날짜 선택
            selectedValue = dateInfo.dateStr;
            // console.log("selectedValue" + selectedValue);
            showDaySchedule(selectedValue);
        },

        // 날짜 출력
        datesSet: function (dateInfo) {
            // 처음 calendar 로드 시 모든 div 숨기고
            $('.single-schedule').hide();

            // 오늘 날짜 설정
            initialSelectedValue = calendar.getDate().toISOString().substring(0, 10);
            var initialSelectedValueStr = initialSelectedValue.replace('-','년 ').replace('-','월 ').concat('일');

            if (selectedValue) {    // 선택한 날짜가 있는 경우
                $('.' + selectedValue).show();
                var selectedValueStr = selectedValue.replace('-','년 ').replace('-','월 ').concat('일');
                document.getElementById("selectedDate").innerHTML = selectedValueStr;
            }else if(initialSelectedValue) {    //선택한 날짜가 없는 경우 오늘 날짜의 일정 출력
                $('.' + initialSelectedValue).show();
                document.getElementById("selectedDate").innerHTML = initialSelectedValueStr;
            }

            // 달력에 표시 중인 달 확인
            var currentDate = dateInfo.view.currentStart;
            var currentMonth = currentDate.getMonth() + 1;
            // console.log("현재 표시중인 일: " + currentDate);
            // console.log("현재 표시중인 달: " + currentMonth);

            // datesRender 이벤트가 발생할 때 버튼 스타일을 변경하고 보이도록 설정
            $('.fc-toolbar button').each(function() {
                $(this).removeClass('btn-primary')
                    .addClass('btn-ab');
            });

            // 'dayGridMonth' 버튼을 비활성화
            var dayGridMonthButton = document.querySelector('.fc-dayGridMonth-button');
            if (dayGridMonthButton) {
                dayGridMonthButton.classList.add('disabled');
            }
        },
        // events: eventsData

    });

    $(document).on('click', '.fc-day', function () {
        $('.fc-day').css({
            'background-color': 'rgba(0,0,0,0)'
        });

        $(this).css({
            'border-radius': '15px',
            'background-color': 'rgba(55,55,55,0.15)'
        });
    });

    getEvents();
    calendar.render();
    showDaySchedule(initialSelectedValue);
}

function getEvents() {
    const scheduleElements = document.querySelectorAll('.schedule_values');

    scheduleElements.forEach(element => {
        let endDate = element.getAttribute('data-schedule-end-date');
        const dataClassList = element.getAttribute('data-class-list');

        // classNo가 0인지를 확인하기 위해 정규식을 사용하여 추출
        const classNoMatch = dataClassList.match(/classNo=0/);

        const event = {
            title: element.getAttribute('data-board-title'),
            start: element.getAttribute('data-schedule-start-date').split('T')[0], // 날짜만 추출
            end: endDate ? addOneDayAndFormat(endDate) : null,
            classNo: parseInt(element.getAttribute('data-class-no'), 10),   // 10진수
            contents: element.getAttribute('data-board-contents'),
            backgroundColor: classNoMatch !== null ? '#3788d8' : '#ff85aa',
            borderColor : classNoMatch !== null ? '#3788d8' : '#ff85aa',
        };

        calendar.addEvent(event);
    });
}

function addOneDayAndFormat(dateString) {
    // T를 기준으로 분리하여 날짜 부분만 가져옴
    var datePart = dateString.split('T')[0];

    // Date 객체로 변환
    var date = new Date(datePart);

    // 하루를 추가
    date.setDate(date.getDate() + 1);

    // 날짜를 'yyyy-mm-dd' 형식으로 변환
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    var day = String(date.getDate()).padStart(2, '0');

    return year + '-' + month + '-' + day;
}


function showDaySchedule(selectedValue){
    // 선택된 값에 따라 해당 div 보이기
    var selectedValueStr = selectedValue.replace('-','년 ').replace('-','월 ').concat('일');
    document.getElementById("selectedDate").innerHTML = selectedValueStr;

    let url = "/schedule/admin/scheduleMainByDay"
    let classNo = $('.dropdown-class').val();

    let param = {
        selectedDate : selectedValue + 'T00:00:00',
        classNo : classNo == null ? null : classNo
    }
    // console.log("param : " + JSON.stringify(param));
    mapList = [];

    commonAjax(url, 'POST', param);
}


$(document).on('show.bs.collapse', '.collapse', function () {
    console.log('collapsed clicked')
    mapList.forEach(function (mapInfo){
        setTimeout(function (){
            console.log(mapInfo[0]);
            mapInfo[0].relayout();
            mapInfo[0].setCenter(new kakao.maps.LatLng(mapInfo[1], mapInfo[2]));
        }, 10)
    });
});


function afterSuccess(response, method) {
    $('.schedule-2').replaceWith($(response).find('.schedule-2'));
    $('.schedules_data').replaceWith($(response).find('.schedules_data'));
    // $('.ifClassNoExist').replaceWith($(response).find('.ifClassNoExist'));

    var count = document.querySelectorAll('.single-schedule').length;
    $('.text-wrapper-16').text("일정 " + count + "개");

    var ifClassNoExist = (document.querySelectorAll('.ifClassNoExist').length);
    if (ifClassNoExist === 1) {
        console.log("reload by class");
        showCalendar();
    }

    var singleSchedule = $(".single-schedule");
    singleSchedule.each(function(index, element) {

        var ifLatlngExist = typeof $("#latlag").attr('data-map-lat') !== 'undefined';

        if (ifLatlngExist) {
            var scheduleNo = $(element).attr('id');
            console.log(scheduleNo);
            //---------카카오지도 api
            var mapLat = $('#latlag[data-schedule-no="'+ scheduleNo +'"]').attr('data-map-lat');
            var mapLng = $('#latlag[data-schedule-no="'+ scheduleNo +'"]').attr('data-map-lng');

            var mapContainer = document.querySelector(`.map_div[id="${scheduleNo}"] #map`); // 지도를 표시할 div
                mapOption = {
                    center: new kakao.maps.LatLng(mapLat, mapLng), // 지도의 중심좌표
                    level: 3 // 지도의 확대 레벨
                };

            var map = null;

            if(mapContainer){
                map = new kakao.maps.Map(mapContainer, mapOption);
            }
            var mapInfo = [map, mapLat, mapLng];
            mapList.push(mapInfo);

            // 마커가 표시될 위치입니다
            var markerPosition = new kakao.maps.LatLng(mapLat, mapLng);

            // 마커를 생성합니다
            var marker = new kakao.maps.Marker({
                position: markerPosition
            });

            // 마커가 지도 위에 표시되도록 설정합니다
            marker.setMap(map);

            // 주소-좌표 변환 객체를 생성합니다
            var geocoder = new kakao.maps.services.Geocoder();

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

            searchDetailAddrFromCoords(mapLat, mapLng).then((infoContent) => {
                var iwContent = infoContent;

                var iwPosition = new kakao.maps.LatLng(mapLat, mapLng);

                var infowindow = new kakao.maps.InfoWindow({
                    position: iwPosition,
                    content: iwContent
                });

                infowindow.open(map, marker);

                $('.iwcontent_div').parent().css({
                    'width': '0',
                    'height': '0',
                });
                $('.iwcontent_div').parent().parent().css({
                    'width': '0',
                    'height': '0'
                });

            }).catch((error) => {
                console.error(error);
            });

            //---------카카오지도 api end
        }
    });
    console.log(mapList);

    if(ifdeleted){
        ifdeleted = false;
        Swal.fire({
            title: "삭제 완료",
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