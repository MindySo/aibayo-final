$(document).ready(function() {
    // 반 선택시 데이터 가져오기
    $('#classSelect').change(function() {
        var selectedClassNo = $(this).val();

        var fullText = $('#selectedDate').text();
        var selectedDate = fullText.replace('오늘날짜 - ', '').trim();

        $.ajax({
            type: 'POST',
            url: '/attendance/getKidAttendance',
            data: { classNo: selectedClassNo, date: selectedDate  },
            success: function(response) {
                $('#studentList').empty();

                response.forEach(function(attendance) {
                    var formattedKidDrop = formatTimeToKoreanStyle(attendance.kidDrop);
                    var formattedKidPickup = formatTimeToKoreanStyle(attendance.kidPickup);

                    var row = '<tr>' +
                        '<td>' + attendance.kidName + '</td>' +
                        '<td>' + attendance.attendanceStatus + '</td>' +
                        '<td>' + formattedKidDrop + '</td>' +
                        '<td>' + formattedKidPickup + '</td>' +
                        '<td>' + attendance.note + '</td>' +
                        '</tr>';

                    $('#studentList').append(row);
                });
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
    });

    // DataTable 초기화
    new DataTable('#attendanceTable', {
        info: false,
        ordering: false,
        paging: false,
        searching: false
    });
});

// 시간 포맷을 한국 스타일로 변환
function formatTimeToKoreanStyle(dateTimeString) {
    if (!dateTimeString || typeof dateTimeString !== 'string') {
        return '';
    }

    var timePart = dateTimeString.split('T')[1]; // 시간 부분 추출
    var timeParts = timePart.split(':'); // 시, 분, 초 분리

    if (timeParts.length < 2) {
        return '';
    }

    var hours = parseInt(timeParts[0], 10);
    var minutes = timeParts[1];

    return hours + '시 ' + minutes + '분';
}
