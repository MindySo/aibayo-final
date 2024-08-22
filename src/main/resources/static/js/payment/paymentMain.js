$(document).ready(function() {

    // 테이블 출력 형식
    formitize();

    // Initialize DataTable
    dataTableInit();

    checkAll();


    $(document).on('click', '.search', function () {

        // 수납상태 리스트로
        var paymentStatusList = [];
        var selectedOptions = document.querySelectorAll('.selectedoption .btn-check');
        selectedOptions.forEach(function(option) {
            if (option.checked) {
                paymentStatusList.push(option.value);
            }
        });

        var startDate = $('#inputStartDate').val() === "" ? null :
            $('#inputStartDate').val().replace(/\./g, '-') + 'T00:00:00';
        var endDate = $('#inputStartDate').val()  === "" ? null :
            $('#inputEndDate').val().replace(/\./g, '-') + 'T00:00:00';
        var inputString = $('.keyword').val();
        var classNo = $('.dropdown-class').val();


        let param = {
            startDate : startDate,
            endDate : endDate,
            inputString : inputString === "" ? null : inputString,
            paymentStatusList : paymentStatusList.length === 0 ? null : paymentStatusList,
            classNo: classNo === "" ? null : classNo
        };

        console.log("param:" + JSON.stringify(param));

        let url = "/payment/admin/paymentBillingMainSearch";

        commonAjax(url, 'POST', param);
    });

    $(document).on('click', '.inputReset', function () {
        var selectedOptions = document.querySelectorAll('.selectedoption .btn-check');
        selectedOptions.forEach(function(option) {
            option.checked = false;
        });

        $('#inputStartDate').val(null);
        $('#inputEndDate').val(null);
        $('.keyword').val(null);
        $('.dropdown-class').val(null);
    });

    tupleCnt();

});

// checkbox 전체 선택
function checkAll() {
    var total;
    var checked;

    $("#checkAll").click(function () {
        if ($("#checkAll").is(":checked")) {
            $("input[class='form-check-input']").prop("checked", true);
        } else {
            $("input[class='form-check-input']").prop("checked", false);
        }
    });

    $("input[class='form-check-input']").click(function () {
        total = $("input[class='form-check-input']").length;
        console.log("total :" + total);
        checked = $("input[class='form-check-input']:checked").length;
        console.log("checked : " + checked);

        if (checked < total) {
            $("#checkAll").prop("checked", false);
        } else {
            $("#checkAll").prop("checked", true);
        }
    });

    // 페이지 클릭 시마다 checkAll() 실행
    var table = $('#datatable-payment').DataTable();
    $('#datatable-payment').on('page.dt', function () {
        var info = table.page.info();
        console.log('Showing page: ' + (info.page) + ' of ' + info.pages);

        total = $("input[class='form-check-input']").length;
        console.log("total :" + total);
        checked = $("input[class='form-check-input']:checked").length;
        console.log("checked : " + checked);

        if (checked < total) {
            $("#checkAll").prop("checked", false);
        } else {
            $("#checkAll").prop("checked", true);
        }
    });
}

function formitize() {
    document.querySelectorAll('.tuple_payment_status').forEach(function (element) {
        var paymentStatus = element.dataset.paymentStatus;

        if (paymentStatus == 0) {
            paymentStatus = '청구';
        } else if (paymentStatus == 1) {
            paymentStatus = '수납';
        } else if (paymentStatus == 2) {
            paymentStatus = '미납';
        } else if (paymentStatus == 3) {
            paymentStatus = '발행취소';
        } else if (paymentStatus == 4) {
            paymentStatus = '승인취소';
        }

        element.textContent = paymentStatus;
    });

    document.querySelectorAll('.tuple_payment_log_reg_date').forEach(function (element) {
        var regDate = element.dataset.regDate;
        element.innerHTML = formatDateTime(regDate);
    });

    document.querySelectorAll('.tuple_payment_start_date').forEach(function (element) {
        var startDate = element.dataset.startDate;
        element.innerHTML = formatDateTime(startDate);
    });

    document.querySelectorAll('.tuple_payment_end_date').forEach(function (element) {
        var endDate = element.dataset.endDate;
        element.innerHTML = formatDateTime(endDate);
    });

    document.querySelectorAll('.tuple_payment_price').forEach(function (element) {
        var paymentPrice = element.dataset.paymentPrice;
        // 숫자를 문자열로 변환하고, 천 단위로 콤마를 추가
        var formattedAmount = paymentPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        element.innerHTML = formattedAmount + '원';
    });

    document.querySelectorAll('.tuple_final_price').forEach(function (element) {
        var paymentPrice = element.dataset.paymentPrice;
        var discountRate = element.dataset.discountRate;
        // 숫자를 문자열로 변환하고, 천 단위로 콤마를 추가
        var formattedAmount = (paymentPrice * (100 - discountRate) * 0.01)
            .toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        element.innerHTML = formattedAmount + '원';
    });
}

// 날짜 텍스트 변환
function formatDateTime(input) {
    if(input === null){
        return '-';
    }else{
        // 날짜와 시간 분리
        var dateTimeParts = input.split('T');
        var datePart = dateTimeParts[0]; // '2024-06-02'
        var timePart = dateTimeParts[1]; // '06:00:00.000000'

        // 날짜 형식을 'YYYY.MM.DD'로 변경
        var formattedDate = datePart.replace(/-/g, '.'); // '2024.06.02'

        // 줄바꿈 추가하여 최종 결과 반환
        return formattedDate + '<br/>' + timePart;
    }
}

function dataTableInit(){
    new DataTable('#datatable-payment', {
        info: false,
        ordering: true,
        paging: true,
        searching: false,
        aaSorting: [],
        order : [[ 3, "desc" ]],

        // 0번째 컬럼(checkbox) 정렬 비활성
        columnDefs: [
            { "targets": [0], "orderable": false }
        ],

        // 페이지 당 데이터 수: 상단에 안 보이게
        layout: {
            topStart: null
        }
    });
}

function tupleCnt() {
    var tupleElement = document.querySelector('.tuples');
    var tupleCount = tupleElement.getAttribute('data-tuple-cnt');

    $('#tupleCnt').text(tupleCount);
}

function afterSuccess(response, method) {
    $('.tableDiv').replaceWith($(response).find('.tableDiv'));
    formitize();
    dataTableInit();
    tupleCnt();

    checkAll();


}