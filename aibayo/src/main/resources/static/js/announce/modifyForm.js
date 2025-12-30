$(document).ready(function () {
    // Summernote 에디터 초기화
    $('.summernote').summernote({
        height: 500,
        lang: 'ko-KR'
    });

    // 폼 데이터 초기화
    let modifyForm = $('#modifyForm');

    // 폼 데이터에서 초기값을 가져와서 설정
    function initializeForm() {
        $('#title').val(modifyForm.data('board-title'));
        $('#content').summernote('code', modifyForm.data('board-contents'));

        // 문자열 '1' 또는 '0'을 기반으로 체크박스의 상태 설정
        $('#primary').prop('checked', modifyForm.data('announce-primary'));
        $('#comment').prop('checked', modifyForm.data('can-comment'));

        // 디버깅을 위해 로그 출력
        console.log("Primary checked (expected '1' for checked): " + (modifyForm.data('announce-primary')));
        console.log("Comment checked (expected '1' for checked): " + (modifyForm.data('can-comment')));
    }

    // 초기화 함수 호출
    initializeForm();

    $('.category').on('click',function (event){
       console.log("카테고리 버튼 클릭");

    });

    $('.category').on('change', function () {
        console.log('Selected option value: ' + $(this).val());
    });

    // 수정 버튼 클릭 이벤트 리스너
    $('#modifyBtn').on('click', function (event) {
        console.log("수정 버튼 클릭");
        event.preventDefault(); // 기본 폼 제출 방지

        let announceTypeElement = $('select[name="announceType"]');
        let titleElement = $('#title');
        let contentElement = $('#content');

        let announceType = announceTypeElement.val();
        let title = titleElement.val();
        let content = contentElement.val();


        // 폼 데이터 유효성 검사
        if (announceType === '0') { // 값 체크
            console.log("말머리 누락");
            alert("카테고리를 입력하여 주세요.");
            announceTypeElement.focus(); // HTML 요소에 포커스 맞추기
            return false;
        }



        if (title.length === 0) {
            console.log("제목 누락");
            alert("제목을 입력하여 주세요.");
            titleElement.focus(); // HTML 요소에 포커스 맞추기
            return;
        }
        if (content.length === 0) {
            console.log("내용 누락");
            alert("내용을 입력하여 주세요.");
            contentElement.focus(); // HTML 요소에 포커스 맞추기
            return;
        }

        // 폼 데이터 설정 함수 호출
        setData(modifyForm);

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
