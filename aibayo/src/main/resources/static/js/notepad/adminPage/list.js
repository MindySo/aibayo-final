function findByDate(parameter) {
    console.log('dateText: ' + parameter.dateText);

    let url = '/notepad/admin/searchDate';
    let data = {
        kinderNo : parameter.kinderNo,
        notepadDate: parameter.dateText
    }

    commonAjax(url, 'POST', data);

}

$(document).ready(function() {
    // // 스크롤 위치 복원
    // if (localStorage.getItem("scrollPosition")) {
    //     $(window).scrollTop(localStorage.getItem("scrollPosition"));
    //     localStorage.removeItem("scrollPosition");
    // }
    //
    // // 페이지네이션 링크 클릭 시 스크롤 위치 저장
    // $(".pagination a").on("click", function() {
    //     localStorage.setItem("scrollPosition", $(window).scrollTop());
    // });
});