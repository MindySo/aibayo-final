$(document).ready(function () {
    $('.summernote').summernote({
        height: 500,
        lang: 'ko-KR'
    });

    $('input[type="radio"]').change(function () {
        setDisplay();
    });

    $('.form-select').change(function () {
        updateAnnWriteScope();
    });

    $('#writeBtn').on('click', function (event) {
        console.log("작성버튼 클릭");
        event.preventDefault(); // 기본 폼 제출 방지

        // HTML 요소를 직접 가져옴
        const announceTypeElement = $('select[name="announceType"]');
        const titleElement = $('#title');
        const contentElement = $('#content');

        // 현재 선택된 값을 가져옴
        const announceType = announceTypeElement.val();
        const title = titleElement.val();
        const content = contentElement.val();
        const canComment = $('#comment').is(':checked');
        console.log("canComment: " + canComment);
        const isPrimary = $('#primary').is(':checked');
        console.log("isPrimary: " + isPrimary);
        const boardType = 1;
        const writer = $('.ann-write').data('login-id');
        const kinderNo =  $('.ann-write').data('kinder-no');





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
            return false;
        }
        if (content.length === 0) {
            console.log("내용 누락");
            alert("내용을 입력하여 주세요.");
            contentElement.focus(); // HTML 요소에 포커스 맞추기
            return false;
        }

        // 데이터 객체 생성
        const announceData = {
            announceType: parseInt(announceType),
            boardTitle: title,
            boardContents: content,
            canComment: canComment ? "1" : "0",
            announcePrimary: isPrimary ? "1" : "0",
            boardType: boardType,
            writer: writer,
            kinderNo: kinderNo
        };

        console.log("announceData: ", announceData);
        let url = "/announce/writeOk";
        commonAjax(url, 'POST', announceData);
    });
});

function afterSuccess(response) {
    console.log("announce.writeForm.js afterSuccess");
    Swal.fire({
        title: "등록 완료",
        text: "창을 닫으면 목록 화면으로 돌아갑니다.",
        icon: "success",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        window.location.href = '/announce/admin/list'; // 성공 시 이동할 페이지
    });
}
