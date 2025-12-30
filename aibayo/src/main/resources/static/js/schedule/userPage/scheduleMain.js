$(document).ready(function() {
    showCalendar();

});

function showCalendar(){
    // 변수가 유지되도록
    var initialSelectedValue;
    var selectedValue;

    var calendarEl = document.getElementById('calendar');

    var eventsData = getEvents();

    var calendar = new FullCalendar.Calendar(calendarEl, {
        googleCalendarApiKey: "AIzaSyAKURukCy6rYdcfKAFsNYhY6wpn7XLzRqA",
        height: "700px",
        expandRows: true,
        locale: 'ko',
        events:
            {
                googleCalendarId : 'ko.south_korea#holiday@group.v.calendar.google.com',
                backgroundColor: 'red'
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
        events: eventsData

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

    calendar.render();
    showDaySchedule(initialSelectedValue);
}

function getEvents() {
    const scheduleElements = document.querySelectorAll('.schedule_values');
    const events = [];


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
            location: '' // location 값이 없으므로 빈 문자열로 설정
        };

        events.push(event);
    });

    return events;
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

    let url = "/schedule/user/scheduleMainByDay"

    let param = {
        selectedDate : selectedValue + 'T00:00:00',
    }
    console.log("param : " + JSON.stringify(param));

    commonAjax(url, 'POST', param);
}

function afterSuccess(response, method) {
    $('.schedule-2').replaceWith($(response).find('.schedule-2'));
    $('.schedules_data').replaceWith($(response).find('.schedules_data'));

    var count = document.querySelectorAll('.single-schedule').length;
    $('.text-wrapper-16').text("일정 " + count + "개");
}