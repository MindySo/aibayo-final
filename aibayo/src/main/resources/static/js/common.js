function initializeMarquee($text_div) {
    let $textCompact = $text_div.find('.text-compact');

    if ($textCompact.length > 0) {
        // 이미 초기화된 경우에는 다시 초기화하지 않음
        // 모달 팝업 시 텍스트 무한 복제 방지
        if ($textCompact.find('.js-marquee').length > 0) {
            return;
        }

        // overflow 발생 시에만 적용
        if ($textCompact[0].scrollWidth > $textCompact[0].offsetWidth) {
            $textCompact.marquee({
                duration: 5000,
                startVisible: true,
                pauseOnHover: true,
                duplicated: true,
                gap: 50 // 공백 크기 설정 (픽셀 단위)
            });

            $text_div.hover(
                function() {
                    // 마우스를 올리면 text-overflow: ellipsis 제거
                    $textCompact.css('text-overflow', 'clip');
                    $textCompact.marquee('resume');
                },
                function() {
                    // 마우스를 떼면 text-overflow: ellipsis 다시 설정
                    $textCompact.css('text-overflow', 'ellipsis');
                    $textCompact.marquee('pause');
                }
            );

            // Start paused
            $textCompact.marquee('pause');
        }
    }

}

// ajax json 전달
function commonAjax(url, type, param) {
    $.ajax({
        url: url,
        type: type,
        data: JSON.stringify(param),
        contentType: 'application/json',
        success: function(response) {
            afterSuccess(response, type);
        },
        error: function(xhr, status, error) {
            console.error("AJAX 요청 오류:", status, error);
        }
    });
}

$(document).ready(function() {
    // 페이지가 로드되면 모든 컨테이너에 대해 marquee 초기화
    $('.text_div').each(function() {
        initializeMarquee($(this));
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const chatIframe = document.getElementById("chatIframe");

    // 페이지 로드 시 채팅팝업을 끈 상태로 설정
    document.body.classList.remove("show-chatbot");
    chatIframe.style.width = '100px';
    chatIframe.style.height = '100px';
});

// 값 입력 안내 메시지 초기화(숨김)
function initMsg() {
    $('.msg').each(function () {
        $(this).hide();
    });
}

$(document).ready(function() {
    $(document).on('click', '#logoutButton', function() {
        $.ajax({
            url: '/logout',
            type: 'POST',
            xhrFields: {
                withCredentials: true
            },
            success: function(response) {
                // 쿠키에서 'jwt' 토큰을 삭제합니다.
                var domain = window.location.hostname;
                document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=' + domain + ';';

                Swal.fire({
                    icon: 'success',
                    title: '로그아웃 성공',
                    text: '로그아웃이 완료되었습니다.',
                    confirmButtonText: '확인'
                }).then(() => {
                    window.location.href = '/member'; // 로그아웃 후 리디렉션 할 페이지
                });
            },
            error: function(xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    title: '로그아웃 실패',
                    text: 'Logout failed. 다시 시도해주세요.',
                    confirmButtonText: '확인'
                });
                console.error('Error:', error);
            }
        });
    });
});
