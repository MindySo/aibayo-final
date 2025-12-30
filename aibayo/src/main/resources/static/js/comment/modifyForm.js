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
    console.log("comment modify");
    console.log('response : ' + response);
    // console.log(`response  : ${JSON.stringify(response)}`);


    if (method === 'POST' && response.commentClass === '1') {
        Swal.fire({
            title: "등록 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            const announceNo = $('#detail').data('announce-no'); // 데이터에서 announceNo 가져오기
            window.location.href = `${window.location.origin}/announce/user/${announceNo}`;
        });
    }


    let announceNo = $('#detail').data('announce-no');
    if (method === 'PUT' && response.comment) {
        Swal.fire({
            title: "수정 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            window.location.href = `${window.location.origin}/announce/user/${announceNo}`;
        });
    }
    if (method === 'DELETE' && response.invisibleFlag === '1' && response.an === false) {
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
    if (method === 'DELETE' && response.an === true) {
        console.log("공지삭제 ㅠ");
        Swal.fire({
            title: "삭제 완료",
            text: "창을 닫으면 이전 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then(() => {
            console.log(`${window.location.origin}/announce/admin/list`);
            window.location.href = `${window.location.origin}/announce/admin/list`;
        });
    }




}
