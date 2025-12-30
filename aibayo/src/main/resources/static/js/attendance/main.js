// document.addEventListener('DOMContentLoaded', function() {
//     var calendarEl = document.getElementById('calendar');
//     var calendar = new FullCalendar.Calendar(calendarEl, {
//         themeSystem: 'bootstrap5',
//         initialView: 'dayGridMonth',
//         locale: 'ko',
//         googleCalendarApiKey: 'AIzaSyAgH-X2n7hFaaKAo5h8_mBWcKbaV-G2hnk',
//         events: {
//             googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
//             className: 'kr-holiday',
//             textColor: 'red'
//         },
//
//         buttonText: {
//             today: '이번달',
//             month: '월간',
//             week: '주간',
//             day: '일간',
//             list: '목록'
//         },
//
//         dateClick: function(info) {
//             var today = new Date();
//             var clickedDate = new Date(info.dateStr);
//             var command = '';
//
//             if (clickedDate.toDateString() === today.toDateString()) {
//                 command = 'detailToday';
//             } else if (clickedDate < today) {
//                 command = 'detailBefore';
//             } else {
//                 command = 'detailAfter';
//             }
//             let formattedClickedDate = clickedDate.getFullYear() + '-'
//                 + String(clickedDate.getMonth() + 1).padStart(2, '0') + '-'
//                 + String(clickedDate.getDate()).padStart(2, '0');
//
//             // 이동할 페이지를 적절하게 설정
//             if (command === 'detailToday') {
//                 window.location.href = '/attendance/detailToday?date=' + formattedClickedDate;
//             } else if (command === 'detailBefore') {
//                 window.location.href = '/attendance/detailBefore?date=' + formattedClickedDate;
//             } else if (command === 'detailAfter') {
//                 window.location.href = '/attendance/detailAfter?date=' + formattedClickedDate;
//             }
//         }
//     });
//     calendar.render();
// });
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        themeSystem: 'bootstrap5',
        initialView: 'dayGridMonth',
        locale: 'ko',
        googleCalendarApiKey: 'AIzaSyAgH-X2n7hFaaKAo5h8_mBWcKbaV-G2hnk',
        events: {
            googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
            className: 'google-cal',  // 공휴일 클래스 이름을 상대방 코드와 동일하게 설정
            backgroundColor: 'transparent',  // 상대방 코드에서 설정한 배경색
            color: 'transparent',  // 상대방 코드에서 설정한 색
            textColor: 'red',  // 텍스트 색상
            eventDataTransform: function(eventData) {
                // URL 속성 제거
                delete eventData.url;
                return eventData;
            }
        },

        buttonText: {
            today: '이번달',
            month: '월간',
            week: '주간',
            day: '일간',
            list: '목록'
        },

        loading: function(isLoading) {
            if (isLoading) {
                $('#calendar').addClass('loading-cal');
                $('#loading-spinner').show();
            } else {
                $('#loading-spinner').hide();
                $('#calendar').removeClass('loading-cal');
            }
        },

        dateClick: function(info) {
            var today = new Date();
            var clickedDate = new Date(info.dateStr);
            var command = '';

            if (clickedDate.toDateString() === today.toDateString()) {
                command = 'detailToday';
            } else if (clickedDate < today) {
                command = 'detailBefore';
            } else {
                command = 'detailAfter';
            }
            let formattedClickedDate = clickedDate.getFullYear() + '-'
                + String(clickedDate.getMonth() + 1).padStart(2, '0') + '-'
                + String(clickedDate.getDate()).padStart(2, '0');

            // 이동할 페이지를 적절하게 설정
            if (command === 'detailToday') {
                window.location.href = '/attendance/detailToday?date=' + formattedClickedDate;
            } else if (command === 'detailBefore') {
                window.location.href = '/attendance/detailBefore?date=' + formattedClickedDate;
            } else if (command === 'detailAfter') {
                window.location.href = '/attendance/detailAfter?date=' + formattedClickedDate;
            }
        }
    });

    calendar.render();
});
