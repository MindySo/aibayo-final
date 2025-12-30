$(document).ready(function() {
    // 모달이 show 될 때 marquee 초기화
    $('.modal').on('shown.bs.modal', function() {
        $('.text_div').each(function() {
            initializeMarquee($(this));
        });
    });
});