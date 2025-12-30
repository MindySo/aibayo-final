$(document).ready(function () {
    // 폼 데이터 초기화
    let settingMenu = $('#settingMenu');

    // 폼 데이터에서 초기값을 가져와서 설정
    function initializeForm() {

        // 문자열 '1' 또는 '0'을 기반으로 체크박스의 상태 설정
        $('#notepad').prop('checked', settingMenu.data('notepad-status'));
        $('#meal').prop('checked', settingMenu.data('meal-status'));
        $('#returnHome').prop('checked', settingMenu.data('return-home-status'));
        $('#medication').prop('checked', settingMenu.data('medication-status'));
        $('#attendance').prop('checked', settingMenu.data('attendance_status'));
        $('#schedule').prop('checked', settingMenu.data('schedule-status'));
        $('#pickDrop').prop('checked', settingMenu.data('pick-drop-status'));
        $('#lifeRecord').prop('checked', settingMenu.data('life-record-status'));
        $('#chat').prop('checked', settingMenu.data('chat-status'));

        // 디버깅을 위해 로그 출력

        console.log(" 알림장 " + (settingMenu.data('notepad-status')));
        console.log(" 식단표 " + (settingMenu.data('meal-status')));
        console.log(" 귀가동의서 " + (settingMenu.data('return-home-status')));
        console.log(" 투약 의뢰서 " + (settingMenu.data('medication-status')));
        console.log(" 출석부  " + (settingMenu.data('attendance_status')));
        console.log(" 스케쥴 " + (settingMenu.data('schedule-status')));
        console.log(" 픽드랍 " + (settingMenu.data('pick-drop-status')));
        console.log(" 생활기록 " + (settingMenu.data('life-record-status')));
        console.log(" 채팅 " + (settingMenu.data('chat-status')));
    }

    // 초기화 함수 호출
    initializeForm();


    // 수정 버튼 클릭 이벤트 리스너
    $('#saveBtn').on('click', function (event) {


        // 폼 데이터 수집
        let param = {
            boardNo: modifyForm.data('board-no'),
            announceNo: modifyForm.data('announce-no'),
            boardTitle: modifyForm.data('board-title'),
            boardContents: modifyForm.data('board-contents'),
            announcePrimary: modifyForm.data('announce-primary'),
            canComment: modifyForm.data('can-comment'),
            announceType: modifyForm.data('announce-type')
        };

        console.log('param: ' + JSON.stringify(param));

        // AJAX 요청 URL
        let url = '/announce/modifyOk';

        // AJAX 요청 함수 호출
        commonAjax(url, 'PUT', param);
    });

    // 토글 버튼 클릭 시 로그 출력
    $('#primary').on('click', function () {
        console.log('Primary checked (returns "1" if checked): ' + ($(this).prop('checked') ? '1' : '0'));
    });
    $('#comment').on('click', function () {
        console.log('Comment checked (returns "1" if checked): ' + ($(this).prop('checked') ? '1' : '0'));
    });

});

function afterSuccess(response) {
    console.log("announce modify");
    Swal.fire({
        title: "수정 완료",
        text: "창을 닫으면 상세 화면으로 돌아갑니다.",
        icon: "success",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        let announceNo = $('#modifyForm').data('announce-no');
        window.location.href = window.location.origin + '/announce/admin/' + announceNo;
    });
}

function setBoardContent(selector) {
    selector.data('board-contents', $('.summernote').summernote('code'));
}

function setData(modifyForm) {
    // 제목 설정
    modifyForm.data('board-title', $('#title').val());

    // 내용 설정
    setBoardContent(modifyForm);

    // 중요 공지 설정 (1 또는 0으로 변환)
    modifyForm.data('announce-primary', $('#primary').prop('checked') ? '1' : '0');

    // 댓글 허용 설정 (1 또는 0으로 변환)
    modifyForm.data('can-comment', $('#comment').prop('checked') ? '1' : '0');

    // 말머리 설정
    modifyForm.data('announce-type', $('.category').val());

    // 디버깅을 위해 로그 출력
    console.log("Set Data - Primary: " + modifyForm.data('announce-primary'));
    console.log("Set Data - Comment: " + modifyForm.data('can-comment'));
}
