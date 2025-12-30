document.querySelectorAll('[data-bs-toggle="modal"]').forEach(function (modalToggle) {
    modalToggle.addEventListener('click', function () {
        var target = modalToggle.getAttribute('data-bs-target');
        var modal = document.querySelector(target);
        var input = modal.querySelector('.reply');

        modal.addEventListener('shown.bs.modal', function () {
            input.focus();
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

    document.addEventListener('DOMContentLoaded', function () {
        var commentFlag = document.getElementById('commentFlag').value;
        console.log("commentFlag : "+commentFlag);
        var commentSection = document.getElementById('commentSection');

        if (commentFlag === '0') {
            commentSection.style.display = 'none';
        }
    });

});

function afterSuccess(response,method) {
    console.log("announce.detail.js afterSuccess")
    if(method === 'DELETE' && response.invisibleFlag === '1'){
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



