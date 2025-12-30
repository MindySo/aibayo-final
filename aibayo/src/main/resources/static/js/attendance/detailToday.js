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

                    var row = '<tr data-kid-no="' + attendance.kidNo + '">' +
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

    // 모달 열기 - 이벤트 위임 방식으로 수정
    $('#studentList').on('click', 'tr[data-kid-no]', function() {
        var row = $(this); // 현재 클릭된 행
        var kidNo = row.data('kid-no'); // data-kid-no 속성에서 아동 번호 가져옴
        var kidName = row.find('td:nth-child(1)').text().trim(); // 첫 번째 열에서 이름 가져옴
        var attendanceStatus = row.find('td:nth-child(2)').text().trim(); // 두 번째 열에서 출결 상태 가져옴
        var kidDrop = row.find('td:nth-child(3)').text().trim(); // 세 번째 열에서 등원 시간 가져옴
        var kidPickup = row.find('td:nth-child(4)').text().trim(); // 네 번째 열에서 하원 시간 가져옴
        var note = row.find('td:nth-child(5)').text().trim(); // 다섯 번째 열에서 비고 가져옴

        console.log("kidNo : " + kidNo);

        // 모달 창에 값을 설정
        $('#updateModal #kidNo').val(kidNo);
        $('#updateModal #attendanceStatus').val(attendanceStatus);
        $('#updateModal #kidDrop').val(kidDrop);
        $('#updateModal #kidPickup').val(kidPickup);
        $('#updateModal #note').val(note);

        // 모달 창 표시
        var myModal = new bootstrap.Modal(document.getElementById('updateModal'));
        myModal.show();
    });

    // 출결 상태 변경 시 등원/하원 시간 입력 필드 활성화/비활성화
    $('#attendanceStatus').on('change', function() {
        var status = $(this).val();
        if (status === '결석') {
            $('#kidDrop, #kidPickup').prop('disabled', true).val(''); // Disable fields and clear value
        } else {
            $('#kidDrop, #kidPickup').prop('disabled', false); // Enable fields
        }
    });

    $('#applyUpdate').on('click', function(e) {
        e.preventDefault();

        // 폼 데이터 수집
        var kidNo = $('#kidNo').val();
        var attendanceStatus = $('#attendanceStatus').val();
        var kidDrop = $('#kidDrop').val();
        var kidPickup = $('#kidPickup').val();
        var note = $('#note').val();

        // 선택된 날짜와 classNo 가져오기
        var fullText = $('#selectedDate').text();
        var selectedDate = fullText.replace('오늘날짜 - ', '').trim();

        const currentUrl = new URL(window.location.href);
        const classNo = currentUrl.searchParams.get('classNo'); // 현재 URL에서 classNo 가져오기

        // JSON 객체 생성 (시간 데이터를 ISO 포맷으로 변환)
        var data = {
            kidNo: kidNo,
            attendanceStatus: attendanceStatus,
            kidDrop: kidDrop ? `${selectedDate}T${kidDrop}:00` : null,  // kidDrop 시간을 ISO 8601 포맷으로 변환
            kidPickup: kidPickup ? `${selectedDate}T${kidPickup}:00` : null,  // kidPickup 시간을 ISO 8601 포맷으로 변환
            note: note,
            attendanceDate: selectedDate,
            classNo: classNo
        };

        // Ajax request
        $.ajax({
            type: 'POST',
            url: '/attendance/updateAttendance',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                // 성공 시 모달을 숨기고 페이지를 새로고침
                var myModal = bootstrap.Modal.getInstance(document.getElementById('updateModal'));
                myModal.hide();
                location.reload();
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 콘솔에 로그 기록 및 경고 표시
                console.error(xhr);
                console.error(status);
                console.error(error);
                Swal.fire({
                    icon: 'warning',
                    title: '출석부 수정 실패',
                    text: '출석부를 수정하는데 문제가 발생했습니다.',
                });
            }
        });
    });

    $('#deleteAttendance').on('click', function(e) {
        e.preventDefault();

        // 삭제 확인
        Swal.fire({
            title: '정말 삭제하시겠습니까?',
            text: "이 작업은 되돌릴 수 없습니다!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '삭제',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                // 삭제할 kidNo 가져오기
                var kidNo = $('#kidNo').val();

                // 선택된 날짜와 classNo 가져오기
                var fullText = $('#selectedDate').text();
                var selectedDate = fullText.replace('오늘날짜 - ', '').trim();

                // const currentUrl = new URL(window.location.href);
                // const classNo = currentUrl.searchParams.get('classNo'); // 현재 URL에서 classNo 가져오기

                // 삭제를 위한 데이터 객체 생성
                var data = {
                    kidNo: kidNo,
                    attendanceDate: selectedDate,
                };

                // Ajax 요청으로 서버에 삭제 요청
                $.ajax({
                    type: 'POST',
                    url: '/attendance/deleteAttendance',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function(response) {
                        // 성공 시 모달을 숨기고 페이지를 새로고침
                        var myModal = bootstrap.Modal.getInstance(document.getElementById('updateModal'));
                        myModal.hide();
                        location.reload();
                    },
                    error: function(xhr, status, error) {
                        // 오류 발생 시 콘솔에 로그 기록 및 경고 표시
                        console.error(xhr);
                        console.error(status);
                        console.error(error);
                        Swal.fire({
                            icon: 'warning',
                            title: '출석부 삭제 실패',
                            text: '출석부를 삭제하는데 문제가 발생했습니다.',
                        });
                    }
                });
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

    // 작성 버튼 클릭 시
    $('#writeBtn').click(function(event) {
        event.preventDefault(); // 기본 링크 동작을 막음

        // 선택된 반 번호를 가져옴
        var selectedClassNo = $('#classSelect').val();
        console.log("선택된 classNo: " + selectedClassNo);

        if (selectedClassNo) {
            // 선택된 classNo를 URL에 포함하여 페이지 이동
            window.location.href = '/attendance/write/' + selectedClassNo;
        } else {
            Swal.fire({
                icon: 'warning',
                title: '반을 선택하세요',
            });
        }
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
