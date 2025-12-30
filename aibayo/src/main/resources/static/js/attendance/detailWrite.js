// $(document).ready(function () {
//     $('#writeBtn').click(function (event) {
//         event.preventDefault(); // 기본 폼 제출을 방지
//
//         var attendanceList = [];
//         var kinderNo = $('#kinderNo').val();
//         var classNo = $('#classNo').val();
//
//         $('#attendanceTable tbody tr').each(function (index, row) {
//             var kidDropTime = $('#kidDrop_' + index).val();
//             var kidPickupTime = $('#kidPickup_' + index).val();
//
//             var currentDate = new Date().toISOString().split('T')[0]; // 2024-08-14 형식
//
//             // kidDrop 및 kidPickup 시간이 없을 경우 공백으로 처리
//             var kidDropDateTime = kidDropTime ? currentDate + ' ' + kidDropTime : '';
//             var kidPickupDateTime = kidPickupTime ? currentDate + ' ' + kidPickupTime : '';
//
//             // 데이터를 HashMap 형태로 구성
//             var attendance = {
//                 kinderNo: kinderNo,
//                 classNo: classNo,
//                 kidNo: $('#kidNo_' + index).val(),
//                 kidName: $('#kidName_' + index).val(),
//                 attendanceStatus: $('#attendanceStatus_' + index).val(),
//                 kidDrop: kidDropDateTime,
//                 kidPickup: kidPickupDateTime,
//                 note: $('#note_' + index).val()
//             };
//
//             attendanceList.push(attendance);
//         });
//
//         $.ajax({
//             type: 'POST',
//             url: '/attendance/write',
//             contentType: 'application/json',
//             data: JSON.stringify(attendanceList),
//             success: function (response) {
//                 window.location.href = '/attendance/detailToday?date=' + new Date().toISOString().split('T')[0];
//             },
//             error: function (xhr, status, error) {
//                 console.error('Error:', error);
//             }
//         });
//     });
// });


$(document).ready(function () {
    $('#writeBtn').click(function (event) {
        event.preventDefault(); // 기본 폼 제출을 방지

        var attendanceList = [];
        var kinderNo = $('#kinderNo').val();
        var classNo = $('#classNo').val();

        $('#attendanceTable tbody tr').each(function (index, row) {
            // 각 행의 체크박스가 선택되어 있는지 확인
            if ($('#select_' + index).is(':checked')) {
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
            }
        });

        if (attendanceList.length > 0) {
            // 선택된 학생이 있을 경우에만 서버로 전송
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
        } else {
            Swal.fire({
                icon: 'warning',
                title: '선택된 학생 없음',
                text: '작성할 학생을 선택해주세요.',
            });
        }
    });
});
