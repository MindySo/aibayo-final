$(document).ready(function () {
    $('#writeBtn').click(function (event) {
        event.preventDefault(); // 기본 폼 제출을 방지

        var attendanceList = [];
        var kinderNo = $('#kinderNo').val();
        var classNo = $('#classNo').val();

        $('#attendanceTable tbody tr').each(function (index, row) {
            var kidDropTime = $('#kidDrop_' + index).val();
            var kidPickupTime = $('#kidPickup_' + index).val();

            var currentDate = new Date().toISOString().split('T')[0]; // 2024-08-14 형식

            // kidDrop 및 kidPickup 시간이 없을 경우 공백으로 처리
            var kidDropDateTime = kidDropTime ? currentDate + ' ' + kidDropTime : '';
            var kidPickupDateTime = kidPickupTime ? currentDate + ' ' + kidPickupTime : '';

            // 데이터를 HashMap 형태로 구성
            var attendance = {
                kinderNo: kinderNo,
                classNo: classNo,
                kidNo: $('#kidNo_' + index).val(),
                kidName: $('#kidName_' + index).val(),
                attendanceStatus: $('#attendanceStatus_' + index).val(),
                kidDrop: kidDropDateTime,
                kidPickup: kidPickupDateTime,
                note: $('#note_' + index).val()
            };

            attendanceList.push(attendance);
        });

        $.ajax({
            type: 'POST',
            url: '/attendance/write',
            contentType: 'application/json',
            data: JSON.stringify(attendanceList),
            success: function (response) {
                window.location.href = '/attendance/detailToday?date=' + new Date().toISOString().split('T')[0];
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
            }
        });
    });
});
