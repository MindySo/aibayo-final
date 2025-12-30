$(document).ready(function(){
    $('.datepicker').datepicker({
        format: 'yyyy.mm.dd',
        autoclose: true,
        language: 'ko',
        orientation: 'bottom',
        todayHighlight: true,
    }).on('changeDate', function(e) {
        var date = e.date;
        $(this).val(date);

        // var dateText = moment(date).format('YYYY-MM-DD HH:mm:ss');
        var dateText = moment(date).format('YYYY-MM-DD');

        // 날짜 검색 function이 있을 경우에만 AJAX 요청 실행
        if (typeof findByDate === "function") {
            let parameter = {
                kinderNo : $('.datepicker').data('kinder-no'),
                kidNo : $('.datepicker').data('kid-no'),
                dateText : dateText
            };
            console.log(parameter);
            findByDate(parameter);
        }
    });
});