// 전역 변수로 commentData 선언
let commentData;
let param;

$(document).ready(function () {
    $('#writeBtn').on('click', function (event) {
        console.log("작성버튼 클릭");
        event.preventDefault(); // 기본 폼 제출 방지

        const commentContent = $('#commentContent').val();
        const commentWriter = $('#detail').data('login-id');
        const announceNo = $('#detail').data('announce-no');
        const boardNo = $('#boardNo').val();
        const commentClass = 0;
        const commentDeleteFlag = '0';
        const invisibleFlag = '0';

        // 데이터 객체를 전역 변수에 할당
        commentData = {
            commentContent: commentContent,
            commentWriter: commentWriter,
            announceNo: announceNo,
            boardNo: boardNo,
            commentClass: commentClass,
            commentDeleteFlag: commentDeleteFlag,
            invisibleFlag: invisibleFlag,
            isComment: false,
        };

        console.log("commentData: ", commentData);
        let url = '/announce/comment/writeOk';

        commonAjax(url, 'POST', commentData);
    });
});


$(document).ready(function () {
    // 댓글 수정 폼 데이터 초기화
    function initializeForm(modal) {
        let modifyForm = $(modal).find('#modifyCommentForm');
        let commentContent = modifyForm.data('comment-content');
        $('#modifiedComment').val(commentContent);
    }

    // 모달 열기 및 데이터 초기화
    $(document).on('show.bs.modal', '.modal', function () {
        initializeForm(this);
    });

    // 댓글 수정 버튼 클릭 이벤트 리스너
    $(document).on('click', '.saveComment', function (event) {
        console.log("댓글 수정 버튼 클릭");
        event.preventDefault(); // 기본 폼 제출 방지

        let modal = $(this).closest('.modal');
        let modifyForm = modal.find('#modifyCommentForm');
        let commentNo = modifyForm.data('comment-no');
        let commentContent = modal.find('#modifiedComment').val();

        Swal.fire({
            title: "정말로 수정하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: "네",
            cancelButtonText: "아니오",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                // 폼 데이터 수집
                let param = {
                    commentNo: commentNo,
                    commentContent: commentContent,
                    isComment: true
                };

                console.log('param: ' + JSON.stringify(param));

                // AJAX 요청 URL
                let url = '/announce/comment/modifyOk';

                // AJAX 요청 함수 호출
                commonAjax(url, 'PUT', param);
            }
        });
    });
});

$(document).ready(function () {
    // 답글 쓰기 버튼 클릭 이벤트
    $('.replyBtn').on('click', function () {
        // 버튼 클릭 시 data-comment-no 속성 값 확인
        const replyId = $(this).find('span').text();
        console.log("Reply ID: ", replyId); // 댓글 ID 로그

        // 모달을 찾고, hidden input에 값을 설정
        const modalId = $(this).data('bs-target');
        const modalElement = $(modalId);
        modalElement.find('#replyId').val(replyId);

        // 데이터 객체 생성
        const replyData = {
            commentGroupNo: replyId,
        };

        console.log("replyData: ", JSON.stringify(replyData));
        let url = "/announce/comment/writeOk";
        commonAjax(url, 'POST', replyData);
    });

    // 답글 저장 버튼 클릭 이벤트
    $(document).on('click', '.save', function () {

        // 저장 버튼이 있는 모달에서 데이터 추출
        const modal = $(this).closest('.modal'); // 현재 클릭된 버튼의 모달 찾기
        const replyId = modal.find('#replyId').val();
        console.log("Reply ID for Save: ", replyId); // 댓글 ID 로그

        const commentContent = modal.find('.replyContent').val();
        console.log("Comment Content: ", commentContent); // 댓글 내용 로그
        const commentWriter= $('#repleWriterId').data('reple-writer');
        const announceNo = $('#detail').data('announce-no'); // 페이지의 #detail 요소에서 값 추출
        const boardNo = $('#boardNo').val(); // 페이지의 #boardNo 요소에서 값 추출
        const commentClass = '1';
        const commentDeleteFlag = '0';
        const invisibleFlag = '0';

        // 새 댓글 데이터 객체 생성
        const NewReplyData = {
            commentContent: commentContent,
            commentWriter: commentWriter,
            announceNo: announceNo,
            boardNo: boardNo,
            commentGroupNo: replyId,
            commentClass: commentClass,
            commentDeleteFlag: commentDeleteFlag,
            invisibleFlag: invisibleFlag,
        };

        console.log("NewReplyData: ", JSON.stringify(NewReplyData));
        let url = "/announce/comment/writeOk";
        commonAjax(url, 'POST', NewReplyData);
    });
});


$(document).ready(function () {
    $('.deleteReply').on('click', function (event) {
        console.log("대댓글 삭제 버튼 클릭");
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "삭제한 댓글은 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }).then((result) => {
            if (result.isConfirmed) {
                let commentNo = $(this).closest('.AAA').find('input').val(); // 삭제할 항목의 comment 가져옵니다.

                let param = {
                    commentNo: commentNo
                };

                console.log(`param: ${JSON.stringify(param)}`);

                let url = "/announce/comment/delete";

                // AJAX 요청을 보내는 함수 호출
                commonAjax(url, 'DELETE', param);
            }
        });
    });
});


$(document).ready(function () {
    $('.deleteComment').on('click', function (event) {
        console.log("댓글 삭제 버튼 클릭");
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "삭제한 댓글은 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }).then((result) => {
            if (result.isConfirmed) {
                let commentNo = $(this).closest('.AAA').find('input').val(); // 삭제할 항목의 comment 가져옵니다.

                let param = {
                    commentNo: commentNo
                };

                console.log(`param: ${JSON.stringify(param)}`);

                let url = "/announce/comment/delete";

                // AJAX 요청을 보내는 함수 호출
                commonAjax(url, 'DELETE', param);
            }
        });
    });
});




$(document).ready(function () {
    $('#deleteBtn').on('click', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "삭제한 공지사항은 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }).then((result) => {
            if (result.isConfirmed) {
                let boardNo = $(this).data('boardNo'); // 삭제할 항목의 boardNo를 가져옵니다.

                let param = {
                    boardNo: boardNo,
                    isAn:true
                };

                console.log(`param: ${JSON.stringify(param)}`);

                let url = "/announce/delete";

                // AJAX 요청을 보내는 함수 호출
                commonAjax(url, 'DELETE', param);
            }
        });
    });
});








// 댓글 수정 버튼 클릭 이벤트 리스너
$(document).on('click', '.saveReple', function (event) {
    console.log("대댓글 수정 버튼 클릭");
    event.preventDefault(); // 기본 폼 제출 방지

    let modal = $(this).closest('.modal');
    let modifyForm = modal.find('#modifyRepleForm');
    let commentNo = modifyForm.data('comment-no');
    let commentContent = modal.find('#modifiedReple').val();

    Swal.fire({
        title: "정말로 수정하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: "네",
        cancelButtonText: "아니오",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // 폼 데이터 수집
            let param = {
                commentNo: commentNo,
                commentContent: commentContent,
                isComment: true
            };

            console.log('param: ' + JSON.stringify(param));

            // AJAX 요청 URL
            let url = '/announce/comment/modifyOk';

            // AJAX 요청 함수 호출
            commonAjax(url, 'PUT', param);
        }
    });
});


function afterSuccess(response, method) {
    console.log("AdminDetail");
    console.log("response : " + JSON.stringify(response));
    console.log("method : " + method);
    console.log("comment : " + response.comment);
    console.log("an : " + response.an);
    console.log("who : " + response.user);

    if (method === 'POST' && response.comment === true) {
        console.log("댓글 등록");
        Swal.fire({
            title: "등록 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            const announceNo = $('#detail').data('announce-no');
            window.location.href = `${window.location.origin}/announce/admin/${announceNo}`;
        });
    }

    let announceNo = $('#detail').data('announce-no');
    if (method === 'PUT' && response.comment === true) {
        console.log("댓글 수정");
        Swal.fire({
            title: "수정 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            const announceNo = $('#detail').data('announce-no');
            window.location.href = `${window.location.origin}/announce/admin/${announceNo}`;
        });
    }
    if (method === 'DELETE' && response.invisibleFlag === '1' && response.comment === true) {
        Swal.fire({
            title: "삭제 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            window.location.href = `${window.location.origin}/announce/admin/${announceNo}`;
        });
    }
    if(method === 'DELETE' && response.an === true){
        Swal.fire({
            title: "삭제 완료",
            text: "창을 닫으면 목록 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            window.location.href = window.location.origin + '/announce/admin/list';
        });
    }



}